package com.sun.bingo.ui.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.sun.bingo.R;
import com.sun.bingo.constant.ConstantParams;
import com.sun.bingo.control.NavigateManager;
import com.sun.bingo.entity.UserEntity;
import com.sun.bingo.framework.dialog.TipDialog;
import com.sun.bingo.util.KeyBoardUtil;
import com.sun.bingo.util.theme.Selector;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.RequestSMSCodeListener;

/**
 * Created by sunfusheng on 15/7/27.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {

    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.met_phone_num)
    MaterialEditText metPhoneNum;
    @InjectView(R.id.met_verify_code)
    MaterialEditText metVerifyCode;
    @InjectView(R.id.tv_verify_code)
    TextView tvVerifyCode;
    @InjectView(R.id.tv_commit)
    TextView tvCommit;
    @InjectView(R.id.ll_root_view)
    LinearLayout llRootView;

    private Handler mHandler = new Handler();
    private CountDownThread countDownThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);

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
        tvVerifyCode.setBackground(Selector.createRoundRectShapeSelector(getColorPrimary()));
        tvCommit.setBackground(Selector.createRoundRectShapeSelector(getColorPrimary()));
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

    private void requestSMSCode() {
        String phoneNum = metPhoneNum.getText().toString().trim();
        if (TextUtils.isEmpty(phoneNum)) {
            TipDialog.showToastDialog(LoginActivity.this, "请输入手机号码");
            return ;
        }
        if (phoneNum.length() != 11) {
            TipDialog.showToastDialog(LoginActivity.this, "请输入有效的手机号码");
            return ;
        }
        BmobSMS.requestSMSCode(this, metPhoneNum.getText().toString().trim(), ConstantParams.SMS_LOGIN_VERIFY_CODE, new RequestSMSCodeListener() {
            @Override
            public void done(Integer integer, BmobException e) {
                if (e == null) {
                    TipDialog.showToastDialog(LoginActivity.this, "验证码发送成功，注意查收");
                    mHandler.post(countDownThread);
                }
            }

        });
    }

    private void signOrLoginByMobilePhone() {
        UserEntity userEntity = new UserEntity();
        userEntity.signOrLoginByMobilePhone(this, metPhoneNum.getText().toString().trim(),
                metVerifyCode.getText().toString().trim(), new LogInListener<UserEntity>() {
                    @Override
                    public void done(UserEntity userEntity, BmobException e) {
                        if (e == null) {
                            NavigateManager.gotoMainActivity(LoginActivity.this);
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
}
