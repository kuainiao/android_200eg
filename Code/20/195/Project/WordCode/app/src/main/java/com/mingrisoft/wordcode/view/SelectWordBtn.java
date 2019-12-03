package com.mingrisoft.wordcode.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.Button;

import java.util.Random;

/**
 * Author LYJ
 * Created on 2017/1/24.
 * Time 15:32
 */

public class SelectWordBtn extends Button{


    private Random random = new Random();//用来获取随机数
    private static float SCALEX = 0.1f;//字体缩放
    private static float DEGRESS = 10.0F;//旋转角度
    private static int MAX = 5 ;//最大
    private float offset;//偏移量
    public SelectWordBtn(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public SelectWordBtn(Context context) {
        super(context);
        init();
    }

    public SelectWordBtn(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /***
     * 初始化
     */
    private void init(){
        int offsetType = random.nextInt(2)*2 - 1;//取正负数
        int offsetValues = random.nextInt(MAX) + 1;//获取1~5的随机数
        offset = offsetType*offsetValues;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.rotate(offset*DEGRESS,getWidth()/2,getHeight()/2);
        setTextScaleX(1.0f + offset*SCALEX);
        super.onDraw(canvas);
    }
}
