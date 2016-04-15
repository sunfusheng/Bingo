package com.framework.proxy.handler;

import android.content.Context;

import com.framework.DroidFramework;
import com.framework.base.BaseAsyncListAdapter;


public class AsyncAdapterHandler extends BaseHandler<BaseAsyncListAdapter> {

    public AsyncAdapterHandler(BaseAsyncListAdapter baseAsyncListAdapter) {
        super(baseAsyncListAdapter);
    }

    @Override
    public Context getContext() {
        BaseAsyncListAdapter adapter = mReference.get();
        if (adapter == null) {
            return DroidFramework.getContext();
        } else {
            return adapter.getContext();
        }
    }

    @Override
    protected BaseAsyncListAdapter checkAvailability() {
        BaseAsyncListAdapter mAdapter = mReference.get();
        if (mAdapter == null || mAdapter.getContext()==null) {
            return null;
        }
        return mAdapter;
    }

}
