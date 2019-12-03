package com.mingrisoft.news.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.mingrisoft.news.R;

public class WebPageActivity extends AppCompatActivity {
    private WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_page);
        //获取控件
        webView = (WebView) findViewById(R.id.webview);
        //设置加载网址
        webView.loadUrl(getIntent().getStringExtra("url"));
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
      webView.setWebViewClient(new WebViewClient(){
          @Override
          public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
              return false;
          }
      });
    }
}
