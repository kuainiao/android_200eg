package com.mingrisoft.lookme;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {

    LeftEyeView leftEyeView;    //左眼睛
    RightEyeView rightEyeView;      //右眼睛
    RelativeLayout relativeLayout;      //背景
    int w, h;    //获取屏幕的宽高

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);//绑定布局
       init(); //初始化
    }

    private void init() {
        /**
         * 初始化左右眼的view
         * */
        leftEyeView = new LeftEyeView(this);
        rightEyeView = new RightEyeView(this);
        /**
         * 获取屏幕的宽高
         * */
        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        w = wm.getDefaultDisplay().getWidth();
        h = wm.getDefaultDisplay().getHeight();
        relativeLayout = (RelativeLayout) findViewById(R.id.activity_main);
        /**
         * 设置左眼的宽高
         * */
        leftEyeView.setMinimumHeight(200);
        leftEyeView.setMinimumWidth(200);
        /**
         * 设置右眼的宽高
         * */
        rightEyeView.setMinimumHeight(200);
        rightEyeView.setMinimumWidth(200);
        /**
         * 绘制出左右眼，并添加到布局中
         * */
        leftEyeView.invalidate();
        rightEyeView.invalidate();
        relativeLayout.addView(leftEyeView);
        relativeLayout.addView(rightEyeView);
        /**
         * 两眼最先出现的坐标
         * */
        rightEyeView.setXY(w / 2 + 100, h / 2);
        leftEyeView.setXY(w / 2 - 100, h / 2);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        float x = event.getX();
        float y = event.getY();
        if (MotionEvent.ACTION_DOWN == event.getAction()) {
            leftEyeView.setZQ(w / 2 - 100, h / 2 - 80);
            rightEyeView.setZQ(w / 2 + 100, h / 2 - 80);


        } else if (MotionEvent.ACTION_MOVE == event.getAction()) {
            int x1 = w / 2, y1 = h / 2; //点1坐标;
            int x2 = (int) event.getX(), y2 = (int) event.getY(); //点2坐标
            int m = Math.abs(x1 - x2);
            int n = Math.abs(y1 - y2);
            double z = Math.sqrt(m * m + n * n);
            int jiaodu = Math.round((float) (Math.asin(n / z) / Math.PI * 180));//最终角度
            Log.e("-----", "jiaodu:" + jiaodu);
            if (x > (w / 2) && y < (h / 2)) { //手指在第一象限
                leftEyeView.setS((float) (90 - jiaodu));
                rightEyeView.setS((float) (90 - jiaodu));


            } else if (x < (w / 2) && y < (h / 2)) { //手指在第二象限
                leftEyeView.setS((float) (270 + jiaodu));
                rightEyeView.setS((float) (270 + jiaodu));

            } else if (x < (w / 2) && y > (h / 2)) { //手指在第三象限
                leftEyeView.setS((float) (270 - jiaodu));
                rightEyeView.setS((float) (270 - jiaodu));

            }
            else if (x > (w / 2) && y > (h / 2)) { //手指在第四象限
                leftEyeView.setS((float) (90 + jiaodu));
                rightEyeView.setS((float) (90 + jiaodu));

            }

        } else if (MotionEvent.ACTION_UP == event.getAction()) {
            leftEyeView.setZQ(w / 2 - 100, h / 2);
            rightEyeView.setZQ(w / 2 + 100, h / 2);
        }
        return super.onTouchEvent(event);
    }

}
