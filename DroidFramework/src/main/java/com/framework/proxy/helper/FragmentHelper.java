package com.framework.proxy.helper;

import com.framework.base.BaseAsyncFragment;
import com.framework.base.BaseControl;
import com.framework.proxy.handler.BaseHandler;


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
