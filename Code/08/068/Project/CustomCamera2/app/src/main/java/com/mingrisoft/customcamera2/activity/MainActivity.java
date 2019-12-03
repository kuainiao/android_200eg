package com.mingrisoft.customcamera2.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.ImageReader;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.mingrisoft.customcamera2.R;
import com.mingrisoft.customcamera2.base.MPermissionsActivity;
import com.mingrisoft.customcamera2.listener.MineCaptureCallback;
import com.mingrisoft.customcamera2.listener.MineImageAvailableListener;
import com.mingrisoft.customcamera2.listener.MineSessionListener;
import com.mingrisoft.customcamera2.listener.MineStateCallback;
import com.mingrisoft.customcamera2.listener.MineTextureListener;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Author LYJ
 * Created on 2017/1/18.
 * Time 17:36
 */
public class MainActivity extends MPermissionsActivity{
    private TextureView preview;//显示预览图像
    private ImageButton takePicture;//获取照片
    private Size previewSize;//图像尺寸
    private CameraDevice cameraDevice;//相机设备
    private ImageReader imageReader;//用于读取图像数据
    /**
     * 用于显示图像的显示角度
     */
    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();

    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();//初始化控件
        addListener();//为控件添加监听
    }

    /**
     * 初始化控件
     */
    private void initView() {
        preview = (TextureView) findViewById(R.id.surface);
        takePicture = (ImageButton) findViewById(R.id.take_photo);
    }

    /**
     *为控件添加监听
     */
    private void addListener() {
        takePicture.setOnClickListener(takePictureListener);
    }

    //要申请的权限
    private String[] requestPermission = {
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };
    /**
     * TextureView的监听器-->重点展示图像
     */
    private MineTextureListener textureListener = new MineTextureListener() {
        @Override
        public void onSurfaceCreate(SurfaceTexture surface, int width, int height) {
            requestPermission(requestPermission, 1000);
        }
        @Override
        public boolean onSurfaceDestroyed(SurfaceTexture surface) {
            toRelease();//释放资源
            return super.onSurfaceDestroyed(surface);
        }
    };

    /**
     * 获取权限成功
     * @param requestCode
     */
    @Override
    public void permissionSuccess(int requestCode) {
        super.permissionSuccess(requestCode);
        if (requestCode == 1000) openCamera();
    }

    /**
     * 打开相机
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void openCamera() {
        Toast.makeText(MainActivity.this, "打开相机", Toast.LENGTH_SHORT).show();
        //通过CameraManager获取相机服务
        CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            String cameraId = manager.getCameraIdList()[0];//返回摄像机列表
            //获取指定相机的特性
            CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);
            //获取摄像头支持的流配置-->涉及图像的所有分辨率以及每一帧的信息
            StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            //获取最大的输出尺寸
            previewSize = map.getOutputSizes(SurfaceTexture.class)[0];
            //权限判断
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            //打开指定的相机
            manager.openCamera(cameraId, stateCallback, null);
            //创建ImageReader对象
            imageReader = ImageReader.newInstance(previewSize.getWidth(),preview.getHeight(), ImageFormat.JPEG,1);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取相机的状态
     */
    private MineStateCallback stateCallback = new MineStateCallback(){
        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onOpened(CameraDevice camera) {
            super.onOpened(camera);
            cameraDevice = camera;//获取摄像头
            startPreview();//开启预览
        }
    };

    //捕获请求
    private CaptureRequest.Builder requestBuilder;
    //预览的会话
    private CameraCaptureSession captureSession;
    //预览线程
    private HandlerThread previewThread;
    private Handler previewHandler;
    /**
     * 开启预览
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void startPreview() {
        if (cameraDevice == null){
            Log.e("startPreview", "cameraDevice is null");
            return;
        }
        //使用TextureView预览相机就是通过使用SurfaceTexture去实现的
        //与SurfaceView使用SurfaceHolder基本使差不多的
        SurfaceTexture texture = preview.getSurfaceTexture();
        if (texture == null){
            Log.e("startPreview", "surface is null");
            return;
        }

        //设置默认缓冲区的尺寸和相机所支持的最大尺寸一样
        texture.setDefaultBufferSize(previewSize.getWidth(), previewSize.getHeight());
        //开启预览所需要的输出Surface
        Surface surface = new Surface(texture);
        try {
            //创建预览请求
            requestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            requestBuilder.addTarget(surface);
            //创建预览会话
            cameraDevice.createCaptureSession(Arrays.asList(surface), new MineSessionListener() {
                @Override
                public void onSessionConfigured(CameraCaptureSession session) {

                    //获取预览会话
                    captureSession = session;
                    //配置预览时使用3A模式(自动曝光、自动对焦、自动白平衡)
                    requestBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
                    //创建用于预览界面的线程
                    previewThread = new HandlerThread("preview");
                    previewThread.start();
                    previewHandler = new Handler(previewThread.getLooper());
                    try {
                        //设置重复捕获图像
                        captureSession.setRepeatingRequest(requestBuilder.build(), null, previewHandler);
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }
            },null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    /*********************************************************************************
     *************************************分割线***************************************
     ********************************************************************************/

    /**
     * 按钮点击的监听器-->重点拍照
     */
    private View.OnClickListener takePictureListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            takePictureAndSave();
        }
    };

    /**
     * 获取照片并保存
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void takePictureAndSave() {
        List<Surface> outputSurfaces = new ArrayList<>(2);
        outputSurfaces.add(imageReader.getSurface());
        outputSurfaces.add(new Surface(preview.getSurfaceTexture()));
        requestBuilder.addTarget(imageReader.getSurface());
        requestBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
        // 设置保存图像的方向
        int rotation = getWindowManager().getDefaultDisplay().getRotation();
        requestBuilder.set(CaptureRequest.JPEG_ORIENTATION, ORIENTATIONS.get(rotation));
        //保存图片的路径
        final File saveFile = new File(Environment.getExternalStorageDirectory()
                + "/DCIM", "MRKJ" + System.currentTimeMillis() + ".jpg");
        //设置图像变化的监听
        imageReader.setOnImageAvailableListener(new MineImageAvailableListener(saveFile,previewHandler),previewHandler);
        try {
            //创建拍照会话
            cameraDevice.createCaptureSession(outputSurfaces, new MineSessionListener() {
                @Override
                public void onSessionConfigured(CameraCaptureSession session) {
                    try {
                        session.capture(requestBuilder.build(), new MineCaptureCallback() {
                            @Override
                            public void onCompleted() {
                                Toast.makeText(MainActivity.this,
                                        saveFile.getPath(), Toast.LENGTH_SHORT).show();
                                startPreview();//开启预览
                            }
                        }, previewHandler);
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }
            }, previewHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    /*********************************************************************************
     *************************************分割线***************************************
     ********************************************************************************/


    /**
     * 界面运行中开启相关操作
     */
    @Override
    protected void onResume() {
        super.onResume();
        if (!takePicture.isActivated()){
            preview.setSurfaceTextureListener(textureListener);
        }
    }

    /**
     * 释放资源
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void toRelease(){
        try {
            //关闭会话
            if (null != captureSession) {
                captureSession.close();
                captureSession = null;
            }
            //关闭摄像头
            if (null != cameraDevice) {
                cameraDevice.close();
                cameraDevice = null;
            }
            //关闭图像阅读器
            if (null != imageReader) {
                imageReader.close();
                imageReader = null;
            }
            //退出线程
            previewThread.quitSafely();
            previewThread.join();
            previewThread = null;
            previewHandler = null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
