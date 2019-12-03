package com.mingrisoft.screencapp;

import android.app.Application;
import android.graphics.Bitmap;


public class BaseApplication extends Application {


  private Bitmap mScreenCaptureBitmap;  //声明一个用于加载截图时的BitMap
  @Override
  public void onCreate() {
    super.onCreate();
  }
  /**
   * set()、get()方法
   * */
  public Bitmap getmScreenCaptureBitmap() {
    return mScreenCaptureBitmap;
  }
  public void setmScreenCaptureBitmap(Bitmap mScreenCaptureBitmap) {
    this.mScreenCaptureBitmap = mScreenCaptureBitmap;
  }
}
