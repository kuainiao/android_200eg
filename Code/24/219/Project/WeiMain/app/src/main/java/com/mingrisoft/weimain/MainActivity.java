package com.mingrisoft.weimain;



import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import view.ChangeIcon;

public class MainActivity extends FragmentActivity implements  ViewPager.OnPageChangeListener {

    private ViewPager lViewPager;   //定义用于滚动视图的ViewPager
    //定义用于切换Fragment集合
    private List<Fragment> lTabs = new ArrayList<Fragment>();
    //定义主视图所显示的文本数组
    private String[] lMain = new String[]
            { "微信", "通讯录", "发现",
                    "我" };
    private FragmentPagerAdapter mAdapter;  //定义适配器
    //定义绘制图标控件集合
    private List<ChangeIcon> lTabIndicators = new
            ArrayList<ChangeIcon>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();                         //调用初始化方法
        initDatas();                        //调用保存初始化数据的方法
        lViewPager.setAdapter(mAdapter);  //配置适配器
        initEvent();                        //调用初始化事件
    }

    /**
     * 初始化事件
     */
    private void initEvent() {
        lViewPager.addOnPageChangeListener(this);
    }

    //初始化数据的方法
    private void initDatas() {
        for (String main : lMain)
        {
            TabFragment tabFragment = new TabFragment();    //创建主视图对象
            Bundle bundle = new Bundle();                   //创建给主视图传递数据的Bundle
            bundle.putString(TabFragment.MAIN, main);     //设置传入的文本
            tabFragment.setArguments(bundle);               //将文本信息传递
            lTabs.add(tabFragment);                        //添加到切换Fragment集合当中
        }
        //创建适配器对象并创建构造方法
        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager())
        {
            @Override
            public Fragment getItem(int position) {     //指定索引的数据
                return lTabs.get(position);
            }

            @Override
            public int getCount() {                     //获取所有数据
                return lTabs.size();
            }
        };
    }
    //初始化方法
    private void initView() {
        lViewPager = (ViewPager) findViewById(R.id.id_viewpager);   //获取布局文件中滚动视图的ViewPager
        //获取自定义图标微信
        ChangeIcon one = (ChangeIcon) findViewById(R.id.id_changeicon_one);
        lTabIndicators.add(one);            //添加到集合当中
        ChangeIcon two = (ChangeIcon) findViewById(R.id.id_changeicon_two);
        lTabIndicators.add(two);
        ChangeIcon three = (ChangeIcon) findViewById(R.id.id_changeicon_three);
        lTabIndicators.add(three);
        ChangeIcon four = (ChangeIcon) findViewById(R.id.id_changeicon_four);
        lTabIndicators.add(four);

        one.setOnClickListener(onClick);           //指定单击事件
        two.setOnClickListener(onClick);
        three.setOnClickListener(onClick);
        four.setOnClickListener(onClick);
        one.setIconAlpha(1.0f);                     //设置微信图标不透明默认为绿色
    }
    View.OnClickListener onClick =new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            resetOtherIconColor();                           //调用重置其他图标颜色方法
            switch (v.getId()) {
                case R.id.id_changeicon_one:                     //如果选中微信图标
                    lTabIndicators.get(0).setIconAlpha(1.0f);   //设置微信图标为不透明显示绿色
                    lViewPager.setCurrentItem(0, false);        //直接切换微信主视图无动画
                    break;
                case R.id.id_changeicon_two:
                    lTabIndicators.get(1).setIconAlpha(1.0f);
                    lViewPager.setCurrentItem(1, false);
                    break;
                case R.id.id_changeicon_three:
                    lTabIndicators.get(2).setIconAlpha(1.0f);
                    lViewPager.setCurrentItem(2, false);
                    break;
                case R.id.id_changeicon_four:
                    lTabIndicators.get(3).setIconAlpha(1.0f);
                    lViewPager.setCurrentItem(3, false);
                    break;
            }
        }
    };

    /**
     * 重置其他图标颜色
     */
    private void resetOtherIconColor() {
        for (int i = 0; i < lTabIndicators.size(); i++)
        {
            lTabIndicators.get(i).setIconAlpha(0);      //设置默认为透明
        }
    }

    /**
     * 根据偏移量设置从右向左滑动时起始图标透明，起始图标右侧绘制颜色
     * 相反从左向右滑动时起始图标设置透明，起始图标左侧图标绘制颜色
     */
    @Override
    public void onPageScrolled(int position, float positionOffset,
                               int positionOffsetPixels) {
        if (positionOffset > 0)     //如果偏移值大于0
        {
            ChangeIcon left = lTabIndicators.get(position);     //获取向左滑下标
            ChangeIcon right = lTabIndicators.get(position+1);  //获取向右滑下标
            left.setIconAlpha(1 - positionOffset);      //设置左侧图标透明，右侧变色
            right.setIconAlpha(positionOffset);       //设置右侧图标透明，左侧变色
        }
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

}
