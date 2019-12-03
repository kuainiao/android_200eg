package com.mingrisoft.bootpagenew;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ScrollingView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ImageView oneView;  //第一张旋转视图
    private View twoView, threeView, fourView, nullView, nullViewTwo;
    private ViewPager viewPager;
    private MyPagerAdapter adapter;   //viewpage的适配器
    private ScrollView scrollView, scrollViewTwo;   //两个滑动的视图
    int width;   //加载屏幕的宽度
    int height;  //加载屏幕的高度

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findID();   //绑定id
        init();
    }

    private void init() {
        /**
         * 获取屏幕的宽高度
         * */
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        width = metric.widthPixels;     // 屏幕宽度（像素）
        height = metric.heightPixels;   // 屏幕高度（像素）
        List<View> list = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            View view = LayoutInflater.from(getApplicationContext()).inflate(
                    R.layout.view_pager, null);
            list.add(view);
        }
        adapter = new MyPagerAdapter(list);    //初始化适配器
        viewPager.setAdapter(adapter);         //ViewPager绑定适配器
        nullView.setAlpha(0);                   //设置图片完全透明
        nullViewTwo.setAlpha(0);                //设置图片完全透明
        nullView.setMinimumHeight(height);       //给第二张透明的图片设置高度
        nullViewTwo.setMinimumHeight(height);    //给第三张透明的图片设置高度
        twoView.setMinimumHeight(height);        //给第二张图片设置高度
        threeView.setMinimumHeight(height);      //给第三张图片设置高度
    }

    private void findID() {
        oneView = (ImageView) findViewById(R.id.one_image);
        twoView = findViewById(R.id.two_view);
        threeView = findViewById(R.id.three_view);
        fourView = findViewById(R.id.four_view);
        nullView = findViewById(R.id.view_null);
        nullViewTwo = findViewById(R.id.view_null_two);
        viewPager = (ViewPager) findViewById(R.id.main_view_pager);
        scrollView = (ScrollView) findViewById(R.id.scroll_view);
        scrollViewTwo = (ScrollView) findViewById(R.id.scroll_view_two);

    }

    @Override
    protected void onStart() {
        super.onStart();
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                switch (position) {
                    case 0:     //当ViewPager滑动到第一页的时候
                        oneView.setRotation(positionOffset * 360);   //设置第一张图片的旋转的角度
                        oneView.setAlpha(1 - positionOffset);        //设置第一张图片的透明度
                        break;
                    case 1:    //当ViewPager滑动到第二页的时候
                        scrollViewTwo.scrollTo(0, 2158);             //设置第二张图片滑动的距离
                        scrollView.scrollTo(0, positionOffsetPixels * 2);
                        break;
                    case 2:  //当ViewPager滑动到第三二页的时候
                        scrollViewTwo.scrollTo(0, 2158 - (positionOffsetPixels * 2));   //设置第三张图片的滑动距离
                        break;
                    case 3:
                        break;

                }


            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                Log.d("-------", "state:" + state);
            }
        });
    }


}
