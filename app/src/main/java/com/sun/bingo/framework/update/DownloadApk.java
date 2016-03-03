package com.sun.bingo.framework.update;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

import com.afollestad.materialdialogs.MaterialDialog;
import com.orhanobut.logger.Logger;
import com.sun.bingo.BingoApplication;
import com.sun.bingo.R;
import com.sun.bingo.control.UrlManager;
import com.sun.bingo.entity.AppEntity;
import com.sun.bingo.entity.VersionEntity;
import com.sun.bingo.framework.dialog.DownloadDialog;
import com.sun.bingo.framework.dialog.TipDialog;
import com.sun.bingo.framework.okhttp.OkHttpProxy;
import com.sun.bingo.framework.okhttp.callback.FileCallBack;
import com.sun.bingo.framework.okhttp.callback.JsonCallBack;
import com.sun.bingo.framework.okhttp.request.RequestCall;
import com.sun.bingo.util.ShareUtil;

import java.io.File;

import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by sunfusheng on 15/8/20.
 */
public class DownloadApk {

    private Context mContext;
    private DownloadDialog downloadDialog;

    private AppEntity appEntity;
    private String fileName = "BingoWorld.apk";
    private String filePath = Environment.getExternalStorageDirectory() + File.separator + BingoApplication.APP_CACHE_DIR + File.separator + fileName;

    public DownloadApk(Context context) {
        this.mContext = context;
        downloadDialog = new DownloadDialog(context);
        appEntity = ShareUtil.getAppEntity(context);
    }

    public void checkVersion() {
        RequestCall build = OkHttpProxy.get().url(UrlManager.URL_APP_VERSION).build();
        build.execute(new JsonCallBack<VersionEntity>() {
            @Override
            public void onSuccess(VersionEntity response) {
                int version = Integer.parseInt(response.getVersion());
                if (version > appEntity.getVersionCode()) {
                    dealWithVersion(response);
                }
            }

            @Override
            public void onFailure(Call request, Exception e) {

            }
        });
    }

    private void dealWithVersion(final VersionEntity entity) {
        String versionInfo = "\n\n下载 " + entity.getName()+" ( "+entity.getVersionShort()+" ) 替换当前版本 ( "+appEntity.getVersionName()+" ) ?";
        TipDialog tipDialog = new TipDialog(mContext);
        tipDialog.show(mContext.getString(R.string.update_app), entity.getChangelog()+versionInfo, mContext.getString(R.string.update_rightnow),
                mContext.getString(R.string.update_no), new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        super.onPositive(dialog);
                        download(entity.getInstallUrl());
                    }
                });
    }

    private void download(final String url) {
        File file = new File(filePath);
        try {
            if (file.exists()) {
                file.delete();
            }
        } catch (Exception e) {
            Logger.i(e.getMessage());
        }

        RequestCall build = OkHttpProxy.get().url(url).build();
        build.execute(new FileCallBack(filePath, fileName) {

            @Override
            public void onStart(Request request) {
                super.onStart(request);
                downloadDialog.show();
            }

            @Override
            public void inProgress(float progress) {
                downloadDialog.getMaterialDialog().setProgress(((int)(progress*100)));
            }

            @Override
            public void onSuccess(File response) {
                downloadDialog.dismiss();
                installPackage(filePath);
            }

            @Override
            public void onFailure(Call request, Exception e) {
                downloadDialog.dismiss();
            }
        });
    }

    /**
     * 安装应用
     */
    private void installPackage(String apkPath) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(apkPath)), "application/vnd.android.package-archive");
        mContext.startActivity(intent);
    }

}
