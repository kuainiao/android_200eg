package com.mingrisoft.customcamera2.listener;

import android.annotation.TargetApi;
import android.media.ImageReader;
import android.os.Build;
import android.os.Handler;

import com.mingrisoft.customcamera2.utils.SaveImageToLocal;

import java.io.File;

/**
 * Author LYJ
 * Created on 2017/1/19.
 * Time 15:05
 * 用来获取照片的数据
 */

@TargetApi(Build.VERSION_CODES.KITKAT)
public class MineImageAvailableListener implements ImageReader.OnImageAvailableListener{
    private File file;
    private  Handler mHandler;
    public MineImageAvailableListener(File file, Handler handler) {
        this.file = file;
        this.mHandler =handler;
    }
    @Override
    public void onImageAvailable(ImageReader reader) {
        mHandler.post(new SaveImageToLocal(reader.acquireNextImage(),file));
    }
}
