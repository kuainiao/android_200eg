package com.mingrisoft.distancesensor;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Build;
import android.os.PowerManager;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends Activity implements SensorEventListener {
    // 定义传感器管理
    SensorManager sensorManager;
    private PowerManager powerManager = null;// 电源管理对象
    private PowerManager.WakeLock localWakeLock = null;// 电源锁
    // 定义一个SoundPool
    SoundPool soundPool;
    int soundId;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 获取手机传感器管理服务
        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        //获取电源管理服务
        powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        // 第一个参数为电源锁级别,第二个参数为Log日志
        localWakeLock = this.powerManager.newWakeLock(32,"锁");
        AudioAttributes attr = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME) // 设置音效使用场景
                // 设置音效的类型
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build();
        soundPool = new SoundPool.Builder()
                .setAudioAttributes(attr) // 设置音效池的属性
                .setMaxStreams(10) // 设置最多可容纳10个音频流，
                .build();
        //加载音乐资源
        soundId = soundPool.load(this, R.raw.music, 1);
    }

    /**
     * 界面获取焦点时，监听距离传感器变化
     */
    @Override
    public void onResume()
    {
        super.onResume();
        // 注册/监听距离传感器
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY),
                SensorManager.SENSOR_DELAY_UI);
    }

    /**
     * 界面停止时，注销监听器
     */
    @Override
    protected void onStop() {
        super.onStop();
        sensorManager.unregisterListener(this);
        localWakeLock.release();    // 释放设备电源锁
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float[] values = event.values;  //获取距离传感器的值
        int sensorType = event.sensor.getType();    //获取传感器类型
        switch (sensorType) {
            case Sensor.TYPE_PROXIMITY:
                if (values[0] == 0.0) {             // 如果值为0，为贴近手机
                    if (localWakeLock.isHeld()) {   //如果电源锁已被唤醒
                    } else {                        //如果没有唤醒
                        localWakeLock.acquire();    // 申请设备电源锁
                    }
                    soundPool.play(soundId, 1, 1, 0, 0, 1);   //播放音效
                } else {                            // 远离手机
                        localWakeLock.setReferenceCounted(false);
                        localWakeLock.release();    // 释放设备电源锁
                }
                break;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
