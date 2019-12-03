package com.mingrisoft.thumbup;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wx.goodview.GoodView;

public class MainActivity extends AppCompatActivity {

    private ImageView tu;//显示点赞图的控件
    private LinearLayout zan;//点赞按钮
    private GoodView mGoodView;//点赞效果
    private boolean isGood;//是否点过赞
    private TextView message;//内容

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化控件
        zan = (LinearLayout) findViewById(R.id.zan);
        tu = (ImageView) findViewById(R.id.tu);
        message = (TextView) findViewById(R.id.message);
        message.setText(Html.fromHtml("我发表了条文章：《想学编程吗？》你是否也可以成为出色的程序员？一看便知。<font color='#56ABD9'>全民学习编程时代悄然开启，你跟上节奏了么？</font>"));
        //初始化点赞效果
        mGoodView = new GoodView(this);
        //设置点赞的点击事件
        zan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isGood){//判断是否点过赞
                    tu.setImageResource(R.mipmap.three1);
                    mGoodView.reset();//重置
                    isGood = false;//设置false
                }else {
                    tu.setImageResource(R.mipmap.three2);
                    mGoodView.setImage(R.mipmap.three2);
                    mGoodView.show(tu);//设置显示的图
                    isGood = true;//设为true
                }
            }
        });
    }
}
