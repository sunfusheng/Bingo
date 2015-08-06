package com.sun.bingo.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.apkfuns.logutils.LogUtils;
import com.sun.bingo.R;
import com.sun.bingo.adapter.RecyclerViewAdapter;
import com.sun.bingo.entity.BingoEntity;
import com.sun.bingo.util.PhoneUtil;
import com.sun.bingo.widget.CircleRefreshLayout;
import com.sun.bingo.widget.ProgressWheel;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

public class DetailActivity extends BaseActivity implements CircleRefreshLayout.OnCircleRefreshListener {

    @InjectView(R.id.backdrop)
    ImageView backdrop;
    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @InjectView(R.id.appbar)
    AppBarLayout appbar;
    @InjectView(R.id.recycler_view)
    RecyclerView recyclerView;
    @InjectView(R.id.circle_refresh_layout)
    CircleRefreshLayout circleRefreshLayout;
    @InjectView(R.id.progress_wheel)
    ProgressWheel progressWheel;
    @InjectView(R.id.main_content)
    CoordinatorLayout mainContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.inject(this);

        initToolBar(toolbar, true, getResources().getString(R.string.my_bingo_title));
        collapsingToolbar.setTitle(getResources().getString(R.string.my_bingo_title));

        initView();
        getBingoEntityList();
    }

    private void initView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        progressWheel.setVisibility(View.VISIBLE);
        circleRefreshLayout.setOnRefreshListener(this);
    }

    protected void completeRefresh() {
        if (circleRefreshLayout != null) {
            circleRefreshLayout.completeRefresh();
        }
        if (progressWheel.getVisibility() == View.VISIBLE) {
            progressWheel.setVisibility(View.GONE);
        }
    }

    @Override
    public void startRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getBingoEntityList();
            }
        }, 1000);
    }

    private void getBingoEntityList() {
        BmobQuery<BingoEntity> newBingoEntities = new BmobQuery<>();
        newBingoEntities.addWhereEqualTo("imei", PhoneUtil.getIMEI(this));
        newBingoEntities.order("-createdAt");
        newBingoEntities.findObjects(this, new FindListener<BingoEntity>() {
            @Override
            public void onSuccess(List<BingoEntity> entities) {
                LogUtils.d(entities);
                recyclerView.setAdapter(new RecyclerViewAdapter(DetailActivity.this, entities));
                completeRefresh();
            }

            @Override
            public void onError(int i, String s) {
                completeRefresh();
            }
        });
    }

    public void checkin(View view) {
        Snackbar.make(view, "checkin success!", Snackbar.LENGTH_SHORT).show();
    }

}
