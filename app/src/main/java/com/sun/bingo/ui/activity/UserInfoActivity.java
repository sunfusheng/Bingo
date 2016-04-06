package com.sun.bingo.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mingle.widget.LoadingView;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.constant.WBConstants;
import com.sina.weibo.sdk.utils.Utility;
import com.sun.bingo.R;
import com.sun.bingo.adapter.RecyclerViewAdapter;
import com.sun.bingo.constant.ConstantParams;
import com.sun.bingo.control.PageControl;
import com.sun.bingo.entity.BingoEntity;
import com.sun.bingo.entity.UserEntity;
import com.sun.bingo.framework.dialog.ToastTip;
import com.sun.bingo.util.UserEntityUtil;
import com.sun.bingo.widget.CircleRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by sunfusheng on 15/11/10.
 */
public class UserInfoActivity extends BaseActivity<PageControl> implements CircleRefreshLayout.OnCircleRefreshListener, IWeiboHandler.Response {

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
    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.iv_user_avatar)
    ImageView ivUserAvatar;
    @Bind(R.id.tv_nick_name)
    TextView tvNickName;
    @Bind(R.id.tv_user_sign)
    TextView tvUserSign;
    @Bind(R.id.ll_user_layout)
    LinearLayout llUserLayout;
    @Bind(R.id.tv_location)
    TextView tvLocation;

    private UserEntity mUserEntity;
    private int lastVisibleItem;
    private LinearLayoutManager mLinearLayoutManager;
    protected List<BingoEntity> mEntities;
    protected RecyclerViewAdapter mAdapter;

    private IWeiboShareAPI mWeiboShareAPI; // 新浪微博分享接口实例

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        ButterKnife.bind(this);

        initSinaShare(savedInstanceState);
        initData();
        initView();
        initListener();
        startRefresh();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        mWeiboShareAPI.handleWeiboResponse(intent, this);
    }

    private void initSinaShare(Bundle savedInstanceState) {
        mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(this, ConstantParams.SINA_APP_KEY);
        mWeiboShareAPI.registerApp();
        if (savedInstanceState != null) {
            mWeiboShareAPI.handleWeiboResponse(getIntent(), this);
        }
    }

    /**
     * 第三方应用发送请求消息到微博，唤起微博分享界面。
     * 同时可以分享文本、图片以及其它媒体资源（网页、音乐、视频、声音中的一种）。
     */
    public void sendMultiMessageToSina(BingoEntity entity) {
        if (!mWeiboShareAPI.isWeiboAppSupportAPI()) {
            Toast.makeText(this, "暂不支持新浪微博分享", Toast.LENGTH_SHORT).show();
            return ;
        }
        // 1. 初始化微博的分享消息
        WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
        if (mWeiboShareAPI.getWeiboAppSupportAPI() >= 10351) {
            TextObject textObject = new TextObject();
            textObject.text = "【Bingo下载页面：https://fir.im/Bingo】 \n "+entity.getDescribe();
            weiboMessage.textObject = textObject;
        }
        weiboMessage.mediaObject = getWebpageObj(entity);

        // 2. 初始化从第三方到微博的消息请求
        SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
        // 用transaction唯一标识一个请求
        request.transaction = String.valueOf(System.currentTimeMillis());
        request.multiMessage = weiboMessage;

        // 3. 发送请求消息到微博，唤起微博分享界面
        mWeiboShareAPI.sendRequest(mActivity, request);
    }

    // 创建新浪微博网页分享对象
    private WebpageObject getWebpageObj(BingoEntity entity) {
        WebpageObject mediaObject = new WebpageObject();
        mediaObject.identify = Utility.generateGUID();
        mediaObject.title = "来自Bingo分享";
        mediaObject.description = entity.getDescribe();

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
        // 设置 Bitmap 类型的图片到视频对象里。 注意：最终压缩过的缩略图大小不得超过 32kb。
        mediaObject.setThumbImage(bitmap);
        mediaObject.actionUrl = entity.getWebsite();
        mediaObject.defaultText = "Bingo分享";
        return mediaObject;
    }

    @Override
    public void onResponse(BaseResponse baseResponse) {
        if(baseResponse!= null){
            switch (baseResponse.errCode) {
                case WBConstants.ErrorCode.ERR_OK:
                    break;
                case WBConstants.ErrorCode.ERR_CANCEL:
                    break;
                case WBConstants.ErrorCode.ERR_FAIL:
                    ToastTip.show("分享失败 " + baseResponse.errMsg);
                    break;
            }
        }
    }

    private void initData() {
        mUserEntity = (UserEntity) getIntent().getSerializableExtra("userEntity");
    }

    private void initView() {
        tvNickName.setText(TextUtils.isEmpty(mUserEntity.getNickName())? "未知":mUserEntity.getNickName());
        tvUserSign.setText(TextUtils.isEmpty(mUserEntity.getUserSign())? "还没有个性签名":mUserEntity.getUserSign());
        UserEntityUtil.setUserAvatarView(mContext, mUserEntity.getUserAvatar(), ivUserAvatar);
        tvLocation.setText(mUserEntity.getCity() + " " + mUserEntity.getDistrict());
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
        mControl.getMyBingoListData(this, mUserEntity.getObjectId());
    }

    //上拉加载数据
    protected void onScrollLast() {
        mControl.getMyBingoListDataMore(this, mUserEntity.getObjectId());
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
