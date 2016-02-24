package com.sun.bingo.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sun.bingo.R;
import com.sun.bingo.constant.ConstantParams;
import com.sun.bingo.control.NavigateManager;
import com.sun.bingo.control.SingleControl;
import com.sun.bingo.entity.UserEntity;
import com.sun.bingo.framework.dialog.ToastTip;
import com.sun.bingo.util.KeyBoardUtil;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.OtherLoginListener;

/**
 * Created by sunfusheng on 15/7/27.
 */
public class LoginActivity extends BaseActivity<SingleControl> implements View.OnClickListener {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.tv_login_sina)
    TextView tvLoginSina;
    @Bind(R.id.ll_root_view)
    LinearLayout llRootView;

    private AuthInfo mAuthInfo;
    private SsoHandler mSsoHandler;
    private Oauth2AccessToken mAccessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        initData();
        initView();
        initListener();
    }

    private void initData() {
        mAuthInfo = new AuthInfo(this, ConstantParams.SINA_APP_KEY, ConstantParams.SINA_REDIRECT_URL, ConstantParams.SINA_SCOPE);
        mSsoHandler = new SsoHandler(this, mAuthInfo);
    }

    @SuppressLint("NewApi")
    private void initView() {
        initToolBar(toolbar, false, "登录");
    }

    private void initListener() {
        llRootView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                KeyBoardUtil.hideKeyboard(LoginActivity.this);
                return false;
            }
        });
        tvLoginSina.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_login_sina:
                mSsoHandler.authorize(new AuthListener());
                break;
        }
    }

    class AuthListener implements WeiboAuthListener {
        @Override
        public void onComplete(Bundle values) {
            mAccessToken = Oauth2AccessToken.parseAccessToken(values);

            if (mAccessToken.isSessionValid()) {
                getAccountSharedPreferences().uid(mAccessToken.getUid());
                getAccountSharedPreferences().access_token(mAccessToken.getToken());
                getAccountSharedPreferences().refresh_token(mAccessToken.getRefreshToken());
                getAccountSharedPreferences().expires_in(mAccessToken.getExpiresTime());

                String date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date(mAccessToken.getExpiresTime()));
                String message = String.format(getString(R.string.weibo_token_format), mAccessToken.getToken(), date);
                Logger.d("--->onComplete", "mAccessToken: " + mAccessToken + "\nmessage: " + message);
                loginSuccess();
            } else {
                ToastTip.show(LoginActivity.this, values.getString("code", ""));
            }
        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onWeiboException(WeiboException e) {
            e.printStackTrace();
        }
    }

    // 新浪账号登录成功后跳转
    private void loginSuccess() {
        BmobUser.BmobThirdUserAuth authEntity = new BmobUser.BmobThirdUserAuth(BmobUser.BmobThirdUserAuth.SNS_TYPE_WEIBO, mAccessToken.getToken(), mAccessToken.getExpiresTime() + "", mAccessToken.getUid());
        BmobUser.loginWithAuthData(LoginActivity.this, authEntity, new OtherLoginListener() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                Logger.d("--->onSuccess", "jsonObject: " + jsonObject.toString());
                myEntity = BmobUser.getCurrentUser(LoginActivity.this, UserEntity.class);
                if (TextUtils.isEmpty(myEntity.getNickName()) || TextUtils.isEmpty(myEntity.getUserSign())) {
                    NavigateManager.gotoProfileActivity(LoginActivity.this, true);
                } else {
                    NavigateManager.gotoMainActivity(LoginActivity.this);
                }
                finish();
            }

            @Override
            public void onFailure(int i, String s) {
                ToastTip.show(LoginActivity.this, "登录失败");
            }
        });
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.push_bottom_in, R.anim.hold_long);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        overridePendingTransition(R.anim.push_bottom_in, R.anim.hold_long);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mSsoHandler != null) {
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.hold_long, R.anim.push_bottom_out);
    }
}
