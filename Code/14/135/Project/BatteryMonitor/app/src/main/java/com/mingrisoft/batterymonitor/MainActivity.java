package com.mingrisoft.batterymonitor;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.BatteryManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity {
    TextView battery_level;             //显示电池电量的百分比
    TextView battery_status;            //显示电池当前状态
    TextView battery_temperature;      //显示电池温度
    TextView battery_voltage;          //显示电池电压
    TextView charging_mode;            //显示电池充电模式
    TextView use_state;                 //显示电池使用状态
    private BatteryReceiver receiver = null;    //电池接收器
    private int percent;                //电池电量百分比
    ImageView img_electricity;          //电池电量图
    Handler handler = new Handler();    //执行充电进度图的handler
    int mark = 0;                      //定义电池充电进度图片的下标
    boolean isru = true;              //充电标记
    //设置电池充电进度图片的数组
    int [] img = {R.drawable.img0,R.drawable.img10,R.drawable.img20,
            R.drawable.img30,R.drawable.img50,R.drawable.img70,
            R.drawable.img80,R.drawable.img90,R.drawable.img100};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //全屏
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,
                WindowManager.LayoutParams. FLAG_FULLSCREEN);

        //获取显示电池电量的百分比控件
        battery_level = (TextView) findViewById(R.id.tv_battery_level);
        //获取显示电池当前状态控件
        battery_status = (TextView) findViewById(R.id.tv_battery_status);
        //获取显示电池温度控件
        battery_temperature = (TextView) findViewById(R.id.tv_battery_temperature);
        //获取显示电池电压控件
        battery_voltage = (TextView) findViewById(R.id.tv_battery_voltage);
        //获取显示电池充电模式控件
        charging_mode = (TextView) findViewById(R.id.tv_charging_mode);
        //获取显示电池使用状态控件
        use_state = (TextView) findViewById(R.id.tv_use_state);
        //获取显示电池电量图控件
        img_electricity = (ImageView) findViewById(R.id.electricity_img);
        receiver = new BatteryReceiver();       //实例化电池接收器
        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(receiver, filter);// 注册BroadcastReceiver
    }
    /**
     * 电池接收器内部类，用于检测电池状态
     */
    class BatteryReceiver extends BroadcastReceiver {
        private String BatteryTemp;
        private String BatteryStatus;
        private String BatteryStatus2;
        private boolean flag;             //充电状态标记

        public void onReceive(Context context, Intent intent) {

            int current = intent.getExtras().getInt("level");// 获得当前电量

            int total = intent.getExtras().getInt("scale");// 获得总电量
            battery_voltage.setText("当前电压为：" + intent.getIntExtra("voltage", 0)
                    + "mV");
            //获取电池温度
            float temperature = intent.getExtras().getInt("temperature") * 0.1f;
            //显示电池温度
            battery_temperature.setText(intent.getExtras().getInt("temperature")
                    * 0.1f + "度");
            percent = current * 100 / total;         //电量百分比
            battery_level.setText(percent + "%");  //显示电量
            dlectricity();                           //调用根据电量显示对应的电量图

            /**
             * 判断电池当前状态
             */
            switch (intent.getIntExtra("status",
                    BatteryManager.BATTERY_STATUS_UNKNOWN)) {

                case BatteryManager.BATTERY_STATUS_CHARGING:
                    isru = true;

                    battery_status.setText("充电状态");
                    battery_status.setTextColor(Color.GREEN);

                    break;

                case BatteryManager.BATTERY_STATUS_DISCHARGING:
                    isru = false;

                    BatteryStatus = "放电状态";
                    battery_status.setText("放电状态");
                    battery_status.setTextColor(Color.YELLOW);
                    break;

                case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
                    isru = false;
                    BatteryStatus = "未充电";
                    battery_status.setText("未充电");
                    battery_status.setTextColor(Color.GREEN);
                    break;

                case BatteryManager.BATTERY_STATUS_FULL:
                    isru = false;
                    BatteryStatus = "充满电";
                    battery_status.setText("充满电");
                    battery_status.setTextColor(Color.GREEN);
                    break;

                case BatteryManager.BATTERY_STATUS_UNKNOWN:
                    isru = false;
                    BatteryStatus = "未知道状态";
                    battery_status.setText("未知道状态");
                    battery_status.setTextColor(Color.BLUE);
                    break;

            }
            /**
             * 判断充电方式
             */
            switch (intent.getIntExtra("plugged",
                    BatteryManager.BATTERY_PLUGGED_AC)) {

                case BatteryManager.BATTERY_PLUGGED_AC:

                    BatteryStatus2 = "AC充电";
                    charging_mode.setText("AC充电");
                    break;

                case BatteryManager.BATTERY_PLUGGED_USB:

                    BatteryStatus2 = "USB充电";
                    charging_mode.setText("USB充电");
                    break;

            }
            /**
             * 判断电池使用状态
             */
            switch (intent.getIntExtra("health",
                    BatteryManager.BATTERY_HEALTH_UNKNOWN)) {

                case BatteryManager.BATTERY_HEALTH_UNKNOWN:

                    BatteryTemp = "未知错误";
                    use_state.setText("未知错误");
                    break;

                case BatteryManager.BATTERY_HEALTH_GOOD:

                    BatteryTemp = "状态良好";
                    use_state.setText("状态良好");
                    break;

                case BatteryManager.BATTERY_HEALTH_DEAD:

                    BatteryTemp = "电池没有电";
                    use_state.setText("电池没有电");
                    break;

                case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE:

                    BatteryTemp = "电池电压过高";
                    use_state.setText("电池电压过高");
                    break;

                case BatteryManager.BATTERY_HEALTH_OVERHEAT:
                    use_state.setText("电池过热");
                    BatteryTemp = "电池过热";

                    break;

            }
            //如果是充电状态就执行充电进度
            if(BatteryManager.BATTERY_STATUS_CHARGING==2){
                if(!flag){
                    handler.postDelayed(runnable, 500);
                    flag = true;
                }
            }else{ //否则显示根据当前电量显示对应的电量图片
                isru = false;
                flag = false;
                handler.removeCallbacks(runnable);
                dlectricity();              //调用根据电量显示对应的电量图
            }



        }
        //根据电量显示对应的电量图
        public void dlectricity(){
            if (percent > 90) {
                img_electricity.setImageResource(R.drawable.img100);
            } else if (percent > 80 && percent <= 90) {
                img_electricity.setImageResource(R.drawable.img90);
            } else if (percent > 70 && percent <= 80) {
                img_electricity.setImageResource(R.drawable.img80);
            } else if (percent > 60 && percent <= 70) {
                img_electricity.setImageResource(R.drawable.img70);
            } else if (percent > 50 && percent <= 60) {
                img_electricity.setImageResource(R.drawable.img50);
            } else if (percent > 40 && percent <= 50) {
                img_electricity.setImageResource(R.drawable.img30);
            } else if (percent > 30 && percent <= 40) {
                img_electricity.setImageResource(R.drawable.img20);
            } else if (percent > 20 && percent <= 30) {
                img_electricity.setImageResource(R.drawable.img10);
            } else if (percent > 0 && percent <= 20) {
                img_electricity.setImageResource(R.drawable.img0);
            }
        };
        /**
         * 每500毫秒更换一次图片实现充电进度
         */
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                if(isru==true){
                    if (mark <img.length ){
                        img_electricity.setImageResource(img[mark]);
                        mark++;
                    }else{
                        mark=0;
                    }

                }
                handler.postDelayed(runnable, 500);
            }
        };
    }
}
