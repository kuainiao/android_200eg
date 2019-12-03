package com.mingrisoft.qrobot;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by dllo on 16/1/18.
 */
public class WebViewActivity extends AppCompatActivity {

    private WebView webView;   //用于显示网页

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_view);  //设置布局
        webView = (WebView) findViewById(R.id.web_view);  //绑定id
        Intent intent = getIntent();   //初始化intent
        String url = intent.getStringExtra("url");  //接受跳转界面传递过来的网址
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());  //初始化网页的链接
        webView.loadUrl(url);   //显示网页
    }
}

