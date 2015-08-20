package com.sun.bingo.framework.update;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

import com.afollestad.materialdialogs.MaterialDialog;
import com.apkfuns.logutils.LogUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.sun.bingo.BingoApplication;
import com.sun.bingo.R;
import com.sun.bingo.entity.AppEntity;
import com.sun.bingo.entity.VersionEntity;
import com.sun.bingo.framework.dialog.DownloadDialog;
import com.sun.bingo.framework.dialog.TipDialog;
import com.sun.bingo.framework.http.HttpCallBack.BaseParserCallBack;
import com.sun.bingo.framework.http.HttpControl.HttpControl;
import com.sun.bingo.util.ShareUtil;

import java.io.File;

/**
 * Created by sunfusheng on 15/8/20.
 */
public class DownloadApk {

    private Context mContext;
    private DownloadDialog downloadDialog;
    protected HttpControl mControl = BingoApplication.getInstance().getHttpControl();

    private AppEntity appEntity;
    private String fileName = "BingoWorld.apk";
    private String filePath = Environment.getExternalStorageDirectory() + File.separator + BingoApplication.APP_CACHE_DIR + File.separator + fileName;

    public DownloadApk(Context context) {
        this.mContext = context;
        downloadDialog = new DownloadDialog(context);
        appEntity = ShareUtil.getAppEntity(context);
    }

    public void checkVersion() {
        mControl.getVersionInfo(new BaseParserCallBack<VersionEntity>() {

            @Override
            protected boolean onSuccessWithObject(VersionEntity versionEntity) {
                int version = Integer.parseInt(versionEntity.getVersion());
                if (version > appEntity.getVersionCode()) {
                    dealWithVersion(versionEntity);
                }
                return super.onSuccessWithObject(versionEntity);
            }

            @Override
            protected Class<VersionEntity> getCurrentClass() {
                return VersionEntity.class;
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
            LogUtils.e(e.getMessage());
        }

        HttpUtils http = new HttpUtils();
        http.download(url, filePath, true, false, new RequestCallBack<File>() {
            @Override
            public void onStart() {
                downloadDialog.show();
            }

            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                double progress = ((double)current/(double)total) * 100;
                downloadDialog.getMaterialDialog().setProgress((int)progress);
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                downloadDialog.dismiss();
            }

            @Override
            public void onSuccess(ResponseInfo<File> responseInfo) {
                downloadDialog.dismiss();
                installPackage(filePath);
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
