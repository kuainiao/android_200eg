package com.mingrisoft.notification;

import android.app.Notification;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private LinearLayout one,two,three;
    private List<String> text;
    private int resID[] = new int[]{R.mipmap.one,R.mipmap.two,R.mipmap.three};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        intiView();//初始化控件
        text = new ArrayList<>();//数据集合
        text.add("android从入门到精通");//标题
        text.add("java编程词典");//标题
        text.add("Java从入门到精通");//标题
        text.add(getString(R.string.one));//简介
        text.add(getString(R.string.two));//简介
        text.add(getString(R.string.three));//简介
    }

    /**
     * 初始化控件
     */
    private void intiView() {
        one = (LinearLayout) findViewById(R.id.one);
        two = (LinearLayout) findViewById(R.id.two);
        three = (LinearLayout) findViewById(R.id.three);
        //添加点击事件
        one.setOnClickListener(this);
        two.setOnClickListener(this);
        three.setOnClickListener(this);
    }

    /**
     * 点击事件
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.one://点击item
                sendNotification(0,3);
                break;
            case R.id.two://点击item
                sendNotification(1,4);
                break;
            case R.id.three://点击item
                sendNotification(2,5);
                break;
        }
    }

    /**
     * 发送通知
     * @param index
     * @param message
     */
    private void sendNotification(int index,int message) {
        NotificationManagerCompat manager = NotificationManagerCompat.from(this);//通知管理器
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);//通知构造器
        builder.setContentTitle(text.get(index));//设置标题
        builder.setDefaults(Notification.DEFAULT_ALL);//设置通知属性
        builder.setLargeIcon(//设置大图标
                BitmapFactory.decodeResource(getResources(),resID[index]));
        builder.setAutoCancel(true);//设置自关闭
        builder.setSmallIcon(R.mipmap.ic_launcher);//设置小图标
        manager.notify(1,builder.build());//发送通知
    }
}
