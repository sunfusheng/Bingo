package com.framework.dialog;

import android.widget.Toast;

import com.framework.DroidFramework;

/**
 * Created by sunfusheng on 15/8/7.
 */
public class ToastTip {

    private static Toast mToast;

    public static void show(String message) {
        if (mToast != null) {
            mToast.cancel();
        }
        int duration = Toast.LENGTH_SHORT;
        if (message.length() > 10) {
            duration = Toast.LENGTH_LONG; //如果字符串比较长，那么显示的时间也长一些。
        }
        mToast = Toast.makeText(DroidFramework.getContext(), message, duration);
        mToast.show();
    }
}
