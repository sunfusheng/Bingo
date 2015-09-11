package com.sun.bingo.framework.base;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.MenuItem;

import com.sun.bingo.R;
import com.sun.bingo.framework.proxy.MessageProxy;
import com.sun.bingo.framework.proxy.ModelMap;
import com.sun.bingo.framework.proxy.common.IRefreshBack;
import com.sun.bingo.framework.proxy.helper.ActivityHelper;

/**
 * 默认使用Toolbar不用ActionBar 在Layout中定义好了Toolbar的布局文件
 */
public class FWBaseActivity<T extends FWBaseControl> extends AppCompatActivity implements IRefreshBack {

    protected T mControl;
    protected MessageProxy messageProxy;
    protected ModelMap mModel;
    private ActivityHelper mHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHelper = new ActivityHelper<T, FWBaseActivity>(this);
        mHelper.onCreate();
        initVar();
    }

    public void initVar() {
        mModel = mHelper.getModelMap();
        messageProxy = mHelper.getMessageProxy();
        mControl = (T) mHelper.getControl();
    }

    @Override
    protected void onStart() {
        mHelper.onStart();
        super.onStart();
    }

    @Override
    protected void onResume() {
        mHelper.onResume();
        super.onResume();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return mHelper.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mHelper.onPause();
    }

    @Override
    protected void onStop() {
        mHelper.onStop();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mHelper.onDestroy();
        super.onDestroy();
    }

    @Override
    public void finish() {
        super.finish();
//        if (!((Object) this).getClass().equals(NewMainEntryActivity.class)) {
//            if (AnimManager.isClose(((Object) this).getClass())) {
//                overridePendingTransition(R.anim.hold_long, R.anim.push_bottom_out);
//            } else {
//                overridePendingTransition(0, 0);
//            }
//        } else {
//            overridePendingTransition(0, 0);
//        }
        overridePendingTransition(0, 0);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
//        if (intent != null && intent.getComponent() != null && !intent.getComponent().getClassName().equals(NewMainEntryActivity.class.getName())) {
//            if (AnimManager.isClose(intent.getComponent().getClassName())) {
//                overridePendingTransition(R.anim.push_bottom_in, R.anim.hold_long);
//            } else {
//                overridePendingTransition(0, 0);
//            }
//            overridePendingTransition(0, 0);
//        }
        overridePendingTransition(0, 0);
    }


//    public void initActionBar(boolean displayHomeAsUpEnabled, int resTitle) {
//        initActionBar(displayHomeAsUpEnabled, getString(resTitle));
//    }
//
//    public void initActionBar(boolean displayHomeAsUpEnabled, String strTitle) {
//        getSupportActionBar().setDisplayHomeAsUpEnabled(displayHomeAsUpEnabled);
//        getSupportActionBar().setTitle(strTitle);
//    }

    public void initActionBar(boolean displayHomeAsUpEnabled, int resTitle) {
        initActionBar(displayHomeAsUpEnabled, getString(resTitle));
    }

    public void initActionBar(boolean displayHomeAsUpEnabled, String strTitle) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar == null) return;
        toolbar.setTitle(strTitle);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(displayHomeAsUpEnabled);
    }


    protected boolean isPaused() {
        return mHelper.isPause();
    }

    protected int getActionBarHeight() {
        int actionBarHeight = getSupportActionBar().getHeight();
        if (actionBarHeight != 0)
            return actionBarHeight;
        final TypedValue tv = new TypedValue();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))
                actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
        } else if (getTheme().resolveAttribute(R.attr.actionBarSize, tv, true))
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
        return actionBarHeight;
    }

    @Override
    public void onRefresh(int requestCode, Bundle bundle) {

    }

}
