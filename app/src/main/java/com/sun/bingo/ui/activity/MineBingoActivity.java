package com.sun.bingo.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sun.bingo.R;
import com.sun.bingo.ui.fragment.FavoriteFragment;
import com.sun.bingo.ui.fragment.MyBingoFragment;
import com.sun.bingo.util.UserEntityUtil;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by sunfusheng on 16/5/4.
 */
public class MineBingoActivity extends BaseActivity {

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.iv_user_avatar)
    ImageView ivUserAvatar;
    @Bind(R.id.tv_nick_name)
    TextView tvNickName;
    @Bind(R.id.tv_user_sign)
    TextView tvUserSign;
    @Bind(R.id.tv_location)
    TextView tvLocation;
    @Bind(R.id.ll_user_layout)
    LinearLayout llUserLayout;
    @Bind(R.id.fl_container)
    FrameLayout flContainer;

    public static final int MY_SHARE = 0;
    public static final int MY_FAVORITE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_bingo);
        ButterKnife.bind(this);

        initData();
        initView();
        initListener();
    }

    private void initData() {
        int index = getIntent().getIntExtra("index", MY_SHARE);
        if (index == MY_SHARE) {
            loadSpecifiedFragment(new MyBingoFragment());
        } else if (index == MY_FAVORITE) {
            loadSpecifiedFragment(new FavoriteFragment());
        }
    }

    private void initView() {
        if (myEntity != null) {
            tvNickName.setText(TextUtils.isEmpty(myEntity.getNickName()) ? "未知" : myEntity.getNickName());
            tvUserSign.setText(TextUtils.isEmpty(myEntity.getUserSign()) ? "还没有个性签名" : myEntity.getUserSign());
            UserEntityUtil.setUserAvatarView(mContext, myEntity.getUserAvatar(), ivUserAvatar);
            tvLocation.setText(myEntity.getCity() + " " + myEntity.getDistrict());
        }
    }

    private void initListener() {
        ivBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }

    private void loadSpecifiedFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fl_container, fragment);
        fragmentTransaction.commitAllowingStateLoss();
    }
}
