package com.sun.bingo.ui.activity;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.webkit.WebView;
import android.widget.TextView;

import com.sun.bingo.R;
import com.sun.bingo.util.theme.AppUtil;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by sunfusheng on 15/9/8.
 */
public class AboutAppActivity extends BaseActivity {

    @InjectView(R.id.tv_version)
    TextView tvVersion;
    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;
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
        initToolBar(toolbar, true, "");
        collapsingToolbar.setTitle(getString(R.string.about_app));
        webView.loadUrl("file:///android_asset/about_app.html");
        tvVersion.setText("Version "+AppUtil.getVersionName(this));
    }

}
