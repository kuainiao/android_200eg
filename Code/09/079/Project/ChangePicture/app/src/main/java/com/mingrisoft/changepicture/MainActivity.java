package com.mingrisoft.changepicture;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Animation showAnimation,hideAnimation;//动画
    private ArrayList<ImageView> images;//图片控件
    private ImageButton lastBtn,nextBtn;//切换按钮
    private int showIndex;//当前显示的图片
    private int lastShowIndex;//之前显示的图片
    private boolean isInit;//是否是初始化界面
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        isInit = true;//设置是初始化界面
        //获取动画
        showAnimation = AnimationUtils.loadAnimation(this,R.anim.show_in);
        hideAnimation = AnimationUtils.loadAnimation(this,R.anim.hide_out);
        //图片控件集合
        images = new ArrayList<>();
        //初始化显示图片的控件
        images.add((ImageView) findViewById(R.id.image_one));
        images.add((ImageView) findViewById(R.id.image_two));
        images.add((ImageView) findViewById(R.id.image_three));
        images.add((ImageView) findViewById(R.id.image_four));
        images.add((ImageView) findViewById(R.id.image_five));
        //初始化按钮控件
        lastBtn = (ImageButton) findViewById(R.id.last);
        nextBtn = (ImageButton) findViewById(R.id.next);
        //设置点击监听事件
        lastBtn.setOnClickListener(this);
        nextBtn.setOnClickListener(this);
        //初始化显示图片
        showImage();
    }

    /**
     * 切换按钮
     * @param v
     */
    @Override
    public void onClick(View v) {
        isInit = false;//设置不是初始化界面
        lastShowIndex = showIndex;//记录当前索引
        switch (v.getId()){
            case R.id.last://上一张图片
                showIndex--;//切换到上一张
                break;
            case R.id.next://下一张图片
                showIndex++;//切换倒下一张
                break;
        }
        showImage();//显示图片
    }

    private void showImage(){
        if (showIndex<0){
            showIndex = 0;//保证索引值正确
            Toast.makeText(this, "已经是第一张了", Toast.LENGTH_SHORT).show();
        }else if(showIndex < images.size()){
            for (int i= 0;i <images.size();i++){
                if (i == showIndex){
                    //显示要显示的图片
                    images.get(i).setVisibility(View.VISIBLE);
                    if (!isInit){//不是第一次进入就执行动画
                        //设置动画
                        images.get(i).setAnimation(showAnimation);
                        showAnimation.start();//开启动画
                        //设置动画
                        images.get(lastShowIndex).setAnimation(hideAnimation);
                        hideAnimation.start();//开启动画
                    }
                    continue;
                }
                //隐藏其他图片
                images.get(i).setVisibility(View.GONE);
            }
        }else {
            //保证索引值正确
            showIndex = images.size() - 1;
            Toast.makeText(this, "已经是最后一张了", Toast.LENGTH_SHORT).show();
        }
    }
}
