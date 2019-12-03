package com.mingrisoft.screencapp;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;


public class FileUtil {

  //系统保存截图的路径
  public static final String SCREENCAPTURE_PATH = "ScreenCapture" + File.separator + "Screenshots" + File.separator;
  public static final String SCREENSHOT_NAME = "Screenshot";
  public static String getAppPath(Context context) {
    if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
      return Environment.getExternalStorageDirectory().toString();
    } else {
      return context.getFilesDir().toString();
    }
  }
  public static String getScreenShots(Context context) {
    StringBuffer stringBuffer = new StringBuffer(getAppPath(context));
    stringBuffer.append(File.separator);
    stringBuffer.append(SCREENCAPTURE_PATH);
    File file = new File(stringBuffer.toString());
    if (!file.exists()) {
      file.mkdirs();
    }
    return stringBuffer.toString();
  }
  /**
   * 设置截屏时为图片起的名字
   * 并进行拼接
   * */
  public static String getScreenShotsName(Context context) {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");
    String date = simpleDateFormat.format(new Date());  //初始化日期
    StringBuffer stringBuffer = new StringBuffer(getScreenShots(context));  //初始化StringBuffer
    stringBuffer.append(SCREENSHOT_NAME);
    stringBuffer.append("_");      //添加"_"
    stringBuffer.append(date);     //添加日期
    stringBuffer.append(".png");   //添加后缀
    return stringBuffer.toString(); //返回字符串
  }
}
