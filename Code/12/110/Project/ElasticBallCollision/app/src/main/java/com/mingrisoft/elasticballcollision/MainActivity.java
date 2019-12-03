package com.mingrisoft.elasticballcollision;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import view.BallView;

public class MainActivity extends AppCompatActivity {
    private Context lContext;                   //设置上下文
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lContext = this;                         //设置上下文
        setContentView(new BallView(lContext));  //设置自定义的弹力球控件
    }
}
