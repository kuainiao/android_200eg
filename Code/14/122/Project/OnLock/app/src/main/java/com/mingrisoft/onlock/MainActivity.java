package com.mingrisoft.onlock;

import android.Manifest;
import android.app.KeyguardManager;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.app.KeyguardManager.KeyguardLock;
import android.os.SystemClock;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends MPermissionsActivity implements View.OnClickListener {
    /**
     * 设备策略服务
     */
    private DevicePolicyManager dpm;

    private AudioRecordManger recordManger;
    private Handler handler;
    private ImageView dayIV,nightIV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();  //初始化
        requestPermission(new String[]{Manifest.permission.RECORD_AUDIO}, 1000);

    }

    @Override
    protected void onStart() {
        super.onStart();
        dayIV.setVisibility(View.VISIBLE);
        nightIV.setVisibility(View.INVISIBLE);
    }

    @Override
    public void permissionSuccess(int requestCode) {
        super.permissionSuccess(requestCode);
        if (requestCode == 1000){
            recordManger.getNoiseLevel();
        }
    }



    /**
     * 初始化
     * */
    private void init() {
        dayIV = (ImageView) findViewById(R.id.day_image);
        nightIV = (ImageView) findViewById(R.id.night_image);

        findViewById(R.id.openAdmin).setOnClickListener(this);
        dpm = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
        handler =new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == 1){
                    double i = (double) msg.obj;
                    if (i >=60){
                        dayIV.setVisibility(View.INVISIBLE);
                        nightIV.setVisibility(View.VISIBLE);
//                        try {
//                            Thread.sleep(1000);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
                        ComponentName who = new ComponentName(MainActivity.this, MyAdmin.class);
                        if (dpm.isAdminActive(who)) {
                            dpm.lockNow();// 锁屏
                            dpm.resetPassword("", 0);// 设置屏蔽密码
                        } else {
                            Toast.makeText(MainActivity.this, "还没有打开管理员权限",Toast.LENGTH_SHORT).show();
                            return false;
                        }
                    }
                    Log.e("------", "msg.what:" + msg.obj);
                }
                return false;
            }
        });
        recordManger = new AudioRecordManger(handler,1);
}

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.openAdmin:
                // 创建一个Intent
                Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                // 我要激活谁
                ComponentName mDeviceAdminSample = new ComponentName(this,MyAdmin.class);
                intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN,mDeviceAdminSample);
                // 劝说用户开启管理员权限
                intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,"请打开管理员权限");
                startActivity(intent);
                break;
        }
    }

}
