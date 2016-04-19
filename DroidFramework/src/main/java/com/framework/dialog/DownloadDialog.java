package com.framework.dialog;

import android.content.Context;
import android.content.DialogInterface;

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
                    .progress(false, 100, false)
                    .cancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            ToastTip.show("已进入后台下载");
                        }
                    })
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
