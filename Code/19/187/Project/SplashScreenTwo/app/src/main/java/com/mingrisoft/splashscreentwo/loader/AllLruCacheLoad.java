package com.mingrisoft.splashscreentwo.loader;

import android.graphics.Bitmap;

import com.android.volley.toolbox.ImageLoader;

/**
 * Created by dllo on 16/1/19.
 */
public class AllLruCacheLoad implements ImageLoader.ImageCache {
    private DiskLruCacheLoad diskLruCacheLoad;
    private LruCacheLoad lruCacheLoad;
    public AllLruCacheLoad() {
        diskLruCacheLoad = new DiskLruCacheLoad();
        lruCacheLoad = new LruCacheLoad();
    }
    @Override
    public Bitmap getBitmap(String url) {
        Bitmap bitmap = diskLruCacheLoad.getBitmap(url);
        if (null == diskLruCacheLoad){
            bitmap =  lruCacheLoad.getBitmap(url);
        }
        return bitmap;
    }
    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        lruCacheLoad.putBitmap(url, bitmap);
        diskLruCacheLoad.putBitmap(url, bitmap);
    }
}
