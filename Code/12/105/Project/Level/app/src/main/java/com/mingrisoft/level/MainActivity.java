package com.mingrisoft.level;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import view.MyView;

public class MainActivity extends Activity implements SensorEventListener {
    // 定义自定义控件圆盘图片
    private MyView myView;
    //定义最大倾斜角度
    int MAXIMUM_ANGLE = 90;
    // 定义传感器管理器
    SensorManager mSensorManager;
    // 定义显示上下左右抬起的角度
    private TextView lx, rx, ty, by;
    // 定义小球位于中间时X与Y的座标
    int ax, ay;
    // 定义水平仪大圆盘中心座标X、Y
    int backCx;
    int backCy;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myView = (MyView) findViewById(R.id.myview);    //获取布局文件自定义控件
        WindowManager wm = (WindowManager) this         //获取窗口管理
                .getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();  //获取屏幕宽度
        int height = wm.getDefaultDisplay().getHeight();//获取屏幕高度
        //获取显示上下左右角度的文本框控件
        lx = (TextView) findViewById(R.id.text_lx);
        rx = (TextView) findViewById(R.id.text_rx);
        ty = (TextView) findViewById(R.id.text_ty);
        by = (TextView) findViewById(R.id.text_by);
        // 获取手机传感器管理服务
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        //计算出水平仪完全水平时小球位置，圆盘图左上角为原点
        ax = (myView.compass.getWidth() - myView.ball.getWidth()) / 2;
        ay = (height - myView.ball.getHeight()) / 2;
        // 计算出水平仪圆盘中心X、Y坐标，图片左上角为原点
        backCx = myView.compass.getWidth() / 2;
        backCy = (height - myView.ball.getHeight()) / 2;
    }

    /**
     * 获取焦点时，注册监听方向传感器
     */
    @Override
    public void onResume() {
        super.onResume();
        // 注册/监听方向传感器
        mSensorManager.registerListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_GAME);
    }

    /**
     * 界面停止时，取消方向传感器的监听
     */
    @Override
    protected void onStop() {
        // 取消方向传感器的监听
        mSensorManager.unregisterListener(this);
        super.onStop();
    }


    @Override
    public void onAccuracyChanged(Sensor arg0, int arg1) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        //获取传感器三轴数据
        float[] values = event.values;
        // 获取传感器的类型
        int sensorType = event.sensor.getType();
        //定义小球当前位置X Y坐标值
        int x, y;
        x = ax;
        y = ay;
        switch (sensorType) {
            case Sensor.TYPE_ORIENTATION:
                // Z轴转过的角度
                int zAngle = (int) values[0];   //后期使用获取方向
                int yAngle = (int) values[1];
                if (yAngle >= 0) {
                    by.setText("" + yAngle);  //显示上方负角度值
                }
                if (yAngle <= 0) {
                    ty.setText("" + yAngle);  //显示下方正角度值
                }

                // Y轴的滚动时与X轴的角度
                int xAngle = (int) values[2];
                if (xAngle >= 0) {
                    rx.setText("" + xAngle);  //显示右侧正角度值
                }
                if (xAngle <= 0) {
                    lx.setText("" + xAngle);  //显示左侧负角度值
                }
                if (yAngle==0 && +xAngle==0){         //如果处于水平状态时
                    myView.accuracy=true;            //设置水平状态
                }else {
                    myView.accuracy=false;           //设置倾斜状态
                }

                // 如果沿x轴的倾斜角　　在最大角度之内则计算出其相应坐标值
                if (Math.abs(xAngle) <= MAXIMUM_ANGLE) {
                    // 根据与x轴的倾斜角度计算X座标的变化值（倾斜角度越大，X座标变化越大）
                    int deltaX = (int) (ax * xAngle / MAXIMUM_ANGLE);
                    x += deltaX;
                }
                // 如果沿x轴的倾斜角已经大于最大角度，小球在最左边
                else if (xAngle > MAXIMUM_ANGLE) {
                    x = 0;
                }
                // 如果与x轴的倾斜角已经小于负的最大角度，小球在最右边
                else {
                    x = ax * 2;
                }
                // 如果沿Y轴的倾斜角还在最大角度之内
                if (Math.abs(yAngle) <= MAXIMUM_ANGLE) {
                    // 根据沿Y轴的倾斜角度计算Y座标的变化值（倾斜角度越大，Y座标变化越大）
                    int deltaY = (int) (ay * yAngle / MAXIMUM_ANGLE);
                    y += deltaY;
                }
                // 如果沿Y轴的倾斜角已经大于，小球在最下边
                else if (yAngle > MAXIMUM_ANGLE) {
                    y = ay * 2;
                }
                // 如果沿Y轴的倾斜角已经小于负的最大角度，小球在最右边
                else {
                    y = 0;
                }
                // 如果计算出来的X、Y座标还位于水平仪的仪表盘内，更新水平仪的小球座标
                if (isContain(x, y)) {
                    myView.ballX = x;
                    myView.ballY = y;
                }
                // 通知系统重回MyView组件重绘界面
                myView.postInvalidate();
                break;
        }


    }

    // 计算x、y点的小球是否处于水平仪的圆盘内
    private boolean isContain(int x, int y) {
        // 计算小球的圆心座标X、Y
        int ballCx = x + myView.ball.getWidth() / 2;
        int ballCy = y + myView.ball.getWidth() / 2;

        // 计算小球的圆心与水平仪圆盘中心之间的距离。
        double distance = Math.sqrt((ballCx - backCx) * (ballCx - backCx)
                + (ballCy - backCy) * (ballCy - backCy));
        // 若两个圆心的距离小于它们的半径差，即可认为处于该点的小球依然位于仪表盘内
        if (distance < ax) {
            return true;
        } else {
            return false;
        }
    }

}
