package com.mingrisoft.action;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.RemoteInput;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {
    private ImageView pic;
    private TextView name,desc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        String title;//标题
        initView();//初始化控件
        Intent intent = getIntent();
        //对比类型
        int type = intent.getIntExtra(ActionType.TYPE ,ActionType.NONE);
        switch (type){
            case ActionType.DEFAULT://系统自带
                title = "通过自带action开启";
                break;
            case ActionType.CUSTOM://自定义
                title = "通过自定义action开启";
                break;
            case ActionType.VOICE://调用话筒
                Bundle remoteInputResults = RemoteInput.getResultsFromIntent(intent);
                CharSequence replyMessage = "";
                if (remoteInputResults != null) {
                    replyMessage = remoteInputResults.getCharSequence("voice");
                }
                Log.i("返回内容", replyMessage.toString());
                title = "通过语音【"+ replyMessage +"】开启";
                break;
            default:
                title = "- -!";
                break;
        }
        getSupportActionBar().setTitle(title);//设置标题
        pic.setImageResource(intent.getIntExtra("imageID",0));//图片
        name.setText(intent.getStringExtra("name"));//名称
        desc.setText(getString(intent.getIntExtra("descID",0)));//简介
    }

    /**
     * 初始化控件
     */
    private void initView() {
        pic = (ImageView) findViewById(R.id.picture);
        name = (TextView) findViewById(R.id.name);
        desc = (TextView) findViewById(R.id.desc);
    }
}
