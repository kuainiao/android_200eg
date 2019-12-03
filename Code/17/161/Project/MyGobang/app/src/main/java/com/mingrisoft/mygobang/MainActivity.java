package com.mingrisoft.mygobang;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity {
    private MyGobangView gobang;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置没有顶部栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //设置全屏显示
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //获取屏幕信息
        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        setContentView(R.layout.activity_main);
        gobang =(MyGobangView)findViewById(R.id.action_settings);
    }
    public void  onStart(View v){
             gobang.reStart();
    }
}
