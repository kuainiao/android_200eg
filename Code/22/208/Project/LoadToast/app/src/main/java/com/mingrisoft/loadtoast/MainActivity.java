package com.mingrisoft.loadtoast;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import net.steamcrafted.loadtoast.LoadToastView;

public class MainActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final LoadToast loadToast = new LoadToast(this);       //创建加载Toast对象
        loadToast.setTranslationY(500);                         //设置起始位置
        //显示按钮单击事件
        findViewById(R.id.show).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadToast.show();                              //显示加载Toast
            }
        });
        //错误按钮单击事件
        findViewById(R.id.error).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadToast.error();                             //显示错误Toast
            }
        });
        //完成按钮单击事件
        findViewById(R.id.success).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadToast.success();                          //显示完成Toast
            }
        });
    }
}





