package com.mingrisoft.nqbximage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/30 0030.
 */

public class MyView extends View {
    private List<MyBean> list = null;
    private int MaxAlpha = 255;// 最大透明度
    private boolean START = true;// 如果不设置这个START进行判断,每次点击屏幕后,屏幕只允许出现一个圆
    private Bitmap bitmap;
    //定义两个常量，这两个常量指定该图片横向、纵向上都被划分为20格。
    private final int WIDTH = 20;
    private final int HEIGHT = 20;
    //记录该图片上包含441个顶点
    private final int COUNT = (WIDTH + 1) * (HEIGHT + 1);
    //定义一个数组，保存Bitmap上的21 * 21个点的座标
    private final float[] verts = new float[COUNT * 2];
    //定义一个数组，记录Bitmap上的21 * 21个点经过扭曲后的座标
    //对图片进行扭曲的关键就是修改该数组里元素的值。
    private final float[] orig = new float[COUNT * 2];

    public MyView(Context context, int drawableId) {
        super(context);
        list = new ArrayList<MyBean>();
        setFocusable(true);
        //根据指定资源加载图片
        bitmap = BitmapFactory.decodeResource(getResources(),
                drawableId);
        //获取图片宽度、高度
        WindowManager wm = (WindowManager) getContext()
                .getSystemService(Context.WINDOW_SERVICE);
        //获取图片宽高
        int bitmapWidth = wm.getDefaultDisplay().getWidth();
        int bitmapHeight = wm.getDefaultDisplay().getHeight();
        int index = 0;
        for (int y = 0; y <= HEIGHT; y++) {
            float fy = bitmapHeight * y / HEIGHT;
            for (int x = 0; x <= WIDTH; x++) {
                float fx = bitmapWidth * x / WIDTH;
                    /*
					 * 初始化orig、verts数组。
					 * 初始化后，orig、verts两个数组均匀地保存了21 * 21个点的x,y座标
					 */
                orig[index * 2 + 0] = verts[index * 2 + 0] = fx;
                orig[index * 2 + 1] = verts[index * 2 + 1] = fy;
                index += 1;
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
			/* 对bitmap按verts数组进行扭曲
			 * 从第一个点（由第5个参数0控制）开始扭曲
			 */
        canvas.drawBitmapMesh(bitmap, WIDTH, HEIGHT, verts
                , 0, null, 0, null);
        for (int i = 0; i < list.size(); i++) {
            MyBean wave = list.get(i);
            canvas.drawCircle(wave.X, wave.Y, wave.radius, wave.paint);
        }
    }

    //工具方法，用于根据触摸事件的位置计算verts数组里各元素的值
    private void warp(float cx, float cy) {
        for (int i = 0; i < COUNT * 2; i += 2) {
            float dx = cx - orig[i + 0];
            float dy = cy - orig[i + 1];
            float dd = dx * dx + dy * dy;
            //计算每个座标点与当前点（cx、cy）之间的距离
            float d = (float) Math.sqrt(dd);
            //计算扭曲度，距离当前点（cx、cy）越远，扭曲度越小
            float pull = 80000 / ((float) (dd * d));
            //对verts数组（保存bitmap上21 * 21个点经过扭曲后的座标）重新赋值
            if (pull >= 1) {
                verts[i + 0] = cx;
                verts[i + 1] = cy;
            } else {
                //控制各顶点向触摸事件发生点偏移
                verts[i + 0] = orig[i + 0] + dx * pull;
                verts[i + 1] = orig[i + 1] + dy * pull;
            }
        }
        //通知View组件重绘
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        //调用warp方法根据触摸屏事件的座标点来扭曲verts数组
        warp(event.getX(), event.getY());
        // 点击屏幕后 半径设为0,alpha设置为255
        MyBean bean = new MyBean();
        bean.radius = 0; // 点击后 半径先设为0
        bean.alpha = MaxAlpha; // alpha设为最大值 255
        bean.width = bean.radius / 8; // 描边宽度 这个随意
        bean.X = (int) event.getX(); // 所绘制的圆的X坐标
        bean.Y = (int) event.getY(); // 所绘制的圆的Y坐标
        bean.paint = initPaint(bean.alpha, bean.width);
        if (list.size() == 0) {
            // 如果不设置这个START进行判断,每次点击屏幕后,屏幕只允许出现一个圆
            START = true;
        }
        //添加进集合
        list.add(bean);
        //更新
        invalidate();
        if (START) {
            //发送消息
            handler.sendEmptyMessage(0);
        }
        return true;

    }


    private Paint initPaint(int alpha, float width) {
        Paint paint = new Paint();//初始化画笔
        paint.setAntiAlias(true);// 抗锯齿
        paint.setStrokeWidth(width);// 描边宽度
        paint.setStyle(Paint.Style.STROKE);// 圆环
        paint.setAlpha(alpha);// 透明度
        paint.setColor(Color.RED);// 颜色
        return paint;
    }

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    Refresh();
                    invalidate();
                    if (list != null && list.size() > 0) {
                        handler.sendEmptyMessageDelayed(0, 50);// 每50毫秒发送
                    }
                    break;

                default:
                    break;
            }
        }

    };

    /***
     * 刷新
     */
    private void Refresh() {
        for (int i = 0; i < list.size(); i++) {
            MyBean bean = list.get(i);
            if (START == false && bean.alpha == 0) {
                list.remove(i);
                bean.paint = null;
                bean = null;
                continue;
            } else if (START == true) {
                START = false;
            }
            bean.radius += 5;// 半径每次+5
            bean.alpha -= 10;// 透明度每次减10
            if (bean.alpha < 0) {
                // 透明度小雨0的时候 赋为0
                bean.alpha = 0;
            }
            bean.width = bean.radius / 8; // 描边宽度设置为半径的1/4
            bean.paint.setAlpha(bean.alpha);
            bean.paint.setStrokeWidth(bean.width);
        }
    }

}