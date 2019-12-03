package com.mingrisoft.cutiv;

import android.Manifest;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends MPermissionsActivity {
    public static final int SHOW_PICTURE = 2;
    private ImageView picture;
    //图片bitmap
    private Bitmap bm1 = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestPermission(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0x0001);

    }

    public void into() {
        picture = (ImageView) findViewById(R.id.picture);

        picture.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                //此处调用了图片选择器
                //如果直接写intent.setDataAndType("image/*");调用的是系统图库
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                //开启意图
                startActivityForResult(intent, SHOW_PICTURE);
            }
        });
    }
    //接收回调信息
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case SHOW_PICTURE:
                if (data != null) {//判断数据是否为空
                    Uri url1 = data.getData();        //获得图片的uri
                    try {
//                    bm1 = MediaStore.Images.Media.getBitmap(resolver, url1);
                        String[] proj = {MediaStore.Images.Media.DATA};
                        //好像是Android多媒体数据库的封装接口，具体的看Android文档
                        Cursor cursor = managedQuery(url1, proj, null, null, null);
                        //按我个人理解 这个是获得用户选择的图片的索引值
                        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                        //将光标移至开头 ，这个很重要，不小心很容易引起越界
                        cursor.moveToFirst();
                        //最后根据索引值获取图片路径
                        String path = cursor.getString(column_index);
                        bm1 = Bimp.revitionImageSize(path);//获取bitmap
                        //设置图片
                        picture.setImageBitmap(bm1);
                        //设置图片占满控件
                        picture.setScaleType(ImageView.ScaleType.FIT_XY);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            default:
                break;
        }
    }

    /**
     * 权限成功回调函数
     *
     * @param requestCode
     */
    public void permissionSuccess(int requestCode) {
        super.permissionSuccess(requestCode);
        switch (requestCode) {
            case 0x0001:
                into();
                break;
        }

    }
}
