package com.sun.bingo.ui.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.github.siyamed.shapeimageview.CircularImageView;
import com.shamanland.fab.FloatingActionButton;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.constant.WBConstants;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.net.openapi.RefreshTokenApi;
import com.sina.weibo.sdk.utils.Utility;
import com.sun.bingo.R;
import com.sun.bingo.constant.ConstantParams;
import com.sun.bingo.control.NavigateManager;
import com.sun.bingo.entity.BingoEntity;
import com.sun.bingo.entity.SinaRefreshTokenEntity;
import com.sun.bingo.entity.UserEntity;
import com.sun.bingo.framework.dialog.TipDialog;
import com.sun.bingo.framework.dialog.ToastTip;
import com.sun.bingo.framework.update.DownloadApk;
import com.sun.bingo.ui.fragment.FavoriteFragment;
import com.sun.bingo.ui.fragment.MyBingoFragment;
import com.sun.bingo.ui.fragment.SquareBingoFragment;
import com.sun.bingo.util.FastJsonUtil;
import com.sun.bingo.util.ShareUtil;
import com.sun.bingo.util.UserEntityUtil;
import com.sun.bingo.util.theme.ColorChooserDialog;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobUser;


public class MainActivity extends BaseActivity implements ColorChooserDialog.Callback, IWeiboHandler.Response {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.fl_container)
    FrameLayout flContainer;
    @Bind(R.id.floating_action_button)
    FloatingActionButton floatingActionButton;
    @Bind(R.id.main_navigation_layout)
    NavigationView mainNavigationLayout;
    @Bind(R.id.main_drawer_layout)
    DrawerLayout mainDrawerLayout;

    private String[] titles;
    private int mCurrentPosition = 0;
    private long lastTime = 0;

    private CircularImageView civUserAvatar;
    private TextView tvNickName;
    private TextView tvUserSign;

    private boolean isShareUrl = true;
    private IWeiboShareAPI mWeiboShareAPI; // 新浪微博分享接口实例

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initSinaShare(savedInstanceState);
        checkBmobUser();
        initData();
        initView();
        initListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkBmobUser();
        checkClipboard();
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

        Bitmap  bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
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

    private void initVersion() {
        new DownloadApk(this).checkVersion();
    }

    // 检查Bmob用户是否登录
    private void checkBmobUser() {
        myEntity = BmobUser.getCurrentUser(this, UserEntity.class);
        if (myEntity == null) {
            NavigateManager.gotoLoginActivity(this);
            finish();
        }
    }

    // 检查剪贴板上是否有http链接
    private void checkClipboard() {
        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData clipData = clipboardManager.getPrimaryClip();
        if (clipData == null) return ;
        ClipData.Item item = clipData.getItemAt(0);
        final String text = item.getText().toString();

        if (!TextUtils.isEmpty(text) && text.startsWith("http") && isShareUrl && !text.equals(getSettingsSharedPreferences().newBingoUrl())) {
            TipDialog tipDialog = new TipDialog(this);
            tipDialog.show("将复制的URL分享到Bingo", text, "立即分享", "暂不", new MaterialDialog.ButtonCallback() {
                @Override
                public void onPositive(MaterialDialog dialog) {
                    super.onPositive(dialog);
                    isShareUrl = false;
                    NavigateManager.gotoEditNewBingoActivity(mActivity, text);
                }

                @Override
                public void onNegative(MaterialDialog dialog) {
                    super.onNegative(dialog);
                    isShareUrl = false;
                    getSettingsSharedPreferences().newBingoUrl(text);
                }
            });
        }
    }

    private void initData() {
        titles = new String[3];
        titles[0] = getString(R.string.menu_square_bingo);
        titles[1] = getString(R.string.menu_my_bingo);
        titles[2] = getString(R.string.menu_my_favorite);

        myEntity = BmobUser.getCurrentUser(this, UserEntity.class);
        if (myEntity != null) {
            initVersion();
            sinaRefreshTokenRequest();
        }

    }

    private void initView() {
        initToolBar(toolbar, false, R.string.app_name);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, mainDrawerLayout, toolbar, 0, 0);
        drawerToggle.syncState();

        controlShowFragment(0);
        setOvalShapeViewBackground(floatingActionButton);
        civUserAvatar = ButterKnife.findById(mainNavigationLayout, R.id.civ_user_avatar);
        tvNickName = ButterKnife.findById(mainNavigationLayout, R.id.tv_nick_name);
        tvUserSign = ButterKnife.findById(mainNavigationLayout, R.id.tv_user_sign);

        if (myEntity != null) {
            UserEntityUtil.setUserAvatarView(civUserAvatar, myEntity.getUserAvatar());
            UserEntityUtil.setTextViewData(tvNickName, myEntity.getNickName());
            UserEntityUtil.setTextViewData(tvUserSign, myEntity.getUserSign());
        }
    }

    private void initListener() {
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavigateManager.gotoEditNewBingoActivity(mActivity, null);
            }
        });

        mainNavigationLayout.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_square_bingo:
                        controlShowFragment(0);
                        break;
                    case R.id.nav_my_bingo:
                        controlShowFragment(1);
                        break;
                    case R.id.nav_favorite_bingo:
                        controlShowFragment(2);
                        break;
                    case R.id.nav_switch_theme:
                        changeTheme();
                        break;
                    case R.id.nav_feedback:
                        ShareUtil.feedback(MainActivity.this);
                        break;
                    case R.id.nav_send_to_friend:
                        ShareUtil.sendToFriend(MainActivity.this);
                        break;
                    case R.id.nav_share:
                        ShareUtil.share(MainActivity.this);
                        break;
                }
                menuItem.setChecked(true);
                return true;
            }
        });

        civUserAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavigateManager.gotoProfileActivity(MainActivity.this, false);
            }
        });
    }

    // 新浪微博刷新Token请求
    private void sinaRefreshTokenRequest() {
        if (TextUtils.isEmpty(getAccountSharedPreferences().refresh_token())) {
            return ;
        }
        RefreshTokenApi.create(this).refreshToken(ConstantParams.SINA_APP_KEY, getAccountSharedPreferences().refresh_token(), new RequestListener() {
            @Override
            public void onComplete(String s) {
                if (!TextUtils.isEmpty(s)) {
                    SinaRefreshTokenEntity entity = FastJsonUtil.parseJson(s, SinaRefreshTokenEntity.class);
                    if (entity != null) {
                        getAccountSharedPreferences().uid(entity.getUid());
                        getAccountSharedPreferences().access_token(entity.getAccess_token());
                        getAccountSharedPreferences().refresh_token(entity.getRefresh_token());
                        getAccountSharedPreferences().expires_in(entity.getExpires_in());
                    }
                }
            }

            @Override
            public void onWeiboException(WeiboException e) {
                e.printStackTrace();
            }
        });
    }

    private void controlShowFragment(int position) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        Fragment oldFragment = fragmentManager.findFragmentByTag(makeTag(mCurrentPosition));
        if (oldFragment != null) {
            fragmentTransaction.hide(oldFragment);
        }
        mCurrentPosition = position;

        Fragment currentFragment = fragmentManager.findFragmentByTag(makeTag(position));
        if (currentFragment != null) {
            fragmentTransaction.show(currentFragment);
        } else {
            fragmentTransaction.add(R.id.fl_container, getFragment(position), makeTag(position));
        }
        fragmentTransaction.commitAllowingStateLoss();

        if (mainDrawerLayout.isShown()) {
            mainDrawerLayout.closeDrawers();
        }
        toolbar.setTitle(titles[position]);
    }

    private String makeTag(int position) {
        return R.id.fl_container + "" + position;
    }

    private Fragment getFragment(int position) {
        Fragment fragment;
        switch (position) {
            case 0:
                fragment = new SquareBingoFragment();
                break;
            case 1:
                fragment = new MyBingoFragment();
                break;
            case 2:
                fragment = new FavoriteFragment();
                break;
            default:
                fragment = new SquareBingoFragment();
                break;
        }
        return fragment;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //空操作解决Fragment重叠问题
    }

    //设置主题
    private void changeTheme() {
        new ColorChooserDialog().show(this, getSettingsSharedPreferences().themeValue());
    }

    @Override
    public void onColorSelection(int index, int color, int darker) {
        getSettingsSharedPreferences().themeValue(index);
        recreate();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_menu_app:
                NavigateManager.gotoAboutAppActivity(this);
                return true;
            case R.id.item_menu_author:
                NavigateManager.gotoAboutAuthorActivity(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        if (mainDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
            mainDrawerLayout.closeDrawers();
            return ;
        }
        if (System.currentTimeMillis() - lastTime < 2000) {
            super.onBackPressed();
        } else {
            lastTime = System.currentTimeMillis();
            ToastTip.show(getString(R.string.toast_exit_tip));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case NavigateManager.PROFILE_REQUEST_CODE:
                if (myEntity != null) {
                    myEntity = BmobUser.getCurrentUser(MainActivity.this, UserEntity.class);
                    UserEntityUtil.setUserAvatarView(civUserAvatar, myEntity.getUserAvatar());
                    UserEntityUtil.setTextViewData(tvNickName, myEntity.getNickName());
                    UserEntityUtil.setTextViewData(tvUserSign, myEntity.getUserSign());
                }
                break;
        }
    }

}
