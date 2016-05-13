package com.sun.bingo.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.sun.bingo.R;
import com.sun.bingo.control.NavigateManager;
import com.sun.bingo.model.eventbus.EventEntity;
import com.sun.bingo.model.eventbus.EventType;
import com.sun.bingo.ui.fragment.MineFragment;
import com.sun.bingo.ui.fragment.MsgFragment;
import com.sun.bingo.ui.fragment.SquareBingoFragment;
import com.sun.bingo.util.update.DownloadApk;
import com.sun.bingo.widget.IconWithTextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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
    @Bind(R.id.iv_mine_dot)
    ImageView ivMineDot;

    private List<IconWithTextView> mTabIconList = new ArrayList<>();
    private List<Fragment> mFragmentList = new ArrayList<>();
    private FragmentPagerAdapter mAdapter;
    private MineFragment mineFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_v2);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        initData();
        initView();
        initListener();
    }

    private void initData() {
        mTabIconList.add(tabHome);
        mTabIconList.add(tabMsg);
        mTabIconList.add(tabMine);

        mineFragment = new MineFragment();

        mFragmentList.add(new SquareBingoFragment());
        mFragmentList.add(new MsgFragment());
        mFragmentList.add(mineFragment);

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

        checkVersion();
    }

    private void checkVersion() {
        if (!TextUtils.isEmpty(getAccountSharedPreferences().ignore_version_name())) {
            ivMineDot.setVisibility(View.VISIBLE);
            getAccountSharedPreferences().is_need_update(true);
            if (mineFragment != null) {
                mineFragment.showDot(true);
            }
        } else {
            new DownloadApk(this).checkVersion(false);
        }
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_v2, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                NavigateManager.gotoEditNewBingoActivity(mActivity, "");
                return true;
            case R.id.item_github:
                NavigateManager.gotoWebPageActivity(mContext, getString(R.string.title_github), getString(R.string.url_github));
                return true;
            case R.id.item_jianshu:
                NavigateManager.gotoWebPageActivity(mContext, getString(R.string.title_jianshu), getString(R.string.url_jianshu));
                return true;
            case R.id.item_blog:
                NavigateManager.gotoWebPageActivity(mContext, getString(R.string.title_blog), getString(R.string.url_blog));
                return true;
            case R.id.item_weibo:
                NavigateManager.gotoWebPageActivity(mContext, getString(R.string.title_weibo), getString(R.string.url_weibo));
                return true;
            default:
                return super.onOptionsItemSelected(item);
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
    public void onPageSelected(int position) {}

    @Override
    public void onPageScrollStateChanged(int state) {}

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventBusListener(EventEntity event) {
        switch (event.getType()) {
            case EventType.EVENT_TYPE_CHANGE_THEME:
                recreate();
                break;
            case EventType.EVENT_TYPE_UPDATE_APP:
                ivMineDot.setVisibility(event.getArg1() == 0? View.VISIBLE:View.INVISIBLE);
                getAccountSharedPreferences().is_need_update(event.getArg1() == 0? true:false);
                if (mineFragment != null) {
                    mineFragment.showDot(event.getArg1() == 0? true:false);
                }
                break;
        }
    }
}
