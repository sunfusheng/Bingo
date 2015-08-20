package com.sun.bingo.framework.dialog;

import android.content.Context;

import com.afollestad.materialdialogs.MaterialDialog;

/**
 * Created by sunfusheng on 15/8/12.
 */
public class DownloadDialog {

    private Context mContext;
    private MaterialDialog materialDialog;

    public DownloadDialog(Context context) {
        this.mContext = context;
    }

    public MaterialDialog getMaterialDialog() {
        return materialDialog;
    }

    public void show() {
        if (materialDialog == null) {
            materialDialog = new MaterialDialog.Builder(mContext)
                    .content("正在下载...")
                    .progress(false, 100, true)
                    .build();
        }
        if (!materialDialog.isShowing()) {
            materialDialog.show();
        }
    }

    public void dismiss() {
        if (materialDialog != null && materialDialog.isShowing()) {
            materialDialog.dismiss();
            materialDialog = null;
        }
    }

}
