package com.sun.bingo.ui.activity;

import android.os.Bundle;

import com.framework.base.BaseControl;
import com.orhanobut.logger.Logger;

/**
 * Created by sunfusheng on 15/8/14.
 */
public abstract class BaseListActivity<T extends BaseControl>  extends BaseActivity<T> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.i("log-activity", "(" + getClass().getSimpleName() + ".java:1)");

    }

}
