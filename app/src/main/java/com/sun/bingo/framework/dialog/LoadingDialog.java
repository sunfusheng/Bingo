package com.sun.bingo.framework.dialog;

import android.content.Context;

import com.afollestad.materialdialogs.MaterialDialog;

/**
 * Created by sunfusheng on 15/8/12.
 */
public class LoadingDialog {

    private static MaterialDialog materialDialog;

    public static void show(Context context) {
        if (materialDialog == null) {
            materialDialog = new MaterialDialog.Builder(context)
                    .content("加载中...")
                    .progress(true, 0)
                    .progressIndeterminateStyle(false)
                    .build();
        }
        if (!materialDialog.isShowing()) {
            materialDialog.show();
        }
    }

    public static void dismiss() {
        if (materialDialog != null && materialDialog.isShowing()) {
            materialDialog.dismiss();
            materialDialog = null;
        }
    }


}
