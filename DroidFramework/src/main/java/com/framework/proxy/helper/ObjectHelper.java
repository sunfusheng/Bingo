package com.framework.proxy.helper;

import com.framework.base.BaseAsyncObject;
import com.framework.base.BaseControl;
import com.framework.proxy.handler.BaseHandler;

/**
 * Created by sunfusheng on 15/11/5.
 */
public class ObjectHelper<T extends BaseControl, R extends BaseAsyncObject> extends BaseHelper<T, R> {

    public ObjectHelper(R referenceObj, BaseHandler handler) {
        super(referenceObj, handler);
    }
}
