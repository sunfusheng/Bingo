package com.framework.dialog;

import android.content.Context;

import com.afollestad.materialdialogs.MaterialDialog;
import com.framework.R;

/**
 * Created by sunfusheng on 15/8/20.
 */
public class TipDialog {

    private Context mContext;
    private MaterialDialog materialDialog;

    public TipDialog(Context context) {
        this.mContext = context;
    }

    public MaterialDialog getMaterialDialog() {
        return materialDialog;
    }

    public void show(String title, String content, String positiveText, String negativeText, MaterialDialog.ButtonCallback callback) {
        if (materialDialog == null) {
            materialDialog = new MaterialDialog.Builder(mContext)
                    .title(title)
                    .content(content)
                    .positiveText(positiveText)
                    .negativeText(negativeText)
                    .negativeColor(mContext.getResources().getColor(R.color.font_black_3))
                    .callback(callback)
                    .build();
        }
        if (!materialDialog.isShowing()) {
            materialDialog.show();
        }
    }

    public void show(String title, String content, MaterialDialog.ButtonCallback callback) {
        show(title, content, mContext.getString(R.string.ok), mContext.getString(R.string.cancel), callback);
    }

    public void dismiss() {
        if (materialDialog != null && materialDialog.isShowing()) {
            materialDialog.dismiss();
            materialDialog = null;
        }
    }

}
