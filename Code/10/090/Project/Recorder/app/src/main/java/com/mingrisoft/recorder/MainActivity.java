package com.mingrisoft.recorder;

import android.Manifest;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class MainActivity extends MPermissionsActivity {

    private String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.RECORD_AUDIO};
    private ImageButton startRecordBtn,stopRecordBtn,startSoundBtn,stopSoundBtn;
    private Chronometer chronometer;
    private LinearLayout play,cancel;
    // 语音操作对象
    private MediaPlayer mPlayer = null;
    private MediaRecorder mRecorder = null;
    // 语音文件保存路径
    private String fileName = null;
    private boolean isSound;
    private boolean isRecord;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        play = (LinearLayout) findViewById(R.id.play);
        cancel = (LinearLayout) findViewById(R.id.cancel);
        startRecordBtn = (ImageButton) findViewById(R.id.start_record);
        stopRecordBtn = (ImageButton) findViewById(R.id.stop_record);
        startSoundBtn = (ImageButton) findViewById(R.id.start_sound);
        stopSoundBtn = (ImageButton) findViewById(R.id.stop_sound);
        chronometer = (Chronometer) findViewById(R.id.chronometers);
        OnClick onClick = new OnClick();
        startRecordBtn.setOnClickListener(onClick);
        stopRecordBtn.setOnClickListener(onClick);
        startSoundBtn.setOnClickListener(onClick);
        stopSoundBtn.setOnClickListener(onClick);
    }

    @Override
    public void permissionSuccess(int requestCode) {
        super.permissionSuccess(requestCode);
        if (requestCode == 1){
            startRecord();
        }
    }

    /**
     * 开始录音
     */
    private void startRecord() {
        if (isSound && !isRecord){
            Toast.makeText(this, "录音播放没有结束", Toast.LENGTH_SHORT).show();
            return;
        }
        // 设置sdcard的路径
        fileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        fileName = fileName + File.separator + System.currentTimeMillis()+"mine.arm";
        chronometer.setBase(SystemClock.elapsedRealtime());//设置计时器的参考时间
        chronometer.start();//开启计时器
        mRecorder = new MediaRecorder();//该类用来记录音频或视频的
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);//设置通过麦克风记录音频
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);//设置文件格式为默认格式
        mRecorder.setOutputFile(fileName);//设置文件输出路径
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);//设置编码为默认值
        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e("startRecord", "prepare() failed");
        }
        mRecorder.start();//开始录制
        isRecord = true;
        Toast.makeText(this, "录音开始了", Toast.LENGTH_SHORT).show();
    }
    /**
     * 停止录音
     */
    private void stopRecord() {
        if (isSound && !isRecord){
            Toast.makeText(this, "录音播放没有结束", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!isRecord){
            return;
        }
        mRecorder.stop();//停止录制
        mRecorder.release();//释放资源
        mRecorder = null;
        chronometer.stop();//停止计时器
        isRecord = false;
        Toast.makeText(this, "录音停止了", Toast.LENGTH_SHORT).show();
    }
    /**
     * 播放录音
     */
    private void startSound() {
        if (fileName == null){
            Toast.makeText(this, "当前没有录音", Toast.LENGTH_SHORT).show();
            return;
        }
        if (isRecord && !isSound){
            Toast.makeText(this, "录音没有结束", Toast.LENGTH_SHORT).show();
            return;
        }
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();
        mPlayer = new MediaPlayer();//获取播放器对象
        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                chronometer.stop();//停止计时
                Toast.makeText(MainActivity.this, "播放结束", Toast.LENGTH_SHORT).show();
            }
        });
        try {
            mPlayer.setDataSource(fileName);//设置播放文件
            mPlayer.prepare();//同步资源
            mPlayer.start();//播放音频
        } catch (IOException e) {
            Log.e("startSound", "播放失败");
        }
        isSound = true;
        play.setVisibility(View.GONE);//隐藏播放按钮
        cancel.setVisibility(View.VISIBLE);//显示停止按钮
        Toast.makeText(this, "播放开始了", Toast.LENGTH_SHORT).show();
    }
    /**
     * 停止播放
     */
    private void stopSound() {
        if (isRecord && !isSound){
            Toast.makeText(this, "播放没有结束", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!isSound){
            return;
        }
        mPlayer.release();//释放资源
        mPlayer = null;
        chronometer.stop();//停止计时器
        isSound = false;
        play.setVisibility(View.VISIBLE);//显示播放按钮
        cancel.setVisibility(View.GONE);//异常取消按钮
        Toast.makeText(this, "播放停止了", Toast.LENGTH_SHORT).show();
    }

    /**
     * 点击事件
     */
    private class OnClick implements View.OnClickListener{
        @Override
        public void onClick(View v) {

            switch (v.getId()){
                case R.id.start_record:
                    requestPermission(permissions,1);
                    break;
                case R.id.stop_record:
                    stopRecord();
                    break;
                case R.id.start_sound:
                    startSound();
                    break;
                case R.id.stop_sound:
                    stopSound();
                    break;
            }
        }
    }
}
