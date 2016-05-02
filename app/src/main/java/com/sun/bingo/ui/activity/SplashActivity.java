package com.sun.bingo.ui.activity;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.sun.bingo.R;
import com.sun.bingo.control.NavigateManager;
import com.sun.bingo.model.UserEntity;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobUser;

/**
 * Created by sunfusheng on 16/4/14.
 */
public class SplashActivity extends BaseActivity {

    @Bind(R.id.iv_logo)
    ImageView ivLogo;
    @Bind(R.id.rl_root_view)
    RelativeLayout rlRootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        initData();
        initView();
    }

    private void initData() {

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
                startActivity();
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
//            NavigateManager.gotoMainActivity(this);
            NavigateManager.gotoSpecifiedActivity(this, MainV2Activity.class);
        }
        finish();
    }
}
