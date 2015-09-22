package com.sun.bingo.framework.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.sun.bingo.R;
import com.sun.bingo.framework.proxy.MessageProxy;
import com.sun.bingo.framework.proxy.ModelMap;
import com.sun.bingo.framework.proxy.common.IRefreshBack;
import com.sun.bingo.framework.proxy.helper.ActivityHelper;
import com.sun.bingo.ui.activity.LoginActivity;
import com.sun.bingo.ui.activity.MainActivity;
import com.umeng.analytics.MobclickAgent;

/**
 * 默认使用Toolbar不用ActionBar
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
        MobclickAgent.onResume(this); //友盟统计
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return mHelper.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mHelper.onPause();
        MobclickAgent.onPause(this); //友盟统计
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
        if (!((Object) this).getClass().equals(MainActivity.class) && !((Object) this).getClass().equals(LoginActivity.class)) {
            overridePendingTransition(R.anim.hold_long, R.anim.slide_right_out);
        } else {
            overridePendingTransition(0, 0);
        }
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        if (intent != null && intent.getComponent() != null && !intent.getComponent().getClassName().equals(MainActivity.class.getName())) {
            overridePendingTransition(R.anim.slide_right_in, R.anim.hold_long);
        } else {
            overridePendingTransition(0, 0);
        }
    }

    protected boolean isPaused() {
        return mHelper.isPause();
    }

    @Override
    public void onRefresh(int requestCode, Bundle bundle) {

    }

}
