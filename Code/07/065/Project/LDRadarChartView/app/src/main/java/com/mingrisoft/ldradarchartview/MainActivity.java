package com.mingrisoft.ldradarchartview;


import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    public RadarChart radarChart;
    public ArrayList<String> x = new ArrayList<String>();
    public ArrayList<Entry> y = new ArrayList<Entry>();
    public ArrayList<RadarDataSet> radarDataSets = new ArrayList<RadarDataSet>();
    public RadarData radarData = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        radarChart = (RadarChart)findViewById(R.id.radar_chart);
        RadarData resultLineData = getRadarData(6, 100);
        showChart();
    }
    /**
     * 初始化数据
     * count 表示坐标点个数，range表示等下y值生成的范围
     */
    public RadarData getRadarData(int count, float range) {
        x.add("PAS传球");
        x.add("SHT射门");
        x.add("PHY对抗");
        x.add("DEF防御");
        x.add("SPD速度");
        x.add("DRI带球");
        //y轴的数据显示数据
        for (int i = 0; i < count; i++) {
            float result = (float) (Math.random() * range) + 3;
            y.add(new Entry(result, i));
        }
        RadarDataSet radarDataSet = new RadarDataSet(y, "人物能力数据");//y轴数据集合
        radarDataSet.setLineWidth(1f);//线宽
        radarDataSet.setColor(Color.BLUE);//线的颜色
        radarDataSet.setFillColor(Color.BLUE);//线框内颜色
        radarDataSet.setDrawFilled(true); // 是否实心填充区域
        radarDataSet.setValueTextColor(Color.WHITE);
        radarDataSet.setHighlightEnabled(false);//设置内容坐标点是否可以点击
        radarDataSets.add(radarDataSet);
        radarData = new RadarData(x, radarDataSets);
        return radarData;
    }

    //设置样式
    public void showChart() {
        radarChart.getYAxis().setEnabled(false);//去掉y轴值
        radarChart.getXAxis().setTextColor(Color.WHITE);//设置x轴文字颜色
        radarChart.setBackgroundColor(getResources().getColor(R.color.colortm));//背景颜色
        radarChart.setData(radarData);//设置数据
        radarChart.setDescription("");//设置描述内容
        Legend legend = radarChart.getLegend();//设置比例图片标示，就是那一组Y的value
        legend.setForm(Legend.LegendForm.CIRCLE);//样式
        legend.setFormSize(6f);//字号
        legend.setTextColor(Color.WHITE);//设置颜色
        radarChart.animateXY(2000,2000);//XY轴的动画
    }
}
