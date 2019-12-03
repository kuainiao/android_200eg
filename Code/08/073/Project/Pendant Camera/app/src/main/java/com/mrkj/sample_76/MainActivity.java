package com.mrkj.sample_76;

import android.Manifest;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.iflytek.cloud.FaceDetector;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.util.Accelerometer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 人脸检测(这里用的是科大讯飞的人脸识别)
 */
public class MainActivity extends MPermissionsActivity implements View.OnClickListener {
    private final static String TAG = MainActivity.class.getSimpleName();
    private SurfaceView mPreviewSurface;//用于显示预览图像
    private SurfaceView mFaceSurface;//用于绘制检测人脸的返回信息
    private SurfaceHolder mSurfaceHolder;//纹理控制器
    private Camera mCamera;//摄像头对象
    private int mCameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;//设置为默认开启前置摄像头
    // 默认设置640*480,截至目前也只是支持640*480
    private int PREVIEW_WIDTH = 640;
    private int PREVIEW_HEIGHT = 480;
    // 预览帧数据存储数组和缓存数组
    private byte[] nv21;
    private byte[] buffer;
    // 缩放矩阵
    private Matrix mScaleMatrix = new Matrix();
    // 加速度感应器，用于获取手机的朝向
    private Accelerometer accelerometer;
    private FaceDetector mFaceDetector;//调用讯飞的SDK来实现人脸识别
    private boolean stop;//人脸检测开关
    private List<Bitmap> bitmapList;//设置显示的挂件
    private ImageButton cryImage, catImage, glassesImage, hatImage, beardImage;


    private boolean frontCamera;
    private FaceRect faceTwo;
    private Canvas canvas;
    private int typeBtn = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SpeechUtility.createUtility(this, "appid=" + "5833f456"); //设置AppKey用于注册,AppID
        initView();//初始化布局
        initData();//初始化数据
        //申请相机权限
        requestPermission(new String[]{Manifest.permission.CAMERA}, 1000);
    }

    //    @Override
