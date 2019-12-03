package com.mingrisoft.tablayout;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private List<View> viewList;
    private String titles[] = new String[]{"体系课程","实战课程","碎片化学习","后端开发","前端开发"};
    private int redIDs[] = new int[]{R.mipmap.mrkj_1,R.mipmap.mrkj_2,R.mipmap.mrkj_3,R.mipmap.mrkj_4,R.mipmap.mrkj_5};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tabLayout = (TabLayout) findViewById(R.id.tab);
        viewPager = (ViewPager) findViewById(R.id.page);
        viewList = new ArrayList<>();//实例化集合
        for (int i = 0; i < titles.length; i++) {
            //加载布局并填充成控件
            View view = getLayoutInflater().inflate(R.layout.item,null);
            view.setBackgroundResource(redIDs[i]);//设置背景
            viewList.add(view);//添加布局
        }
        viewPager.setAdapter(new MyAdapter());//绑定适配器
        tabLayout.setupWithViewPager(viewPager,true);//绑定ViewPager
    }

    /**
     * ViewPage适配器
     */
    class MyAdapter extends PagerAdapter{
        @Override
        public int getCount() {
            return viewList.size();
        }
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
        /**
         * 加载item
         * @param container
         * @param position
         * @return
         */
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view  = viewList.get(position);
            container.addView(view);//添加
            return view;
        }
        /**
         * 销毁Item
         * @param container
         * @param position
         * @param object
         */
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(viewList.get(position));//移除
        }
        /**
         * 关联指示器
         * @param position
         * @return
         */
        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }
}
