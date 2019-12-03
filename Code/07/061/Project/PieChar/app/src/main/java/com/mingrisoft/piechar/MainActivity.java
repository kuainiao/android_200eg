package com.mingrisoft.piechar;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public PieChart mChart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mChart = (PieChart) findViewById(R.id.pie_chart);
        PieData mPieData = getPieData(4, 100);
        showChart(mChart, mPieData);
    }
    private void showChart(PieChart pieChart, PieData pieData) {
        pieChart.setHoleColorTransparent(true);
        //半径
        pieChart.setHoleRadius(60f);
        //半透明圈
        pieChart.setTransparentCircleRadius(64f);
        //pieChart.setHoleRadius(0)//实心圆
        pieChart.setDescription("");
        //饼状图中间可以添加文字
        pieChart.setDrawCenterText(true);
        pieChart.setDrawHoleEnabled(true);
        //初始旋转角度
        pieChart.setRotationAngle(90);
        //可以手动旋转
        pieChart.setRotationEnabled(true);
        //显示成百分比
        pieChart.setUsePercentValues(true);
        //饼状图中间的文字
        pieChart.setCenterText("家庭支出");
        pieChart.setCenterTextColor(Color.BLUE);
        pieChart.setCenterTextSize(16f);
        pieChart.setCenterTextSizePixels(100f);
        //设置数据
        pieChart.setData(pieData);
        //设置比例图
        Legend mLegend = pieChart.getLegend();
        //最右边显示
        mLegend.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
        mLegend.setXEntrySpace(7f);
        mLegend.setYEntrySpace(5f);
        //设置动画
        pieChart.animateXY(1000, 1000);
    }
    //分成几部分
    private PieData getPieData(int count, float range) {
        //xVals用来表示每个饼块上的内容
        ArrayList<String> xValues = new ArrayList<String>();
            //饼块上显示文字
            xValues.add("衣");
            xValues.add("食");
            xValues.add("住");
            xValues.add("行");
            xValues.add("其它");
        //yVals用来表示封装每个饼块的实际数据
        ArrayList<Entry> yValues = new ArrayList<Entry>();
        // 饼图数据
        //将一个饼形图分成五个部分
        //所以 40代表的百分比就是40%
        float quarterly1 = 10;
        float quarterly2 = 20;
        float quarterly3 = 25;
        float quarterly4 = 40;
        float quarterly5 = 5;
        //添加数据到数组
        yValues.add(new Entry(quarterly1, 0));
        yValues.add(new Entry(quarterly2, 1));
        yValues.add(new Entry(quarterly3, 2));
        yValues.add(new Entry(quarterly4, 3));
        yValues.add(new Entry(quarterly5, 4));
        //y轴的集合/*显示在比例图上*/
        PieDataSet pieDataSet = new PieDataSet(yValues, "2016 家庭支出");
        //设置个饼状图之间的距离
        pieDataSet.setSliceSpace(0f);
        ArrayList<Integer> colors = new ArrayList<Integer>();
        // 饼图颜色
        colors.add(Color.GRAY);
        colors.add(Color.rgb(114, 188, 223));
        colors.add(Color.rgb(255, 123, 124));
        colors.add(Color.rgb(57, 135, 200));
        colors.add(Color.rgb(205, 205, 205));
        pieDataSet.setColors(colors);
        //设置圆盘文字颜色
        pieDataSet.setValueTextColor(Color.WHITE);
        pieDataSet.setValueTextSize(15f);
        //设置是否显示区域百分比的值
        //设置数据样式
        pieDataSet.setValueFormatter(new ValueFormatter()
        { @Override
        public String getFormattedValue(float value, Entry entry,
                                        int dataSetIndex, ViewPortHandler viewPortHandler)
        { return ""+(int)value+"%"; }
        });
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float px = 5 * (metrics.densityDpi / 160f);
        // 选中态多出的长度
        pieDataSet.setSelectionShift(px);
        PieData pieData = new PieData(xValues, pieDataSet);
        return pieData;
    }
}

