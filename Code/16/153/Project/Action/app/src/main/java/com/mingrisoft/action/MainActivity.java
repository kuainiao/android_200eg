package com.mingrisoft.action;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.RequiresApi;
import android.support.annotation.StringRes;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.RemoteInput;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private LinearLayout one, two, three;
    private List<String> text;
    private int resID[] = new int[]{R.mipmap.one, R.mipmap.two, R.mipmap.three};
    private int stringID[] = new int[]{R.string.one,R.string.two,R.string.three};
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
     *
     * @param v
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT_WATCH)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.one://点击item
                sendNotification(0, 3);
                break;
            case R.id.two://点击item
                sendNotification(1, 4);
                break;
            case R.id.three://点击item
                sendNotification(2, 5);
                break;
        }
    }

    /**
     * 发送通知
     *
     * @param index
     * @param message
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT_WATCH)
    private void sendNotification(int index, int message) {
        //创建意图
        Intent defaultAction = getActionIntent(ActionType.DEFAULT,resID[index],
                text.get(index),stringID[index]);//系统自带
        Intent customAction = getActionIntent(ActionType.CUSTOM,resID[index],
                text.get(index),stringID[index]);//自定义
        Intent voiceAction = getActionIntent(ActionType.VOICE,resID[index],
                text.get(index),stringID[index]);//语音
        PendingIntent defaultPending = PendingIntent.getActivity(this, 1, defaultAction,
                PendingIntent.FLAG_UPDATE_CURRENT);//系统自带
        PendingIntent customPending = PendingIntent.getActivity(this, 2, customAction,
                PendingIntent.FLAG_UPDATE_CURRENT);//自定义
        PendingIntent voicePending = PendingIntent.getActivity(this, 3, voiceAction,
                PendingIntent.FLAG_UPDATE_CURRENT);//语音
        NotificationManagerCompat manager = NotificationManagerCompat.from(this);//通知管理器
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);//通知构造器
        builder.setContentTitle(text.get(index));//设置标题
        builder.setDefaults(Notification.DEFAULT_ALL);//设置通知属性
        NotificationCompat.BigTextStyle style = new NotificationCompat.BigTextStyle();
        style.bigText(text.get(message));//设置内容
        builder.setStyle(style);//设置大文本样式
        builder.setLargeIcon(//设置大图标
                BitmapFactory.decodeResource(getResources(), resID[index]));
        builder.setAutoCancel(true);//设置自关闭
        builder.setContentIntent(defaultPending);//在手机上打开
        builder.setSmallIcon(R.mipmap.ic_launcher);//设置小图标
        builder.extend(new NotificationCompat.WearableExtender()
                .addAction(new NotificationCompat.Action.Builder
                        (R.mipmap.ic_launcher,"语音动作",voicePending)
                        .addRemoteInput(new RemoteInput.Builder("voice")
                                .setLabel("查看详情")
                                .build())
                        .build())
                .addAction(new NotificationCompat.Action.Builder
                        (R.mipmap.ic_launcher,"自定义动作",customPending)
                        .build()));
        manager.notify(1, builder.build());//发送通知
    }


    private Intent getActionIntent(int type,@DrawableRes int imageID, String title,@StringRes int descID){
        Intent intent = new Intent(this,ResultActivity.class);
        intent.putExtra(ActionType.TYPE,type)
                .putExtra("imageID",imageID)//图片资源ID
                .putExtra("name",title)//名称
                .putExtra("descID",descID);//简介资源ID
        return intent;
    }
}
