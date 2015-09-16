package com.sun.bingo.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.jpardogo.android.googleprogressbar.library.GoogleProgressBar;
import com.orhanobut.logger.Logger;
import com.sun.bingo.R;
import com.sun.bingo.widget.CircleRefreshLayout;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by sunfusheng on 15/8/14.
 */
public abstract class BaseListActivity extends BaseActivity implements CircleRefreshLayout.OnCircleRefreshListener {

    @InjectView(R.id.recycler_view)
    RecyclerView recyclerView;
    @InjectView(R.id.circle_refresh_layout)
    CircleRefreshLayout circleRefreshLayout;
    @InjectView(R.id.google_progressBar)
    GoogleProgressBar googleProgressBar;
    @InjectView(R.id.toolbar)
    Toolbar toolbar;

    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bingo_list);
        ButterKnife.inject(this);
        Logger.i("(" + getClass().getSimpleName() + ".java)");

        initView();
        getBingoEntityList();
    }

    private void initView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        googleProgressBar.setVisibility(View.VISIBLE);
        circleRefreshLayout.setOnRefreshListener(this);
    }

    protected void completeRefresh() {
        if (circleRefreshLayout != null) {
            circleRefreshLayout.completeRefresh();
        }
        if (googleProgressBar.getVisibility() == View.VISIBLE) {
            googleProgressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void startRefresh() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getBingoEntityList();
            }
        }, 1000);
    }

    protected abstract void getBingoEntityList();

    protected void initToolBar(boolean homeAsUpEnabled, String title) {
        initToolBar(toolbar, homeAsUpEnabled, title);
    }

    protected void initToolBar(boolean homeAsUpEnabled, int resTitle) {
        initToolBar(toolbar, homeAsUpEnabled, resTitle);
    }
}
