package com.mingrisoft.mbliv;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.qiushui.blurredview.BlurredView;


public class MainActivity extends AppCompatActivity {
    private BlurredView mBlurredView;
    private RecyclerView mRecyclerView;
    private int mScrollerY;
    private int mAlpha;
    //声明调节模糊效果按钮控件
    private Button mBasicBtn;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.blurred_weather_activity);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //初始化控件
        mBlurredView = (BlurredView) findViewById(R.id.yahooweather_blurredview);
        mRecyclerView = (RecyclerView) findViewById(R.id.yahooweather_recyclerview);
        // 初始化调节模糊按钮
        mBasicBtn = (Button) findViewById(R.id.basic_blur_btn);
        //调节模糊效果按钮点击事件
        mBasicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //进入自主调节模糊效果页
                startActivity(new Intent(MainActivity.this, BlurredViewBasicActivity.class));
                finish();
            }
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(new RecyclerViewAdapter(this));

        //监听滑动事件
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            //滑动完成改变调用
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
            //滑动时候调用
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mScrollerY += dy;
                if (Math.abs(mScrollerY) > 1000) {
                    //设置图片上移距离
                    mBlurredView.setBlurredTop(100);
                    mAlpha = 100;
                } else {
                    //设置图片上移距离
                    mBlurredView.setBlurredTop(mScrollerY / 10);
                    //获取绝对值
                    mAlpha = Math.abs(mScrollerY) / 10;
                }
                mBlurredView.setBlurredLevel(mAlpha);//决定模糊的级别，alpha在0到100之间。
            }
        });

    }
}
