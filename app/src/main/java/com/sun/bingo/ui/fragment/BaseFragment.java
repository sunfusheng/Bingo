package com.sun.bingo.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.framework.base.BaseAsyncFragment;
import com.framework.base.BaseControl;
import com.orhanobut.logger.Logger;
import com.sun.bingo.control.manager.ImageManager;
import com.sun.bingo.model.UserEntity;
import com.sun.bingo.model.sharedpreferences.AccountSharedPreferences;
import com.sun.bingo.model.sharedpreferences.LocationSharedPreferences;
import com.sun.bingo.model.sharedpreferences.SettingsSharedPreferences;

import cn.bmob.v3.BmobUser;
import de.devland.esperandro.Esperandro;


public class BaseFragment<T extends BaseControl> extends BaseAsyncFragment<T> implements View.OnClickListener {

    protected Context mContext;
    protected Activity mActivity;

    protected UserEntity mUserEntity;
    protected ImageManager mImageManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.i("log-fragment", "(" + getClass().getSimpleName() + ".java:1)");

        init();
    }

    private void init() {
        mContext = getActivity();
        mActivity = getActivity();
        mUserEntity = BmobUser.getCurrentUser(getActivity(), UserEntity.class);
        mImageManager = new ImageManager(mContext);
    }

    @Override
    public void onClick(View v) {

    }

    // SharedPreferences
    protected <P> P getSharedPreferences(Class<P> spClass) {
        return Esperandro.getPreferences(spClass, getActivity());
    }
    public AccountSharedPreferences getAccountSharedPreferences() {
        return getSharedPreferences(AccountSharedPreferences.class);
    }
    public SettingsSharedPreferences getSettingsSharedPreferences() {
        return getSharedPreferences(SettingsSharedPreferences.class);
    }
    public LocationSharedPreferences getLocationSharedPreferences() {
        return getSharedPreferences(LocationSharedPreferences.class);
    }

}
