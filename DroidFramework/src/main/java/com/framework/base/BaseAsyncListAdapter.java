package com.framework.base;

import android.content.Context;
import android.os.Bundle;

import com.framework.proxy.MessageProxy;
import com.framework.proxy.ModelMap;
import com.framework.proxy.common.IRefreshBack;
import com.framework.proxy.helper.AdapterHelper;

import java.util.List;

/**
 * Created by sunfusheng on 15/11/5.
 */
public abstract class BaseAsyncListAdapter<T extends BaseControl, E> extends BaseListAdapter<E> implements IRefreshBack {

    protected T mControl;
    protected MessageProxy messageProxy;
    protected ModelMap mModel;
    private AdapterHelper mHelper;

    public BaseAsyncListAdapter(Context context) {
        super(context);
        mHelper = new AdapterHelper<T, BaseAsyncListAdapter>(this);
        mHelper.onCreate();
        initParams();
    }

    public BaseAsyncListAdapter(Context context, List<E> list) {
        super(context, list);
        mHelper = new AdapterHelper<T, BaseAsyncListAdapter>(this);
        mHelper.onCreate();
        initParams();
    }

    public void initParams() {
        mModel = mHelper.getModelMap();
        messageProxy = mHelper.getMessageProxy();
        mControl = (T) mHelper.getControl();
    }

    public void onResume() {
        mHelper.onResume();
    }

    public void onDestroy() {
        mHelper.onDestroy();
        mContext = null;
    }

    public Context getContext() {
        return mContext;
    }

    @Override
    public void onRefresh(int requestCode, Bundle bundle) {
    }

}
