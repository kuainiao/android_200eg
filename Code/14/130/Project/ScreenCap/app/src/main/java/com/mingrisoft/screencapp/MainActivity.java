package com.mingrisoft.screencapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends MPermissionsActivity {


  public static final int REQUEST_MEDIA_PROJECTION = 18;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    requestCapturePermission();  //获取手机截屏权限
    checkedSystemAlertWindow();   //开启悬浮窗权限
    requestPermission(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1000);
  }


  public void requestCapturePermission() {

    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
      //5.0 之后才允许使用屏幕截图
      return;
    }

    MediaProjectionManager mediaProjectionManager = (MediaProjectionManager)
        getSystemService(Context.MEDIA_PROJECTION_SERVICE);
    startActivityForResult(
        mediaProjectionManager.createScreenCaptureIntent(),
        REQUEST_MEDIA_PROJECTION);
  }

  @Override
  protected void onSystemPermissionSuccess(int i) {
    super.onSystemPermissionSuccess(i);
    if (i == 1000){

    }

  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    switch (requestCode) {
      case REQUEST_MEDIA_PROJECTION:
        if (resultCode == RESULT_OK && data != null) {
        Log.e("1111111111111111", resultCode + "");
          FloatWindowsService.setResultData(data);
          startService(new Intent(getApplicationContext(), FloatWindowsService.class));
        }
        break;
    }
  }
}
