package com.mingrisoft.friendcircle;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import com.mingrisoft.friendcircle.adapter.MyPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/1/24.
 */

public class ShowImageActivity extends AppCompatActivity {
    private ViewPager viewPager;  //声明viewpage
    private List<View> listViews = null;  //用于获取图片资源
    private int index = 0;   //获取当前点击的图片位置
    private MyPagerAdapter adapter;   //ViewPager的适配器

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);  //去除标题栏
        setContentView(R.layout.show_image_layout);    //绑定布局

        inint();   //初始化

    }

    private void inint() {
        final Intent intent = getIntent();   //获取intent传递的信息
        viewPager = (ViewPager) findViewById(R.id.show_view_pager);  //绑定viewpager的id
        listViews = new ArrayList<View>();   //初始化list
        for (int i = 0; i < intent.getIntArrayExtra("image").length; i++) {  //for循环将试图添加到list中
            View view = LayoutInflater.from(getApplicationContext()).inflate(
                    R.layout.view_pager_item, null);   //绑定viewpager的item布局文件
            ImageView iv = (ImageView) view.findViewById(R.id.view_image);   //绑定布局中的id
            iv.setBackgroundResource(intent.getIntArrayExtra("image")[i]);   //设置当前点击的图片
            listViews.add(view);
            /**
             * 图片的长按监听
             * */
            iv.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    //弹出提示，提示内容为当前的图片位置
                    Toast.makeText(ShowImageActivity.this, "这是第" + (index + 1) + "图片", Toast.LENGTH_SHORT).show();
                    return false;
                }
            });
        }
        adapter = new MyPagerAdapter(listViews);
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(new PageChangeListener()); //设置viewpager的改变监听
                         //截取intent获取传递的值
        viewPager.setCurrentItem(intent.getIntExtra("id", 0));    //viewpager显示指定的位置

    }

    /**
     * pager的滑动监听
     * */
    private class PageChangeListener implements OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageSelected(int arg0) {
            index = arg0;
        }

    }
}
