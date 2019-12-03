package com.mingrisoft.lookme;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Administrator on 2016/11/23.
 */

public class LeftEyeView extends View {

    Paint
//            paint,
            paint2;
    float x = 50, y = 50, z = 50, q = 50, s = 0;

    public LeftEyeView(Context context) {
        super(context);
//        paint = new Paint();
        paint2 = new Paint();
    }

    public LeftEyeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

//        paint.setStyle(Paint.Style.STROKE);
        // 消除锯齿
//        paint.setAntiAlias(true);
        // 消除锯齿
        paint2.setAntiAlias(true);
        // 设置画笔的颜色
        paint2.setColor(Color.BLACK);
        // 设置paint的外框宽度
//        paint.setStrokeWidth(2);

        canvas.save();                                  //锁定画布
        canvas.rotate(s, x, y);                        //旋转45
//        paint.setColor(Color.BLUE);                     //设置画笔颜色
//        canvas.drawCircle(x, y, 100, paint);
        canvas.drawCircle(z, q, 20, paint2);
        canvas.restore();
    }

    public void setXY(float x, float y) {
        this.x = x;
        this.y = y;
        z = x;
        q = y;
        invalidate();
    }

    public void setZQ(float z, float q) {
        this.z = z;
        this.q = q;
        invalidate();
    }

    public void setS(float s) {
        this.s = s;
        invalidate();
    }
}
