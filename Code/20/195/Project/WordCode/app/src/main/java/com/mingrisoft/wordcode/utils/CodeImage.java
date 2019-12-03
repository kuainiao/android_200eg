package com.mingrisoft.wordcode.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.util.TypedValue;

import java.util.Arrays;
import java.util.Random;

/**
 * Author LYJ
 * Created on 2017/1/24.
 * Time 16:14
 */

public class CodeImage extends Drawable{

    private static int DEFAULT_WIDTH = 120;
    private static int DEFAULT_HEIGHT = 40;
    private Paint mPaint;//画笔
    private Context mContext;//上下文
    private int width;//宽度
    private int height;//高度
    private int boundWidth;//边框宽度
    private char[] drawText;//文本数组
    private Random random = new Random();//获取随机阿数
    public CodeImage(Context context,int drawWidth,int drawHeight,String text){
        init(context,text,drawWidth,drawHeight);
    }
    public CodeImage(Context context,String text) {
        init(context,text,DEFAULT_WIDTH,DEFAULT_HEIGHT);
    }

    /**
     * 初始化
     * @param context
     * @param drawWidth
     * @param drawHeight
     */
    private void init(Context context,String text ,int drawWidth, int drawHeight) {
        mContext = context;
        width = dp2px(drawWidth * 0.8f);
        height = dp2px(drawHeight* 0.8f);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStrokeWidth(dp2px(1));
        mPaint.setStyle(Paint.Style.STROKE);
        boundWidth = dp2px(1);
        drawText = text.toCharArray();
        Log.i("验证码", Arrays.toString(drawText));
    }

    @Override
    public void draw(Canvas canvas) {
        mPaint.setStrokeWidth(dp2px(1));
        mPaint.setColor(Color.BLACK);
        mPaint.setTextSize(sp2px(14));
        mPaint.setTextAlign(Paint.Align.CENTER);
       for (int i = 0 ; i <drawText.length;i++ ){
           int offsetType = random.nextInt(2)*2 - 1;//取正负数
           int offsetValues = random.nextInt(5) + 1;//获取1~5的随机数
           int x = width/4 *i+ width/4/2;
           int y = height/2+height/4;
           int offset = offsetType*offsetValues;
           mPaint.setTextSkewX(1.0f + offset * 0.1f);
           mPaint.setTextScaleX(1.0f + offsetType*0.1f);
           mPaint.setStrokeCap(Paint.Cap.ROUND);
           mPaint.setStrokeJoin(Paint.Join.MITER);
           canvas.drawText(drawText,i,1,x+dp2px(10),y,mPaint);
       }
    }

    private int dp2px(float values){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                values,mContext.getResources().getDisplayMetrics());
    }
    private int sp2px(float values){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                values,mContext.getResources().getDisplayMetrics());
    }
    @Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
    }
    @Override
    public void setColorFilter(ColorFilter colorFilter) {
        mPaint.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    @Override
    public int getIntrinsicHeight() {
        return height;
    }

    @Override
    public int getIntrinsicWidth() {
        return width;
    }
}
