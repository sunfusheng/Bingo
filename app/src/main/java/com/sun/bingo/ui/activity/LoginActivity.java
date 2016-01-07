package com.sun.bingo.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.sun.bingo.R;
import com.sun.bingo.constant.ConstantParams;
import com.sun.bingo.control.NavigateManager;
import com.sun.bingo.entity.UserEntity;
import com.sun.bingo.framework.dialog.ToastTip;
import com.sun.bingo.util.KeyBoardUtil;

import butterknife.ButterKnife;
import butterknife.Bind;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.RequestSMSCodeListener;

/**
 * Created by sunfusheng on 15/7/27.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.met_phone_num)
    MaterialEditText metPhoneNum;
    @Bind(R.id.met_verify_code)
    MaterialEditText metVerifyCode;
    @Bind(R.id.tv_verify_code)
    TextView tvVerifyCode;
    @Bind(R.id.tv_commit)
    TextView tvCommit;
    @Bind(R.id.ll_root_view)
    LinearLayout llRootView;

    private Handler mHandler = new Handler();
    private CountDownThread countDownThread;

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
        countDownThread = new CountDownThread();
    }

    @SuppressLint("NewApi")
    private void initView() {
        initToolBar(toolbar, false, "登录");
        setRoundRectShapeViewBackground(tvVerifyCode);
        setRoundRectShapeViewBackground(tvCommit);
    }

    private void initListener() {
        llRootView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                KeyBoardUtil.hideKeyboard(LoginActivity.this);
                return false;
            }
        });
        tvVerifyCode.setOnClickListener(this);
        tvCommit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_verify_code:
                requestSMSCode();
                break;
            case R.id.tv_commit:
                signOrLoginByMobilePhone();
                break;
        }
    }

    private boolean isPhoneNumOK() {
        String phoneNum = metPhoneNum.getText().toString().trim();
        if (TextUtils.isEmpty(phoneNum)) {
            ToastTip.show(LoginActivity.this, "请输入手机号码");
            return false;
        }
        if (phoneNum.length() != 11) {
            ToastTip.show(LoginActivity.this, "请输入有效的手机号码");
            return false;
        }
        return true;
    }

    private boolean isVerifyCodeOK() {
        String verifyCode = metVerifyCode.getText().toString().trim();
        if (TextUtils.isEmpty(verifyCode)) {
            ToastTip.show(LoginActivity.this, "请输入您收到的验证码");
            return false;
        }
        return true;
    }

    private void requestSMSCode() {
        if (!isPhoneNumOK()) return;
        BmobSMS.requestSMSCode(this, metPhoneNum.getText().toString().trim(), ConstantParams.SMS_LOGIN_VERIFY_CODE, new RequestSMSCodeListener() {
            @Override
            public void done(Integer integer, BmobException e) {
                if (e == null) {
                    ToastTip.show(LoginActivity.this, "验证码发送成功，请注意查收");
                    mHandler.post(countDownThread);
                } else {
                    Logger.d("BmobException", "Error code: "+e.getErrorCode()+", "+e.getMessage());
                    ToastTip.show(LoginActivity.this, "Error code: " + e.getErrorCode() + ", " + e.getMessage(), Toast.LENGTH_SHORT);
                }
            }

        });
    }

    private void signOrLoginByMobilePhone() {
        if (!isPhoneNumOK()) return;
        if (!isVerifyCodeOK()) return;
        UserEntity userEntity = new UserEntity();
        userEntity.signOrLoginByMobilePhone(this, metPhoneNum.getText().toString().trim(),
                metVerifyCode.getText().toString().trim(), new LogInListener<UserEntity>() {
                    @Override
                    public void done(UserEntity userEntity, BmobException e) {
                        if (e == null) {
                            if (TextUtils.isEmpty(userEntity.getNickName()) || TextUtils.isEmpty(userEntity.getUserSign()) || TextUtils.isEmpty(userEntity.getUserSign())) {
                                NavigateManager.gotoProfileActivity(LoginActivity.this, true);
                            } else {
                                NavigateManager.gotoMainActivity(LoginActivity.this);
                            }
                            finish();
                        } else {
                            Logger.d("BmobException", "Error code: "+e.getErrorCode()+", "+e.getMessage());
                            ToastTip.show(LoginActivity.this, "Error code: " + e.getErrorCode() + ", " + e.getMessage(), Toast.LENGTH_SHORT);
                        }
                    }
                });
    }

    /**
     * 倒计时类
     */
    private class CountDownThread implements Runnable {

        private int value = 60; //倒计时长(S)
        private static final int UPDATE_VALUE_FREQUENCY = 1000; //更新频率（ms）
        private boolean isChanging; //是否处于倒计时中

        @Override
        public void run() {
            if (value > 0) {
                isChanging = true;
                tvVerifyCode.setEnabled(false);
                tvVerifyCode.setText(getChangeText(true));
                mHandler.postDelayed(this, UPDATE_VALUE_FREQUENCY);
            } else {
                isChanging = false;
                value = 60;
                tvVerifyCode.setEnabled(true);
                tvVerifyCode.setText(getChangeText(false));
            }
        }

        //获取变化中的值
        private String getChangeText(boolean isChanging) {
            if (isChanging) {
                return getString(R.string.login_get_verify_code, value--);
            }
            return getString(R.string.login_verify_code);
        }

        //数据是否处于变化中
        public boolean getIsChanging() {
            return isChanging;
        }
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
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.hold_long, R.anim.push_bottom_out);
    }
}
