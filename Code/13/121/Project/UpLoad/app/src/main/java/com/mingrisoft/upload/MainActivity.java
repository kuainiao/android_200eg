package com.mingrisoft.upload;

import android.Manifest;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;

import com.liji.takephoto.TakePhoto;

import java.io.File;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends MPermissionsActivity implements TakePhoto.onPictureSelected{
    private final String TAG = "MainActivity";
    private CircleImageView iamge;//显示头像
    private final int requestCode = 1000;//请求码
    private TakePhoto takePhoto;//用来获取图片
    private OkHttpClient okHttpClient;
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == 1){
                String path = (String) msg.obj;
                //显示更改后的图像
                iamge.setImageBitmap(BitmapFactory.decodeFile(path));
            }
            return false;
        }
    });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iamge = (CircleImageView) findViewById(R.id.head);
        takePhoto = new TakePhoto(this);//实例化对象
        takePhoto.setOnPictureSelected(this);//设置回调
        //创建网络请求对象
        okHttpClient = new OkHttpClient.Builder().build();
    }

    private String [] request ={Manifest.permission.WRITE_EXTERNAL_STORAGE};
    /**
     * 点击事件
     * @param view
     */
    public void changeHead(View view){
        requestPermission(request,requestCode);
    }

    /**
     * 权限请求成功
     * @param requestCode
     */
    @Override
    public void permissionSuccess(int requestCode) {
        super.permissionSuccess(requestCode);
        if (requestCode == this.requestCode){
            takePhoto.show();//显示
        }
    }

    /**
     * 获取到图片
     * @param path
     */
    @Override
    public void select(final String path) {
        Log.i(TAG, "select: "+path);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                    handler.obtainMessage(1,path).sendToTarget();
                } catch (InterruptedException e) {

                }

            }
        }).start();

//        upload(path);-->只差后台数据接口
    }

    /**
     * 上传
     */
    private void upload(final String path) {
        File file = new File(path);//获取文件
        RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
        RequestBody requestBody = new MultipartBody.Builder()//表单
                .setType(MultipartBody.FORM)//设置类型
                .addPart(Headers.of(
                        "Content-Disposition",
                        "form-data; name=\"???\""),
                        RequestBody.create(null, "???"))
                .addPart(Headers.of(
                        "Content-Disposition",
                        "form-data; name=\"???\"; filename=\"image.jpg\""), fileBody)
                .build();//构建
        Request request = new Request.Builder()//创建请求
                .url("url")//添加请求链接
                .post(requestBody)//添加请求体
                .build();//构建请求
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //上传成功后将发送消息让界面中图片刷新
                handler.obtainMessage(1,path).sendToTarget();
            }
        });
    }
}
