package com.mingrisoft.scatterchart;

import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.github.mikephil.charting.charts.ScatterChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.ScatterData;
import com.github.mikephil.charting.data.ScatterDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    public ScatterChart scatterChart;
    private Random random;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        scatterChart = (ScatterChart) findViewById(R.id.scatter_chart);
        random = new Random();
        initScatterChart();
    }
    private void initScatterChart() {
        ArrayList<Entry> yVals = new ArrayList<>();
        //初始化横纵坐标内容
        final ArrayList<String> xVals = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            xVals.add((i + 1) + "月");
            //产生数据
            yVals.add(new Entry(random.nextInt(1000), i));
        }
        //封装数据
        ScatterDataSet scatterDataSet = new ScatterDataSet(yVals, "每月支出");
        //设置丰富多彩的颜色
        scatterDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        //生成Scatterdata对象
        ScatterData scatterData = new ScatterData(xVals, scatterDataSet);
        //设置对应数据
        scatterChart.setData(scatterData);
        scatterChart.getLegend().setPosition(Legend.LegendPosition.ABOVE_CHART_LEFT);
        scatterChart.getLegend().setForm(Legend.LegendForm.CIRCLE);
        //绑定点击事件
        scatterChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onValueSelected(Entry entry, int i, Highlight highlight) {
                //弹出提示
                Toast.makeText(MainActivity.this, xVals.get(entry.getXIndex())+"支出： " +
                        entry.getVal()+"元", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected() {
            }
        });
        //设置X轴位置
        scatterChart.getXAxis().setPosition(XAxis.XAxisPosition.TOP);
        //右侧Y轴关闭
        scatterChart.getAxisRight().setEnabled(false);
        //设置最小Y值
        scatterChart.getAxisLeft().setAxisMinValue(0.0f);
        //设置纵向网格线条颜色
        scatterChart.getXAxis().setGridColor(Color.GRAY);
        //设置横向网格颜色大小
        scatterChart.getAxisLeft().setGridColor(Color.GRAY);
        //设置描述内容
        scatterChart.setDescription("支出表");
        //设置描述文字的字体
        scatterChart.setDescriptionTextSize(20.f);
        //动画效果
        scatterChart.animateXY(1000, 1000);
    }
}
