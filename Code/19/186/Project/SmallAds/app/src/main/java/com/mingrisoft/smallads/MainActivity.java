package com.mingrisoft.smallads;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private AlertDialog dlg;                               //定义对话框
    private Button btn_Startads;                          //启动小广告按钮
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_Startads= (Button) findViewById(R.id.start_btn);        //获取启动小广告按钮
        btn_Startads.setOnClickListener(this);                      //设置启动按钮的单击事件
    }

    /**
     *启动按钮的单击事件
     */
    @Override
    public void onClick(View v) {
        //创建对话框实例
        dlg = new AlertDialog.Builder(this,R.style.MyDialog).create();
        dlg.show();                             //显示对话框
        Window window = dlg.getWindow();        //获取对话框窗口
        window.setGravity(Gravity.CENTER);
        window.setContentView(R.layout.dialog);   //设置对话框布局文件
        //获取显示广告的图像控件
        ImageView adsImageView= (ImageView) window.findViewById(R.id.ads_img);
        //小广告图像控件的单击事件
        adsImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //设置跳转的网页地址
                Uri uri = Uri.parse("http://www.mingrisoft.com/");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);      //启动跳转
            }
        });
    }
}
