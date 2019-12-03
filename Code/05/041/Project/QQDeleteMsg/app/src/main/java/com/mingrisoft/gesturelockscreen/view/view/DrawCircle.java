package com.mingrisoft.gesturelockscreen.view.view;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.OvershootInterpolator;

/**
 * Created by Root on 2016/6/24.
 */
public class DrawCircle extends View {

    private Paint mPaint;
    private PointF mCicleCenterPoint;
    private PointF mDragCicleCenter;
    private float mRadius;
    private float mDragRadius;
    private Paint mTextPaint;
    private int mStatusBarHeight;
    private PointF mPointA;
    private PointF mPointB;
    private PointF mPointC;
    private PointF mPointD;
    private PointF mPointE;
    private int mRang;
    private boolean isOutOfRang = false;
    private float saveRdius = 0;
    private boolean isDisappear = false;
    private int mNumber = 0;

    public DrawCircle(Context context) {
        this(context, null);
    }

    public DrawCircle(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DrawCircle(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mPaint = new Paint();               //初始化画圆画笔
        mPaint.setColor(Color.RED);         //红色的画笔
        mPaint.setAntiAlias(true);          //抗锯齿
        mPaint.setStyle(Paint.Style.FILL);  //填充
        mPaint.setDither(true);             //防抖动
        /**
         * 定义三个float
         * */
        mRadius = dp2px(context, 9);
        mDragRadius = mRadius;
        saveRdius = mRadius;
        mTextPaint = new Paint();            //初始化写文字画笔
        mTextPaint.setTextAlign(Paint.Align.CENTER);//设置居中
        mTextPaint.setColor(Color.WHITE);   //设置白色
        mTextPaint.setTextSize(mRadius * 1.2f);
        mTextPaint.setAntiAlias(true);       //抗锯齿
        mTextPaint.setDither(true);         //防止抖动
        mStatusBarHeight = getStatusBarHeight();


    }


    public void setCenterPoint(float x, float y) {


        mCicleCenterPoint = new PointF(x, y);

        mDragCicleCenter = new PointF(x, y);
        /**
         * 设置A,B,C,D,E的点
         * */
        mPointA = new PointF(x + mRadius, y);
        mPointB = new PointF(x - mRadius, y);
        mPointC = new PointF(x + mRadius, y);
        mPointD = new PointF(x - mRadius, y);
        mPointE = new PointF(x, y);
    }


    public void setDragRang(int rang) {
        mRang = rang;
    }

    public void setNumber(int number) {
        mNumber = number;

    }

    @Override
    protected void onDraw(Canvas canvas) {

        canvas.save();
        canvas.translate(0, -mStatusBarHeight);


        if (!isOutOfRang) {

            Path path = new Path();    //初始化路径
            path.moveTo(mPointA.x, mPointA.y);  //到A点
            path.quadTo(mPointE.x, mPointE.y, mPointC.x, mPointC.y); //从A走贝塞尔曲线到C点
            path.lineTo(mPointD.x, mPointD.y);                       //从C走直线到D点
            path.quadTo(mPointE.x, mPointE.y, mPointB.x, mPointB.y); //从D点走贝塞尔曲线到B点
            path.close();                                             //关闭路径,形成回路最后终点B点直线到起点A点
            canvas.drawPath(path, mPaint);                           //画出路径
            canvas.drawCircle(mCicleCenterPoint.x, mCicleCenterPoint.y, mRadius, mPaint); //画固定圆
        }

        if (!isDisappear) {


            canvas.drawCircle(mDragCicleCenter.x, mDragCicleCenter.y, mDragRadius, mPaint);//画拖动圆


            canvas.drawText("" + mNumber, mDragCicleCenter.x, mDragCicleCenter.y + mDragRadius / 2, mTextPaint); //画文字
        }

        canvas.restore();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        float delX;
        float dely;
        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:

                break;

            case MotionEvent.ACTION_MOVE:

                mDragCicleCenter.x = event.getRawX();         //手机坐标
                mDragCicleCenter.y = event.getRawY();
                ComputerFivePoint();                            //计算ABCD坐标

                delX = Math.abs(mDragCicleCenter.x - mCicleCenterPoint.x);
                dely = Math.abs(mDragCicleCenter.y - mCicleCenterPoint.y);

                if (Math.sqrt(delX * delX + dely * dely) <= mRang) {         //拖动距离
                    mRadius = (float) (saveRdius - Math.sqrt(delX * delX + dely * dely) / mRang * (mRadius / 7 * 6));           //固定圆半径缩小
                } else {
                    isOutOfRang = true;
                }

                break;

            case MotionEvent.ACTION_UP:

                if (isOutOfRang) {

                    delX = Math.abs(mDragCicleCenter.x - mCicleCenterPoint.x);
                    dely = Math.abs(mDragCicleCenter.y - mCicleCenterPoint.y);
                    if (Math.sqrt(delX * delX + dely * dely) < mRang) {
                        isOutOfRang = false;
                        mDragCicleCenter.x = mCicleCenterPoint.x;
                        mDragCicleCenter.y = mCicleCenterPoint.y;
                        ComputerFivePoint();
                    } else {
                        isDisappear = true;
                        invalidate();

                        if (monDragBallListener != null) {
                            monDragBallListener.onDisappear(this, mDragCicleCenter.x, mDragCicleCenter.y); //消失的时候
                        }
                    }

                } else {

                    ValueAnimator animator = ValueAnimator.ofFloat(1.0f);
                    animator.setInterpolator(new OvershootInterpolator(5.0f));

                    final PointF startPoint = new PointF(mDragCicleCenter.x, mDragCicleCenter.y);
                    animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {

                            float fraction = animation.getAnimatedFraction();  //百分比

                            mDragCicleCenter.x = startPoint.x + (mCicleCenterPoint.x - startPoint.x) * fraction;
                            mDragCicleCenter.y = startPoint.y + (mCicleCenterPoint.y - startPoint.y) * fraction;
                            Log.d("DragBall", "fraction:" + fraction);

                            ComputerFivePoint();
                        }
                    });

                    animator.setDuration(200);
                    animator.start();

                    animator.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            if (monDragBallListener != null) {
                                monDragBallListener.onReset();
                            }
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    });
                }

                mRadius = saveRdius;          //固定圆半径还原


                break;
        }

        return true;
    }

    /**
     * 计算图中的ABCDE点
     */
    private void ComputerFivePoint() {

        mPointE.x = mDragCicleCenter.x / 2 - mCicleCenterPoint.x / 2 + mCicleCenterPoint.x;
        mPointE.y = mDragCicleCenter.y / 2 - mCicleCenterPoint.y / 2 + mCicleCenterPoint.y;

        double tan = (mDragCicleCenter.x - mCicleCenterPoint.x) / (mDragCicleCenter.y - mCicleCenterPoint.y);
        double atan = Math.atan(tan);  //得到角度
        mPointA.x = (float) (mCicleCenterPoint.x + Math.cos(atan) * mRadius);
        mPointA.y = (float) (mCicleCenterPoint.y - Math.sin(atan) * mRadius);
        mPointB.x = (float) (mCicleCenterPoint.x - Math.cos(atan) * mRadius);
        mPointB.y = (float) (mCicleCenterPoint.y + Math.sin(atan) * mRadius);


        mPointC.x = (float) (mDragCicleCenter.x + Math.cos(atan) * mDragRadius);
        mPointC.y = (float) (mDragCicleCenter.y - Math.sin(atan) * mDragRadius);
        mPointD.x = (float) (mDragCicleCenter.x - Math.cos(atan) * mDragRadius);
        mPointD.y = (float) (mDragCicleCenter.y + Math.sin(atan) * mDragRadius);

        invalidate();
    }


    public int getStatusBarHeight() {//获取状态栏高度
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }


    public interface onDragBallListener {
        void onDisappear(DrawCircle drawCircle, float x, float y);

        void onReset();
    }

    private onDragBallListener monDragBallListener;

    public void setonDragBallListener(onDragBallListener onDragBallListener) {
        monDragBallListener = onDragBallListener;
    }

    public int dp2px(Context context, int dp) {

        return (int) (context.getResources().getDisplayMetrics().density * dp);
    }
}
