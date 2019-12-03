package com.mingrisoft.screenrecord;

import android.annotation.TargetApi;
import android.app.Service;
import android.content.Intent;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.MediaRecorder;
import android.media.projection.MediaProjection;
import android.os.Binder;
import android.os.Build;
import android.os.Environment;
import android.os.HandlerThread;
import android.os.IBinder;

import java.io.File;
import java.io.IOException;


public class RecordService extends Service {
  private MediaProjection mediaProjection;    //多媒体投影对象
  private MediaRecorder mediaRecorder;        //多媒体录影机
  private VirtualDisplay virtualDisplay;      //虚拟显示
  //设置录像参数
  private boolean running;
  private int width = 720;
  private int height = 1080;
  private int dpi;


  @Override
  public IBinder onBind(Intent intent) {    //必须实现的绑定方法
    return new RecordBinder();              //返回MyBinder服务对象
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    return START_STICKY;
  }

  @Override
  public void onCreate() {
    super.onCreate();
    HandlerThread serviceThread = new HandlerThread("service_thread",
        android.os.Process.THREAD_PRIORITY_BACKGROUND);
    serviceThread.start();
    running = false;
    mediaRecorder = new MediaRecorder();
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
  }
  //设置录屏数据返回的对象
  public void setMediaProject(MediaProjection project) {
    mediaProjection = project;
  }

  public boolean isRunning() {
    return running;
  }
  //设置录像参数
  public void setConfig(int width, int height, int dpi) {
    this.width = width;
    this.height = height;
    this.dpi = dpi;
  }

  public boolean startRecord() {
    if (mediaProjection == null || running) {
      return false;
    }

    initRecorder();   //录像的初始化工作
    createVirtualDisplay();
    mediaRecorder.start();
    running = true;
    return true;
  }

  /**
   * 停止录制
   * @return
     */
  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public boolean stopRecord() {
    if (!running) {
      return false;
    }
    running = false;
    mediaRecorder.stop();
    mediaRecorder.reset();
    virtualDisplay.release();
    mediaProjection.stop();

    return true;
  }

  /**
   * 创建虚拟画面
   * 第一个参数：虚拟画面名称
   * 第二个参数：虚拟画面的宽度
   * 第三个参数：虚拟画面的高度
   * 第四个参数：虚拟画面的标志
   * 第五个参数：虚拟画面输出的Surface
   * 第六个参数：虚拟画面回调接口
   */
  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  private void createVirtualDisplay() {
    virtualDisplay = mediaProjection.createVirtualDisplay("MainScreen", width, height, dpi,
        DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR, mediaRecorder.getSurface(), null, null);
  }
  //录像的初始化工作
  private void initRecorder() {

    //设置音频源（配音类的音频源主要源于文件）
    mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
    //设置视频源：Surface和Camera 两种
    mediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);
    //设置视频输出格式
    mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
    //设置视频编码格式
    mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
    //设置音频编码格式
    mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
    //设置视频输出路径
    mediaRecorder.setOutputFile(getsaveDirectory() + System.currentTimeMillis() + ".mp4");
    //设置视频尺寸大小
    mediaRecorder.setVideoSize(width, height);
    //设置视频编码的码率，码率越大 越接近真实的视频
    mediaRecorder.setVideoEncodingBitRate(5 * 1024 * 1024);
    //设置视频编码的帧率，帧数越高，所占内存越大
    mediaRecorder.setVideoFrameRate(30);
    try {
      mediaRecorder.prepare();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  //视频保存位置
  public String getsaveDirectory() {
    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
      String rootDir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "ScreenRecord" + "/";

      File file = new File(rootDir);
      if (!file.exists()) {
        if (!file.mkdirs()) {
          return null;
        }
      }



      return rootDir;
    } else {
      return null;
    }
  }

  public class RecordBinder extends Binder {
    public RecordService getRecordService() {
      return RecordService.this;
    }
  }
}