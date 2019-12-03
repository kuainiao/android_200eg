package com.mingrisoft.printpicture;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView image = (ImageView) findViewById(R.id.picture);//实例化控件
        //加载动画
        Animation animation = AnimationUtils.loadAnimation(this,R.anim.print);
        image.setAnimation(animation);//设置动画
        animation.setRepeatCount(Animation.INFINITE);//设置动画循环播放
        animation.setRepeatMode(Animation.RESTART);//动画在开始出播放
        animation.start();//播放动画
    }
}
