package com.mingrisoft.notdisturb;

import android.app.Service;
import android.content.Context;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private AudioManager aManger;  //音频管理器
    private Vibrator mVibrator;  //声明一个振动器对象
    private ImageView onIV,offIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        onIV = (ImageView) findViewById(R.id.on_image);
        offIV = (ImageView) findViewById(R.id.off_image);
        onIV.setOnClickListener(this);
        offIV.setOnClickListener(this);
        // 获取系统的音频服务
        aManger = (AudioManager) getSystemService(Service.AUDIO_SERVICE);
        // 设置震动大小可以通过改变pattern来设定，如果开启时间太短，震动效果可能感觉不到
        mVibrator = (Vibrator) getApplication().getSystemService(Service.VIBRATOR_SERVICE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.on_image:
                offIV.setVisibility(View.VISIBLE);
                onIV.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, "已关闭免打扰", Toast.LENGTH_SHORT).show();
                //声音模式
                aManger.setRingerMode(AudioManager.RINGER_MODE_NORMAL);  //开启手机铃声
                aManger.setVibrateSetting(AudioManager.VIBRATE_TYPE_NOTIFICATION, AudioManager.VIBRATE_SETTING_ON);  //开启震动
                aManger.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER, AudioManager.VIBRATE_SETTING_ON);         //开启震动

                break;
            case R.id.off_image:
                onIV.setVisibility(View.VISIBLE);
                offIV.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, "已开启免打扰", Toast.LENGTH_SHORT).show();
                //静音模式
                aManger.setRingerMode(AudioManager.RINGER_MODE_SILENT);  //设置静音
                aManger.setVibrateSetting(AudioManager.VIBRATE_TYPE_NOTIFICATION, AudioManager.VIBRATE_SETTING_OFF);  //关闭震动
                aManger.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER, AudioManager.VIBRATE_SETTING_OFF);      //关闭震动
                break;
        }
    }
}
