package com.mingrisoft.splashscreentwo.loader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import com.android.volley.toolbox.ImageLoader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by dllo on 16/1/19.
 */
public class DiskLruCacheLoad implements ImageLoader.ImageCache{
    private String cacheDir = "";
    public DiskLruCacheLoad() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            //获取文件的根目录
            File root = Environment.getExternalStorageDirectory();
            //将文件存入到这个文件夹里
            File cacheFile = new File(root.getAbsolutePath() + "/cache");
            //如果没有这个文件夹
            if (!cacheFile.exists()) {
                //创建文件夹
                cacheFile.mkdir();
            }
            cacheDir = cacheFile.getAbsolutePath();
        }
    }
    @Override
    public Bitmap getBitmap(String url) {
        if (!url.contains(".png") && !url.contains(".jpg") && !url.contains("jpeg")){
           return null;
        }
        //截取url，从“／”开始截取到“.”加上后买呢4个字节
        url = url.substring(url.lastIndexOf("/"), url.lastIndexOf(".") + 4);
        //从图片工厂取出图片
        return BitmapFactory.decodeFile(cacheDir + "/" + url);
    }
    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        if (!url.contains(".png") && !url.contains(".jpg") && !url.contains("jpeg")){
            return;
        }
        //截取url，从“／”开始截取到“.”加上后买呢4个字节
        url = url.substring(url.lastIndexOf("/"), url.lastIndexOf(".") + 4);
        FileOutputStream fos = null;
        //  读流存储图片
        try {
            fos = new FileOutputStream(cacheDir+"/"+url);
            bitmap.compress(Bitmap.CompressFormat.PNG,100,fos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }finally {
            //最后进行关流操作
            if (null != fos){
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
