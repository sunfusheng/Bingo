package com.sun.bingo.ui.fragment;

import android.os.Bundle;

import com.framework.base.BaseAsyncFragment;
import com.framework.base.BaseControl;
import com.orhanobut.logger.Logger;
import com.sun.bingo.model.UserEntity;

import cn.bmob.v3.BmobUser;
import de.devland.esperandro.Esperandro;


public class BaseFragment<T extends BaseControl> extends BaseAsyncFragment<T> {

    protected UserEntity userEntity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.i("log-fragment", "(" + getClass().getSimpleName() + ".java:1)");
        init();
    }

    private void init() {
        userEntity = BmobUser.getCurrentUser(getActivity(), UserEntity.class);
    }

    protected <P> P getSharedPreferences(Class<P> spClass) {
        return Esperandro.getPreferences(spClass, getActivity());
    }
}
