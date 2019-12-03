package com.mingrisoft.customcamera2.listener;

import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.os.Build;
import android.support.annotation.RequiresApi;

/**
 * Author LYJ
 * Created on 2017/1/19.
 * Time 15:11
 * 创建会话的回调
 */

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public abstract class MineCaptureCallback extends CameraCaptureSession.CaptureCallback {
    @Override
    public final void onCaptureCompleted(CameraCaptureSession session, CaptureRequest request, TotalCaptureResult result) {
        super.onCaptureCompleted(session, request, result);
        onCompleted();
    }

    public abstract void onCompleted();
}
