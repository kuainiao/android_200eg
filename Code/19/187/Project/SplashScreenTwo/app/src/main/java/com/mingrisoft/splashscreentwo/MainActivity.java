package com.mingrisoft.splashscreentwo;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.mingrisoft.splashscreentwo.loader.AllLruCacheLoad;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button nextBtn;
    private TextView timeTv;
    private NetworkImageView imageView;
    private Timer timer;
    int i = 5;
    private String url = "";
    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        nextBtn = (Button) findViewById(R.id.next_btn);
        nextBtn.setOnClickListener(this);
        timeTv = (TextView) findViewById(R.id.time_tv);
        imageView = (NetworkImageView) findViewById(R.id.splash_image);
        queue = Volley.newRequestQueue(this);      //初始化单例列队
        AllLruCacheLoad cache = new AllLruCacheLoad();   //初始化缓存
        final ImageLoader imageloader = new ImageLoader(queue, cache);  //初始化缓存
        imageView.setImageUrl(url, imageloader);     //开始加载图片
        imageView.setDefaultImageResId(R.mipmap.bj);    //加载等待过程中的显示的图片

        timer = new Timer();       //初始化计时器
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Message message = new Message();  //初始化message
                message.what = 1;
                message.obj = i;                //将剩余的计时时间传给handler
                handler.sendMessage(message);  //发送消息给handler
                i--;  //开始倒计时
            }
        }, 0, 1000);  //每个1s执行一次该方法
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.next_btn:
                Intent intent = new Intent(MainActivity.this, HomeActivity.class);   //跳转到主界面
                startActivity(intent);   //开始跳转
                timer.cancel();          //取消计时器
                finish();                //finish当前界面
                break;
        }
    }

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == 1) {
                Log.e("-----", "msg.obj:" + msg.obj);
                timeTv.setText(i + "");      //显示倒计时时间
                if (i == 0) {                //如果计时时间结束后  自动跳转主界面
                    timer.cancel();          //取消计时器
                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);   //跳转到主界面
                    startActivity(intent);   //开始跳转
                    finish();                //finish当前界面
                }
            }
            return false;
        }
    });

    @Override
    protected void onStop() {
        super.onStop();
        timer.cancel();
    }
}
