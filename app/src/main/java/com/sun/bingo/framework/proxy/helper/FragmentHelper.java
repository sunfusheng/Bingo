package com.sun.bingo.framework.proxy.helper;

import com.sun.bingo.framework.base.BaseControl;
import com.sun.bingo.framework.base.BaseAsyncFragment;
import com.sun.bingo.framework.proxy.handler.BaseHandler;

public class FragmentHelper<T extends BaseControl, R extends BaseAsyncFragment> extends BaseHelper<T, R> {

    public FragmentHelper(R refrenceObj, BaseHandler handler) {
        super(refrenceObj, handler);
    }

    public void onDestoryView() {
        if (mControl != null) {
            mControl.onDestroyView();
        }
    }

    public void onAttachView() {}
}
