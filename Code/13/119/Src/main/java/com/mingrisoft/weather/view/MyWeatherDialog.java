package com.mingrisoft.weather.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.mingrisoft.weather.R;
import com.mingrisoft.weather.clazz.Bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Author LYJ
 * Created on 2017/1/21.
 * Time 20:41
 */

public class MyWeatherDialog extends Dialog{
    private ViewPager pager;
    private static List<Bean.ResultEntity.DataEntity.WeatherEntity> list;
    private static List<View> views;
    private LayoutInflater inflater;
    public MyWeatherDialog(Context context) {
        super(context, R.style.MyDialog);
        inflater = LayoutInflater.from(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog);
        Window window = this.getWindow();
        DisplayMetrics dm = getContext().getResources().getDisplayMetrics();
        WindowManager.LayoutParams p = window.getAttributes();
        p.height = dm.heightPixels /3;
        p.width = (int) (dm.widthPixels * 0.9f);
        window.setAttributes(p);
        initView();
        initData();

    }

    private void initData() {
        if (list != null){
            views = new ArrayList<>();

            for (int i = 0;i< list.size();i++){
                View view  =inflater.inflate(R.layout.item,null);
                TextView d_t = (TextView) view.findViewById(R.id.d_t);
                TextView d_w = (TextView) view.findViewById(R.id.d_w);
                TextView n_t = (TextView) view.findViewById(R.id.n_t);
                TextView n_w = (TextView) view.findViewById(R.id.n_w);
                TextView date = (TextView) view.findViewById(R.id.d_date);
                TextView week = (TextView) view.findViewById(R.id.d_week);
                Bean.ResultEntity.DataEntity.WeatherEntity entity = list.get(i);
                d_t.setText(entity.getInfo().getDay().get(0) + "~" + entity.getInfo().getDay().get(2) + "℃");
                d_w.setText(entity.getInfo().getDay().get(1));
                n_t.setText(entity.getInfo().getNight().get(0) + "~" + entity.getInfo().getNight().get(2) + "℃");
                n_w.setText(entity.getInfo().getNight().get(1));
                date.setText("日期：" + entity.getDate());
                week.setText("星期" + entity.getWeek());
                views.add(view);
            }
            pager.setAdapter(new WeatherAdapter());
        }

    }

    private void initView() {
        pager = (ViewPager) findViewById(R.id.pager);
    }

    public void setWeather(List<Bean.ResultEntity.DataEntity.WeatherEntity> list){
       this.list = list;
    }


    private static class WeatherAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return views.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = views.get(position);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(views.get(position));
        }
    }
}
