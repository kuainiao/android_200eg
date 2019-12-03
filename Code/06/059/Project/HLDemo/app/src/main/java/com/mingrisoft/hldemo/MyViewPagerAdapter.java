package com.mingrisoft.hldemo;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

public class MyViewPagerAdapter  extends PagerAdapter {
    private List<ImageView> listViews;

    public MyViewPagerAdapter(List<ImageView> listViews) {
        //初始化控件
        this.listViews = listViews;
    }

    @Override
    public int getCount() {
        //列表长度
        return listViews == null ? 0 : listViews.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        //选择图片显示控件的
        ImageView imageView = listViews.get(position);
        //添加控件
        container.addView(imageView);
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(listViews.get(position));
    }
}
