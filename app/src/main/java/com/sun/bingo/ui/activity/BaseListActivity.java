package com.sun.bingo.ui.activity;

import android.os.Bundle;

import com.orhanobut.logger.Logger;
import com.sun.bingo.framework.base.BaseControl;

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
