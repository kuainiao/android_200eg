package com.mingrisoft.weimain;


import android.graphics.Color;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/11/23.
 */

public class TabFragment extends Fragment {
    private String lMain = "Default";  //主视图显示文本

    public static final String MAIN = "title";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        if (getArguments() != null)  //如果已经传值
        {
            lMain = getArguments().getString(MAIN);  //将获取到的值，赋值给主视图显示文本
        }
        TextView tv = new TextView(getActivity());  //创建TextView对象
        tv.setTextSize(20);                         //设置文本字体大小
        tv.setBackgroundColor(Color.parseColor("#ffffffff"));   //设置文本字体颜色
        tv.setText(lMain);                                     //设置文本显示内容
        tv.setGravity(Gravity.CENTER);                          //设置文本显示位置居中
        return tv;
    }

}
