package com.sun.bingo.framework.proxy.helper;

import com.sun.bingo.framework.base.FWBaseControl;
import com.sun.bingo.framework.base.FWBaseFragment;
import com.sun.bingo.framework.proxy.handler.BaseHandler;

public class FragmentHelper<T extends FWBaseControl, R extends FWBaseFragment> extends BaseAsyncHelper<T, R> {

    public FragmentHelper(R refrenceObj, BaseHandler handler) {
        super(refrenceObj, handler);
    }

    public void onDestoryView() {
        if (mControl != null) {
            mControl.onDestroyView();
        }
    }

    public void onAttachView() {
    }

}
