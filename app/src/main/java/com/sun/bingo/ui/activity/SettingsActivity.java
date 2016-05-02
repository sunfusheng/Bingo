package com.sun.bingo.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.sun.bingo.R;
import com.sun.bingo.control.NavigateManager;
import com.sun.bingo.util.ShareUtil;
import com.sun.bingo.util.theme.ColorChooserDialog;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobUser;

/**
 * Created by sunfusheng on 16/5/2.
 */
public class SettingsActivity extends BaseActivity implements ColorChooserDialog.Callback {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.tv_change_theme)
    TextView tvChangeTheme;
    @Bind(R.id.tv_send_to_friend)
    TextView tvSendToFriend;
    @Bind(R.id.tv_share)
    TextView tvShare;
    @Bind(R.id.tv_feedback)
    TextView tvFeedback;
    @Bind(R.id.tv_about)
    TextView tvAbout;
    @Bind(R.id.tv_logout)
    TextView tvLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);

        initData();
        initView();
        initListener();
    }

    private void initData() {

    }

    private void initView() {
        initToolBar(toolbar, true, R.string.action_settings);
    }

    private void initListener() {
        tvChangeTheme.setOnClickListener(this);
        tvSendToFriend.setOnClickListener(this);
        tvShare.setOnClickListener(this);
        tvFeedback.setOnClickListener(this);
        tvAbout.setOnClickListener(this);
        tvLogout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_change_theme:
                changeTheme();
                break;
            case R.id.tv_send_to_friend:
                ShareUtil.sendToFriend(mContext);
                break;
            case R.id.tv_share:
                ShareUtil.share(mContext);
                break;
            case R.id.tv_feedback:
                ShareUtil.feedback(mContext);
                break;
            case R.id.tv_about:
                NavigateManager.gotoSpecifiedActivity(mContext, AboutActivity.class);
                break;
            case R.id.tv_logout:
                logout();
                break;
        }
    }

    //设置主题
    private void changeTheme() {
        new ColorChooserDialog().show(this, getSettingsSharedPreferences().themeValue());
        NavigateManager.gotoMainActivity(mActivity);
        finish();
    }

    @Override
    public void onColorSelection(int index, int color, int darker) {
        getSettingsSharedPreferences().themeValue(index);
        recreate();
    }

    // 退出登录
    private void logout() {
        new MaterialDialog.Builder(this)
                .title("提示")
                .content("确认退出登录？")
                .contentColor(getResources().getColor(R.color.font_black_3))
                .positiveText(R.string.ok)
                .negativeText(R.string.cancel)
                .negativeColor(getResources().getColor(R.color.font_black_3))
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        getAccountSharedPreferences().uid(null);
                        getAccountSharedPreferences().access_token(null);
                        getAccountSharedPreferences().refresh_token(null);
                        getAccountSharedPreferences().expires_in(0);
                        BmobUser.logOut(mContext);
                        NavigateManager.gotoMainActivity(mActivity);
                        finish();
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                    }
                })
                .show();
    }
}
