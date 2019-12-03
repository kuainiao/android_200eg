package com.mingrisoft.brokenscreen;


import android.content.res.TypedArray;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.mingrisoft.brokenscreen.tool.BrokenTouchListener;
import com.mingrisoft.brokenscreen.tool.BrokenView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private BrokenView mBrokenView;     //自定义的碎屏视图
    private RelativeLayout parentLayout;  // 父类的相对布局
    private ImageView imageView;       //图片
    private boolean hasAlpha;     //设置一个boolean的值，用来判断是否已经碎屏
    private int resids[];          //用于加载图片
    private BrokenTouchListener colorfulListener, whiteListener;  //屏幕的长按监听事件
    private Paint whitePaint;      //画笔
    private ImageButton imageButton;   //用于重新加载界面的按钮


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);    //绑定布局文件
        initView();                                 //初始化控件
        mBrokenView = BrokenView.add2Window(this);   //绑定碎屏视图
        whitePaint = new Paint();                   //初始化画笔
        whitePaint.setColor(0xffffffff);        //设置画笔的颜色
        //初始化碎屏监听
        colorfulListener = new BrokenTouchListener.Builder(mBrokenView).build();
        //将画笔设置到监听上
        whiteListener = new BrokenTouchListener.Builder(mBrokenView).setPaint(whitePaint).build();
        setOnTouchListener();
    }

    /**
     * 初始化
     * */
    private void initView() {
        parentLayout = (RelativeLayout) findViewById(R.id.demo_parent);
        imageView = (ImageView) findViewById(R.id.demo_image);
        imageButton = (ImageButton) findViewById(R.id.action_refresh);
        //点击刷新按钮时，将界面重新加载
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.action_refresh:
                        mBrokenView.reset();
                        refreshDate();   //刷新数据
                        setOnTouchListener(); //设置监听的方法
                        setViewVisible();     //将视图显示出来
                        break;
                }
            }
        });
        TypedArray ar = getResources().obtainTypedArray(R.array.imgArray);
        int len = ar.length();
        resids = new int[len];
        for (int i = 0; i < len; i++)
            resids[i] = ar.getResourceId(i, 0);
        refreshDate();
    }

    /**
     * 设置显示及隐藏的方法
     * */
    private void setViewVisible() {
        parentLayout.setVisibility(View.VISIBLE);
        imageView.setVisibility(View.VISIBLE);
    }

    /**
     * 刷新的方法
     * */
    public void refreshDate() {
        Random rand = new Random();
        int pos = rand.nextInt(resids.length);
        imageView.setImageResource(resids[pos]);
        if (pos == 0 || pos == 1 || pos == 2)
            hasAlpha = true;
        else
            hasAlpha = false;
    }

    /**
     * 设置监长长按的监听事件
     * */
    public void setOnTouchListener() {

        parentLayout.setOnTouchListener(colorfulListener); //父布局的设置碎屏监听

        if (hasAlpha)
            imageView.setOnTouchListener(whiteListener);
        else
            imageView.setOnTouchListener(colorfulListener);
    }

}
