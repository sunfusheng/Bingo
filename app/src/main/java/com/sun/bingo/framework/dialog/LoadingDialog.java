package com.sun.bingo.framework.dialog;

import android.content.Context;

import com.afollestad.materialdialogs.MaterialDialog;

/**
 * Created by sunfusheng on 15/8/12.
 */
public class LoadingDialog {

    private Context mContext;
    private MaterialDialog dialog;

    public LoadingDialog(Context context) {
        this.mContext = context;
    }

    public MaterialDialog getDialog() {
        return dialog;
    }

    public void show() {
        if (dialog == null) {
            dialog = new MaterialDialog.Builder(mContext)
                    .content("加载中...")
                    .progress(true, 0)
                    .progressIndeterminateStyle(false)
                    .build();
            dialog.setCanceledOnTouchOutside(true);
        }
        if (!dialog.isShowing()) {
            dialog.show();
        }
    }

    public void showCancelDialog() {
        if (dialog == null) {
            dialog = new MaterialDialog.Builder(mContext)
                    .content("加载中...")
                    .progress(true, 0)
                    .progressIndeterminateStyle(false)
                    .build();
            dialog.setCanceledOnTouchOutside(false);
        }
        if (!dialog.isShowing()) {
            dialog.show();
        }
    }

    public void dismiss() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }
    }

    public boolean isShowing() {
        return dialog.isShowing();
    }

}
