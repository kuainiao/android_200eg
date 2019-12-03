package com.mingrisoft.videodialog;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends Activity {


    private Button btn_showVideoDialog;         //显示视频对话框的按钮
    private SurfaceView surfaceView;             //显示图像的控件
    private MediaPlayer mediaPlayer;             //播放器
    private SurfaceHolder surfaceHolder;         //用于控制surfaceView
    private VideoDialog videoDialog;              //自定义视频播放对话框
    private final int SDK_PERMISSION = 1;                //申请权限

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getPersimmions();                                   //获取SD卡读写权限
        //获取显示视频对话框的按钮
        btn_showVideoDialog = (Button) findViewById(R.id.showVideo);
        btn_showVideoDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoDialog = new VideoDialog(MainActivity.this,
                        R.style.MyDialog);//新建对话框
                videoDialog.setCanceledOnTouchOutside(true);
                //获取dialog中的控件
                View view = videoDialog.getDialogView();
                surfaceView = (SurfaceView) view.findViewById(R.id.surfaceView);
                playVideo();                 //调用播放视频方法
                videoDialog.show();         //显示对话框
            }
        });


    }
    /**
     * 添加SD卡读写动态权限
     */
    private void getPersimmions() {
        //判断系统版本是否大于6.0系统
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ArrayList<String> permissions = new ArrayList<String>();
            //判断是否开启内存卡读写权限
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                //添加权限
                permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);

                if (permissions.size() > 0) {
                    requestPermissions(permissions.toArray(new String[permissions.size()]), SDK_PERMISSION);
                }
            }
        }
    }


    private void playVideo() {
        // 创建播放器
        mediaPlayer = new MediaPlayer();
        //获取surfaceHolder
        surfaceHolder = surfaceView.getHolder();
        // 设置播放时打开屏幕
        surfaceHolder.setKeepScreenOn(true);
        //设置音频类型
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            // 设置需要播放的视频路径,本实例中将视频保存在手机sd卡的根目录下
            mediaPlayer.setDataSource(Environment.getExternalStorageDirectory()+"/video.mp4");
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            //准备
            mediaPlayer.prepare();
        } catch (IllegalStateException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        //等待surfaceHolder初始化完成才能执行mPlayer.setDisplay(surfaceHolder)
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                // 把视频画面输出到SurfaceView
                mediaPlayer.setDisplay(surfaceHolder);
                mediaPlayer.start();        //播放视频
            }
        });

        //视频播放完成后的操作
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (mediaPlayer != null)
                    mediaPlayer.release();//重置mediaplayer等待下一次播放
                if (videoDialog.isShowing())
                    videoDialog.dismiss(); //关闭对话框
            }
        });
    }


    @Override
    protected void onDestroy() {
        // 释放资源
        mediaPlayer.release();
        super.onDestroy();
    }
}
