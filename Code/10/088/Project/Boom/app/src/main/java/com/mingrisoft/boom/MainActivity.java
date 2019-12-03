package com.mingrisoft.boom;

        import android.annotation.TargetApi;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
        import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {
    private SoundPool soundPool;//声明对象
    private int soundId;
    private RelativeLayout relativeLayout;
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //实例化布局
        relativeLayout = (RelativeLayout) findViewById(R.id.activity_main);
        AudioAttributes attr = new AudioAttributes.Builder()
        .setUsage(AudioAttributes.USAGE_GAME) // 设置音效使用场景
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)// 设置音效的类型
                .build();
        soundPool = new SoundPool.Builder()
                .setAudioAttributes(attr) // 设置音效池的属性
                .setMaxStreams(10) // 设置最多可容纳10个音频流，
                .build();//创建soundPool对象
        soundId = soundPool.load(this, R.raw.shot, 1);//加载音频
    }

    /**
     * 每一次的触摸屏幕都会开启一段音效
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN://触摸屏幕
                //更改图片
                relativeLayout.setBackgroundResource(R.mipmap.background1);
                break;
            case MotionEvent.ACTION_UP://触摸抬起
                soundPool.play(soundId, 1, 1, 0, 0, 1);//播放音频
                //更改图片
                relativeLayout.setBackgroundResource(R.mipmap.background2);
                return true;
        }
        return super.onTouchEvent(event);
    }
}
