package com.mingrisoft.mybird;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/12/15 0015.
 */
public class BirdGameView extends SurfaceView implements SurfaceHolder.Callback {
    //控制对象 SurfaceHolder信息
    private SurfaceHolder holder = null;
    //画笔
    protected Paint mPaint;
    //画线点坐标
    int x = 0;
    //小鸟高度
    int birdy = 0;
    //标识是否第一次
    Boolean k = true;
    //自身的大小
    protected int mWidth, mHeight;
    //背景
    private Bitmap bj;
    //小鸟
    private Bitmap bird_im;
    //障碍
    private Bitmap top_za, bom_za;
    //暂停图片
    private Bitmap game_over;
    private Bitmap start_btn;
    //图片宽高：
    private int bird_h, bird_w, up_h, up_w;
    //障碍1 x坐标
    private int za_x1 = 0;
    //障碍2 x坐标
    private int za_x2 = 0;
    //分数
    private int score = 0;
    //绘制线程
    private ThreadDrawUI threadDrawUI;
    //绘制线程是否开启标识
    private boolean isDrawUI = true;
    //逻辑线程
    private ThreadLogic threadLogic;
    //逻辑线程是否开启标识
    private boolean isLogic = true;
   //构造函数
    public BirdGameView(Context context, AttributeSet attr) {
        super(context, attr);
        // TODO Auto-generated constructor stub
        //创建holder
        holder = getHolder();
        //获得holder回调信息
        holder.addCallback(this);
    }
    //获取自身大小
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        //自身的宽
        mWidth = w;
        //自身的高
        mHeight = h;
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
        // TODO Auto-generated method stub
    }

    //创建的时候调用方法
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // TODO Auto-generated method stub
        //创建绘制文字画笔
        mPaint = new Paint();
        //加载图片资源
        game_over = BitmapFactory.decodeResource(getResources(), R.drawable.bird_gameover);
        start_btn = BitmapFactory.decodeResource(getResources(), R.drawable.bird_start_btn);
        bj = BitmapFactory.decodeResource(getResources(), R.drawable.bird_bg);
        bird_im = BitmapFactory.decodeResource(getResources(), R.drawable.bird_hero);
        //获取小鸟图片宽高用于绘制以及判断
        bird_h = bird_im.getHeight();
        bird_w = bird_im.getWidth();
        top_za = BitmapFactory.decodeResource(getResources(), R.drawable.obstacle_up);
        bom_za = BitmapFactory.decodeResource(getResources(), R.drawable.obstacle_down);
        //获取障碍宽高用于判断游戏结束用
        up_h = top_za.getHeight();
        up_w = top_za.getWidth();
        //开启线程标识
        isLogic = true;
        isDrawUI = true;
        //绘制线程
        threadDrawUI = new ThreadDrawUI();
        //逻辑线程
        threadLogic = new ThreadLogic();
        //开启线程
        threadDrawUI.start();
        threadLogic.start();
    }
    //退出结束线程
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        isDrawUI = false;
        isLogic = false;
    }
    //手势判断
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (k) {//判断是否结束游戏
                float startx = event.getX();
                float starty = event.getY();
                //判断是否点击重新开始游戏
                if (startx < mWidth / 2 + start_btn.getWidth() / 2 && startx > mWidth / 2 - start_btn.getWidth() / 2) {
                    if (starty > mHeight/2 + game_over.getHeight() && starty < mHeight/2 + game_over.getHeight() + start_btn.getHeight()) {
                        isDrawUI = true;
                        isLogic = true;
                        //绘制线程
                        threadDrawUI = new ThreadDrawUI();
                        //逻辑线程
                        threadLogic = new ThreadLogic();
                        //开启线程
                        threadDrawUI.start();
                        threadLogic.start();
                    }
                }
            } else {//游戏没结束执行下面方法
                birdy -= 80;//使小鸟向上
            }
        }
        return true;
    }

    //绘图线程
    class ThreadDrawUI extends Thread {
        public void run() {
            Canvas canvas;
            while (isDrawUI) {
                canvas = null;
                try {
                    // 锁定整个画布，在内存要求比较高的情况下，建议参数不要为null
                    canvas = holder.lockCanvas(null);
                    synchronized (holder) {//同步
                        if (isDrawUI) {
                            //w和h分别是屏幕的宽和高，也就是你想让图片显示的宽和高
                            RectF rectF = new RectF(0, 0, mWidth, mHeight);
                            //绘制背景
                            canvas.drawBitmap(bj, null, rectF, null);
                            //绘制小鸟
                            canvas.drawBitmap(bird_im, mWidth / 2, birdy, null);
                            //绘制障碍1
                            canvas.drawBitmap(top_za, za_x1, -up_h / 2, null);
                            canvas.drawBitmap(bom_za, za_x1, mHeight - up_h / 2, null);
                            if (za_x1 < mWidth) {
                                //绘制障碍2
                                canvas.drawBitmap(top_za, za_x2, -up_h / 2, null);
                                canvas.drawBitmap(bom_za, za_x2, mHeight - up_h / 2, null);
                            }
                            mPaint.setColor(Color.RED);
                            mPaint.setTextSize(30f);
                            //绘制文字
                            canvas.drawText("分数" + score, 100, 100, mPaint);
                        }
                    }
                } finally {//使用finally语句保证下面的代码一定会被执行
                    if (canvas != null) {
                        //解锁更新屏幕显示内容
                        holder.unlockCanvasAndPost(canvas);
                    }
                }
            }
        }
    }

    //逻辑线程
    class ThreadLogic extends Thread {
        @Override
        public void run() {
            while (isLogic) {
                if (k) {//游戏结束后重新开始游戏
                    //设置初始高度
                    birdy = mHeight / 2;
                    za_x1 = mWidth;
                    za_x2 = -up_w;
                    k = false;
                } else {//游戏进行中
                    //移动小鸟
                    birdy += 1;
                    //移动障碍
                    za_x1--;
                    za_x2--;
                    //循环障碍1方法
                    if (za_x1 < -up_w) {
                        za_x1 = mWidth;
                    }
                    //循环障碍2方法
                    if (za_x1 == mWidth / 2) {
                        za_x2 = mWidth;
                    }
                    //设置得分方法
                    if (za_x1 == mWidth / 2 - up_w||za_x2 == mWidth / 2 - up_w) {
                        score += 1;
                    }
                    //判断游戏结束方法
                    if (za_x1 < mWidth / 2 + bird_w && za_x1 > mWidth / 2 - up_w) {
                        if (birdy + bird_h < mHeight - up_h / 2 && birdy > up_h / 2) {

                        } else {
                            GameOver();
                        }
                    }
                    //判断游戏结束方法
                    if (za_x2 < mWidth / 2 + bird_w && za_x2 > mWidth / 2 - up_w) {
                        if (birdy + bird_h < mHeight - up_h / 2 && birdy > up_h / 2) {

                        } else {
                            GameOver();
                        }
                    }
                }
                try {
                    //线程休眠 数值越小 游戏越流畅 相当于游戏帧
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //游戏结束方法
    public void GameOver() {
        //游戏结束绘制图画
        // 锁定整个画布，在内存要求比较高的情况下，建议参数不要为null
        isDrawUI = false;
        isLogic = false;
        Canvas canvas = holder.lockCanvas(null);
        //游戏结束画面
        onGameStop(canvas);
        holder.unlockCanvasAndPost(canvas);
        //游戏结束标识
        k = true;
        //清空得分
        score = 0;
    }
    //绘制游戏结束画面
    private void onGameStop(Canvas canvas) {
        RectF rectF = new RectF(0, 0, mWidth, mHeight);
        //绘制背景
        canvas.drawBitmap(bj, null, rectF, null);
        //绘制游戏结束画面
        canvas.drawBitmap(game_over, mWidth / 2 - game_over.getWidth() / 2,
                mHeight / 2 - game_over.getHeight(), null);
        //绘制重新开始按钮
        canvas.drawBitmap(start_btn, mWidth / 2 - start_btn.getWidth() / 2,
                mHeight / 2 + game_over.getHeight(), null);
        mPaint.setColor(Color.WHITE);
        mPaint.setTextSize(50f);
        //绘制得分
        canvas.drawText("得分：" + score, mWidth / 2 - 100, mHeight / 2 - game_over.getHeight() * 2, mPaint);
    }
}
