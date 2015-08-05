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
import com.sun.bingo.R;
import com.sun.bingo.widget.CircleRefreshLayout;

import butterknife.ButterKnife;
import butterknife.InjectView;


public abstract class BaseFragment extends Fragment implements CircleRefreshLayout.OnCircleRefreshListener {

    @InjectView(R.id.recycler_view)
    RecyclerView recyclerView;
    @InjectView(R.id.circle_refresh_layout)
    CircleRefreshLayout circleRefreshLayout;
    @InjectView(R.id.google_progressBar)
    GoogleProgressBar googleProgressBar;

    private Handler mHandler = new Handler();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_bingo_list, container, false);
        ButterKnife.inject(this, rootView);

        initView();
        startRefresh();
        return rootView;
    }

    private void initView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
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
