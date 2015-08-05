package com.sun.bingo.util;

import android.content.Context;
import android.telephony.TelephonyManager;

/**
 * Created by sunfusheng on 15/7/21.
 */
public class PhoneUtil {

    public static String getIMEI(Context context) {
        return ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
    }
}
