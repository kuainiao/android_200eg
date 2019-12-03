package com.mingrisoft.numberprogressbar;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.mingrisoft.numberprogressbar.numberprogressbar.NumberProgressBar;
import com.mingrisoft.numberprogressbar.numberprogressbar.OnProgressBarListener;

import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends ActionBarActivity implements OnProgressBarListener, View.OnClickListener {
    private Timer timer;   //计时器
    private ImageView imageView,finishImage,backImage,newImage;
    private NumberProgressBar bnp;  //进度条

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();  //初始化

    }

    private void init() {
        /**
         * 绑定id
         *
         * 设置监听
         * */
        imageView = (ImageView) findViewById(R.id.image_background);
        backImage = (ImageView) findViewById(R.id.back_image);
        newImage = (ImageView) findViewById(R.id.new_image);
        finishImage = (ImageView) findViewById(R.id.finish_image);
        finishImage.setOnClickListener(this);
        backImage.setOnClickListener(this);
        newImage.setOnClickListener(this);
        bnp = (NumberProgressBar)findViewById(R.id.numberbar1);
        bnp.setOnProgressBarListener(this);
        timer = new Timer();    //初始化计时器
       startTimer();  //开始计时的方法
    }

    /**
     * 开启计时器的方法
     * */
    private void startTimer() {
        /**
         * 计时器
         * 1000标示这个计时器最先打开后延迟1s执行
         * 100标示每隔0.1秒执行一次
         * */
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        bnp.incrementProgressBy(1);  //每次给进度条书+1
                    }
                });
            }
        }, 1000, 100);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();  //结束计时
    }

    /**
     * 进度条进度的监听
     * */
    @Override
    public void onProgressChange(int current, int max) {
        if(current == max) {  //当进度条加载完成后的操作
            timer.cancel();
            bnp.setProgress(0);
            bnp.setVisibility(View.INVISIBLE);
            imageView.setVisibility(View.VISIBLE);
            backImage.setVisibility(View.INVISIBLE);
            newImage.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_image:   //点击返回按钮
                timer.cancel();   //取消计时
                bnp.setProgress(0);
                bnp.setVisibility(View.INVISIBLE);
                imageView.setVisibility(View.INVISIBLE);
                backImage.setVisibility(View.INVISIBLE);
                newImage.setVisibility(View.VISIBLE);
                break;
            case R.id.new_image:    //点击刷新按钮
                backImage.setVisibility(View.VISIBLE);
                newImage.setVisibility(View.INVISIBLE);
                timer = new Timer();
                bnp.setProgress(0);
                bnp.setVisibility(View.VISIBLE);
                imageView.setVisibility(View.INVISIBLE);
                startTimer();
                break;
            case R.id.finish_image:   //点击结束按钮
                finish();
                break;
        }
    }
}
