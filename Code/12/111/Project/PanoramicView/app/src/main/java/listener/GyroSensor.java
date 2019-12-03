package listener;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import java.util.LinkedList;

import view.ImageScrollView;

/**
 * Created by gjz on 21/12/2016.
 */

public class GyroSensor implements SensorEventListener {
    //传感器管理
    private SensorManager sensorManager;

    //计算纳秒时间
    private static final float nanosecond = 1.0f / 1000000000.0f;

    //上次传感器事件发生时纳秒的时间
    private long lastTimestamp;

    //弧度的装置已经沿Y轴旋转
    private double rotateRadianY;

    //弧度的装置已经沿X轴
    private double rotateRadianX;
    // 该装置应沿X轴和Y轴显示图像的边界最大弧度
    // 值必须在（0，π/ 2 ]
    private double maxRotateRadian = Math.PI / 9;

    //当设备旋转时要通知全景图像视图
    private LinkedList<ImageScrollView> mViews = new LinkedList<>();

    public void register(Context context) {
        //获取传感器服务
        if (sensorManager == null) {
            sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        }
        //获取陀螺仪传感器
        Sensor mSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        //设置传感器监听器
        sensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_FASTEST);
        //初始化传感器触发时间和旋转弧度
        lastTimestamp = 0;
        rotateRadianY = rotateRadianX = 0;
    }

    /**
     * 注销传感器监听器
     */
    public void unregister() {
        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
            sensorManager = null;
        }
    }

    public void addPanoramaImageView(ImageScrollView view) {
        if (view != null && !mViews.contains(view)) {
            mViews.addFirst(view);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (lastTimestamp == 0) {
            lastTimestamp = event.timestamp;
            return;
        }
        //获取三个轴的信息
        float rotateX = Math.abs(event.values[0]);
        float rotateY = Math.abs(event.values[1]);
        float rotateZ = Math.abs(event.values[2]);
        //进行横向全景实现
        if (rotateY > rotateX + rotateZ) {//如果Y轴值大于其它两个轴的值，证明手机处于横向滚动
            //计算事件经过的时间
            final float dT = (event.timestamp - lastTimestamp) * nanosecond;
            //计算沿Y轴旋转的弧度
            rotateRadianY += event.values[1] * dT;
            //设置左侧抬起向右最大的旋转弧度
            if (rotateRadianY > maxRotateRadian) {
                rotateRadianY = maxRotateRadian;
                //设置右侧抬起向左最大的旋转弧度
            } else if (rotateRadianY < -maxRotateRadian) {
                rotateRadianY = -maxRotateRadian;
            } else {
                //刷新图片显示的进度
                for (ImageScrollView view : mViews) {
                    if (view != null && view.getOrientation()
                                == ImageScrollView.HORIZONTAL_ORIENTATION) {
                        view.updateProgress((float) (rotateRadianY / maxRotateRadian));
                    }
                }
            }
            //进行纵向全景实现
        }
        else if (rotateX > rotateY + rotateZ) {
            final float dT = (event.timestamp - lastTimestamp) * nanosecond;
            rotateRadianX += event.values[0] * dT;
            if (rotateRadianX > maxRotateRadian) {
                rotateRadianX = maxRotateRadian;
            } else if (rotateRadianX < -maxRotateRadian) {
                rotateRadianX = -maxRotateRadian;
            } else {
                for (ImageScrollView view : mViews) {
                    if (view != null && view.getOrientation() == ImageScrollView.VERTICAL_ORIENTATION) {
                        view.updateProgress((float) (rotateRadianX / maxRotateRadian));
                    }
                }
            }
        }
        //刷新上一次触发的时间
        lastTimestamp = event.timestamp;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


}
