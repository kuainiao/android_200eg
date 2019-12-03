package com.mingrisoft.screenrecord;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
  private static final int RECORD_REQUEST_CODE  = 101;  //录制标记

  private MediaProjectionManager projectionManager;   //创建多媒体投影服务管理
  private MediaProjection mediaProjection;            //创建多媒体投影对象
  private RecordService recordService;                //录制服务
  private Button startBtn;                            //录屏按钮

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    //获取媒体投影服务
    projectionManager = (MediaProjectionManager) getSystemService(MEDIA_PROJECTION_SERVICE);
    setContentView(R.layout.activity_main);
    //自动申请存储与录音权限
    ActivityCompat.requestPermissions(this,
              new String[] {Manifest.permission.RECORD_AUDIO,Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1000);
    Intent intent = new Intent(MainActivity.this, RecordService.class);       //创建启动Service的Intent
    bindService(intent, connection, BIND_AUTO_CREATE);          //绑定指定Service
    //获取开始录制按钮
    startBtn = (Button) findViewById(R.id.start_record);
    startBtn.setEnabled(false);   //设置默认不可用
    //开始录制按钮的单击事件
    startBtn.setOnClickListener(new View.OnClickListener() {
      @TargetApi(Build.VERSION_CODES.LOLLIPOP)
      @Override
      public void onClick(View v) {
        //如果录制服务启动了
        if (recordService.isRunning()) {
          Toast.makeText(recordService, "停止录像", Toast.LENGTH_SHORT).show();
          recordService.stopRecord();       //停止录制
          startBtn.setText(R.string.start_record);     //设置按钮显示文字

        } else {
          //创建启动屏幕录制的Intent
          Intent captureIntent = projectionManager.createScreenCaptureIntent();
          //启动屏幕录制
          startActivityForResult(captureIntent, RECORD_REQUEST_CODE);
        }
      }
    });


  }

  /**
   * 界面销毁解除绑定服务
   */
  @Override
  protected void onDestroy() {
    super.onDestroy();
    recordService.stopRecord();
    unbindService(connection);  //解除绑定服务
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == RECORD_REQUEST_CODE && resultCode == RESULT_OK) {
      //获取录屏数据
      mediaProjection = projectionManager.getMediaProjection(resultCode, data);
      //设置录屏数据
      recordService.setMediaProject(mediaProjection);
      //启动录屏服务
      recordService.startRecord();
      //设置按钮文字
      startBtn.setText(R.string.stop_record);
    }
  }

  @Override
  public boolean onKeyDown(int keyCode, KeyEvent event) {

    if (keyCode == KeyEvent.KEYCODE_BACK ){
      if (recordService.isRunning()){
        Toast.makeText(this, "请先单击停止录制按钮，然后再关闭应用程序！", Toast.LENGTH_SHORT).show();
      }else {
          unbindService(connection);  //解除绑定服务
          recordService.stopRecord();
          finish();
      }
    }
      return false;
  }

  //设置与后台Service进行通讯
    private ServiceConnection connection = new ServiceConnection() {
    @Override
    public void onServiceConnected(ComponentName className, IBinder service) {
      //创建该对象用于获取屏幕的宽度与高度（像素）
      DisplayMetrics metrics = new DisplayMetrics();
      //获取屏幕像素
      getWindowManager().getDefaultDisplay().getMetrics(metrics);
      //创建录制服务对象
      RecordService.RecordBinder binder = (RecordService.RecordBinder) service;
      //获取后台录制服务信息
      recordService = binder.getRecordService();
      //设置录像参数
      recordService.setConfig(metrics.widthPixels, metrics.heightPixels, metrics.densityDpi);
      startBtn.setEnabled(true);        //设置录制按钮可用状态
      //根据录屏服务状态设置按钮文字，服务开启时设置文字为停止录屏
      startBtn.setText(recordService.isRunning() ? R.string.stop_record : R.string.start_record);
    }

    @Override
    public void onServiceDisconnected(ComponentName arg0) {}
  };


}
