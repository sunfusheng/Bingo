package com.sun.bingo.ui.activity;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.sun.bingo.R;
import com.sun.bingo.control.NavigateManager;
import com.sun.bingo.entity.BingoEntity;
import com.sun.bingo.framework.dialog.ToastTip;

import butterknife.ButterKnife;
import butterknife.Bind;
import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;

public class BingoDetailActivity extends BaseActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.webView)
    WebView webView;
    @Bind(R.id.smoothProgressBar)
    SmoothProgressBar smoothProgressBar;
    @Bind(R.id.tv_error_msg)
    TextView tvErrorMsg;

    private BingoEntity bingoEntity;
    private WebSettings settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bingo_detail);
        ButterKnife.bind(this);

        initToolBar(toolbar, true, "Bingo详情");
        initData();
        initView();
    }

    private void initData() {
        bingoEntity = (BingoEntity) getIntent().getSerializableExtra("entity");
        if (bingoEntity == null) {
            ToastTip.show("该作者提交的Bingo有问题");
        }
    }

    private void initView() {
        settings = webView.getSettings();
//        settings.setJavaScriptEnabled(true); //如果访问的页面中有Javascript，则WebView必须设置支持Javascript
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setSupportZoom(true); //支持缩放
        settings.setBuiltInZoomControls(true); //支持手势缩放
        settings.setDisplayZoomControls(false); //是否显示缩放按钮

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null); //启动硬件加速
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            settings.setLoadsImagesAutomatically(true); //支持自动加载图片
        } else {
            settings.setLoadsImagesAutomatically(false);
        }

        settings.setUseWideViewPort(true); //将图片调整到适合WebView的大小
        settings.setLoadWithOverviewMode(true); //自适应屏幕
        settings.setDomStorageEnabled(true);
        settings.setAppCacheEnabled(true);
        settings.setSaveFormData(true);
        settings.setSupportMultipleWindows(true);
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); //优先使用缓存

        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY); //可使滚动条不占位
        webView.setHorizontalScrollbarOverlay(true);
        webView.setHorizontalScrollBarEnabled(true);
        webView.requestFocus();

        webView.loadUrl(bingoEntity.getWebsite());
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                smoothProgressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                smoothProgressBar.setVisibility(View.GONE);
                if (!settings.getLoadsImagesAutomatically()) {
                    settings.setLoadsImagesAutomatically(true);
                }
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                if (!TextUtils.isEmpty(description)) {
                    tvErrorMsg.setVisibility(View.VISIBLE);
                    tvErrorMsg.setText("errorCode: " + errorCode + "\ndescription: " + description);
                }
            }
        });
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                initToolBar(toolbar, true, title);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_bingo_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_open_by_explore:
                NavigateManager.gotoSystemExplore(this, bingoEntity.getWebsite());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack();//返回上一页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
