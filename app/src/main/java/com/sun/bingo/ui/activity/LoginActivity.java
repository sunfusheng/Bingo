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

import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sun.bingo.R;
import com.sun.bingo.constant.ConstantParams;
import com.sun.bingo.control.NavigateManager;
import com.sun.bingo.control.SingleControl;
import com.sun.bingo.entity.SinaUserInfoEntity;
import com.sun.bingo.entity.UserEntity;
import com.sun.bingo.framework.dialog.LoadingDialog;
import com.sun.bingo.framework.dialog.ToastTip;
import com.sun.bingo.framework.http.HttpCallBack.BaseParserCallBack;
import com.sun.bingo.framework.http.HttpControl.HttpControl;
import com.sun.bingo.util.KeyBoardUtil;

import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.OtherLoginListener;
import cn.bmob.v3.listener.UpdateListener;

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
    private LoadingDialog mLoadingDialog;

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
        mLoadingDialog = new LoadingDialog(this);
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
                loginSuccess();
            } else {
                ToastTip.show(LoginActivity.this, values.getString("code", ""));
            }
        }

        @Override
        public void onCancel() {
            ToastTip.show(LoginActivity.this, "暂时只支持微博账号登录哦");
        }

        @Override
        public void onWeiboException(WeiboException e) {
            e.printStackTrace();
        }
    }

    // 新浪账号登录成功后跳转
    private void loginSuccess() {
        if (mLoadingDialog != null) {
            mLoadingDialog.showCancelDialog("正在加载，请稍候....");
        }
        BmobUser.BmobThirdUserAuth authEntity = new BmobUser.BmobThirdUserAuth(BmobUser.BmobThirdUserAuth.SNS_TYPE_WEIBO, mAccessToken.getToken(), mAccessToken.getExpiresTime() + "", mAccessToken.getUid());
        BmobUser.loginWithAuthData(LoginActivity.this, authEntity, new OtherLoginListener() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                myEntity = BmobUser.getCurrentUser(LoginActivity.this, UserEntity.class);
                getSinaUserInfo();
            }

            @Override
            public void onFailure(int i, String s) {
                ToastTip.show(LoginActivity.this, "登录失败");
                mLoadingDialog.dismiss();
            }
        });
    }

    // 获取新浪用户信息
    private void getSinaUserInfo() {
        HttpControl httpControl = new HttpControl();
        httpControl.getSinaUserInfo(getAccountSharedPreferences().access_token(), getAccountSharedPreferences().uid(),
                new BaseParserCallBack<SinaUserInfoEntity>() {
                    @Override
                    protected boolean onSuccessWithObject(SinaUserInfoEntity sinaUserInfoEntity) {
                        updateUserInfo(sinaUserInfoEntity);
                        return super.onSuccessWithObject(sinaUserInfoEntity);
                    }

                    @Override
                    protected void onFailureCallBack(Exception e, String msg) {
                        super.onFailureCallBack(e, msg);
                        gotoMainOrProfile();
                    }

                    @Override
                    protected Class<SinaUserInfoEntity> getCurrentClass() {
                        return SinaUserInfoEntity.class;
                    }
                });
    }

    // 更新Bmob用户信息
    private void updateUserInfo(SinaUserInfoEntity entity) {
        if (entity == null) {
            gotoMainOrProfile();
            return;
        }
        boolean isUpdate = false;
        if (TextUtils.isEmpty(myEntity.getNickName()) && !TextUtils.isEmpty(entity.getScreen_name())) {
            myEntity.setNickName(entity.getScreen_name());
            isUpdate = true;
        }
        if (TextUtils.isEmpty(myEntity.getUserAvatar()) && !TextUtils.isEmpty(entity.getAvatar_hd())) {
            myEntity.setUserAvatar(entity.getAvatar_hd());
            isUpdate = true;
        }
        if (TextUtils.isEmpty(myEntity.getUserSign()) && !TextUtils.isEmpty(entity.getDescription())) {
            myEntity.setUserSign(entity.getDescription());
            isUpdate = true;
        }
        if (isUpdate) {
            myEntity.update(LoginActivity.this, myEntity.getObjectId(), new UpdateListener() {
                @Override
                public void onSuccess() {
                    gotoMainOrProfile();
                }

                @Override
                public void onFailure(int i, String s) {
                    gotoMainOrProfile();
                }
            });
        } else {
            gotoMainOrProfile();
        }
    }

    // 跳转到主界面或用户信息界面
    private void gotoMainOrProfile() {
        mLoadingDialog.dismiss();
        if (TextUtils.isEmpty(myEntity.getNickName()) || TextUtils.isEmpty(myEntity.getUserAvatar())) {
            NavigateManager.gotoProfileActivity(LoginActivity.this, true);
        } else {
            NavigateManager.gotoMainActivity(LoginActivity.this);
        }
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mLoadingDialog.dismiss();
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.push_bottom_in, R.anim.hold_long);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.hold_long, R.anim.push_bottom_out);
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
}
