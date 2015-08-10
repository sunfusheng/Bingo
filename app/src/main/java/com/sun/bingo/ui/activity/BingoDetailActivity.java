package com.sun.bingo.ui.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.sun.bingo.R;
import com.sun.bingo.entity.BingoEntity;
import com.sun.bingo.framework.dialog.TipDialog;

import butterknife.ButterKnife;
import butterknife.InjectView;
import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;

public class BingoDetailActivity extends BaseActivity {

    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.webView)
    WebView webView;
    @InjectView(R.id.smoothProgressBar)
    SmoothProgressBar smoothProgressBar;
    @InjectView(R.id.tv_error_msg)
    TextView tvErrorMsg;

    private BingoEntity bingoEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bingo_detail);
        ButterKnife.inject(this);

        initToolBar(toolbar, true, "Bingo详情");
        initData();
        initView();
    }

    private void initData() {
        bingoEntity = (BingoEntity) getIntent().getSerializableExtra("entity");
        if (bingoEntity == null) {
            TipDialog.showToastDialog("该作者提交的Bingo有问题");
        }
    }

    private void initView() {
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true); //如果访问的页面中有Javascript，则WebView必须设置支持Javascript
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); //优先使用缓存
        settings.setLoadsImagesAutomatically(true); //支持自动加载图片

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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (webView.canGoBack()) {
                webView.goBack();//返回上一页面
                return true;
            } else {
                finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

}
