package com.mingrisoft.lucky;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private PrizeView prizeView;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //获取控件
        prizeView = (PrizeView) this.findViewById(R.id.lotterywheel);
        //获取抽奖的结果
        prizeView.setOnSelectListener(new PrizeView.SelectListener() {
            @Override
            public void onSelect(String name) {
                //弹出框提示转盘结果
                Toast.makeText(getApplicationContext(), name, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
