package com.sun.bingo.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;

import com.sun.bingo.R;
import com.sun.bingo.ui.fragment.MineFragment;
import com.sun.bingo.ui.fragment.MsgFragment;
import com.sun.bingo.ui.fragment.SquareBingoFragment;
import com.sun.bingo.widget.IconWithTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * Created by sunfusheng on 16/4/14.
 */
public class MainV2Activity extends BaseActivity implements ViewPager.OnPageChangeListener {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.view_pager)
    ViewPager viewPager;
    @Bind(R.id.tab_home)
    IconWithTextView tabHome;
    @Bind(R.id.rl_tab_home)
    RelativeLayout rlTabHome;
    @Bind(R.id.tab_msg)
    IconWithTextView tabMsg;
    @Bind(R.id.rl_tab_msg)
    RelativeLayout rlTabMsg;
    @Bind(R.id.tab_mine)
    IconWithTextView tabMine;
    @Bind(R.id.rl_tab_mine)
    RelativeLayout rlTabMine;

    private List<IconWithTextView> mTabIconList = new ArrayList<>();
    private List<Fragment> mFragmentList = new ArrayList<>();
    private FragmentPagerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_v2);
        ButterKnife.bind(this);

        initData();
        initView();
        initListener();
    }

    private void initData() {
        mTabIconList.add(tabHome);
        mTabIconList.add(tabMsg);
        mTabIconList.add(tabMine);

        mFragmentList.add(new SquareBingoFragment());
        mFragmentList.add(new MsgFragment());
        mFragmentList.add(new MineFragment());

        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return mTabIconList.size();
            }

            @Override
            public Fragment getItem(int position) {
                return mFragmentList.get(position);
            }
        };
    }

    private void initView() {
        initToolBar(toolbar, false, R.string.app_name);
        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(mAdapter);

        resetTabList();
        tabHome.setIconAlpha(1.0f);
        viewPager.setCurrentItem(0, false);
    }

    private void initListener() {
        rlTabHome.setOnClickListener(this);
        rlTabMsg.setOnClickListener(this);
        rlTabMine.setOnClickListener(this);

        viewPager.addOnPageChangeListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        resetTabList();
        switch (v.getId()) {
            case R.id.rl_tab_home:
                tabHome.setIconAlpha(1.0f);
                viewPager.setCurrentItem(0, false);
                break;
            case R.id.rl_tab_msg:
                tabMsg.setIconAlpha(1.0f);
                viewPager.setCurrentItem(1, false);
                break;
            case R.id.rl_tab_mine:
                tabMine.setIconAlpha(1.0f);
                viewPager.setCurrentItem(2, false);
                break;
        }
    }

    private void resetTabList() {
        for (int i = 0; i < mTabIconList.size(); i++) {
            mTabIconList.get(i).setIconAlpha(0.0f);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (positionOffset > 0) {
            IconWithTextView left = mTabIconList.get(position);
            IconWithTextView right = mTabIconList.get(position + 1);
            left.setIconAlpha(1 - positionOffset);
            right.setIconAlpha(positionOffset);
        }
    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
