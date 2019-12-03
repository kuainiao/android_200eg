package com.mingrisoft.downloadpicture;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private ProgressDialog dialog;
    private ImageView image;
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == 100){
                dialog.dismiss();//关闭弹窗
                Bitmap bitmap = (Bitmap) msg.obj;
                //将下载的图片显示在的ImageView中
                image.setImageBitmap(bitmap);
            }
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dialog = new ProgressDialog(this);//创建弹窗
        dialog.setTitle("提示");//设置弹窗的标题
        dialog.setMessage("下载图片中,请稍等！");//设置弹窗提示内容
        image = (ImageView) findViewById(R.id.image1);
    }

    /**
     * 点击事件
     */
    public void down(View view){
        dialog.show();//显示弹出
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);//设置延迟3秒
                    handler.obtainMessage(100,
                            getImageFromNet("http://www.mingrisoft.com/Public/uploads/book_cover/590fd276c02b9.png")).sendToTarget();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 下载图片
     * @param downUrl
     * @return
     * @throws IOException
     */
    private Bitmap getImageFromNet(String downUrl) throws IOException {
        Bitmap bitmap = null;
        URL url = new URL(downUrl);//设置下载的UR
        //通过Url开启一个连接
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(5000);//设置超市时间
        conn.setRequestMethod("GET");//设置请求模式
        conn.setDoInput(true);//开启输入流
        conn.connect();//打开连接
        if (conn.getResponseCode() == 200){//判断返回码
            InputStream is = conn.getInputStream();//获取输入流
            bitmap = BitmapFactory.decodeStream(is);//将流转换成Bitmap对象
            conn.disconnect();//断开连接
            is.close();//关闭输入流
        }
        return bitmap;//返回bitmap
    }
}
