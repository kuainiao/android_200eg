package com.mingrisoft.customcamera2.listener;

import android.graphics.SurfaceTexture;
import android.util.Log;
import android.view.TextureView;

/**
 * 用于显示图像的预览
 * Author LYJ
 * Created on 2017/1/18.
 * Time 17:44
 */

public abstract class MineTextureListener implements TextureView.SurfaceTextureListener{
    @Override
    public final void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        Log.w("MineTextureListener", "onSurfaceTextureAvailable: ");
        onSurfaceCreate(surface,width,height);
    }

    @Override
    public final void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
        Log.w("MineTextureListener", "onSurfaceTextureSizeChanged: ");
    }

    @Override
    public final boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        Log.w("MineTextureListener", "onSurfaceTextureDestroyed: ");
        return onSurfaceDestroyed(surface);
    }

    @Override
    public final void onSurfaceTextureUpdated(SurfaceTexture surface) {
        Log.w("MineTextureListener", "onSurfaceTextureUpdated: ");
    }

    /**
     * 这里只需用到onSurfaceTextureAvailable()
     * -->相当于SurfaceView纹理创建时调用的Create()
     */
    public abstract void onSurfaceCreate(SurfaceTexture surface, int width, int height);

    /**
     * 当纹理被销毁时调用-->与SurfaceView也有相似之处
     * @param surface
     */
    public boolean onSurfaceDestroyed(SurfaceTexture surface){
        return false;
    }
}
