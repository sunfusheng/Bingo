package com.sun.bingo.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.mingle.widget.LoadingView;
import com.orhanobut.logger.Logger;
import com.sun.bingo.R;
import com.sun.bingo.adapter.RecyclerViewAdapter;
import com.sun.bingo.entity.BingoEntity;
import com.sun.bingo.entity.UserEntity;
import com.sun.bingo.framework.base.BaseAsyncFragment;
import com.sun.bingo.framework.base.BaseControl;
import com.sun.bingo.framework.eventbus.EventEntity;
import com.sun.bingo.framework.eventbus.EventType;
import com.sun.bingo.widget.CircleRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobUser;
import de.greenrobot.event.EventBus;


public abstract class BaseListFragment<T extends BaseControl> extends BaseAsyncFragment<T> implements CircleRefreshLayout.OnCircleRefreshListener {

    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.circle_refresh_layout)
    CircleRefreshLayout circleRefreshLayout;
    @Bind(R.id.loadingView)
    LoadingView loadingView;
    @Bind(R.id.tv_status)
    TextView tvStatus;
    @Bind(R.id.ll_status)
    FrameLayout llStatus;

    protected UserEntity userEntity;

    private int lastVisibleItem;
    private LinearLayoutManager mLinearLayoutManager;

    protected List<BingoEntity> mEntities;
    protected RecyclerViewAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.i("log-fragment", "(" + getClass().getSimpleName() + ".java)");
        initData();
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_bingo_list, container, false);
        ButterKnife.bind(this, rootView);

        initView();
        initListener();
        startRefresh();
        return rootView;
    }

    private void initData() {
        userEntity = BmobUser.getCurrentUser(getActivity(), UserEntity.class);
    }

    private void initView() {
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLinearLayoutManager);
        loadingView.setVisibility(View.VISIBLE);

        mEntities = new ArrayList<>();
        mAdapter = new RecyclerViewAdapter(getActivity(), mEntities);
        recyclerView.setAdapter(mAdapter);
    }

    private void initListener() {
        recyclerView.addOnScrollListener(new PauseOnScrollListener());
        circleRefreshLayout.setOnRefreshListener(this);
    }

    class PauseOnScrollListener extends RecyclerView.OnScrollListener {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            switch (newState) {
                case RecyclerView.SCROLL_STATE_IDLE:
                    int size = recyclerView.getAdapter().getItemCount();
                    if (lastVisibleItem + 1 == size && mAdapter.isLoadMoreShown() &&
                            !mAdapter.getLoadMoreViewText().equals(getString(R.string.load_data_adequate))) {
                        onScrollLast();
                    }
                    break;
                case RecyclerView.SCROLL_STATE_DRAGGING:
                    break;
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            lastVisibleItem = mLinearLayoutManager.findLastVisibleItemPosition();
        }
    }

    private void completeRefresh() {
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
        EventBus.getDefault().unregister(this);
    }

    public void onEventMainThread(EventEntity event) {
        switch (event.getType()) {
            case EventType.UPDATE_BINGO_LIST:
                onRefreshStart();
                break;
        }
    }

    @Override
    public void startRefresh() {
        //让子弹飞一会儿，防止刷新太快哦
        messageProxy.postRunnableDelay(new Runnable() {
            @Override
            public void run() {
                onRefreshStart();
            }
        }, 500);
    }

    /**
     * －－－－－－－－－－－－－－－－－－－－－
     * 请求列表数据的回调接口们，嗨，你们好。
     * －－－－－－－－－－－－－－－－－－－－－
     */

    //数据为空
    public void getDataEmpty() {
        completeRefresh();
        llStatus.setVisibility(View.VISIBLE);
        mAdapter.setLoadMoreViewVisibility(View.GONE);

        tvStatus.setVisibility(View.VISIBLE);
        tvStatus.setText(getString(emptyDataString()));

        mEntities.clear();
        mAdapter.notifyDataSetChanged();
    }

    //数据足够PAGE_SIZE
    public void getDataAdequate() {
        completeRefresh();
        llStatus.setVisibility(View.GONE);
        mAdapter.setLoadMoreViewVisibility(View.VISIBLE);
        mAdapter.setLoadMoreViewText(getString(R.string.loading_data));

        List<BingoEntity> entities = mModel.getList(1);
        mEntities.clear();
        mEntities.addAll(entities);
        mAdapter.notifyDataSetChanged();
    }

    //数据不足PAGE_SIZE
    public void getDataInadequate() {
        completeRefresh();
        llStatus.setVisibility(View.GONE);
        mAdapter.setLoadMoreViewVisibility(View.GONE);

        List<BingoEntity> entities = mModel.getList(1);
        mEntities.clear();
        mEntities.addAll(entities);
        mAdapter.notifyDataSetChanged();
    }

    //加载失败
    public void getDataFail() {
        completeRefresh();
        llStatus.setVisibility(View.VISIBLE);
        mAdapter.setLoadMoreViewVisibility(View.GONE);

        tvStatus.setVisibility(View.VISIBLE);
        tvStatus.setText(R.string.load_data_fail);

        mEntities.clear();
        mAdapter.notifyDataSetChanged();
    }

    //数据为空 (More)
    public void getMoreDataEmpty() {
        mAdapter.setLoadMoreViewVisibility(View.GONE);
    }

    //数据足够PAGE_SIZE (More)
    public void getMoreDataAdequate() {
        mAdapter.setLoadMoreViewVisibility(View.VISIBLE);
        List<BingoEntity> entities = mModel.getList(2);
        mEntities.addAll(entities);
        mAdapter.notifyDataSetChanged();
    }

    //数据不足PAGE_SIZE (More)
    public void getMoreDataInadequate() {
        mAdapter.setLoadMoreViewVisibility(View.VISIBLE);
        mAdapter.setLoadMoreViewText(getString(R.string.load_data_adequate));
        List<BingoEntity> entities = mModel.getList(2);
        mEntities.addAll(entities);
        mAdapter.notifyDataSetChanged();
    }

    //加载失败 (More)
    public void getMoreDataFail() {
        mAdapter.setLoadMoreViewVisibility(View.VISIBLE);
        mAdapter.setLoadMoreViewText(getString(R.string.load_data_fail));
    }

    /**
     * －－－－－－－－－－－－－－－－－－－－－
     * 抽象方法们，你们辛苦啦！
     * －－－－－－－－－－－－－－－－－－－－－
     */

    protected abstract void onRefreshStart(); //下拉刷新数据
    protected abstract void onScrollLast(); //上拉加载数据
    protected abstract int emptyDataString(); //数据为空时的显示文字

}
