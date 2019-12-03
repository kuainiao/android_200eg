package com.mingrisoft.suspendedwindow;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Created by Administrator on 2017/1/25.
 */

public class WindowService extends Service {
    //设置跳转启动条件
    public static final String OPERATION = "operation";
    public static final int WINDOW_SHOW = 0;
    public static final int WINDOW_HIDE = 1;

    private boolean isAdd = false;                        //是否已增加悬浮窗
    private static WindowManager windowManager;              //窗口管理器服务
    private static WindowManager.LayoutParams layoutParams;        //窗口布局参数
    private LinearLayout float_show;                        //悬浮窗布局
    private ImageView leftView, moveView, rightView;      //悬浮窗图片

    private int screen_width;                               //屏幕的宽度
    int default_right;                                       //悬浮窗位置默认右侧
    int default_left;
    Animation show_anim;                                     //显示悬浮窗动画
    Animation hide_anim;                                     //隐藏悬浮窗动画

    // 是否有移动事件
    private boolean isMove = false;


    @Override
    public void onCreate() {
        super.onCreate();
        init();                                   //初始化
        createFloatView();                        //调用创建悬浮窗
    }

    private void init() {
        windowManager = (WindowManager) getApplicationContext().getSystemService(
                Context.WINDOW_SERVICE);        //获取窗口管理服务
        //获取屏幕宽度与高度
        screen_width = windowManager.getDefaultDisplay().getWidth();

        //显示动画
        show_anim = AnimationUtils.loadAnimation(WindowService.this,
                R.anim.show_anim);
        //隐藏动画
        hide_anim = AnimationUtils.loadAnimation(WindowService.this,
                R.anim.hide_anim);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);

