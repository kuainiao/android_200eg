package com.mingrisoft.splash;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import java.util.Random;

/**
 * Created by Administrator on 2016/11/28.
 */

public class SlapActivity extends AppCompatActivity {

    private ImageView imageView;
    private Animation animation;
    private int resids[];          //用于加载图片

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       //设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.slap_activity_layout);
        animation = AnimationUtils.loadAnimation(this, R.anim.enlarge);
        imageView = (ImageView) findViewById(R.id.slap_image);
        TypedArray ar =getResources().obtainTypedArray(R.array.imgArray); //获取图片数组
        int len = ar.length();  //获取数组的长度
        resids = new int[len];   //初始化加载图片的数组
        for (int i = 0; i < len; i++)
            resids[i] = ar.getResourceId(i, 0);   //for循环吧图片读取出来
        Random rand = new Random();          //初始化随机数
        int pos = rand.nextInt(resids.length);   //随机获取出小于数组长度的数
        imageView.setImageResource(resids[pos]);   //把图片显示出来

        /**
         * 开启线程
         * */
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    imageView.startAnimation(animation);  //给图片设置放大的动画
                    Thread.sleep(5000);       //此界面沉睡5秒
                } catch (InterruptedException e) {
                    e.printStackTrace();

                }
                toSecond();   //执行跳转下一个界面的方法
            }
        }).start();
    }

    /**
     * 跳转到下一个界面
     * */
    public void toSecond() {
        Intent intent = new Intent(this, MainActivity.class);  //跳转到主界面
        startActivity(intent);  //开始跳转
        finish();   //finish当前界面
    }
}
