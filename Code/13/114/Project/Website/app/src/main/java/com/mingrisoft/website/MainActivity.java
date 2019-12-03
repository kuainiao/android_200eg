package com.mingrisoft.website;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

public class MainActivity extends AppCompatActivity {

    private WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //获取控件
        webView = (WebView) findViewById(R.id.webview);
        //设置加载网址
        webView.loadUrl("http://www.mingrisoft.com/");
        //设置支持JavaScript
        webView.getSettings().setJavaScriptEnabled(true);
        //设置启用视meta标签的支持
        webView.getSettings().setUseWideViewPort(true);
        //不显示控制页面缩放的按钮
        webView.getSettings().setDisplayZoomControls(false);
        //使用内部缩放机制
        webView.getSettings().setBuiltInZoomControls(true);
        //设置支持缩放手势
        webView.getSettings().setSupportZoom(true);
        //设置适应设备屏幕
        webView.getSettings().setLoadWithOverviewMode(true);
    }
}