        int operation = WINDOW_SHOW;
        if (intent != null)
            //设置两种操作条件,判断MainActivity类中传递过来的Intent信息，是显示还是隐藏
            operation = intent.hasExtra(OPERATION) ? intent.getIntExtra(
                    OPERATION, WINDOW_SHOW) : WINDOW_SHOW;
        switch (operation) {
            case WINDOW_SHOW:        //如果是显示
                //发送显示消息
                mHandler.sendEmptyMessage(WINDOW_SHOW);
                break;
            case WINDOW_HIDE:
                //发送隐藏消息
                mHandler.sendEmptyMessage(WINDOW_HIDE);
                break;
        }
    }

    /**
     * Handler消息机制
     */
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case WINDOW_SHOW:
                    if (!isAdd) {     //如果悬浮窗没有显示的状态下
                        //添加悬浮窗布局与布局参数
                        windowManager.addView(float_show, layoutParams);
                        //添加显示动画
                        float_show.startAnimation(show_anim);
                        isAdd = true;         //悬浮窗显示状态

                    } else {
                        float_show.startAnimation(show_anim);
                    }
                    break;
                case WINDOW_HIDE:
                    if (isAdd) {
                        //在主窗口中删除显示的悬浮窗口
                        windowManager.removeView(float_show);
                        isAdd = false;        //设置悬浮窗口没有显示
                    }
                    break;
            }
        }
    };

    // // 设置悬浮窗的宽高
    public void updateDefaultLeftRight() {
        //右侧显示尺寸
        default_right = (screen_width - layoutParams.width) / 2;
        //左侧显示尺寸
        default_left = -1 * default_right;
    }

    /**
     * 创建悬浮窗
     */
    private void createFloatView() {
        //加载悬浮窗布局文件
        float_show = (LinearLayout) LayoutInflater
                .from(getApplicationContext()).inflate(R.layout.window_image,
                        null);
        //获取显示的图片分别是左、移动中、右
        leftView = (ImageView) float_show.findViewById(R.id.left_view);
        moveView = (ImageView) float_show.findViewById(R.id.move_view);
        rightView = (ImageView) float_show.findViewById(R.id.right_view);

        //窗口布局参数
        layoutParams = new WindowManager.LayoutParams();
        //获取应用程序包的名称
        String packname = getPackageName();
        //获取程序包管理
        PackageManager pm = getPackageManager();
        //判断应用包，是否授予了悬浮窗口的权限
        boolean permission = (PackageManager.PERMISSION_GRANTED ==
                pm.checkPermission("android.permission.SYSTEM_ALERT_WINDOW", packname));
        //根据权限设置窗口类型
        if(permission){
            layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        }else{
            layoutParams.type = WindowManager.LayoutParams.TYPE_TOAST;
        }
        layoutParams.format = PixelFormat.RGBA_8888; // 设置图片格式，效果为背景透明

        // 设置窗口的标志
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;

        updateDefaultLeftRight();                //调用设置悬浮窗的尺寸
        layoutParams.x = default_left;               //悬浮窗布局默认位置左侧
        showLeftView();                         //显示左侧悬浮窗口
        // 设置悬浮窗的Touch监听
        float_show.setOnTouchListener(new View.OnTouchListener() {
            int lastX, lastY;       //按下时的坐标
            int paramX, paramY;     //记录屏幕中心的位置
            int dx, dy;              //移动时的坐标

            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //获取按下悬浮窗时的坐标
                        lastX = (int) event.getRawX();
                        lastY = (int) event.getRawY();
                        //保存悬浮窗在屏幕中心的哪个位置
                        paramX = layoutParams.x;
                        paramY = layoutParams.y;
                        isMove = false;       //关闭移动状态
                        break;
                    case MotionEvent.ACTION_MOVE:
                        //获取移动中的坐标
                        dx = (int) event.getRawX() - lastX;
                        dy = (int) event.getRawY() - lastY;
                        //设置移动中的悬浮图标在屏幕中心的哪个位置
                        layoutParams.x = paramX + dx;
                        layoutParams.y = paramY + dy;

                        showMoveView();             //显示移动中的悬浮窗
                        isMove = true;            //设置悬浮窗移动状态
                        //更新悬浮窗位置
                        windowManager.updateViewLayout(float_show, layoutParams);
                        break;
                    case MotionEvent.ACTION_UP:
                        if (isMove) {     //如果悬浮窗处于拖动状态
                            // 如果发生了移动操作,才进行松开的处理,避免和单击事件冲突
                            int dx1 = (int) event.getRawX();   //获取手指抬起时坐标
                            if (dx1 > (screen_width/2)){      //横向坐标大于屏幕中间时
                                showRightView();                //显示桌面右边图标
                                updateDefaultLeftRight();       //设置悬浮窗宽高
                                layoutParams.x = default_right;      //显示在右侧
                            } else {                            //小于屏幕中间时
                                showLeftView();                 //显示左侧悬浮窗口
                                updateDefaultLeftRight();       //设置悬浮窗宽高
                                layoutParams.x = default_left;       //显示在左侧
                            }
                            layoutParams.y = paramY + dy;
                            //更新悬浮窗位置
                            windowManager.updateViewLayout(float_show, layoutParams);

                        }

                        break;
                }
                return false;
            }
        });
        //添加悬浮窗布局
        windowManager.addView(float_show, layoutParams);
        isAdd = true;                             //设置悬浮窗显示状态
    }


    // 显示桌面左边的图标
    private void showLeftView() {
        //设置悬浮窗的宽高
        layoutParams.width = 84;
        layoutParams.height = 134;
        leftView.setVisibility(View.VISIBLE);   //显示左侧悬浮窗
        rightView.setVisibility(View.GONE);     //隐藏右侧悬浮窗
        moveView.setVisibility(View.GONE);    //隐藏移动中悬浮窗
    }

    // 显示桌面右边的图标
    private void showRightView() {
        layoutParams.width = 84;
        layoutParams.height = 134;
        leftView.setVisibility(View.GONE);
        rightView.setVisibility(View.VISIBLE);
        moveView.setVisibility(View.GONE);
    }

    // 显示移动时的图标
    public void showMoveView() {
        layoutParams.width = 103;
        layoutParams.height = 145;
        leftView.setVisibility(View.GONE);
        rightView.setVisibility(View.GONE);
        moveView.setVisibility(View.VISIBLE);
    }

}

