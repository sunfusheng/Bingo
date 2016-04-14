package com.sun.bingo.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

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

    protected Context mContext;
    protected Activity mActivity;

    protected UserEntity myEntity;
    protected LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.i("log-activity", "(" + getClass().getSimpleName() + ".java:1)");

        mContext = this;
        mActivity = this;

        ThemeUtil.changeTheme(this);
        initSystemBarTint();
        init();
    }

    private void init() {
        myEntity = BmobUser.getCurrentUser(this, UserEntity.class);
        GlobalParams.screenWidth = DisplayUtil.getWindowWidth(this);
        GlobalParams.screenHeight = DisplayUtil.getWindowHeight(this);
        loadingDialog = new LoadingDialog(this);
    }

    public void initSystemBarTint() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintColor(getColorPrimary());
        }
    }

    // 设置状态栏颜色
    public void setStatusBarTintColor(int resId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintColor(getResources().getColor(resId));
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
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(homeAsUpEnabled);
        }
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

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        if (intent != null && intent.getComponent() != null && !intent.getComponent().getClassName().equals(SplashActivity.class.getName())) {
            overridePendingTransition(android.R.anim.fade_in, R.anim.hold_long);
        }
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        if (intent != null && intent.getComponent() != null && !intent.getComponent().getClassName().equals(SplashActivity.class.getName())) {
            overridePendingTransition(android.R.anim.fade_in, R.anim.hold_long);
        }
    }

    @Override
    public void finish() {
        super.finish();
        if (!((Object) this).getClass().equals(SplashActivity.class) && !((Object) this).getClass().equals(MainActivity.class) && !((Object) this).getClass().equals(LoginActivity.class)) {
            overridePendingTransition(R.anim.hold_long, android.R.anim.fade_out);
        }
    }

}
