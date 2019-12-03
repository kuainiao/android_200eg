package com.mingrisoft.flybird;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private int screenWidth;//获取屏幕宽度
    private ImageView bird;//大雁
    private AnimationDrawable birdAnimation;//帧动画
    private AnimatorSet birdAnimatorset;//属性动画
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindowWidth();//获取屏幕宽度
        bird = (ImageView) findViewById(R.id.bird);
        bird.setTranslationX(-screenWidth);//设置大雁摆放位置向左平移一个屏幕的宽
        birdAnimation = (AnimationDrawable) bird.getDrawable();//获取帧动画
        //设置bird的动画
        birdAnimatorset = new AnimatorSet();
        ObjectAnimator birdAnimatorR = ObjectAnimator
                            .ofFloat(bird,"translationX",screenWidth);
        birdAnimatorR.setDuration(30*1000);//设置动画时间
        birdAnimatorR.setInterpolator(new LinearInterpolator());//设置插值器
        birdAnimatorR.setRepeatCount(ValueAnimator.RESTART);//设置从头开始循环
        birdAnimatorR.setRepeatCount(ValueAnimator.INFINITE);//设置循环播放
        birdAnimatorset.play(birdAnimatorR);//向动画集合中添加动画
        birdAnimation.start();//开启帧动画
        birdAnimatorset.start();//开启属性动画
    }
    /**
     * 获取屏幕的宽度
     */
    private void getWindowWidth(){
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenWidth = dm.widthPixels;
    }
}
