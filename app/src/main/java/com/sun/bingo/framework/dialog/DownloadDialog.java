package com.sun.bingo.framework.dialog;

import android.content.Context;

import com.afollestad.materialdialogs.MaterialDialog;

/**
 * Created by sunfusheng on 15/8/12.
 */
public class DownloadDialog {

    private static MaterialDialog materialDialog;

    public static void show(Context context) {
        if (materialDialog == null) {
            materialDialog = new MaterialDialog.Builder(context)
                    .content("正在下载...")
                    .progress(false, 100, false)
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
