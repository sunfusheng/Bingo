package com.sun.bingo.framework.proxy.helper;

import com.sun.bingo.framework.base.BaseAsyncObject;
import com.sun.bingo.framework.base.BaseControl;
import com.sun.bingo.framework.proxy.handler.BaseHandler;

/**
 * Created by sunfusheng on 15/11/5.
 */
public class ObjectHelper<T extends BaseControl, R extends BaseAsyncObject> extends BaseHelper<T, R> {

    public ObjectHelper(R referenceObj, BaseHandler handler) {
        super(referenceObj, handler);
    }
}
