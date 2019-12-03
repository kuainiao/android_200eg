package com.mingrisoft.suspendedwindow;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btn_show, btn_hide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //获取按钮并设置单击事件
        btn_show = (Button) findViewById(R.id.btn_show);
        btn_hide = (Button) findViewById(R.id.btn_hide);
        btn_show.setOnClickListener(this);
        btn_hide.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_show:         //单击显示按钮
                Intent show = new Intent(this, WindowService.class);
                //设置启动条件
                show.putExtra(WindowService.OPERATION,
                        WindowService.WINDOW_SHOW);
                //启动跳转
                startService(show);
                finish();
                break;
            case R.id.btn_hide:          //单击隐藏按钮
                Intent hide = new Intent(this, WindowService.class);
                //设置启动条件
                hide.putExtra(WindowService.OPERATION,
                        WindowService.WINDOW_HIDE);
                //启动跳转
                startService(hide);
                //关闭当前界面
                finish();
                break;
        }
    }
}
