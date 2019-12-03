package com.mingrisoft.yishiyancamera;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.tutk.IOTC.AVIOCTRLDEFs;
import com.tutk.IOTC.Camera;
import com.tutk.IOTC.IRegisterIOTCListener;
import com.tutk.IOTC.Monitor;
import com.tutk.IOTC.Packet;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;

/**
 * Created by Administrator on 2017/2/24.
 */

public class CameraActivity extends AppCompatActivity implements View.OnClickListener {

    String name, UID, code;
    private TextView connectTv;
    private Button topBtn, downBtn, leftBtn, rightBtn, snapshotBtn,rotateVerBtn,rotateHorBtn, videotapeBtn,voiceBtn;
    private Camera mCamera;
    private MyIOTCListener myIOTCListener;
    private Monitor monitor;
    private boolean photograph = false, videotape = true;
    private Calendar calendar;

    private boolean VerticalFlip = false;
    private boolean LevelFlip = false;
    private boolean mIsListening = false;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Camera.CONNECTION_STATE_CONNECTING:
                    //连接中...
                    connectTv.setText("连接状态：连接中...");
                    break;

                case Camera.CONNECTION_STATE_CONNECTED:
                    //连接成功
                    connectTv.setText("连接状态：连接成功");
                    //显示摄像头画面
                    showCamera();
                    break;
                case Camera.CONNECTION_STATE_CONNECT_FAILED:
                    //连接失败
                    connectTv.setText("连接状态：连接失败");
                    break;
                case Camera.CONNECTION_STATE_TIMEOUT:
                    //连接超时
                    connectTv.setText("连接状态：连接超时");
                    break;
                case 666:
                    Toast.makeText(CameraActivity.this, "拍照成功", Toast.LENGTH_SHORT).show();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    /**
     * 显示画面
     */
    private void showCamera() {
        //判断连接是否成功
        if (mCamera != null && mCamera.isChannelConnected(Camera.DEFAULT_AV_CHANNEL)) {
            monitor.setMaxZoom(1.0f);  //设置最大焦距
            monitor.attachCamera(mCamera, Camera.DEFAULT_AV_CHANNEL); //连接摄像头
            //开始显示摄像头
            //第一个true    第二个true是画面  第三个true
            mCamera.startShow(Camera.DEFAULT_AV_CHANNEL, true, true, true);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        UID = intent.getStringExtra("UID");
        code = intent.getStringExtra("code");
        findId();
        connet(name, UID, code);
        calendar = Calendar.getInstance();
    }

    /**
     * 连接
     */
    private void connet(String name, String uid, String code) {
        Camera.init();  //初始化摄像头  初始化视频音频的通道
        mCamera = new Camera();
        myIOTCListener = new MyIOTCListener();  //注册回调接口
        mCamera.registerIOTCListener(myIOTCListener);  //注册监听
        mCamera.connect(uid); //连接摄像头
        //参数1：通道号(与后面用到的一致)
        mCamera.start(Camera.DEFAULT_AV_CHANNEL, name, code);
        //发送测试是否连接成功（参数1：通道号，参数2：指令类型，参数3：指令参数）
//        mCamera.sendIOCtrl(Camera.DEFAULT_AV_CHANNEL,
//                AVIOCTRLDEFs.IOTYPE_USER_IPCAM_GETSUPPORTSTREAM_REQ,
//                AVIOCTRLDEFs.SMsgAVIoctrlGetSupportStreamReq.parseContent());
    }

    /**
     * 绑定id
     * */
    private void findId() {
        topBtn = (Button) findViewById(R.id.top_btn);
        downBtn = (Button) findViewById(R.id.down_btn);
        leftBtn = (Button) findViewById(R.id.left_btn);
        rightBtn = (Button) findViewById(R.id.right_btn);
        voiceBtn = (Button) findViewById(R.id.voice_btn);
        rotateVerBtn= (Button) findViewById(R.id.btn_rotate_ver);
        rotateHorBtn = (Button) findViewById(R.id.btn_rotate_hor);
        rotateVerBtn.setOnClickListener(this);
        rotateHorBtn.setOnClickListener(this);
        topBtn.setOnClickListener(this);
        downBtn.setOnClickListener(this);
        leftBtn.setOnClickListener(this);
        rightBtn.setOnClickListener(this);
        connectTv = (TextView) findViewById(R.id.connect_tv);
        monitor = (Monitor) findViewById(R.id.monitor);
        snapshotBtn = (Button) findViewById(R.id.btn_snapshot);
        videotapeBtn = (Button) findViewById(R.id.btn_videotape);
        snapshotBtn.setOnClickListener(this);
        videotapeBtn.setOnClickListener(this);
        voiceBtn.setOnClickListener(this);
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.top_btn:  //上调
                //参数含义：方向，速度，距离，触摸点的个数，外接设备，通道号
                mCamera.sendIOCtrl(Camera.DEFAULT_AV_CHANNEL, AVIOCTRLDEFs.IOTYPE_USER_IPCAM_PTZ_COMMAND,
                        AVIOCTRLDEFs.SMsgAVIoctrlPtzCmd.parseContent((byte) AVIOCTRLDEFs.AVIOCTRL_PTZ_UP,
                                (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) Camera.DEFAULT_AV_CHANNEL));
                break;
            case R.id.down_btn://下调
                mCamera.sendIOCtrl(Camera.DEFAULT_AV_CHANNEL, AVIOCTRLDEFs.IOTYPE_USER_IPCAM_PTZ_COMMAND,
                        AVIOCTRLDEFs.SMsgAVIoctrlPtzCmd.parseContent((byte) AVIOCTRLDEFs.AVIOCTRL_PTZ_DOWN,
                                (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) Camera.DEFAULT_AV_CHANNEL));
                break;
            case R.id.left_btn://左调
                mCamera.sendIOCtrl(Camera.DEFAULT_AV_CHANNEL, AVIOCTRLDEFs.IOTYPE_USER_IPCAM_PTZ_COMMAND,
                        AVIOCTRLDEFs.SMsgAVIoctrlPtzCmd.parseContent((byte) AVIOCTRLDEFs.AVIOCTRL_PTZ_LEFT,
                                (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) Camera.DEFAULT_AV_CHANNEL));
                break;
            case R.id.right_btn://右调
                mCamera.sendIOCtrl(Camera.DEFAULT_AV_CHANNEL, AVIOCTRLDEFs.IOTYPE_USER_IPCAM_PTZ_COMMAND,
                        AVIOCTRLDEFs.SMsgAVIoctrlPtzCmd.parseContent((byte) AVIOCTRLDEFs.AVIOCTRL_PTZ_RIGHT,
                                (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) Camera.DEFAULT_AV_CHANNEL));
                break;
            case R.id.btn_snapshot:  //拍照
                photograph = true;
                break;

            case R.id.btn_rotate_hor:  //左右翻转

                if (VerticalFlip) {
                     VerticalFlip = false;
                    mCamera.sendIOCtrl(Camera.DEFAULT_AV_CHANNEL,
                            AVIOCTRLDEFs.IOTYPE_USER_IPCAM_SET_VIDEOMODE_REQ,
                            AVIOCTRLDEFs.SMsgAVIoctrlSetVideoModeReq
                                    .parseContent(Camera.DEFAULT_AV_CHANNEL,
                                            (byte) 0));
                } else {
                    VerticalFlip = true;
                    mCamera.sendIOCtrl(Camera.DEFAULT_AV_CHANNEL,
                            AVIOCTRLDEFs.IOTYPE_USER_IPCAM_SET_VIDEOMODE_REQ,
                            AVIOCTRLDEFs.SMsgAVIoctrlSetVideoModeReq
                                    .parseContent(Camera.DEFAULT_AV_CHANNEL,
                                            (byte) 2));
                }
                break;
            case R.id.btn_rotate_ver://上下翻转
                if (LevelFlip) {
                    LevelFlip = false;
                    mCamera.sendIOCtrl(Camera.DEFAULT_AV_CHANNEL,
                            AVIOCTRLDEFs.IOTYPE_USER_IPCAM_SET_VIDEOMODE_REQ,
                            AVIOCTRLDEFs.SMsgAVIoctrlSetVideoModeReq
                                    .parseContent(Camera.DEFAULT_AV_CHANNEL,
                                            (byte) 0));


                } else {
                    LevelFlip = true;
                    mCamera.sendIOCtrl(Camera.DEFAULT_AV_CHANNEL,
                            AVIOCTRLDEFs.IOTYPE_USER_IPCAM_SET_VIDEOMODE_REQ,
                            AVIOCTRLDEFs.SMsgAVIoctrlSetVideoModeReq
                                    .parseContent(Camera.DEFAULT_AV_CHANNEL,
                                            (byte) 1));
                }
                break;

            case R.id.voice_btn:  //开启语音实时监听
                if (!mIsListening) {
                    //打开开关
                    mCamera.startListening(Camera.DEFAULT_AV_CHANNEL, true);  //开始监听
                    mIsListening = true;
                    voiceBtn.setBackgroundResource(R.drawable.voice);  //设置按钮的背景

                } else {
                    //关闭开关
                    mCamera.stopListening(Camera.DEFAULT_AV_CHANNEL);    //停止监听
                    mIsListening = false;
                    voiceBtn.setBackgroundResource(R.drawable.no_voice);  //设置按钮的背景
                }
                break;
            case R.id.btn_videotape:  //录像

                /**
                 * 是否录像
                 * */
                if (videotape) {
                    videotape = false;
                    videotapeBtn.setBackgroundResource(R.drawable.stop);
                    Toast.makeText(this, "开始录像", Toast.LENGTH_SHORT).show();
                    mCamera.startListening(Camera.DEFAULT_AV_CHANNEL, true);
//                    mCamera.stopSpeaking(Camera.DEFAULT_AV_CHANNEL);
                    File rootFolder = new File(Environment
                            .getExternalStorageDirectory()
                            .getAbsolutePath()
                            + "/Record/");
                    File targetFolder = new File(
                            rootFolder.getAbsolutePath());
                    if (!rootFolder.exists()) {
                        try {
                            rootFolder.mkdir();
                        } catch (SecurityException se) {
                        }
                    }

                    if (!targetFolder.exists()) {
                        try {
                            targetFolder.mkdir();
                        } catch (SecurityException se) {
                        }
                    }
                    String path = "/sdcard/Record/" + calendar.getTime() + ".mp4";
                    mCamera.startRecording(path);
                } else {
                    videotape = true;
                    mCamera.stopListening(Camera.DEFAULT_AV_CHANNEL);
//                    mCamera.stopSpeaking(Camera.DEFAULT_AV_CHANNEL);
                    mCamera.stopRecording();
                    Toast.makeText(this, "录像已保存在本地SD中", Toast.LENGTH_SHORT).show();
                    videotapeBtn.setBackgroundResource(R.drawable.video);
                }

                break;

        }
    }


