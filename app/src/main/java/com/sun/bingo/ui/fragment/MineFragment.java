package com.sun.bingo.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sun.bingo.R;
import com.sun.bingo.control.NavigateManager;
import com.sun.bingo.model.UserEntity;
import com.sun.bingo.ui.activity.MainV2Activity;
import com.sun.bingo.ui.activity.SettingsActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobUser;

/**
 * Created by sunfusheng on 16/4/14.
 */
public class MineFragment extends BaseFragment {

    @Bind(R.id.tv_nick_name)
    TextView tvNickName;
    @Bind(R.id.tv_user_sign)
    TextView tvUserSign;
    @Bind(R.id.iv_user_avatar)
    ImageView ivUserAvatar;
    @Bind(R.id.rl_user_info)
    RelativeLayout rlUserInfo;
    @Bind(R.id.tv_my_bingo)
    TextView tvMyBingo;
    @Bind(R.id.tv_my_favorite)
    TextView tvMyFavorite;
    @Bind(R.id.iv_settings_dot)
    ImageView ivSettingsDot;
    @Bind(R.id.rl_settings)
    RelativeLayout rlSettings;

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
            rootView = inflater.inflate(R.layout.fragment_mine, null);
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
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            initUserInfo();
            showDot(getAccountSharedPreferences().is_need_update());
        }
    }

    private void initData() {

    }

    private void initView() {
        initUserInfo();
    }

    private void initListener() {
        rlUserInfo.setOnClickListener(this);
        rlSettings.setOnClickListener(this);
    }

    private void initUserInfo() {
        mUserEntity = BmobUser.getCurrentUser(getActivity(), UserEntity.class);
        if (mUserEntity != null) {
            tvNickName.setText(mUserEntity.getNickName() + "");
            tvUserSign.setText(mUserEntity.getUserSign() + "");
            mImageManager.loadCircleImage(mUserEntity.getUserAvatar(), ivUserAvatar);
        }
    }

    public void showDot(boolean isShow) {
        if (ivSettingsDot != null) {
            ivSettingsDot.setVisibility(isShow ? View.VISIBLE : View.INVISIBLE);
            getAccountSharedPreferences().is_need_update(isShow);
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.rl_user_info:
                NavigateManager.gotoProfileActivity(mActivity, false);
                break;
            case R.id.rl_settings:
                NavigateManager.gotoSpecifiedActivity(mContext, SettingsActivity.class);
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
