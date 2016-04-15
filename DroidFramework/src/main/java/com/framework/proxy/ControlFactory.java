package com.framework.proxy;

import android.content.Context;

import com.framework.proxy.callback.AsyncMethodAtomInterceptor;
import com.framework.proxy.callback.Interceptor;
import com.framework.proxy.filter.AsyncMethodFilter;

import java.io.File;

public class ControlFactory {

    public static File mCacheDir;

    // 使用该框架必须先初始化
    public static void init(Context context) {
        mCacheDir = context.getDir("dx", Context.MODE_PRIVATE);
    }

    public static <T> T getControlInstance(Class<T> clazz, MessageProxy mMethodCallBack) {
        Enhancer<T> enhancer;
        if (mMethodCallBack != null) {
            enhancer = new Enhancer<T>(mCacheDir, clazz,
                    new Class[]{mMethodCallBack.getClass()},
                    new Object[]{mMethodCallBack});
        } else {
            enhancer = new Enhancer<T>(mCacheDir, clazz);
        }

        enhancer.addCallBacks(new Interceptor[]{new AsyncMethodAtomInterceptor(mMethodCallBack)});
        enhancer.addFilter(new AsyncMethodFilter());
        return enhancer.create();
    }

    public static <T> T getControlInstance(Class<T> clazz) {
        return getControlInstance(clazz, null);
    }

}
