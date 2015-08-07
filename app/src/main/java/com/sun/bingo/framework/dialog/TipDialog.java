package com.sun.bingo.framework.dialog;

import android.view.Gravity;
import android.widget.Toast;

import com.sun.bingo.BingoApplication;

/**
 * Created by sunfusheng on 15/8/7.
 */
public class TipDialog {

    private static Toast mToast;

    public static void showToastDialog(String message) {
        if (mToast != null) {
            mToast.cancel();
        }
        int duration = Toast.LENGTH_SHORT;
        if (message.length() > 15) {
            duration = Toast.LENGTH_LONG; //如果字符串比较长，那么显示的时间也长一些。
        }
        mToast = Toast.makeText(BingoApplication.getInstance(), message, duration);
        mToast.setGravity(Gravity.CENTER, 0, 0);
        mToast.show();
    }
}
