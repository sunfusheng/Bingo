package com.sun.bingo.framework.update;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat.Builder;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class MutilDownloader extends Service {
    private final static String THIS_FILE = "Downloader";

    public final static String EXTRA_ICON = "icon";
    public final static String EXTRA_TITLE = "title";
    public final static String EXTRA_OUTPATH = "outpath";
    public final static String EXTRA_PENDING_FINISH_INTENT = "pendingIntent";

    public int NOTIF_DOWNLOAD = 0;

    private NotificationManager notificationManager;
    private HttpClient client;
    private List<String> downloaderUrls = Collections.synchronizedList(new ArrayList<String>());
    private ConcurrentHashMap<String, String> downloaded = new ConcurrentHashMap<>();
    private HashMap<String, Integer> countManager = new HashMap<>();
    private Handler mHandler = new Handler();

    @Override
    public void onCreate() {
        super.onCreate();
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        client = getHttpClient();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        client.getConnectionManager().shutdown();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    private class DownLoadThread extends Thread {
        private Intent mIntent;

        DownLoadThread(Intent intent) {
            mIntent = intent;
        }

        @Override
        public void run() {
            onHandleIntent(mIntent);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            new DownLoadThread(intent).start();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void toastMessage(final String message) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MutilDownloader.this, message, Toast.LENGTH_LONG).show();
            }
        });
    }


    protected void onHandleIntent(Intent intent) {
        String outPath = intent.getStringExtra(EXTRA_OUTPATH);
        String title = intent.getStringExtra(EXTRA_TITLE);

        if (downloaderUrls.contains(title)) {
            toastMessage(title + "已在下载中，无法重复下载");
            return;
        }

        if (downloaded.containsKey(title)) {
            Integer count = countManager.get(title);
            if (count == null) {
                countManager.put(title, 1);
                installPackage(downloaded.get(title));
                return;
            } else {
                countManager.remove(title);
            }
        }
        toastMessage(title + "正在下载...");

        downloaderUrls.add(title);

        // Build notification
        Builder nb = new Builder(this);
        int flag = NOTIF_DOWNLOAD++;
        nb.setWhen(System.currentTimeMillis());
        nb.setContentTitle(title);
        nb.setSmallIcon(android.R.drawable.stat_sys_download);
        nb.setOngoing(true);
        nb.setProgress(50, 0, false);
        nb.setContentIntent(PendingIntent.getActivity(getApplicationContext(),
                0, new Intent(), PendingIntent.FLAG_CANCEL_CURRENT));

        File output = new File(outPath);
        if (output.exists()) {
            output.delete();
        }
        notificationManager.notify(flag, nb.build());

        ResponseHandler<Boolean> responseHandler = new FileStreamResponseHandler(output, new Progress(nb, flag));

        boolean hasReply = false;
        try {
            HttpGet getMethod = new HttpGet(intent.getData().toString());
            hasReply = client.execute(getMethod, responseHandler);
        } catch (Exception e) {
        }

        notificationManager.cancel(flag);
        if (hasReply) {
            if (changeMode(outPath)) {
                installPackage(outPath);
            } else {
                showFinishNotify(intent, nb, flag);
            }
        } else {
            toastMessage(title + "下载失败");
        }
        downloaderUrls.remove(title);
        downloaded.put(title, outPath);
    }

    private void showFinishNotify(Intent intent, Builder nb, int flag) {
        PendingIntent pendingIntent = (PendingIntent) intent
                .getParcelableExtra(EXTRA_PENDING_FINISH_INTENT);

        if (pendingIntent != null) {
            nb.setContentIntent(pendingIntent);
            nb.setAutoCancel(true);
            nb.setOngoing(false);
            nb.setSmallIcon(android.R.drawable.stat_sys_download_done);
            nb.setContentText("任务完成，点击安装").setProgress(0, 0, false);
            notificationManager.notify(flag, nb.build());
        }
    }

    private boolean changeMode(String outPath) {
        boolean chemoded = false;
        Process process = null;
        try {
            process = Runtime.getRuntime().exec("chmod 777 " + outPath);
            int status = process.waitFor();
            chemoded = status == 0;
        } catch (Exception e) {
            Log.e(THIS_FILE, "Unable to make the apk file readable", e);
        } finally {
            if (process != null) {
                process.destroy();
            }
        }
        return chemoded;
    }

    private void installPackage(String outPath) {
        Intent mIntent = new Intent();
        mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mIntent.setAction(Intent.ACTION_VIEW);
        mIntent.setDataAndType(Uri.fromFile(new File(outPath)),
                "application/vnd.android.package-archive");
        startActivity(mIntent);
    }

    private class Progress {
        private int mFlag;
        private Builder mBuilder;
        private int oldState = 0;

        Progress(Builder builder, int flag) {
            mFlag = flag;
            this.mBuilder = builder;
        }

        public void run(long progress, long total) {
            int newState = (int) Math.round(progress * 50.0f
                    / total);
            if (oldState != newState) {
                mBuilder.setProgress(50, newState, false);
                notificationManager.notify(mFlag,
                        mBuilder.build());
                oldState = newState;
            }
        }
    }


    private class FileStreamResponseHandler implements ResponseHandler<Boolean> {
        private Progress mProgress;
        private File mFile;

        FileStreamResponseHandler(File outputFile, Progress progress) {
            mFile = outputFile;
            mProgress = progress;
        }

        public Boolean handleResponse(HttpResponse response)
                throws ClientProtocolException, IOException {
            FileOutputStream fos = new FileOutputStream(mFile.getPath());

            HttpEntity entity = response.getEntity();
            boolean done = false;
            try {
                if (entity != null) {
                    Long length = entity.getContentLength();
                    InputStream input = entity.getContent();
                    byte[] buffer = new byte[4096];
                    int size = 0;
                    int total = 0;
                    while (true) {
                        size = input.read(buffer);
                        if (size == -1)
                            break;
                        fos.write(buffer, 0, size);
                        total += size;
                        mProgress.run(total, length);
                    }
                    done = true;
                }
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(THIS_FILE, "Problem on downloading:");
            } finally {
                fos.close();
            }
            return done;
        }

    }

    private HttpClient getHttpClient() {
        final HttpParams httpParams = new BasicHttpParams();

        // timeout: get connections from connection pool
        ConnManagerParams.setTimeout(httpParams, 10000);
        // timeout: connect to the server
        HttpConnectionParams.setConnectionTimeout(httpParams, 10000);
        // timeout: transfer data from server
        HttpConnectionParams.setSoTimeout(httpParams, 10000);

        // set max connections per host
        ConnManagerParams.setMaxConnectionsPerRoute(httpParams, new ConnPerRouteBean(5));
        // set max total connections
        ConnManagerParams.setMaxTotalConnections(httpParams, 5);

        // use expect-continue handshake
        HttpProtocolParams.setUseExpectContinue(httpParams, true);
        // disable stale check
        HttpConnectionParams.setStaleCheckingEnabled(httpParams, false);

        HttpProtocolParams.setVersion(httpParams, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(httpParams, HTTP.UTF_8);

        HttpClientParams.setRedirecting(httpParams, false);

        // set user agent
        String userAgent = "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2) Gecko/20100115 Firefox/3.6";
        HttpProtocolParams.setUserAgent(httpParams, userAgent);

        // disable Nagle algorithm
        HttpConnectionParams.setTcpNoDelay(httpParams, true);

        HttpConnectionParams.setSocketBufferSize(httpParams, 1024);

        // scheme: http and https
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        schemeRegistry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));

        ClientConnectionManager manager = new ThreadSafeClientConnManager(httpParams, schemeRegistry);

        return new DefaultHttpClient(manager, httpParams);
    }

}
