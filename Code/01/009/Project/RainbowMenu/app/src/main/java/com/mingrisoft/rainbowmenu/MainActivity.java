package com.mingrisoft.rainbowmenu;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {
    private ImageButton imageButton_a1,imageButton_b2;     //触发菜单的按钮
    private RelativeLayout l2,l3;                             //二级与三级菜单布局

    private boolean isl2Show = true;                    //判断二级菜单是否显示
    private boolean isl3Show = true;                    //盘算三级菜单是否显示
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //获取触发菜单的按钮
        imageButton_a1 = (ImageButton) findViewById(R.id.a_1);
        imageButton_b2 = (ImageButton) findViewById(R.id.b_2);
        //获取二级菜单与三级菜单布局
        l2 = (RelativeLayout) findViewById(R.id.level_2);
        l3 = (RelativeLayout) findViewById(R.id.level_3);
        //单击该按钮显示或隐藏三级菜单
        imageButton_b2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(isl3Show){
                    //隐藏3级导航菜单
                    MyAnimation.animationOUT(l3, 500, 0);
                }else {
                    //显示3级导航菜单
                    MyAnimation.animationIN(l3, 500);

                }
                //根据当前的显示状态设置为相反的状态，如3级菜单已经关闭。这里将设置状态为false
                isl3Show = !isl3Show;

            }
        });
        //单击该按钮显示二级菜单或隐藏二级三级菜单
        imageButton_a1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(!isl2Show){
                    //显示2级导航菜单
                    MyAnimation.animationIN(l2, 500);
                } else {
                    if(isl3Show){
                        //隐藏3级导航菜单
                        MyAnimation.animationOUT(l3, 500, 0);
                        //隐藏2级导航菜单
                        MyAnimation.animationOUT(l2, 500, 500);
                        isl3Show = !isl3Show;
                    }
                    else {
                        //隐藏2级导航菜单
                        MyAnimation.animationOUT(l2, 500, 0);
                    }
                }
                isl2Show = !isl2Show;
            }
        });
    }
}



