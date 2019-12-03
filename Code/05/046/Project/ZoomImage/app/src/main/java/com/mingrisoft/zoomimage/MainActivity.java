package com.mingrisoft.zoomimage;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private ZoomImageView zoomImg;
    private Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        zoomImg = (ZoomImageView) findViewById(R.id.image);
        bitmap = BitmapFactory.decodeResource(this.getResources(), R.mipmap.c); //添加图片文件
        zoomImg.setImage(bitmap);  //把图片传给ZoomImageView
    }
}
