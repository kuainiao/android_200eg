package com.mingrisoft.broken;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import tyrantgit.explosionfield.ExplosionField;

public class MainActivity extends AppCompatActivity {
    private ExplosionField mExplosionField;
    private ImageView image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        image = (ImageView) findViewById(R.id.picture);//初始化控件
        mExplosionField = ExplosionField.attach2Window(this);//获取粒子动画对象
        addListener();//增加监听事件
    }

    private void addListener(){
        //设置控件的点击事件
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mExplosionField.explode(v);//执行粒子动画
                v.setOnClickListener(null);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.reset,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.reset){
            image.setScaleX(1f);//设置X轴缩放值
            image.setScaleY(1f);//设置Y轴缩放值
            image.setAlpha(1f);//设置透明度
            image.setClickable(true);//设置可点击
            addListener();//增加监听
            mExplosionField.clear();//清出效果
        }
        return super.onOptionsItemSelected(item);
    }
}