//    public void permissionSuccess(int requestCode) {
//        super.permissionSuccess(requestCode);
//        if (requestCode == 1000) {
////            Toast.makeText(MainActivity.this,"获取文件写入权限成功！",Toast.LENGTH_SHORT).show();
//        }
//        if (requestCode == 2000) {
//            openCamera();//打开摄像头
//        }
//    }
//相机权限申请完成后，调用权限的回调方法打开摄像头
    @Override
    public void onRequestPermissionsResult
    (int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        openCamera();           //打开摄像头
    }

    /**
     * 用于显示摄像头拍摄到的图像
     */
    private SurfaceHolder.Callback mPreviewCallback = new SurfaceHolder.Callback() {

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {

            closeCamera();//关闭摄像头，并释放资源
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            openCamera();   //打开相机
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            mScaleMatrix.setScale(width / (float) PREVIEW_HEIGHT,
                    height / (float) PREVIEW_WIDTH);//设置缩放比例
        }
    };

    /**
     * 设置纹理尺寸
     */
    private void setSurfaceSize() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels;
        int height = (int) (width * PREVIEW_WIDTH / (float) PREVIEW_HEIGHT);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, height);
        params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        mPreviewSurface.setLayoutParams(params);
    }

    //返回界面时开启摄像头，用于锁屏时解锁后开启摄像头
    @Override
    protected void onRestart() {
        super.onRestart();
        openCamera();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        nv21 = new byte[PREVIEW_WIDTH * PREVIEW_HEIGHT * 2];
        buffer = new byte[PREVIEW_WIDTH * PREVIEW_HEIGHT * 2];
        accelerometer = new Accelerometer(this);
        mFaceDetector = FaceDetector.createDetector(this, null);//实例化人脸检测对象
    }

    /**
     * 初始化控件
     */
    private void initView() {
        mPreviewSurface = (SurfaceView) findViewById(R.id.preview);
        mFaceSurface = (SurfaceView) findViewById(R.id.face);
        catImage = (ImageButton) findViewById(R.id.cat_image);
        cryImage = (ImageButton) findViewById(R.id.cry_image);
        hatImage = (ImageButton) findViewById(R.id.hat_image);
        glassesImage = (ImageButton) findViewById(R.id.glasses_image);
        beardImage = (ImageButton) findViewById(R.id.beard_image);
        catImage.setOnClickListener(this);
        cryImage.setOnClickListener(this);
        hatImage.setOnClickListener(this);
        glassesImage.setOnClickListener(this);
        beardImage.setOnClickListener(this);
        mPreviewSurface.getHolder().addCallback(mPreviewCallback);
        mSurfaceHolder = mFaceSurface.getHolder();
        mSurfaceHolder.setFormat(PixelFormat.TRANSPARENT);
        mFaceSurface.setZOrderOnTop(true);

        setSurfaceSize();
    }

    /**
     * 打开摄像头
     */
    private void openCamera() {
        if (mCamera != null) {
            return;
        }
        try {
            mCamera = Camera.open(mCameraId);
            if (Camera.CameraInfo.CAMERA_FACING_FRONT == mCameraId) {
                Toast.makeText(this, "前置摄像头已开启，点击可切换", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "后置摄像头已开启，点击可切换", Toast.LENGTH_SHORT).show();
            }
            setCameraDisplayOrientation(this, mCameraId, mCamera);//设置摄像头的预览方向
        } catch (Exception e) {
            e.printStackTrace();
            closeCamera();
            return;
        }
        //获取摄像头参数对象
        Camera.Parameters params = mCamera.getParameters();
        //设置预览的格式
        params.setPreviewFormat(ImageFormat.NV21);
        //设置预览的分辨率，这里设置640*480，到目前为止只支持该分辨率的人脸检测
        params.setPreviewSize(PREVIEW_WIDTH, PREVIEW_HEIGHT);
        //给摄像头设置参数配置
        mCamera.setParameters(params);
        //给摄像头设置预览回到，这里使用的Lambda表达式代表的只有一个回调函数的匿名内部类
        mCamera.setPreviewCallback((data, camera) -> System.arraycopy(data, 0, nv21, 0, data.length));
        try {
            mCamera.setPreviewDisplay(mPreviewSurface.getHolder());
            mCamera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 释放摄像头资源
     */
    private void closeCamera() {
        if (null != mCamera) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    /***
     * 加载bitma图片
     */
    @Override
    protected void onStart() {
        super.onStart();
        bitmapList = new ArrayList<>();
        Bitmap a = BitmapFactory.decodeResource(getResources(), R.mipmap.note);
        Bitmap b = BitmapFactory.decodeResource(getResources(), R.mipmap.lefteye);
        Bitmap c = BitmapFactory.decodeResource(getResources(), R.mipmap.righteye);
        Bitmap d = BitmapFactory.decodeResource(getResources(), R.mipmap.cattop);
        Bitmap e = BitmapFactory.decodeResource(getResources(), R.mipmap.catearleft);
        Bitmap f = BitmapFactory.decodeResource(getResources(), R.mipmap.catearright);
        Bitmap g = BitmapFactory.decodeResource(getResources(), R.mipmap.hat);
        Bitmap h = BitmapFactory.decodeResource(getResources(), R.mipmap.beard);
        Bitmap j = BitmapFactory.decodeResource(getResources(), R.mipmap.glasses);
        bitmapList.add(0, a);
        bitmapList.add(1, b);
        bitmapList.add(2, c);
        bitmapList.add(3, d);
        bitmapList.add(4, e);
        bitmapList.add(5, f);
        bitmapList.add(6, g);
        bitmapList.add(7, h);
        bitmapList.add(8, j);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (null != accelerometer) {
            accelerometer.start();
        }
        stop = false;
        new Thread(() -> {
            while (!stop) {
                if (null == nv21) {
                    continue;
                }
                synchronized (nv21) {
                    System.arraycopy(nv21, 0, buffer, 0, nv21.length);
                }
                int direction = Accelerometer.getDirection();// 获取手机朝向
                frontCamera = (Camera.CameraInfo.CAMERA_FACING_FRONT == mCameraId);
                if (frontCamera) {
                    direction = (4 - direction) % 4;// 0,1,2,3,4分别表示0,90,180,270和360度
                }
                String result = mFaceDetector.trackNV21(
                        buffer, PREVIEW_WIDTH, PREVIEW_HEIGHT, 1, direction);//获取人脸检测结果
                FaceRect face = Result.result(result);//获取返回的数据
                faceTwo = new FaceRect();
                faceTwo = face;
                Log.e(TAG, "result:" + result);//输出检测结果,该结果为JSON数据
                canvas = mSurfaceHolder.lockCanvas();//锁定画布用于绘制
                if (null == canvas) {
                    continue;
                }
                canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);//清除之前的绘制图像
                canvas.setMatrix(mScaleMatrix);
                if (face == null) {
                    mSurfaceHolder.unlockCanvasAndPost(canvas);
                    continue;
                }
                if (face != null) {

                    face.bound = DrawFaceRect.RotateDeg90(face.bound, PREVIEW_HEIGHT);//绘制人脸的区域
                    if (face.point != null) {//绘制脸上关键点
                        for (int i = 0; i < face.point.length; i++) {
                            face.point[i] = DrawFaceRect.RotateDeg90(face.point[i], PREVIEW_HEIGHT);
                        }
//                        //绘制人脸检测的区域
                        DrawFaceRect.drawFaceRect(typeBtn, bitmapList, canvas, face, PREVIEW_WIDTH, frontCamera);
                    }
                } else {
                    Log.e(TAG, "没有检测出人脸");
                }
                mSurfaceHolder.unlockCanvasAndPost(canvas);
            }
        }).start();
    }


    @Override
    protected void onPause() {
        super.onPause();
        closeCamera();
        if (null != accelerometer) {
            accelerometer.stop();
        }
        stop = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 销毁对象
        mFaceDetector.destroy();
    }

    /**
     * 设置摄像头图像的预览角度
     *
     * @param activity
     * @param cameraId
     * @param camera
     */
    public static void setCameraDisplayOrientation(
            Activity activity, int cameraId, android.hardware.Camera camera) {
        android.hardware.Camera.CameraInfo info =
                new android.hardware.Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay()
                .getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        camera.setDisplayOrientation(result);
    }

    /***
     * 点击不同的按钮执行不同的操作
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cat_image:
                if (typeBtn != 2) {
                    typeBtn = 2;
                    if (faceTwo != null) {
                        //绘制人脸检测的区域
                        DrawFaceRect.drawFaceRect(typeBtn, bitmapList, canvas, faceTwo, PREVIEW_WIDTH, frontCamera);
                    }else {
                        Toast.makeText(this, "没有获取到面部信息！", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.cry_image:
                if (typeBtn != 1) {
                    typeBtn = 1;
                    if (faceTwo != null) {
                        //绘制人脸检测的区域
                        DrawFaceRect.drawFaceRect(typeBtn, bitmapList, canvas, faceTwo, PREVIEW_WIDTH, frontCamera);
                    }else {
                        Toast.makeText(this, "没有获取到面部信息！", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.hat_image:
                if (typeBtn != 3) {
                    typeBtn = 3;
                    if (faceTwo != null) {
                        //绘制人脸检测的区域
                        DrawFaceRect.drawFaceRect(typeBtn, bitmapList, canvas, faceTwo, PREVIEW_WIDTH, frontCamera);
                    }else {
                        Toast.makeText(this, "没有获取到面部信息！", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.glasses_image:
                if (typeBtn != 4) {
                    typeBtn = 4;
                    if (faceTwo != null) {
                        //绘制人脸检测的区域
                        DrawFaceRect.drawFaceRect(typeBtn, bitmapList, canvas, faceTwo, PREVIEW_WIDTH, frontCamera);
                    }else {
                        Toast.makeText(this, "没有获取到面部信息！", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.beard_image:
                if (typeBtn != 5) {
                    typeBtn = 5;
                    if (faceTwo != null) {
                        //绘制人脸检测的区域
                        DrawFaceRect.drawFaceRect(typeBtn, bitmapList, canvas, faceTwo, PREVIEW_WIDTH, frontCamera);
                    }else {
                        Toast.makeText(this, "没有获取到面部信息！", Toast.LENGTH_SHORT).show();
                    }
                }
                break;


        }
    }
}
