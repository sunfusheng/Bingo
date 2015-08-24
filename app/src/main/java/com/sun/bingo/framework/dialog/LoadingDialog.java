package com.sun.bingo.framework.dialog;

import android.content.Context;

import com.afollestad.materialdialogs.MaterialDialog;

/**
 * Created by sunfusheng on 15/8/12.
 */
public class LoadingDialog {

    private Context mContext;
    private MaterialDialog materialDialog;

    public LoadingDialog(Context context) {
        this.mContext = context;
    }

    public MaterialDialog getMaterialDialog() {
        return materialDialog;
    }

    public void show() {
        if (materialDialog == null) {
            materialDialog = new MaterialDialog.Builder(mContext)
                    .content("加载中...")
                    .progress(true, 0)
                    .progressIndeterminateStyle(false)
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
