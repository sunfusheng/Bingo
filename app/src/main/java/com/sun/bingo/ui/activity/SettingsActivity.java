package com.sun.bingo.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.sun.bingo.R;
import com.sun.bingo.control.NavigateManager;
import com.sun.bingo.model.eventbus.EventEntity;
import com.sun.bingo.model.eventbus.EventType;
import com.sun.bingo.util.AppUtil;
import com.sun.bingo.util.ShareUtil;
import com.sun.bingo.util.theme.ColorChooserDialog;
import com.sun.bingo.util.update.DownloadApk;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobUser;

/**
 * Created by sunfusheng on 16/5/2.
 */
public class SettingsActivity extends BaseActivity implements ColorChooserDialog.Callback {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_change_theme)
    TextView tvChangeTheme;
    @BindView(R.id.tv_send_to_friend)
    TextView tvSendToFriend;
    @BindView(R.id.tv_share)
    TextView tvShare;
    @BindView(R.id.tv_feedback)
    TextView tvFeedback;
    @BindView(R.id.tv_about)
    TextView tvAbout;
    @BindView(R.id.tv_logout)
    TextView tvLogout;
    @BindView(R.id.iv_version_dot)
    ImageView ivVersionDot;
    @BindView(R.id.tv_version)
    TextView tvVersion;
    @BindView(R.id.rl_check_update)
    RelativeLayout rlCheckUpdate;

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
        tvVersion.setText(AppUtil.getVersionName(this));
        ivVersionDot.setVisibility(getAccountSharedPreferences().is_need_update()? View.VISIBLE:View.INVISIBLE);
    }

    private void initListener() {
        rlCheckUpdate.setOnClickListener(this);
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
            case R.id.rl_check_update:
                new DownloadApk(this).checkVersion(true);
                break;
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
    }

    @Override
    public void onColorSelection(int index, int color, int darker) {
        getSettingsSharedPreferences().themeValue(index);
        recreate();
        EventBus.getDefault().post(new EventEntity(EventType.EVENT_TYPE_CHANGE_THEME));
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
