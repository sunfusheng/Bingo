package com.framework.proxy.helper;

import com.framework.base.BaseAsyncListAdapter;
import com.framework.base.BaseControl;
import com.framework.proxy.handler.AsyncAdapterHandler;

/**
 * Created by sunfusheng on 15/11/5.
 */
public class AdapterHelper<T extends BaseControl, R extends BaseAsyncListAdapter> extends BaseHelper<T, R> {

    public AdapterHelper(R refrenceObj) {
        super(refrenceObj, new AsyncAdapterHandler(refrenceObj));
    }
}
