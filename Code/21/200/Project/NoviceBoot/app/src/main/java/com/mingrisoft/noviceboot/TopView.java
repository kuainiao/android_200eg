package com.mingrisoft.noviceboot;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by Administrator on 2017/1/23.
 */

public class TopView extends View {

    private Paint pCircle, pMath, pText;
    private int h = 500, w = 500, s = 10, d = 10, x = 0, y = 0;
    private String text;

    public TopView(Context context, AttributeSet attrs) {
        super(context, attrs);
        pCircle = new Paint();
        pMath = new Paint();
        pText = new Paint();
        pCircle.setColor(Color.WHITE);
        pMath.setColor(Color.BLACK);
        pText.setColor(Color.WHITE);
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);

    }

    public TopView(Context context) {
        super(context);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        /**
         * 设置重叠部分透明
         * */
        canvas.drawRect(w/2-130, 250, w/2+330,330, pCircle);   //画矩形
        pMath.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.XOR));  //设置重叠透明
        canvas.drawRect(0, 0, w, h, pMath);      //画矩形
        pText.setTextSize(50);                   //绘制提示文字的大小
        canvas.drawText(text, w/2-100, 400, pText);      //绘制提示文字

    }

    /**
     * 根据屏幕的宽高设置矩形的大小
     * */
    public void getWindowWH(int w, int h) {
        this.h = h;
        this.w = w;
        invalidate();   //重新绘制
    }

    public void getMyRect(int s, int d, int x, int y, String text) {
        this.s = s;
        this.d = d;
        this.x = x;
        this.y = y;
        this.text = text;
        invalidate();
    }
}
