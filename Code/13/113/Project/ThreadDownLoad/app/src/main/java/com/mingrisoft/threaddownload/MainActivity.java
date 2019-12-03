package com.mingrisoft.threaddownload;

import android.Manifest;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MainActivity extends MPermissionsActivity {

    private ProgressBar bar;//进度条
    private ImageView image;//文字
    private int count;//标记
    private boolean isDownLoad;//是否正在下载
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case 100://更改界面效果
                    bar.setVisibility(View.VISIBLE);
                    image.setVisibility(View.VISIBLE);
                    break;
                case 200://更新进度条进度
                    int progress = (int) msg.obj;
                    if (progress == 100){
                        Toast.makeText(MainActivity.this, "下载完成！", Toast.LENGTH_SHORT).show();
                    }
                    bar.setProgress(progress);
                    break;
                case 300://下载完成
                    isDownLoad = false;
                    Toast.makeText(MainActivity.this, "下载完成", Toast.LENGTH_SHORT).show();
                    break;
            }
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bar = (ProgressBar) findViewById(R.id.progress_bar);
        image = (ImageView) findViewById(R.id.image_qq);
    }

    public void down(View view) {
        requestPermission(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1000);
    }

    @Override
    public void permissionSuccess(int requestCode) {
        super.permissionSuccess(requestCode);
        //判断请求码是否等于1000
        if (requestCode == 1000) {
            if (isDownLoad){
                Toast.makeText(this,"文件正被下载中！",Toast.LENGTH_SHORT).show();
                return;
            }
            isDownLoad = true;//设置为下载中
            count = 0;//初始化计数器
            //获取下载器
            final DownLoadManger manger = new DownLoadManger("http://www.imooc.com/mobile/mukewang.apk");
            //开启子线程
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //开启下载文件功能
                    manger.downLoadFileTest();
                    handler.sendEmptyMessage(100);
                }
            }).start();
            //获取下载进度
            manger.setProgessListener(new DownLoadManger.Progress() {
                //初始化sum
                float sum = 0;
                @Override
                public void get(int size, int all) {
                    sum += size;//累加
                    //获取最接近的整数
                    int progress = Math.round(sum / (float) all * 100);
                    if (progress == 100){
                        count += 1;//计数器自增
                        handler.sendEmptyMessage(300);
                    }
                    if (count <= 1){
                        //更新进度条
                        Log.i("progress", ""+progress);
                        handler.obtainMessage(200,progress).sendToTarget();
                    }
                }
            });
        }
    }
}
