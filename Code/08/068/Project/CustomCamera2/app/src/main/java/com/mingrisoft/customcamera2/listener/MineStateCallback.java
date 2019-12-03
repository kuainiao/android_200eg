package com.mingrisoft.customcamera2.listener;

import android.hardware.camera2.CameraDevice;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

/**
 * 返回摄像头的状态
 * Author LYJ
 * Created on 2017/1/18.
 * Time 18:46
 */

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class MineStateCallback extends CameraDevice.StateCallback {
    @Override
    public void onOpened(CameraDevice camera) {
        Log.w("MineStateCallback", "onOpened: ");
    }

    @Override
    public void onDisconnected(CameraDevice camera) {
        Log.w("MineStateCallback", "onDisconnected: ");
    }

    @Override
    public void onError(CameraDevice camera, int error) {
        Log.w("MineStateCallback", "onError: ");
        switch (error){
            case ERROR_CAMERA_DEVICE:
                Log.w("MineStateCallback", " indicating that the camera device has encountered a fatal error");
                break;
            case ERROR_CAMERA_DISABLED:
                Log.w("MineStateCallback", " indicating that the camera device could not be opened due to a device policy");
                break;
            case ERROR_CAMERA_IN_USE:
                Log.w("MineStateCallback", " indicating that the camera device is in use already");
                break;
            case ERROR_CAMERA_SERVICE:
                Log.w("MineStateCallback", " indicating that the camera service has encountered a fatal error");
                break;
            case ERROR_MAX_CAMERAS_IN_USE:
                Log.w("MineStateCallback", " indicating that the camera device could not be opened because there are too many other open camera devices");
                break;
        }
    }
}
