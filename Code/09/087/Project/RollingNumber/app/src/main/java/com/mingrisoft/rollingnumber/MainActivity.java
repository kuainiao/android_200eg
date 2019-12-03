package com.mingrisoft.rollingnumber;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import view.ScrollingDigitalAnimation;

public class MainActivity extends AppCompatActivity {

    private ScrollingDigitalAnimation money;        //显示金钱的自定义控件
    private ScrollingDigitalAnimation number;       //显示数字的自定义控件
    private ScrollingDigitalAnimation percentage;   //显示百分比的自定义控件
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        money = (ScrollingDigitalAnimation) findViewById(R.id.text);
        number = (ScrollingDigitalAnimation) findViewById(R.id.text1);
        percentage = (ScrollingDigitalAnimation) findViewById(R.id.text2);
    }

    /**
     *启动按钮单击事件
     */
    public void start(View view) {
        money.setPrefixString("¥");                             //设置符号
        money.setNumberString("9", "9999999999");      //设置起始于结束的数字
        number.setNumberString("1234567890");
        percentage.setPostfixString("%");
        percentage.setNumberString("0.99", "99.99");
    }
}
