package com.mingrisoft.mylightsout;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


/**
 * Created by Administrator on 2016/12/15 0015.
 */
public class LightGameView extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder holder = null; //控制对象
    //画笔
    protected Paint mPaint;
    //路径
    protected Path mPath;
    //标识是否第一次
    Boolean kill = false;
    //自身的大小
    protected int mWidth, mHeight;
    private ArrayList<Point> ps = new ArrayList<>();
    //分数
    private int score = 0;
    //绘制线程
    private ThreadDrawUI threadDrawUI;
    private boolean isDrawUI = true;
    //灯泡图片
    private Bitmap light_on;
    private Bitmap light_off;
    //灯泡亮起数组
    Map<Point, Boolean> mapLists;
    //暂停图片
    private Bitmap game_over;
    private Bitmap start_btn;
    private int level = 1;  //等级
    private static int mScreenWidth;
    private static int mScreenHeight;
    public LightGameView(Context context, AttributeSet attr) {
        super(context);
        // TODO Auto-generated constructor stub
        holder = getHolder();
        mPaint = new Paint();
        mPath = new Path();
        //translucent半透明 transparent透明
        setZOrderOnTop(true);
        //窗口支持透明度
        getHolder().setFormat(PixelFormat.TRANSLUCENT);
        holder.addCallback(this);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mWidth = w;
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
        //加载资源
        game_over = BitmapFactory.decodeResource(getResources(), R.drawable.bird_gameover);
        start_btn = BitmapFactory.decodeResource(getResources(), R.drawable.bird_start_btn);
        light_on = BitmapFactory.decodeResource(getResources(), R.drawable.light_on);
        light_off = BitmapFactory.decodeResource(getResources(), R.drawable.light_off);
        //判断初始灯的开关
        inzy();
    }
    private void inzy() {
        //绘制画笔
        mPaint.setColor(Color.WHITE);
        mPaint.setTextSize(50);
        mPaint.setAntiAlias(true);
        mapLists = new HashMap<Point, Boolean>();
        //设置数组记录亮起的等 默认全部关闭
        for (int j = 0; j < 5; j++) {
            for (int i = 0; i < 5; i++) {
                Point p = new Point(i, j);
                //默认所有灯关闭
                mapLists.put(p, false);
            }
        }
        //设置默认亮灯 根据关卡设置
        for (int i = 0; i < level+3; i++) {
            int x = new Random().nextInt(4);
            int y = new Random().nextInt(4);
            //判断开启灯
            mapLists.put(new Point(x, y), true);
        }
        isDrawUI = true;
        //绘制线程
        threadDrawUI = new ThreadDrawUI();
        //开启线程
        threadDrawUI.start();
        kill = false;
    }

    //退出结束线程
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        isDrawUI = false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            //点击位置
            float x = event.getX();
            float y = event.getY();
            if (kill) {
                if (x < mWidth / 2 + start_btn.getWidth() / 2 && x > mWidth / 2 - start_btn.getWidth() / 2) {
                    if (y > mHeight / 2 + game_over.getHeight() && y < mHeight / 2 + game_over.getHeight() + start_btn.getHeight()) {
                        level++;
                        mapLists.clear();
                        inzy();
                    }
                }
            } else {
                //默认点
                int p_x = 10;
                int p_y = 10;
                //灯泡宽度
                int k = mWidth / 5 - 4;
                for (int i = 0; i < 5; i++) {
                    if (x > i * k && x < (i + 1) * k) {
                        p_x = i;
                    }
                    if (y > i * k && y < (i + 1) * k) {
                        p_y = i;
                    }
                }
                if (p_x != 10 && p_y != 10) {
                    //判断点击位置灯的变化
                    Boolean bl = mapLists.get(new Point(p_x, p_y));
                    if (bl) {
                        mapLists.put(new Point(p_x, p_y), false);
                    } else {
                        mapLists.put(new Point(p_x, p_y), true);
                    }
                    //上
                    if (p_y - 1 >= 0) {
                        Boolean bl1 = mapLists.get(new Point(p_x, p_y - 1));
                        if (bl1) {
                            mapLists.put(new Point(p_x, p_y - 1), false);
                        } else {
                            mapLists.put(new Point(p_x, p_y - 1), true);
                        }
                    }
                    // 下
                    if (p_y + 1 <= 4) {
                        Boolean bl2 = mapLists.get(new Point(p_x, p_y + 1));
                        if (bl2) {
                            mapLists.put(new Point(p_x, p_y + 1), false);
                        } else {
                            mapLists.put(new Point(p_x, p_y + 1), true);
                        }
                    }
                    // 左
                    if (p_x - 1 >= 0) {
                        Boolean bl3 = mapLists.get(new Point(p_x - 1, p_y));
                        if (bl3) {

                            mapLists.put(new Point(p_x - 1, p_y), false);

                        } else {
                            mapLists.put(new Point(p_x - 1, p_y), true);
                        }
                    }
                    // 右
                    if (p_x + 1 <= 4) {
                        Boolean bl4 = mapLists.get(new Point(p_x + 1, p_y));
                        if (bl4) {
                            mapLists.put(new Point(p_x + 1, p_y), false);
                        } else {
                            mapLists.put(new Point(p_x + 1, p_y), true);
                        }
                    }
                }else {
                     if(x>mWidth / 2 - start_btn.getWidth() / 2&&x<mWidth / 2 +start_btn.getWidth() / 2){
                         if(y>mWidth+ start_btn.getHeight()&&y<mWidth+start_btn.getHeight()*2){
                             //直接将map删除了
                             mapLists.clear();
                             isDrawUI = false;
                             kill = true;
                             inzy();
                         }
                     }
                }
            }
        }
        return true;
    }

    //循环遍历数组 判断等是否全部关闭
    public boolean checkgameover() {
        for (Map.Entry<Point, Boolean> entry : mapLists.entrySet()) {
            Boolean value = entry.getValue();
            if (value == true) {
                return false;
            }
        }
        return true;
    }

    //绘图线程
    class ThreadDrawUI extends Thread {
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public void run() {
            Canvas canvas;
            while (isDrawUI) {
                canvas = null;
                try {
                    // 锁定整个画布，在内存要求比较高的情况下，建议参数不要为null
                    canvas = holder.lockCanvas(null);
                    synchronized (holder) {//同步
                        if (isDrawUI) {
                            canvas.drawColor(Color.BLACK);
                            DisplayMetrics dm = getResources().getDisplayMetrics();
                            mScreenWidth = dm.widthPixels;
                            mScreenHeight = dm.heightPixels;
                            Bitmap bmp = ((BitmapDrawable)getResources().getDrawable(R.drawable.gamebj)).getBitmap();
                            Bitmap mBitmap = Bitmap.createScaledBitmap(bmp, mScreenWidth, mScreenHeight, true);
                            canvas.drawBitmap(mBitmap,0,0,null);
                            //单个布局宽，高
                            int w = mWidth / 5 - 4;
                            int h = w;
                            for (int j = 0; j < 5; j++) {
                                for (int i = 0; i < 5; i++) {
                                    Point p = new Point(i, j);
                                    RectF rectF = new RectF(1 + i * w, 1 + j * h, w + i * w, h + j * h);
                                    if(!mapLists.equals(null)) {
                                        if (mapLists.get(p)) {
                                            canvas.drawBitmap(light_on, null, rectF, null);
                                        } else {
                                            canvas.drawBitmap(light_off, null, rectF, null);
                                        }
                                    }
                                }
                            }
                            canvas.drawText("当前关卡:"+level,mWidth / 2 - start_btn.getWidth() / 2,
                                    mWidth+start_btn.getHeight()/2,mPaint);
                            canvas.drawBitmap(start_btn, mWidth / 2 - start_btn.getWidth() / 2,
                                    mWidth + start_btn.getHeight(), null);
                            if (checkgameover()) {
                                GameOver(canvas);
                            }
                        }
                    }

                } finally {//使用finally语句保证下面的代码一定会被执行
                    if (canvas != null) {
                        //更新屏幕显示内容
                        holder.unlockCanvasAndPost(canvas);
                    }
                }
            }
        }
    }



    //游戏结束方法
    public void GameOver(Canvas canvas) {
        //游戏结束绘制图画
        // 锁定整个画布，在内存要求比较高的情况下，建议参数不要为null
        isDrawUI = false;
        onGameStop(canvas);
        //游戏结束标识
        kill = true;
    }

    //绘制游戏结束画面
    private void onGameStop(Canvas canvas) {
//        canvas.drawColor(Color.WHITE);
        Bitmap bmp = ((BitmapDrawable)getResources().getDrawable(R.drawable.gamebj)).getBitmap();
        Bitmap mBitmap = Bitmap.createScaledBitmap(bmp, mScreenWidth, mScreenHeight, true);
        canvas.drawBitmap(mBitmap,0,0,null);
        canvas.drawBitmap(game_over, mWidth / 2 - game_over.getWidth() / 2,
                mHeight / 2 - game_over.getHeight(), null);
        canvas.drawBitmap(start_btn, mWidth / 2 - start_btn.getWidth() / 2,
                mHeight / 2 + game_over.getHeight(), null);
        canvas.drawText("当前关卡：" +level, mWidth / 2 - 150,
                mHeight / 2 - game_over.getHeight() * 2, mPaint);
        canvas.drawText("点击进入下一关", mWidth / 2 - 200,
                mHeight / 2 - game_over.getHeight() * 3, mPaint);
    }
}
