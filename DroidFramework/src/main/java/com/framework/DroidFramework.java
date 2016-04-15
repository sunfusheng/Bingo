package com.framework;

import android.content.Context;

import com.framework.proxy.ControlFactory;

/**
 * Created by sunfusheng on 16/4/13.
 */
public class DroidFramework {

    private static Context mContext;
    public static final String LOG_URL = "log-url";
    public static final String LOG_CONTENT = "log-content";
    public static boolean DEBUG = true;
    public static boolean LOG = true;

    // 使用该框架必须先初始化
    public static void init(Context context, boolean isDebug, boolean isLog) {
        mContext = context;
        DEBUG = isDebug;
        LOG = isLog;

        // 初始化代理类
        ControlFactory.init(context);
    }

    public static Context getContext() {
        if (mContext == null) {
            throw new RuntimeException("使用该框架必须先初始化");
        }
        return mContext;
    }

}
