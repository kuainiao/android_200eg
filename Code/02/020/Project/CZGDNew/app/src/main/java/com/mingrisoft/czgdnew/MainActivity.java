package com.mingrisoft.czgdnew;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化垂直滚动事件
        VerTextView tv_ver = (VerTextView) findViewById(R.id.tv_ver);
        //绑定点击事件
        tv_ver.setListener(new VerTextView.onTouchListener() {
            @Override
            public void touchListener(String s) {
                //弹出提示信息
                Toast.makeText(MainActivity.this, s, Toast.LENGTH_LONG).show();
            }
        });
    }
}
