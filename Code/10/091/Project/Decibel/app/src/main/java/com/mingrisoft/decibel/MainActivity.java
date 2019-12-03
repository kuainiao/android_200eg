package com.mingrisoft.decibel;

import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends MPermissionsActivity implements View.OnClickListener{

    private Button switchs;//开关按钮
    private DialPlateView dialPlateView;//显示分贝
    private boolean isStart;//用于判断是否开启录音
    private AudioRecordManger audioRecordManger;//音频管理
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == 1){
                double values = (double) msg.obj;//获取分贝值
                float db = (float) values;//类型转换
                dialPlateView.upDataValues(db);//更新控件显示
            }
            return false;
        }
    });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //实例化控件
        switchs = (Button) findViewById(R.id.switchs);
        dialPlateView = (DialPlateView) findViewById(R.id.show);
        switchs.setOnClickListener(this);
        //获取声音管理器
        audioRecordManger = new AudioRecordManger(handler,1);
    }

    @Override
    public void onClick(View v) {
        //如果没有开启测试择退出方法
        if (isStart){
            AudioRecordManger.isGetVoiceRun = false;
            switchs.setText("START");
            isStart = false;
            Toast.makeText(this, "分贝测试结束了", Toast.LENGTH_SHORT).show();
            return;
        }
        //开启权限申请
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            requestPermission(new String[]{Manifest.permission.RECORD_AUDIO},1000);
        }else {
            startTest();//开启测试
        }
    }

    @Override
    public void permissionSuccess(int requestCode) {
        super.permissionSuccess(requestCode);
        if (requestCode == 1000 && !isStart){
            startTest();
        }
    }


    /**
     * 开启分贝的测试
     */
    private void startTest(){
        audioRecordManger.getNoiseLevel();
        switchs.setText("STOP");
        isStart = true;
        Toast.makeText(this, "分贝测试开始了", Toast.LENGTH_SHORT).show();
    }
}
