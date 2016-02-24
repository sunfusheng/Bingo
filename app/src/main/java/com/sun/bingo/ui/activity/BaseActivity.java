package com.sun.bingo.ui.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.orhanobut.logger.Logger;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.sun.bingo.R;
import com.sun.bingo.constant.GlobalParams;
import com.sun.bingo.entity.UserEntity;
import com.sun.bingo.framework.base.BaseAsyncActivity;
import com.sun.bingo.framework.base.BaseControl;
import com.sun.bingo.framework.dialog.LoadingDialog;
import com.sun.bingo.sharedpreferences.AccountSharedPreferences;
import com.sun.bingo.sharedpreferences.LocationSharedPreferences;
import com.sun.bingo.sharedpreferences.SettingsSharedPreferences;
import com.sun.bingo.util.DisplayUtil;
import com.sun.bingo.util.theme.Selector;
import com.sun.bingo.util.theme.ThemeUtil;

import cn.bmob.v3.BmobUser;

public class BaseActivity<T extends BaseControl> extends BaseAsyncActivity<T> {

    protected UserEntity myEntity;
    protected DisplayImageOptions userImageOptions;
    protected LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.i("log-activity", "(" + getClass().getSimpleName() + ".java:1)");

        ThemeUtil.changeTheme(this);
        initSystemBarTint(true);
        initData();
    }

    private void initData() {
        myEntity = BmobUser.getCurrentUser(this, UserEntity.class);
        userImageOptions = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisc(true)
                .showImageOnLoading(R.drawable.ic_user)
                .showImageForEmptyUri(R.drawable.ic_user)
                .showImageOnFail(R.drawable.ic_user)
                .build();

        GlobalParams.screenWidth = DisplayUtil.getWindowWidth(this);
        GlobalParams.screenHeight = DisplayUtil.getWindowHeight(this);
        loadingDialog = new LoadingDialog(this);
    }

    public void initSystemBarTint(boolean on) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(on);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(on);
            tintManager.setStatusBarTintColor(getColorPrimary());
        }
    }

    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    public int getColorPrimary() {
        TypedValue typedValue = new  TypedValue();
        getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true);
        return typedValue.data;
    }

    public int getDarkColorPrimary() {
        TypedValue typedValue = new  TypedValue();
        getTheme().resolveAttribute(R.attr.colorPrimaryDark, typedValue, true);
        return typedValue.data;
    }

    protected void setOvalShapeViewBackground(View view) {
        if (Build.VERSION.SDK_INT >= 16) {
            view.setBackground(Selector.createOvalShapeSelector(getColorPrimary()));
        } else {
            view.setBackgroundDrawable(Selector.createOvalShapeSelector(getColorPrimary()));
        }
    }

    protected void setRoundRectShapeViewBackground(View view) {
        if (Build.VERSION.SDK_INT >= 16) {
            view.setBackground(Selector.createRoundRectShapeSelector(getColorPrimary()));
        } else {
            view.setBackgroundDrawable(Selector.createRoundRectShapeSelector(getColorPrimary()));
        }
    }

    protected void setRectShapeViewBackground(View view) {
        if (Build.VERSION.SDK_INT >= 16) {
            view.setBackground(Selector.createRectShapeSelector(getColorPrimary()));
        } else {
            view.setBackgroundDrawable(Selector.createRectShapeSelector(getColorPrimary()));
        }
    }

    public void initToolBar(Toolbar toolbar, boolean homeAsUpEnabled, String title) {
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(homeAsUpEnabled);
    }

    public void initToolBar(Toolbar toolbar, boolean homeAsUpEnabled, int resTitle) {
        initToolBar(toolbar, homeAsUpEnabled, getString(resTitle));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
