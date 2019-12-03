package com.mingrisoft.enterview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.Normalizer;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String VIDEO_NAME = "welcome_videoi.mp4";   //加载的登录视频

    private VideoView mVideoView;     //用于加载视频的

    private InputType inputType = InputType.NONE;   //枚举

    private Button buttonLeft, buttonRight;   //定义下方两个按钮

    private TextView appName;   //定义App的名字

    private LinearLayout editAll;   //输入框

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**
         * 使播放视频充满屏幕，包括状态栏下方也充满视频
         * */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        findView();   //绑定控件的id

        initView();   //进行控件的初始化

        File videoFile = getFileStreamPath(VIDEO_NAME);  //读取视频文件
        if (!videoFile.exists()) {
            videoFile = copyVideoFile();
        }

        playVideo(videoFile);   //播放视频

        playAnim();   //动画
    }

    /**
     * 绑定控件的id方法
     * */
    private void findView() {
        mVideoView = (VideoView) findViewById(R.id.videoView);
        buttonLeft = (Button) findViewById(R.id.buttonLeft);
        buttonRight = (Button) findViewById(R.id.buttonRight);
        appName = (TextView) findViewById(R.id.appName);
        editAll = (LinearLayout) findViewById(R.id.edit_all);
    }

/**
 * 按钮设置监听
 * */
    private void initView() {
        buttonRight.setOnClickListener(this);
        buttonLeft.setOnClickListener(this);
    }

    /**
     * 播放音乐
     * */
    private void playVideo(File videoFile) {
        mVideoView.setVideoPath(videoFile.getPath());  //获取视频的路径
        mVideoView.setLayoutParams(new RelativeLayout.LayoutParams(-1, -1));  //给视频设置布局
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.setLooping(true);  //
                mediaPlayer.start();   //开始播放
            }
        });
    }


    /**
     * 动画
     * */
    private void playAnim() {
        ObjectAnimator anim = ObjectAnimator.ofFloat(appName, "alpha", 0,1);   //设置透明度
        anim.setDuration(4000);   //动画时间
        anim.setRepeatCount(1);  //设置动画重复（如果大于0，永不重复）
        anim.setRepeatMode(ObjectAnimator.REVERSE);
        anim.start();     //开始动画
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                appName.setVisibility(View.INVISIBLE);  //动画结束后小时“UserEnter”
                editAll.setVisibility(View.VISIBLE);    //显示待输入的框
            }
        });
    }

    @NonNull
    private File copyVideoFile() {
        File videoFile;  //定义文件
        try {
            FileOutputStream fos = openFileOutput(VIDEO_NAME, MODE_PRIVATE);        //输出字节文件流
            InputStream in = getResources().openRawResource(R.raw.welcome_videoi);   //输入字节文件流
            byte[] buff = new byte[1024];       //装换成byte字节
            int len = 0;
            while ((len = in.read(buff)) != -1) {  //开始转换
                fos.write(buff, 0, len);
            }
        } catch (FileNotFoundException e) {   //跑出没有找到文件异常
            e.printStackTrace();
        } catch (IOException e) {              //跑出io流异常
            e.printStackTrace();
        }
        videoFile = getFileStreamPath(VIDEO_NAME);   //获取视频文件的路径
        if (!videoFile.exists())
            throw new RuntimeException("视频文件有问题，你确定你有welcome_video.mp4 RES /原文件夹吗？");
        return videoFile;            //返回视频文件
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mVideoView.stopPlayback();   //停止视频播放
    }

    @Override
    public void onClick(View view) {
        switch (inputType) {
            case NONE:
                if (view == buttonLeft) {
                    inputType = InputType.LOGIN;
                    buttonLeft.setText(R.string.button_confirm_login);
                    buttonRight.setText(R.string.button_cancel_login);
                } else if (view == buttonRight) {
                    inputType = InputType.SIGN_UP;
                    buttonLeft.setText(R.string.button_confirm_signup);
                    buttonRight.setText(R.string.button_cancel_signup);
                }

                break;
            case LOGIN:
                if (view == buttonLeft) {
                } else if (view == buttonRight) {
                }
                inputType = InputType.NONE;
                buttonLeft.setText(R.string.button_login);
                buttonRight.setText(R.string.button_signup);
                break;
            case SIGN_UP:
                if (view == buttonLeft) {

                } else if (view == buttonRight) {

                }
                inputType = InputType.NONE;
                buttonLeft.setText(R.string.button_login);
                buttonRight.setText(R.string.button_signup);
                break;
        }
    }

    enum InputType {   //枚举
        NONE, LOGIN, SIGN_UP;
    }
}
