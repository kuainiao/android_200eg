package com.mingrisoft.codecard.drawable;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;

/**
 * 作者： LYJ
 * 功能：
 * 创建日期： 2017/3/6
 */

public class CodeImageDrawable extends Drawable{

    private Paint paint;
    private Context mContext;
    private String code;
    private Bitmap background;
    public CodeImageDrawable(Context context , String codeStr, Bitmap back) {
        this.mContext = context.getApplicationContext();
        this.code = codeStr;
        this.background = back;
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.BLACK);
        paint.setDither(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setTextAlign(Paint.Align.CENTER);
    }
    @Override
    public void draw(Canvas canvas) {
        paint.setTextSize(sp2px(18));
        Rect rect = new Rect(0,0,canvas.getWidth(),canvas.getHeight());
        canvas.drawBitmap(background,rect,rect,null);
        canvas.drawText(code,canvas.getWidth()/2,getBaseLine(canvas.getHeight(),0),paint);
    }
    /**
     * 获取文字绘制基线
     *
     * @param bottom
     * @param top
     * @return
     */
    private int getBaseLine(int bottom, int top) {
        Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
        return (bottom + top - fontMetrics.bottom - fontMetrics.top) / 2;
    }
    @Override
    public void setAlpha(int alpha) {
        paint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
        paint.setColorFilter(colorFilter);
    }


    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    private int dp2px(int values){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,values,
                mContext.getResources().getDisplayMetrics());
    }
    private int sp2px(int values){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,values,
                mContext.getResources().getDisplayMetrics());
    }
}
