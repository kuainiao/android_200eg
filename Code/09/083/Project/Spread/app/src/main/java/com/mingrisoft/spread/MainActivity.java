package com.mingrisoft.spread;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {
    private RelativeLayout home;
    private ImageView image;
    private LinearLayout showAnimation;
    private Animator animator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化RelativeLayout
        home = (RelativeLayout) findViewById(R.id.home_group);
        //初始化ImageView
        image = (ImageView) findViewById(R.id.image1);
        //初始化LinearLayout
        showAnimation = (LinearLayout) findViewById(R.id.showAnimation);
        //设置ImageView的点击事件
        image.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                int width = home.getWidth();//获取宽度
                int height = home.getHeight();//获取高度
                int centerX = width>>1;//获取宽度的一半
                int centerY = height>>1;//获取高度的一半
                float finalRadius = (float) Math.hypot(width, height);//获取半径
                animator = ViewAnimationUtils.createCircularReveal(//设置动画
                        showAnimation, centerX, centerY, 0, finalRadius);
                animator.setInterpolator(new AccelerateDecelerateInterpolator());//设置插值器
                animator.setDuration(3000);//设置动画时间
                animator.start();//播放动画
            }
        });
    }
}