    private class MyIOTCListener implements IRegisterIOTCListener {
        //返回摄像头的画面
        @Override
        public void receiveFrameData(Camera camera, int i, Bitmap bitmap) {
            /**
             * 是否拍照
             * */
            if (photograph) {
                saveMyBitmap(bitmap, String.valueOf(calendar.getTime()));
                photograph = false;
            }
            if (!mIsListening) {
                mCamera.stopListening(Camera.DEFAULT_AV_CHANNEL);
            }
        }

        @Override
        public void receiveFrameDataForMediaCodec(Camera camera, int avChannel,
                                                  byte[] buf, int length, int pFrmNo,
                                                  byte[] pFrmInfoBuf, boolean isIframe, int codecId) {

        }

        //返回摄像头的的其他信息
        @Override
        public void receiveFrameInfo(Camera camera, int sessionChannel, long bitRate, int frameRate, int onlineNm, int frameCount, int incompleteFrameCount) {


        }

        //返回当前会话信息
        @Override
        public void receiveSessionInfo(Camera camera, int i) {

        }

        /**
         * 返回通道信息
         */
        @Override
        public void receiveChannelInfo(Camera camera, int channel, int responseCode) {
            Log.d("MyIOTCListener", "MyIOTCListener" + responseCode);
            mHandler.sendEmptyMessage(responseCode);  //更改主线程的UI
        }

        //返回指令结果信息
        @Override
        public void receiveIOCtrlData(Camera camera, int avChannel, int avIOCtrlMsgType, byte[] data) {
            Log.e("------", "avChannel:" + avChannel);
            Log.e("------", "avIOCtrlMsgType:" + avIOCtrlMsgType);
        }
    }

    /**
     * 照相（将传递过来的bitmap转换成手机图片）
     */
    public void saveMyBitmap(Bitmap mBitmap, String bitName) {
        File f = new File("/sdcard/" + bitName + ".jpg");
        try {
            FileOutputStream fOut = new FileOutputStream(f);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
            fOut.flush();
            fOut.close();

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        mHandler.sendEmptyMessage(666);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //释放资源断开连接
        mCamera.stopShow(Camera.DEFAULT_AV_CHANNEL);
        monitor.deattachCamera();
        mCamera.stopListening(Camera.DEFAULT_AV_CHANNEL);
//        mCamera.stopSpeaking(Camera.DEFAULT_AV_CHANNEL);
        mCamera.stopRecording();
        mCamera.stop(Camera.DEFAULT_AV_CHANNEL);
        mCamera.disconnect();
        mCamera.unregisterIOTCListener(myIOTCListener);
        Camera.uninit();
    }
}
