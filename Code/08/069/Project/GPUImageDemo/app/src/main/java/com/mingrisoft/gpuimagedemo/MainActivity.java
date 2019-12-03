package com.mingrisoft.gpuimagedemo;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.Surface;

import jp.co.cyberagent.android.gpuimage.GPUImage;
import jp.co.cyberagent.android.gpuimage.GPUImageFilter;

public class MainActivity extends MPermissionsActivity {
    private GPUImage mGPUImage; //用于展示滤镜的效果
    private GPUImageFilter mFilter;//滤镜
    private Camera camera;//摄像头对象
    private int orientation;//图像的显示角度
    private int cameraID = Camera.CameraInfo.CAMERA_FACING_BACK;//摄像ID代表前置摄像头或后置摄像头

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //获取GPUImage对象
        mGPUImage = new GPUImage(this);
        //设置预览
        mGPUImage.setGLSurfaceView((GLSurfaceView) findViewById(R.id.surfaceView));
        //设置点击事件，用于打开具有滤镜信息的滤镜列表
        findViewById(R.id.button_choose_filter).setOnClickListener(
                v -> SelectFilter.showDialog(MainActivity.this,
                        filter -> switchFilterTo(filter)));
    }

    @Override
    public void permissionSuccess(int requestCode) {
        super.permissionSuccess(requestCode);
        if (requestCode == 1000){
            setCameraPreView();
        }
    }

    /**
     * 设置滤镜
     *
     * @param filter 用于设置滤镜的效果
     */
    private void switchFilterTo(final GPUImageFilter filter) {
        if (mFilter == null
                || (filter != null && !mFilter.getClass().equals(filter.getClass()))) {
            mFilter = filter;//获取滤镜对象
            mGPUImage.setFilter(mFilter);//设置选择的滤镜
        }
    }

    /**
     * 开启预览
     */
    private void setCameraPreView() {
        if (checkCameraHardware()) {
            camera = getCameraInstance();//获取摄像头
            setCameraDisplayOrientation(this, cameraID, camera);//设置摄像角度
            mGPUImage.setUpCamera(camera, orientation, false, false);//设置图像的预览效果
        }
    }

    /**
     * 检查设备的摄像头是否可用
     *
     * @return 返回true则摄像头可用
     */
    private boolean checkCameraHardware() {
        return this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    /**
     * 获取摄像头对象
     *
     * @return
     */
    private Camera getCameraInstance() {
        Camera camera = null;
        try {
            camera = Camera.open(cameraID);
        } catch (Exception e) {
        }
        return camera;
    }

    /**
     * 设置图像的预览方向（官方推荐方法）
     *
     * @param activity
     * @param cameraId
     * @param camera
     */
    private void setCameraDisplayOrientation(
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
            result = (360 - result) % 360;
        } else {
            result = (info.orientation - degrees + 360) % 360;
        }
        this.orientation = result;
        camera.setDisplayOrientation(result);
    }

    /**
     * 程序运行时开启摄像头
     */
    @Override
    protected void onResume() {
        super.onResume();
        requestPermission(new String[]{Manifest.permission.CAMERA},1000);
    }
    /**
     * 界面失去焦点后释放资源
     */
    @Override
    protected void onPause() {
        super.onPause();
        camera.setPreviewCallback(null);
        camera.release();
        camera = null;
    }
}
