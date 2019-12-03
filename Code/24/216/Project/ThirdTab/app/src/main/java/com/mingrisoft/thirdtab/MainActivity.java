package com.mingrisoft.thirdtab;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ClipPagerTitleView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String[] CHANNELS = new String[]{"CUPCAKE", "DONUT", "ECLAIR", "GINGERBREAD", "HONEYCOMB", "ICE_CREAM_SANDWICH", "JELLY_BEAN", "KITKAT", "LOLLIPOP", "M", "NOUGAT"};
    private List<String> mDataList = new ArrayList<String>(Arrays.asList(CHANNELS));
    private ExamplePagerAdapter mExamplePagerAdapter = new ExamplePagerAdapter(mDataList);//适配器
    private ViewPager mViewPager;//ViewPager
    private MagicIndicator mMagicIndicator;//指示器
    private CommonNavigator mCommonNavigator;//Tab

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mViewPager.setAdapter(mExamplePagerAdapter);//创建适配器
        mMagicIndicator = (MagicIndicator) findViewById(R.id.magic_indicator1);
        mMagicIndicator.setBackgroundColor(Color.parseColor("#d43d3d"));//设置背景颜色
        mCommonNavigator = new CommonNavigator(this);//创建对象
        mCommonNavigator.setSkimOver(true);
        mCommonNavigator.setAdapter(adapter);
        mMagicIndicator.setNavigator(mCommonNavigator);//设置指示器
        ViewPagerHelper.bind(mMagicIndicator, mViewPager);//绑定ViewPager
    }
    private CommonNavigatorAdapter adapter = new CommonNavigatorAdapter() {//设置选项卡的数据
        @Override
        public int getCount() {
            return mDataList.size();
        }
        @Override
        public IPagerTitleView getTitleView(Context context, final int index) {
            ClipPagerTitleView clipPagerTitleView = new ClipPagerTitleView(context);
            clipPagerTitleView.setText(mDataList.get(index));//设置Tab内容
            clipPagerTitleView.setTextColor(Color.parseColor("#f2c4c4"));//设置字体颜色
            clipPagerTitleView.setClipColor(Color.WHITE);//是指选中颜色
            clipPagerTitleView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewPager.setCurrentItem(index);//设置ViewPager显示界面
                }
            });
            return clipPagerTitleView;
        }

        @Override
        public IPagerIndicator getIndicator(Context context) {
            return null;
        }
    };
}
