package com.mingrisoft.systemcamera;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private TextView txt;//声明提示文字控件
    private ImageView image;//声明图片控件
    private Button btn;//声明按钮控件
    private File file;//用于储存图片文件对象

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*
        获取控件对象
         */
        txt = (TextView) findViewById(R.id.txt);//获取文本控件对象
        image = (ImageView) findViewById(R.id.show_image);//回去图片控件对象
        btn = (Button) findViewById(R.id.button);//获取按钮控件对象
       /*
       设置点击事件
        */
        btn.setOnClickListener(v -> {
            //如果版本大>=API23
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                //检查权限
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    //进入到这里代表没有权限.
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        AlertDialog.Builder alert = new AlertDialog.Builder(this);
                        alert.setTitle("重要提示");
                        alert.setMessage("该权限用于SD卡的读写操作，如果取消将影响程序的功能使用！");
                        alert.setPositiveButton("我知道了", (dialog, which) -> applyForPermission());
                        alert.create();
                        alert.show();
                    } else {
                        applyForPermission();//申请权限
                    }
                } else {
                    setDialog();//设置弹窗
                }
            } else {
                setDialog();//设置弹窗
            }
        });
    }

    /**
     * 申请权限
     */
    private void applyForPermission() {
        //申请权限
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                1000);
    }

    /**
     * 创建提示弹窗
     */
    private void setDialog() {
        //创建提示窗
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("提示");//设置弹窗的标题
        alertDialog.setMessage("是否使用系统相机进行拍照？");//设置弹窗的内容
        alertDialog.setPositiveButton("确定",//设置确定按钮的点击事件
                (dialog, which) -> {
                    Toast.makeText(this, "点击了确认", Toast.LENGTH_SHORT).show();
                    getPictureFromCamera();//调用方法打开系统摄像头拍照
                });
        alertDialog.setNeutralButton("取消",//设置取消按钮的点击事件
                (dialog, which) -> Toast.makeText(this, "点击了取消", Toast.LENGTH_SHORT).show());
        alertDialog.create();//创建弹窗
        alertDialog.show();//显示弹窗
    }

    /**
     * 调用系统摄像头拍照
     */
    private void getPictureFromCamera() {
        // 判断存储卡是否可以用，不可用跳出方法并提示
        if (!hasSdcard()) {
            Toast.makeText(this, "储存卡不可用", Toast.LENGTH_SHORT).show();
            return;
        }
        //如果储存卡可用则执行下面的方法接调用系统摄像头设备
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");//调用系统摄像头的意图
        file = new File(Environment.getExternalStorageDirectory(), "my_image");//设置图片保存的位置
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));//向意图中传值
        startActivityForResult(intent, 1000);//设置带有返回值的跳转界面方法，请求值设为1000
    }

    /**
     * 判断SD卡是否可用
     *
     * @return
     */
    private boolean hasSdcard() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 返回当前界面
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //如果返回resultCode不等于RESULT_OK，跳出方法不再执行下列的操作
        if (resultCode != RESULT_OK) {
            return;
        }
        //如果等于，则对请求值进行判断，区分是调用系统摄像头，还是调用系统裁剪图片
        switch (requestCode) {
            case 1000://调用系统摄像头返回
                crop(Uri.fromFile(file));//调用裁剪方法
                break;
            case 2000://调用系统裁剪返回
                image.setImageBitmap(BitmapFactory.decodeFile(file.getPath()));
                txt.setText("图片的保存路径【" + file.getAbsolutePath() + "】");
                break;
            default:
                break;
        }
    }

    /**
     * 调用系统裁剪图片
     * @param uri
     */
    private void crop(Uri uri) {
        Log.e("URI", uri.getPath());
        Log.e("URI", uri.toString());
        // 裁剪图片意图
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // 裁剪框的比例，1：1
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // 裁剪后输出图片的尺寸大小
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("scale", true);//黑边
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        // 图片格式
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);// 取消人脸识别
        intent.putExtra("return-data", true);// true:不返回uri，false：返回uri
        startActivityForResult(intent, 2000);
    }

    /**
     * 请求权限的返回
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1000) {
            Log.i("申请权限个数", grantResults.length + "");
            //如果申请权限成功
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setDialog();
            } else {
                Toast.makeText(this, "未获得权限", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
