package com.sun.bingo.framework.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.ant.liao.GifView;
import com.sun.bingo.R;
import com.sun.bingo.util.DisplayUtil;

/**
 * Created by sunfusheng on 15/10/8.
 */
public class CommonDialog {

    public static void showOkCancelDialog(Activity activity, String content, SureListener sureListner) {
        showOkCancelDialog(activity, content, activity.getString(R.string.ok), activity.getString(R.string.cancel), sureListner);
    }

    public static void showOkCancelDialog(Activity activity, String content, String okText, String cancelText, final SureListener sureListner) {
        LayoutInflater inflater = activity.getLayoutInflater();
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        View view = inflater.inflate(R.layout.dialog_common_layout, null);

        TextView tvContent = (TextView) view.findViewById(R.id.tv_content);
        TextView tvOk = (TextView) view.findViewById(R.id.tv_ok);
        TextView tvCancel = (TextView) view.findViewById(R.id.tv_cancel);
        tvContent.setText(content);
        tvOk.setText(okText);
        tvCancel.setText(cancelText);

        builder.setView(view).create();
        final AlertDialog dlg = builder.show();

        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlg.dismiss();
                if (sureListner != null) {
                    sureListner.onClick(v);
                }
            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlg.dismiss();
            }
        });
    }

    public static void showSuccessDialog(Activity activity, final DismissListener dismissListener) {
        final Dialog dialog = new Dialog(activity, R.style.DialogStyle);
        dialog.setContentView(R.layout.dialog_success_layout);
        GifView gifView = (GifView)dialog.findViewById(R.id.gifView);
        gifView.setGifImage(R.drawable.gif_success);
        gifView.setShowDimension(DisplayUtil.dip2px(activity, 30), DisplayUtil.dip2px(activity, 30));
        gifView.showAnimation();
        dialog.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
                if (dismissListener != null) {
                    dismissListener.onDismiss();
                }
            }
        }, 1000);
    }

    public interface SureListener {
        void onClick(View view);
    }

    public interface DismissListener {
        void onDismiss();
    }
}
