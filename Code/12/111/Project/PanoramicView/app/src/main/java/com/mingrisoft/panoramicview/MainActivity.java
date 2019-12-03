package com.mingrisoft.panoramicview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import listener.GyroSensor;
import view.ImageScrollView;

public class MainActivity extends AppCompatActivity {
    //陀螺仪监听器
    private GyroSensor gyroSensor;
    //滚动视图控件
    private ImageScrollView horizontalScrollView,verticalScrollView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //陀螺仪监听对象
        gyroSensor = new GyroSensor();
        //获取滚动视图控件
        horizontalScrollView= (ImageScrollView) findViewById(R.id.panorama_image_view1);
        verticalScrollView= (ImageScrollView) findViewById(R.id.panorama_image_view2);
        //设置监听器
        horizontalScrollView.setGyroscopeObserver(gyroSensor);
        verticalScrollView.setGyroscopeObserver(gyroSensor);
    }

    /**
     * 获取焦点注册监听器
     */
    @Override
    protected void onResume() {
        super.onResume();
        gyroSensor.register(this);
    }

    /**
     * 界面停止时注销监听器
     */
    @Override
    protected void onPause() {
        super.onPause();
        gyroSensor.unregister();
    }
}
