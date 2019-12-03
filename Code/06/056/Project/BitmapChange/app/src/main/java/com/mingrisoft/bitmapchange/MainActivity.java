package com.mingrisoft.bitmapchange;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    private Bitmap bitmap;
    private ImageView huiduIV,oldIV,bingdongIV;
    private Button huiduBT,oldBT,bingdongBT;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化控件
        huiduIV=(ImageView)findViewById(R.id.huiduIV);
        oldIV=(ImageView)findViewById(R.id.oldIV);
        bingdongIV=(ImageView)findViewById(R.id.bingdongIV);
        huiduBT=(Button)findViewById(R.id.huiduBT);
        oldBT=(Button)findViewById(R.id.oldBT);
        bingdongBT=(Button)findViewById(R.id.bingdongBT);
        //获取图片资源转换为Bitmap类型
        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.bb);
        //单击事件
        huiduBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //隐藏处理按钮
                huiduBT.setVisibility(View.GONE);
                //显示灰度图片
                huiduIV.setImageBitmap(BitmapUtil.huidu(bitmap));
            }
        });
        oldBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oldBT.setVisibility(View.GONE);
                //设置怀旧图片
                oldIV.setImageBitmap(BitmapUtil.huaijiu(bitmap));
            }
        });
        bingdongBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bingdongBT.setVisibility(View.GONE);
                //设置冰冻图片
                bingdongIV.setImageBitmap(BitmapUtil.ice(bitmap));
            }
        });


    }
}
