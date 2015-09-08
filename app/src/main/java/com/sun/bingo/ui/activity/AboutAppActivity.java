package com.sun.bingo.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.webkit.WebView;

import com.sun.bingo.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by sunfusheng on 15/9/8.
 */
public class AboutAppActivity extends BaseActivity {

    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.webView)
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_app);
        ButterKnife.inject(this);

        initView();
    }

    private void initView() {
        initToolBar(toolbar, true, R.string.about_app);
        webView.loadUrl("file:///android_asset/about.html");
    }

}
