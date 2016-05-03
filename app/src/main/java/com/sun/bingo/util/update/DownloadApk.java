package com.sun.bingo.util.update;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

import com.afollestad.materialdialogs.MaterialDialog;
import com.framework.dialog.DownloadDialog;
import com.framework.dialog.TipDialog;
import com.framework.dialog.ToastTip;
import com.framework.okhttp.OkHttpProxy;
import com.framework.okhttp.callback.FileCallBack;
import com.framework.okhttp.callback.JsonCallBack;
import com.framework.okhttp.request.RequestCall;
import com.orhanobut.logger.Logger;
import com.sun.bingo.BingoApp;
import com.sun.bingo.R;
import com.sun.bingo.control.UrlManager;
import com.sun.bingo.model.VersionEntity;
import com.sun.bingo.model.eventbus.EventEntity;
import com.sun.bingo.model.eventbus.EventType;
import com.sun.bingo.model.sharedpreferences.AccountSharedPreferences;
import com.sun.bingo.util.AppUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.File;

import de.devland.esperandro.Esperandro;
import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by sunfusheng on 15/8/20.
 */
public class DownloadApk {

    private Context mContext;
    private DownloadDialog downloadDialog;

    private String fileName = "bingo.apk";
    private String filePath = Environment.getExternalStorageDirectory() + File.separator + BingoApp.APP_ROOT_DIR + File.separator;
    private String apkPathName = filePath + fileName;
    private AccountSharedPreferences preferences;

    public DownloadApk(Context context) {
        this.mContext = context;
        preferences = Esperandro.getPreferences(AccountSharedPreferences.class, mContext);
        downloadDialog = new DownloadDialog(context);
    }

    public void checkVersion(final boolean showTip) {
        RequestCall build = OkHttpProxy.get().url(UrlManager.URL_APP_VERSION).build();
        build.execute(new JsonCallBack<VersionEntity>() {
            @Override
            public void onSuccess(VersionEntity response) {
                int version = Integer.parseInt(response.getVersion());
                if (version > AppUtil.getVersionCode(mContext)) {
                    dealWithVersion(response);
                    EventBus.getDefault().post(new EventEntity(EventType.EVENT_TYPE_UPDATE_APP, 0));
                } else {
                    EventBus.getDefault().post(new EventEntity(EventType.EVENT_TYPE_UPDATE_APP, 1));
                    if (showTip) {
                        ToastTip.show("已是最新版本");
                    }
                }
            }

            @Override
            public void onFailure(Call request, Exception e) {

            }
        });
    }

    public void dealWithVersion(final VersionEntity entity) {
        String versionInfo = "\n\n下载 " + entity.getName()+" ( V"+entity.getVersionShort()+" ) 替换当前版本 ( "+AppUtil.getVersionName(mContext)+" ) ?";
        TipDialog tipDialog = new TipDialog(mContext);
        tipDialog.show(mContext.getString(R.string.update_app), entity.getChangelog()+versionInfo, mContext.getString(R.string.update_rightnow),
                mContext.getString(R.string.update_no), new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        super.onPositive(dialog);
                        preferences.ignore_version_name("");
                        preferences.is_need_update(false);
                        download(entity.getInstallUrl());
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        super.onNegative(dialog);
                        preferences.ignore_version_name("V" + entity.getVersionShort());
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
                installPackage(apkPathName);
            }

            @Override
            public void onFailure(Call request, Exception e) {
                downloadDialog.dismiss();
            }
        });
    }

    // 安装应用
    private void installPackage(String apkPath) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(apkPath)), "application/vnd.android.package-archive");
        mContext.startActivity(intent);
    }

}
