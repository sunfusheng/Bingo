package com.sun.bingo.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sun.bingo.R;
import com.sun.bingo.ui.activity.MainV2Activity;

import butterknife.ButterKnife;

/**
 * Created by sunfusheng on 16/4/14.
 */
public class MsgFragment extends BaseFragment {

    private MainV2Activity mActivity;
    private View rootView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = (MainV2Activity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // 判断rootView是否为空，避免每次来到页面都加载
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_msg, null);
            ButterKnife.bind(this, rootView);

            initData();
            initView();
            initListener();
        } else {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }
        }
        return rootView;
    }

    private void initData() {

    }

    private void initView() {

    }

    private void initListener() {

    }
}
