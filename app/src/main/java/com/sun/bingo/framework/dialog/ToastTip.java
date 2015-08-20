package com.sun.bingo.framework.dialog;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/**
 * Created by sunfusheng on 15/8/7.
 */
public class ToastTip {

    private static Toast mToast;

    public static void showToastDialog(Context context, String message) {
        if (mToast != null) {
            mToast.cancel();
        }
        int duration = Toast.LENGTH_SHORT;
        if (message.length() > 10) {
            duration = Toast.LENGTH_LONG; //如果字符串比较长，那么显示的时间也长一些。
        }
        mToast = Toast.makeText(context, message, duration);
        mToast.setGravity(Gravity.CENTER, 0, 0);
        mToast.show();
    }
}
