package com.sun.bingo.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.github.siyamed.shapeimageview.CircularImageView;
import com.shamanland.fab.FloatingActionButton;
import com.sun.bingo.R;
import com.sun.bingo.adapter.FragmentAdapter;
import com.sun.bingo.control.NavigateManager;
import com.sun.bingo.entity.UserEntity;
import com.sun.bingo.ui.fragment.MyBingoFragment;
import com.sun.bingo.ui.fragment.SquareBingoFragment;
import com.sun.bingo.util.UserEntityUtil;
import com.sun.bingo.util.theme.ColorChooserDialog;
import com.sun.bingo.util.theme.Selector;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.bmob.v3.BmobUser;


public class MainActivity extends BaseActivity implements ColorChooserDialog.Callback {

    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.tab_layout)
    TabLayout tabLayout;
    @InjectView(R.id.app_bar_layout)
    AppBarLayout appBarLayout;
    @InjectView(R.id.view_pager)
    ViewPager viewPager;
    @InjectView(R.id.main_coordinator_layout)
    CoordinatorLayout mainCoordinatorLayout;
    @InjectView(R.id.main_navigation_layout)
    NavigationView mainNavigationLayout;
    @InjectView(R.id.main_drawer_layout)
    DrawerLayout mainDrawerLayout;
    @InjectView(R.id.floating_action_button)
    FloatingActionButton floatingActionButton;

    private CircularImageView civUserAvatar;
    private TextView tvNickName;
    private TextView tvUserSign;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        initData();
        initView();
        initListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    private void initData() {
        userEntity = BmobUser.getCurrentUser(this, UserEntity.class);
        if (userEntity == null) {
            NavigateManager.gotoLoginActivity(this);
            finish();
        }
    }

    @SuppressLint("NewApi")
    private void initView() {
        initToolBar(toolbar, false, R.string.app_name);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, mainDrawerLayout, toolbar, 0, 0);
        drawerToggle.syncState();
        initTabLayout();

        floatingActionButton.setBackground(Selector.createOvalShapeSelector(getColorPrimary()));
        civUserAvatar = (CircularImageView) mainNavigationLayout.findViewById(R.id.civ_user_avatar);
        tvNickName = (TextView) mainNavigationLayout.findViewById(R.id.tv_nick_name);
        tvUserSign = (TextView) mainNavigationLayout.findViewById(R.id.tv_user_sign);

        UserEntityUtil.setUserAvatarView(civUserAvatar, userEntity.getUserAvatar());
        UserEntityUtil.setTextViewData(tvNickName, userEntity.getNickName());
        UserEntityUtil.setTextViewData(tvUserSign, userEntity.getUserSign());

    }

    private void initTabLayout() {
        List<String> titles = new ArrayList<>();
        titles.add(getResources().getString(R.string.square_bingo_title));
        titles.add(getResources().getString(R.string.my_bingo_title));
        tabLayout.addTab(tabLayout.newTab().setText(titles.get(0)));
        tabLayout.addTab(tabLayout.newTab().setText(titles.get(1)));
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new SquareBingoFragment());
        fragments.add(new MyBingoFragment());
        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager(), fragments, titles);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabsFromPagerAdapter(adapter);
    }

    private void initListener() {
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavigateManager.gotoEditNewBingoActivity(MainActivity.this);
//                NavigateManager.gotoRichEditorActivity(MainActivity.this);
            }
        });

        mainNavigationLayout.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_switch_theme:
                        changeTheme();
                        break;
                    case R.id.nav_favorite_bingo:
                        break;
                }
                return false;
            }
        });

        civUserAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavigateManager.gotoProfileActivity(MainActivity.this);
            }
        });
    }

    private void changeTheme() {
        new ColorChooserDialog().show(this, getSettingsSharedPreferences().themeValue());
    }

    @Override
    public void onColorSelection(int index, int color, int darker) {
        getSettingsSharedPreferences().themeValue(index);
        recreate();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_setting:
                Toast.makeText(MainActivity.this, "menu_setting", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case NavigateManager.PROFILE_REQUEST_CODE:
                userEntity = BmobUser.getCurrentUser(MainActivity.this, UserEntity.class);
                UserEntityUtil.setUserAvatarView(civUserAvatar, userEntity.getUserAvatar());
                UserEntityUtil.setTextViewData(tvNickName, userEntity.getNickName());
                UserEntityUtil.setTextViewData(tvUserSign, userEntity.getUserSign());
                break;
        }
    }
}
