package com.sun.bingo.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.siyamed.shapeimageview.CircularImageView;
import com.mingle.widget.LoadingView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sun.bingo.R;
import com.sun.bingo.adapter.RecyclerViewAdapter;
import com.sun.bingo.control.PageControl;
import com.sun.bingo.entity.BingoEntity;
import com.sun.bingo.entity.UserEntity;
import com.sun.bingo.util.UserEntityUtil;
import com.sun.bingo.widget.CircleRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by sunfusheng on 15/11/10.
 */
public class UserInfoActivity extends BaseActivity<PageControl> implements CircleRefreshLayout.OnCircleRefreshListener {

    @InjectView(R.id.recycler_view)
    RecyclerView recyclerView;
    @InjectView(R.id.circle_refresh_layout)
    CircleRefreshLayout circleRefreshLayout;
    @InjectView(R.id.loadingView)
    LoadingView loadingView;
    @InjectView(R.id.tv_status)
    TextView tvStatus;
    @InjectView(R.id.ll_status)
    FrameLayout llStatus;
    @InjectView(R.id.iv_back)
    ImageView ivBack;
    @InjectView(R.id.civ_user_avatar)
    CircularImageView civUserAvatar;
    @InjectView(R.id.tv_nick_name)
    TextView tvNickName;
    @InjectView(R.id.tv_user_sign)
    TextView tvUserSign;
    @InjectView(R.id.ll_user_layout)
    LinearLayout llUserLayout;
    @InjectView(R.id.tv_location)
    TextView tvLocation;

    private UserEntity mUserEntity;
    private int lastVisibleItem;
    private LinearLayoutManager mLinearLayoutManager;
    protected List<BingoEntity> mEntities;
    protected RecyclerViewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        ButterKnife.inject(this);

        initData();
        initView();
        initListener();
        startRefresh();
    }


    private void initData() {
        mUserEntity = (UserEntity) getIntent().getSerializableExtra("userEntity");
    }

    private void initView() {
        tvNickName.setText(TextUtils.isEmpty(userEntity.getNickName())? "未知":userEntity.getNickName());
        tvUserSign.setText(TextUtils.isEmpty(userEntity.getUserSign())? "还没有个性签名":userEntity.getUserSign());
        UserEntityUtil.setUserAvatarView(civUserAvatar, userEntity.getUserAvatar());
        tvLocation.setText(getLocationSharedPreferences().city() + " " + getLocationSharedPreferences().district());
        llUserLayout.setBackgroundColor(getColorPrimary());

        mLinearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLinearLayoutManager);
        loadingView.setVisibility(View.VISIBLE);

        mEntities = new ArrayList<>();
        mAdapter = new RecyclerViewAdapter(this, mEntities);
        recyclerView.setAdapter(mAdapter);
    }

    private void initListener() {
        recyclerView.addOnScrollListener(new PauseOnScrollListener());
        circleRefreshLayout.setOnRefreshListener(this);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    class PauseOnScrollListener extends RecyclerView.OnScrollListener {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            switch (newState) {
                case RecyclerView.SCROLL_STATE_IDLE:
                    ImageLoader.getInstance().resume();
                    int size = recyclerView.getAdapter().getItemCount();
                    if (lastVisibleItem + 1 == size && mAdapter.isLoadMoreShown() &&
                            !mAdapter.getLoadMoreViewText().equals(getString(R.string.load_data_adequate))) {
                        onScrollLast();
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

    private void completeRefresh() {
        if (circleRefreshLayout != null) {
            circleRefreshLayout.completeRefresh();
        }
        if (loadingView != null && loadingView.getVisibility() == View.VISIBLE) {
            loadingView.setVisibility(View.GONE);
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

    //下拉刷新数据
    protected void onRefreshStart() {
        mControl.getMyBingoListData(this);
    }

    //上拉加载数据
    protected void onScrollLast() {
        mControl.getMyBingoListDataMore(this);
    }

    //数据为空
    public void getDataEmpty() {
        completeRefresh();
        llStatus.setVisibility(View.VISIBLE);
        mAdapter.setLoadMoreViewVisibility(View.GONE);

        tvStatus.setVisibility(View.VISIBLE);
        tvStatus.setText("暂无分享的文章");

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

}
