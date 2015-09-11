package com.sun.bingo.framework.proxy.helper;

import android.view.MenuItem;

import com.sun.bingo.framework.base.FWBaseActivity;
import com.sun.bingo.framework.base.FWBaseControl;
import com.sun.bingo.framework.proxy.handler.ActivityHandler;

public class ActivityHelper<T extends FWBaseControl, R extends FWBaseActivity> extends BaseAsyncHelper<T, R> {

    private boolean isPause;

    public ActivityHelper(R refrenceObj) {
        super(refrenceObj, new ActivityHandler(refrenceObj));
    }

    public void onCreate() {
        super.onCreate();
    }

    public void onResume() {
        isPause = false;
        super.onResume();
    }

    public void onPause() {
        isPause = true;
        super.onPause();
    }

    public void onDestroy() {
        super.onDestroy();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mReferenceObj.finish();
                return true;
        }
        return false;
    }

    public boolean isPause() {
        return isPause;
    }

}
