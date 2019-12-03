package com.mingrisoft.hldemo;

import android.support.v7.app.AppCompatActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;
public class MainActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private LinearLayout ll_main;
    private List<ImageView> imageViews;
    private int pagerWidth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //图片滑动控件
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        ll_main = (LinearLayout) findViewById(R.id.activity_main);
        //加载图片添加到图片数组
        initData();
        //设置的是 viewpager 排除第一个默认加载的 view 的个数
        mViewPager.setOffscreenPageLimit(3);
        pagerWidth = (int) (getResources().getDisplayMetrics().widthPixels * 3.0f / 5.0f);
        ViewGroup.LayoutParams lp = mViewPager.getLayoutParams();
        if (lp == null) {
            lp = new ViewGroup.LayoutParams(pagerWidth, ViewGroup.LayoutParams.MATCH_PARENT);
        } else {
            lp.width = pagerWidth;
        }
        //设置viewPager位置
        mViewPager.setLayoutParams(lp);
        //设置页与页直接的距离
        mViewPager.setPageMargin(-50);
        //绑定手势事件
        ll_main.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                //返回viewPageer对手势的处理事件
                return mViewPager.dispatchTouchEvent(motionEvent);
            }
        });
        //设置页面切换动画
        mViewPager.setPageTransformer(true, new ViewPageTransformer());
        //绑定适配器
        mViewPager.setAdapter(new MyViewPagerAdapter(imageViews));
    }

    private void initData() {
        //装载图片控件数组
        imageViews = new ArrayList<>();
        //新建图片控件
        ImageView first = new ImageView(MainActivity.this);
        //设置图片背景
        first.setImageBitmap(ImageUtil.getReverseBitmapById(R.drawable.book1, MainActivity.this));
        ImageView second = new ImageView(MainActivity.this);
        second.setImageBitmap(ImageUtil.getReverseBitmapById(R.drawable.book2, MainActivity.this));
        ImageView third = new ImageView(MainActivity.this);
        third.setImageBitmap(ImageUtil.getReverseBitmapById(R.drawable.book3, MainActivity.this));
        ImageView fourth = new ImageView(MainActivity.this);
        fourth.setImageBitmap(ImageUtil.getReverseBitmapById(R.drawable.book4, MainActivity.this));
        //添加图片显示控件到数组
        imageViews.add(first);
        imageViews.add(second);
        imageViews.add(third);
        imageViews.add(fourth);
    }
}
