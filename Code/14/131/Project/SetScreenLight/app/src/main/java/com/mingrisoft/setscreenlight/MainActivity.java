package com.mingrisoft.setscreenlight;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.SeekBar;

public class MainActivity extends AppCompatActivity {

    private BrightnessUtils utils;
    private SeekBar seekBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //全屏
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,
                WindowManager.LayoutParams. FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        setContentView(R.layout.activity_main);
        utils =new BrightnessUtils();   //初始化工具类
        seekBar = (SeekBar) findViewById(R.id.set_light);   //绑定id
        seekBar.setProgress(utils.getScreenBrightness(this)); //获取当前屏幕亮度设置进度条
        seekBar.setMax(225);  //设置进度条的最大长度为225
        /**
         * 对进度条进行监听
         * */
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {  //改变中的监听
                utils.setSystemBrightness(MainActivity.this,i);  //将当前的进度条的值传给系统
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {  //开始改变时监听
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {   //结束改变时监听
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
