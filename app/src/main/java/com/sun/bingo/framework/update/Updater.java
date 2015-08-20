package com.sun.bingo.framework.update;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.sun.bingo.R;

import java.io.File;

public class Updater {

    private Context context;

    public Updater(Context context) {
        this.context = context;
    }


    public void downloadApk(String fileUrl, String fileName) {
        new UpdaterPopupLauncher(context, fileUrl, fileName).start();
    }


    private class UpdaterPopupLauncher extends Thread {

        private Context context;
        private String fileUrl;
        private String fileName;

        public UpdaterPopupLauncher(Context context, String fileUrl, String fileName) {
            this.context = context;
            this.fileUrl = fileUrl;
            this.fileName = fileName;
        }

        @Override
        public void run() {
            Intent it = new Intent(context, MutilDownloader.class);
            it.setData(Uri.parse(fileUrl));
            it.putExtra(MutilDownloader.EXTRA_ICON, R.drawable.ic_launcher);
            it.putExtra(MutilDownloader.EXTRA_TITLE, fileName);
            it.putExtra(MutilDownloader.EXTRA_OUTPATH, new File(context.getCacheDir(), fileName).getAbsolutePath());

            Intent mIntent = new Intent();
            mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mIntent.setAction(Intent.ACTION_VIEW);
            mIntent.setDataAndType(Uri.fromFile(new File(context.getCacheDir(), fileName)),
                    "application/vnd.android.package-archive");

            PendingIntent pi = PendingIntent.getActivity(context, 0, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            it.putExtra(MutilDownloader.EXTRA_PENDING_FINISH_INTENT, pi);
            context.startService(it);
        }
    }

}
