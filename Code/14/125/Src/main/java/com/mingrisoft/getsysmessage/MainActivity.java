package com.mingrisoft.getsysmessage;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    int width = 0;
    int height = 0;
    float density = 0;
    int densityDpi = 0;
    private BatteryReceiver receiver = null;

    private TextView msgText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        msgText = (TextView) findViewById(R.id.message);
        /**
         * 初始化获取手机电量的广播
         * */
        receiver = new BatteryReceiver();
        //获取手机电量的action
        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(receiver, filter);//注册BroadcastReceiver
    }

    private void init() {
        /**
         * 获取手机屏幕大小
         * */
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);  //获取当前屏幕
        width = metric.widthPixels;  // 屏幕宽度（像素）
        height = metric.heightPixels;  // 屏幕高度（像素）
        density = metric.density;  // 屏幕密度（0.75 / 1.0 / 1.5）
        densityDpi = metric.densityDpi;  // 屏幕密度DPI（120 / 160/ 240）
    }

    /**
     * 获取手机内存大小
     *
     * @return
     */
    private String getTotalMemory() {
        String str1 = "/proc/meminfo";// 系统内存信息文件
        String str2;
        String[] arrayOfString;
        long initial_memory = 0;
        try {
            FileReader localFileReader = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(localFileReader, 8192);
            str2 = localBufferedReader.readLine();// 读取meminfo第一行，系统总内存大小

            arrayOfString = str2.split("\\s+");
            for (String num : arrayOfString) {
                Log.i(str2, num + "\t");
            }

            initial_memory = Integer.valueOf(arrayOfString[1]).intValue() * 1024;// 获得系统总内存，单位是KB，乘以1024转换为Byte
            localBufferedReader.close();

        } catch (IOException e) {
        }
        return Formatter.formatFileSize(getBaseContext(), initial_memory);// Byte转换为KB或者MB，内存大小规格化
    }

    /**
     * 获取当前可用内存大小
     *
     * @return
     */
    private String getAvailMemory() {
        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);
        return Formatter.formatFileSize(getBaseContext(), mi.availMem);
    }


    private class BatteryReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            int current = intent.getExtras().getInt("level");//获得当前电量
            int total = intent.getExtras().getInt("scale");//获得总电量
            int percent = current * 100 / total;
            StringBuffer sb = new StringBuffer();
            sb.append("主板： " + Build.BOARD);
            sb.append("\n系统启动程序版本号： " + Build.BOOTLOADER);
            sb.append("\n系统定制商： " + Build.BRAND);
            sb.append("\ncpu指令集： " + Build.CPU_ABI);
            sb.append("\n设置参数： " + Build.DEVICE);
            sb.append("\n显示屏参数：" + Build.DISPLAY);
            sb.append("\n无线电固件版本：" + Build.getRadioVersion());
            sb.append("\n硬件识别码： " + Build.FINGERPRINT);
            sb.append("\n硬件名称： " + Build.HARDWARE);
            sb.append("\n硬件制造商： " + Build.MANUFACTURER);
            sb.append("\n版本： " + Build.MODEL);
            sb.append("\n硬件序列号： " + Build.SERIAL);
            sb.append("\n手机制造商： " + Build.PRODUCT);
            sb.append("\nUSER: " + Build.USER);
            sb.append("\n系统版本" + Build.VERSION.RELEASE);
            sb.append("\n手机当前电量：" + percent + "%");
            sb.append("\n手机屏幕高度: " + height);
            sb.append("\n手机屏幕宽度:" + width);
            sb.append("\n手机屏幕密度：" + density);
            sb.append("\n手机屏幕密度DPI：" + densityDpi);
            sb.append("\n手机内存大小：" + getTotalMemory());
            sb.append("\n当前可用内存：" + getAvailMemory());
            msgText.setText(sb);
        }
    }
}
