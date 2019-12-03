package com.mingrisoft.customtoast;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.util.TypedValue;

/**
 * Author LYJ
 * Created on 2017/1/26.
 * Time 09:42
 */

public class BackgroundDrawable extends Drawable{
    private Paint paint;
    private Context mContext;
    public BackgroundDrawable(@ColorInt int color,Context context) {
        mContext = context.getApplicationContext();
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(color);
        paint.setDither(true);
        paint.setStyle(Paint.Style.FILL);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void draw(Canvas canvas) {
        int width = canvas.getWidth();
        int height = canvas.getHeight();
        canvas.drawRoundRect(0,0,width,height,dp2px(20),dp2px(20),paint);
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
}
