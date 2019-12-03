package com.mingrisoft.guaguale;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.mingrisoft.guaguale.util.MyView;

public class MainActivity extends AppCompatActivity {
    //声明刮刮乐控件
    private MyView lotteryMyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //导入布局
        setContentView(R.layout.activity_main);
       //初始化刮刮乐控件
        lotteryMyView = (MyView) findViewById(R.id.lottery_myview);
    }
   //再抽一次方法
    public void  onAB(View v){
        //调用在抽一次方法
        lotteryMyView.againLotter();
    }
}
