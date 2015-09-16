package com.sun.bingo.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mingle.widget.LoadingView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.orhanobut.logger.Logger;
import com.sun.bingo.R;
import com.sun.bingo.adapter.RecyclerViewAdapter;
import com.sun.bingo.entity.BingoEntity;
import com.sun.bingo.entity.UserEntity;
import com.sun.bingo.framework.base.FWBaseControl;
import com.sun.bingo.framework.base.FWBaseFragment;
import com.sun.bingo.widget.CircleRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.bmob.v3.BmobUser;


public abstract class BaseFragment<T extends FWBaseControl> extends FWBaseFragment<T> implements CircleRefreshLayout.OnCircleRefreshListener {

    @InjectView(R.id.recycler_view)
    RecyclerView recyclerView;
    @InjectView(R.id.circle_refresh_layout)
    CircleRefreshLayout circleRefreshLayout;
    @InjectView(R.id.loadingView)
    LoadingView loadingView;

    private Handler mHandler = new Handler();
    protected UserEntity userEntity;

    private int lastVisibleItem;
    private LinearLayoutManager mLinearLayoutManager;

    protected List<BingoEntity> mEntities;
    protected RecyclerViewAdapter mAdapter;
    protected int pageCount = 10;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.i("(" + getClass().getSimpleName() + ".java)");
        _initData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_bingo_list, container, false);
        ButterKnife.inject(this, rootView);

        _initView();
        _initListener();
        startRefresh();
        return rootView;
    }

    private void _initData() {
        userEntity = BmobUser.getCurrentUser(getActivity(), UserEntity.class);
    }

    private void _initView() {
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLinearLayoutManager);
        loadingView.setVisibility(View.VISIBLE);
    }

    private void _initListener() {
        mEntities = new ArrayList<>();
        mAdapter = new RecyclerViewAdapter(getActivity(), mEntities);
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnScrollListener(new PauseOnScrollListener());
        circleRefreshLayout.setOnRefreshListener(this);
    }

    class PauseOnScrollListener extends RecyclerView.OnScrollListener {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            switch (newState) {
                case RecyclerView.SCROLL_STATE_IDLE:
                    ImageLoader.getInstance().resume();
                    if (lastVisibleItem+1 == recyclerView.getAdapter().getItemCount()) {
                        loadingingData();
                    }
                    break;
                case RecyclerView.SCROLL_STATE_DRAGGING:
                    ImageLoader.getInstance().pause();
                    break;
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            lastVisibleItem = mLinearLayoutManager.findLastVisibleItemPosition();
        }
    }

    protected void completeRefresh() {
        if (circleRefreshLayout != null) {
            circleRefreshLayout.completeRefresh();
        }
        if (loadingView != null && loadingView.getVisibility() == View.VISIBLE) {
            loadingView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @Override
    public void startRefresh() {
        //让子弹飞一会儿，防止刷新太快哦
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                refreshingData();
            }
        }, 1000);
    }

    protected abstract void refreshingData(); //下拉刷新数据
    protected abstract void loadingingData(); //上拉加载数据

}
