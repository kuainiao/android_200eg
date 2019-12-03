package com.mingrisoft.phonenet.utils;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;

import com.mingrisoft.phonenet.R;


public class TSP extends ImageView {
    private static final int COUNTS = 1;
    private int translateSize;//周期性
    private int counts = COUNTS;//周期数
    private int controllerPointHeight;//控制点的高度
    private int darwHeight;//绘制位置-->固定高度
    private int translateWidth;//平移距离
    private Paint fore, back, text;
    private Path water;
    private int line, num;
    private int width, height;
    ValueAnimator a;

    public TSP(Context context) {
        super(context);

    }

    public TSP(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
        line = height;
        translateSize = w / counts;
        controllerPointHeight = h / counts / counts / 16;
        darwHeight = h / counts;

    }

    private void init() {
        //关闭硬件加速
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        fore = new Paint(Paint.ANTI_ALIAS_FLAG);  //初始化画笔
        fore.setDither(true);
        fore.setStyle(Paint.Style.FILL);
        fore.setColor(R.color.wave);   // 设置画笔的颜色
        back = new Paint(Paint.ANTI_ALIAS_FLAG);  //初始化画笔
        back.setDither(true);
        back.setStyle(Paint.Style.FILL);
        fore.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        back.setColor(R.color.bj);
        text = new Paint();   //初始化画笔
        text.setColor(Color.WHITE);// 设置画笔的颜色
        text.setTextAlign(Paint.Align.CENTER);  //设置文本的位置
        text.setTextSize(sp2px(12));
        water = new Path();
        a = ValueAnimator.ofFloat(1, 0);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.TRANSPARENT);
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, getWidth() / 2, back);  //画圆
        water.reset();   //重置
        canvas.save();   //画布保存
        canvas.translate(-translateSize + translateWidth, 0);
        water.moveTo(0, line);   //水波的移动
        for (int i = 0; i < counts + 1; i++) {
            water.quadTo(translateSize / 4 + translateSize * i, line - controllerPointHeight, translateSize / 2 + translateSize * i, line);
            water.quadTo(translateSize / 4 * 3 + translateSize * i, line + controllerPointHeight, translateSize + translateSize * i, line);
        }
        water.lineTo(width + translateSize, height);
        water.lineTo(-translateSize, height);
        water.close();
        canvas.drawPath(water, fore);
        canvas.restore();
        canvas.drawText(num + "kb/s", getWidth() / 2, getHeight() / 2, text);  //绘制网速
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }


    /**
     * 开始动画
     * */
    public void startAm() {
        a.setDuration(10 * 1000);
        a.setRepeatMode(ValueAnimator.RESTART);
        a.setRepeatCount(ValueAnimator.INFINITE);
        a.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float a = (float) valueAnimator.getAnimatedValue();
                translateWidth = (int) (a * translateSize);
                invalidate();
            }
        });
        a.start();
    }
    /**
     * 设置水波的高度
     * */
    public void setWaveHeight(int num) {
        this.num = num;
        line = height - num / 10;
        invalidate();
        if (!a.isRunning()) {
            startAm();   //调用开始动画的方法
        }
    }

    /**
     * 停止动画
     * */
    public void stop() {
        a.cancel(); //将动画清除
    }

    private int sp2px(int values) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, values, getResources().getDisplayMetrics());
    }
}
