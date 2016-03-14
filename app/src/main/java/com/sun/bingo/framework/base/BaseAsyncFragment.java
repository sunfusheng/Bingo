package com.sun.bingo.framework.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.sun.bingo.framework.proxy.MessageProxy;
import com.sun.bingo.framework.proxy.ModelMap;
import com.sun.bingo.framework.proxy.common.IRefreshBack;
import com.sun.bingo.framework.proxy.handler.AsyncFragmentHandler;
import com.sun.bingo.framework.proxy.helper.FragmentHelper;

import de.devland.esperandro.Esperandro;

public class BaseAsyncFragment<T extends BaseControl> extends Fragment implements IRefreshBack {

    protected T mControl;
    protected MessageProxy messageProxy;
    protected ModelMap mModel;
    private FragmentHelper mHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHelper = new FragmentHelper(this, new AsyncFragmentHandler(this));
        mHelper.onCreate();
        initParams();
    }

    public void initParams() {
        mModel = mHelper.getModelMap();
        messageProxy = mHelper.getMessageProxy();
        mControl = (T) mHelper.getControl();
    }

    @Override
    public void onResume() {
        super.onResume();
        mHelper.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mHelper.onPause();
    }

    @Override
    public void onDestroyView() {
        mHelper.onDestoryView();
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        mHelper.onDestroy();
        super.onDestroy();
    }

    protected <P> P getSharedPreferences(Class<P> spClass) {
        return Esperandro.getPreferences(spClass, getActivity());
    }

    @Override
    public void onRefresh(int requestCode, Bundle bundle) {

    }

}
