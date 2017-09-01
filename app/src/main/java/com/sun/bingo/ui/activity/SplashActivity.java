package com.sun.bingo.ui.activity;

import android.Manifest;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.framework.util.Utils;
import com.framework.util.permission.IPermissionCallback;
import com.sun.bingo.BingoApp;
import com.sun.bingo.R;
import com.sun.bingo.constant.ConstantParams;
import com.sun.bingo.control.NavigateManager;
import com.sun.bingo.model.UserEntity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import im.fir.sdk.FIR;

/**
 * Created by sunfusheng on 16/4/14.
 */
public class SplashActivity extends BaseActivity {

    @BindView(R.id.iv_logo)
    ImageView ivLogo;
    @BindView(R.id.rl_root_view)
    RelativeLayout rlRootView;

    private static final String[] PERMISSIONS = {Manifest.permission.READ_PHONE_STATE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        rlRootView.setBackgroundColor(getColorPrimary());

        AnimationSet set = new AnimationSet(true);
        AlphaAnimation alphaAnim = new AlphaAnimation(0.5f, 1.0f);
        set.addAnimation(alphaAnim);

        ScaleAnimation scaleAnim = new ScaleAnimation(1.0f, 1.5f, 1.0f, 1.5f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        set.addAnimation(scaleAnim);
        set.setFillAfter(true);
        set.setDuration(1000);

        set.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                checkPermission(PERMISSIONS, new IPermissionCallback() {
                    @Override
                    public void onCheckPermission(List<String> failedPermissions) {
                        if (Utils.isEmpty(failedPermissions)) {
                            FIR.init(BingoApp.getContext());
                            Bmob.initialize(BingoApp.getContext(), ConstantParams.BMOB_APP_ID);
                            startActivity();
                        }
                    }
                });
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        ivLogo.startAnimation(set);
    }

    private void startActivity() {
        myEntity = BmobUser.getCurrentUser(this, UserEntity.class);
        if (myEntity == null) {
            NavigateManager.gotoLoginActivity(this);
        } else {
            NavigateManager.gotoMainActivity(this);
//            NavigateManager.gotoSpecifiedActivity(this, MainV2Activity.class);
        }
        finish();
    }
}
