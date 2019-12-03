package com.mingrisoft.lookme;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Administrator on 2016/11/23.
 */

public class RightEyeView extends View {

    Paint paint2;
    float x = 50, y = 50, z = 50, q = 50, s = 0;

    public RightEyeView(Context context) {
        super(context);
        paint2 = new Paint();
    }

    public RightEyeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        canvas.save();
//        canvas.rotate(5);
//        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
//        canvas.restore();

        // 消除锯齿
        paint2.setAntiAlias(true);
        // 设置画笔的颜色
        paint2.setColor(Color.BLACK);
        // 设置paint的外框宽度
//        paint.setStrokeWidth(2);
        canvas.save();                                  //锁定画布
        canvas.rotate(s, x, y);                        //旋转45
//        canvas.drawCircle(x, y, 100, paint);
        canvas.drawCircle(z, q, 20, paint2);
        canvas.restore();


    }

    /**
     * 手指抬起时眼睛的方向
     */
    public void setXY(float x, float y) {
        this.x = x;
        this.y = y;
        z = x;
        q = y;
        invalidate();
    }

    /**
     * 手指点在屏幕上时设置眼睛的方向
     */
    public void setZQ(float z, float q) {
        this.z = z;
        this.q = q;
        invalidate();
    }

    /**
     * 设置画布旋转的角度
     */
    public void setS(float s) {
        this.s = s;
        invalidate();
    }

}
