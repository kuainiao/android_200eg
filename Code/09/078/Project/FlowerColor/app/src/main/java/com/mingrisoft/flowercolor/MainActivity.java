package com.mingrisoft.flowercolor;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {
    private RelativeLayout view;//声明控件
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化控件
        view  = (RelativeLayout) findViewById(R.id.activity_main);
        //属性动画
        ObjectAnimator parentAnimator = ObjectAnimator.ofInt(
                view,"backgroundColor",
                Color.parseColor("#ff0000"),
                Color.parseColor("#0000ff"));
        parentAnimator.setEvaluator(new ArgbEvaluator());//设置颜色估值器
        parentAnimator.setRepeatCount(ValueAnimator.INFINITE);//播放次数：循环
        parentAnimator.setRepeatMode(ValueAnimator.REVERSE);//播放模式：循环播放时不是从头开始，从结尾开始
        parentAnimator.setDuration(5000);//播放时间5000ms
        parentAnimator.start();//播放动画
    }
}
