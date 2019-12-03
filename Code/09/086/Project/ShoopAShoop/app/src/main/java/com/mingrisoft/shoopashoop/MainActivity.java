package com.mingrisoft.shoopashoop;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;

import java.util.ArrayList;

import view.WaveControlLayout;

public class MainActivity extends Activity {
    private ImageView found;                  //找到的图片
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //获取自定义波浪控件
        final WaveControlLayout waveControlLayout =
                (WaveControlLayout)findViewById(R.id.activity_main);
        final Handler handler=new Handler();                          //创建handler消息机制
        found=(ImageView)findViewById(R.id.found);                    //获取显示找到的图片控件
        ImageView button=(ImageView)findViewById(R.id.centerImage);  //获取触发的按钮
        button.setOnClickListener(new View.OnClickListener() {        //设置按钮单击事件
            @Override
            public void onClick(View view) {
                    waveControlLayout.startWaveAnimation();                //启动波浪动画
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            found();            //调用显示找到图片的动画方法
                        }
                    },3000);                    //设置3秒后执行
            }
        });
    }
    /**
     * 找到图片的动画方法
     */
    private void found(){
        AnimatorSet animatorSet = new AnimatorSet();        //顺序播放动画对象
        animatorSet.setDuration(500);                       //设置动画持续时间
        //设置动画加速减速插补器
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        ArrayList<Animator> animatorList=new ArrayList<Animator>();        //创建动画数组
        //设置横向的从隐藏到显示,1.5f是动画拉伸的跨度
        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(found, "ScaleX", 0f, 1.5f, 1f);
        animatorList.add(scaleXAnimator);                           //添加至动画数组
        //设置纵向的从隐藏到显示
        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(found, "ScaleY", 0f, 1.5f, 1f);
        animatorList.add(scaleYAnimator);               //添加至动画数组
        animatorSet.playTogether(animatorList);         //设置播放的所有动画
        found.setVisibility(View.VISIBLE);             //设置找到图片空间显示
        animatorSet.start();                            //启动动画
    }
}
