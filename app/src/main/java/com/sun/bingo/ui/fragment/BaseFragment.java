package com.sun.bingo.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jpardogo.android.googleprogressbar.library.GoogleProgressBar;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sun.bingo.R;
import com.sun.bingo.entity.UserEntity;
import com.sun.bingo.widget.CircleRefreshLayout;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.bmob.v3.BmobUser;


public abstract class BaseFragment extends Fragment implements CircleRefreshLayout.OnCircleRefreshListener {

    @InjectView(R.id.recycler_view)
    RecyclerView recyclerView;
    @InjectView(R.id.circle_refresh_layout)
    CircleRefreshLayout circleRefreshLayout;
    @InjectView(R.id.google_progressBar)
    GoogleProgressBar googleProgressBar;

    private Handler mHandler = new Handler();
    protected UserEntity userEntity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_bingo_list, container, false);
        ButterKnife.inject(this, rootView);

        initView();
        getBingoEntityList();
        return rootView;
    }

    private void initData() {
        userEntity = BmobUser.getCurrentUser(getActivity(), UserEntity.class);
    }

    private void initView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addOnScrollListener(new PauseOnScrollListener());
        googleProgressBar.setVisibility(View.VISIBLE);
        circleRefreshLayout.setOnRefreshListener(this);
    }

    /**
     *
     */
    class PauseOnScrollListener extends RecyclerView.OnScrollListener {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            switch (newState) {
                case RecyclerView.SCROLL_STATE_IDLE:
                    ImageLoader.getInstance().resume();
                    break;
                case RecyclerView.SCROLL_STATE_DRAGGING:
                    ImageLoader.getInstance().pause();
                    break;
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
        }
    }

    protected void completeRefresh() {
        if (circleRefreshLayout != null) {
            circleRefreshLayout.completeRefresh();
        }
        if (googleProgressBar != null && googleProgressBar.getVisibility() == View.VISIBLE) {
            googleProgressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
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

}
