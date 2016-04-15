package com.framework.proxy.handler;

import android.app.Activity;
import android.content.Context;

import com.framework.DroidFramework;

public class AsyncObjectHandler extends BaseHandler<Object> {

    private Context mContext;

    public AsyncObjectHandler(Object obj) {
        super(obj);
    }

    public AsyncObjectHandler(Object obj, Context context) {
        super(obj);
        this.mContext = context;
    }

    @Override
    public Context getContext() {
        if (mContext == null) {
            return DroidFramework.getContext();
        } else {
            return mContext;
        }
    }

    @Override
    protected Object checkAvailability() {
        Context context = getContext();
        if (context != null) {
            if ((context instanceof Activity) && ((Activity) context).isFinishing()) {
                return null;
            }
        }
        return mReference.get();
    }

}
