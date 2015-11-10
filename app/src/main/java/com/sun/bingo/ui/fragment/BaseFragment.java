package com.sun.bingo.ui.fragment;

import android.os.Bundle;

import com.orhanobut.logger.Logger;
import com.sun.bingo.entity.UserEntity;
import com.sun.bingo.framework.base.BaseAsyncFragment;
import com.sun.bingo.framework.base.BaseControl;

import cn.bmob.v3.BmobUser;


public class BaseFragment<T extends BaseControl> extends BaseAsyncFragment<T> {

    protected UserEntity userEntity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.i("log-fragment", "(" + getClass().getSimpleName() + ".java:1)");
        initData();
    }

    private void initData() {
        userEntity = BmobUser.getCurrentUser(getActivity(), UserEntity.class);
    }
}
