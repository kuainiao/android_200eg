package com.mingrisoft.wechatvideo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import view.RecordProgress;
import view.RecordView;

public class MainActivity extends AppCompatActivity implements RecordView.OnRecordStausChangeListener {

    private RecordView recordView;              //视频录制控件
    private Button btnVideo;                   //按住拍按钮
    private TextView tvUp;                      //显示上滑取消文字
    private TextView tvRelease;                 //显示松开取消文字
    private RecordProgress recordProgress;                 //拍摄进度条
    private final int SDK_PERMISSION = 1;                //申请权限

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getPersimmions();                                   //获取SD卡读写权限
    }


    /**
     * 添加SD卡读写动态权限
     */
    private void getPersimmions() {
        /***
         * 判断系统版本是否超过6.0以上，如果是就进行添加权限 ，低于6.0将直接加载进行初始化
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ArrayList<String> permissions = new ArrayList<String>();
            //判断是否开启内存卡读写权限、摄像头、录音
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                //添加权限
                permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                permissions.add(Manifest.permission.CAMERA);
                permissions.add(Manifest.permission.RECORD_AUDIO);
                if (permissions.size() > 0) {
                    requestPermissions(permissions.toArray(new String[permissions.size()]), SDK_PERMISSION);
                }
            } else {
                //调用获取相关控件方法
                initView();
                //监听事件
                initListener();
            }
        } else {
            //调用获取相关控件方法
            initView();
            //监听事件
            initListener();
        }
    }

    /**
     * 动态权限的回调方法
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //调用获取相关控件方法
        initView();
        recordView.openCamera();
        //监听事件
        initListener();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        recordView = (RecordView) findViewById(R.id.vrvVideo);   //获取视频录制控件
        btnVideo = (Button) findViewById(R.id.btnVideo);               //获取按住拍按钮
        tvUp = (TextView) findViewById(R.id.tv_up);             //获取显示上滑取消文字
        tvRelease = (TextView) findViewById(R.id.tv_release);             //获取显示松开取消文字
        recordProgress = (RecordProgress) findViewById(R.id.rp);                     //获取拍摄的进度条
        recordProgress.setRecordTime(10);                                            //设置最长的录制时间
    }

    /**
     * 监听事件处理
     */
    private void initListener() {
        //按住拍按钮的触摸事件
        btnVideo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:                               //手指按下的事件
                        recordProgress.start();                                             //开启拍摄进度条
                        recordProgress.setProgressColor(Color.parseColor("#1AAD19"));      //设置进度条颜色
                        tvUp.setVisibility(View.VISIBLE);                 //显示上滑取消文字
                        tvRelease.setVisibility(View.GONE);                    //隐藏松开取消文字
                        recordView.record(MainActivity.this);                    //开始录制
                        break;
                    case MotionEvent.ACTION_UP:                                //手指抬起事件
                        recordProgress.stop();                                 //停止拍摄进度条
                        tvUp.setVisibility(View.GONE);                    //隐藏上滑取消文字
                        tvRelease.setVisibility(View.GONE);                    //隐藏松开取消文字
                        if (recordView.getTimeCount() > 5) {       //判断已录制的秒数大于5秒
                            if (!isMove(v, event)) {             //判断手指没有移动的情况下
                                onRecrodFinish();                  //录制完成
                            }
                        } else {
                            if (!isMove(v, event)) {             //录制的秒数小于5秒
                                Toast.makeText(getApplicationContext(), "视频时长太短", Toast.LENGTH_SHORT).show();
                                //删除小于5秒的视频
                                if (recordView.getVecordFile() != null)
                                    recordView.getVecordFile().delete();
                            }
                        }
                        resetVideoRecord();     //停止录制（释放相机后重新打开相机）
                        break;
                    case MotionEvent.ACTION_MOVE:                  //手指移动事件
                        if (isMove(v, event)) {                   //如果手指移动
                            tvUp.setVisibility(View.GONE);    //隐藏上滑取消文字
                            tvRelease.setVisibility(View.VISIBLE); //显示松开取消文字
                            //设置进度条颜色
                            recordProgress.setProgressColor(Color.parseColor("#FF1493"));
                        } else {
                            tvUp.setVisibility(View.VISIBLE);
                            tvRelease.setVisibility(View.GONE);
                            recordProgress.setProgressColor(Color.parseColor("#1AAD19"));
                        }
                        break;
                }
                return true;
            }
        });
    }

    /**
     * 判断手指是否移动
     */
    private boolean isMove(View v, MotionEvent event) {
        int[] location = new int[2];
        v.getLocationOnScreen(location);
        if (event.getRawX() < location[0] || event.getRawX() > location[0]
                + v.getWidth() || event.getRawY() < location[1] - 40) {
            return true;
        }
        return false;
    }

    /**
     * 录制完成
     */
    @Override
    public void onRecrodFinish() {
        runOnUiThread(new Runnable() {      //在UI线程上运行指定的操作
            @Override
            public void run() {
                //隐藏上滑取消文字与松开取消文字
                tvUp.setVisibility(View.GONE);
                tvRelease.setVisibility(View.GONE);
                resetVideoRecord();         //停止录制（释放相机后重新打开相机）
                Toast.makeText(MainActivity.this, "录制成功！",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onRecording(int timeCount, int recordMaxTime) {

    }

    @Override
    public void onRecordStart() {
    }

    /**
     * 停止录制（释放相机后重新打开相机）
     */
    public void resetVideoRecord() {
        recordView.stop();
        recordView.openCamera();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}



