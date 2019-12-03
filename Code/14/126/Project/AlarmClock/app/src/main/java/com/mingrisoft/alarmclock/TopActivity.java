package com.mingrisoft.alarmclock;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by Administrator on 2017/1/3.
 */

public class TopActivity extends AppCompatActivity {

    private PowerManager.WakeLock mWakelock;
    private MediaPlayer mediaPlayer1 = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**
         * 这句话是把此页面显示到手机屏幕的最上层
         * */
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);// init powerManager
        mWakelock = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP |
                PowerManager.SCREEN_DIM_WAKE_LOCK, "target");
        setContentView(R.layout.top_layout);    //绑定布局文件
        mediaPlayer1 = MediaPlayer.create(this, R.raw.congcongnanian);  //初始化mediaPlayer
        mWakelock.acquire();      //开启闹钟闹钟
        mediaPlayer1.seekTo(0);    //音乐从头开始
        mediaPlayer1.start();      //播放音乐
        findViewById(R.id.i_know).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                mediaPlayer1.pause();
            }
        });
    }
}
