package com.mingrisoft.vedioplayer;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.VideoView;

public class MainActivity extends AppCompatActivity {

    private VideoView video;//播放控件
    private MediaController controller;//控制器
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //试着全屏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        //初始化控件
        video = (VideoView) findViewById(R.id.video);
        //实例化控制器
        controller = new MediaController(this);
        //设置播放文件路径
        String uri = "android.resource://" + getPackageName() + "/" + R.raw.test;
        //设置播放源
        video.setVideoURI(Uri.parse(uri));
        //设置控制器
        video.setMediaController(controller);
        //给控制器设置播放器控件
        controller.setMediaPlayer(video);
        video.requestFocus();//获取焦点
        //设置视频加载回调
        video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                //播放视频
                video.start();
            }
        });
    }
}
