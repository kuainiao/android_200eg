package com.mingrisoft.customtoast;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.widget.Button;

import com.jakewharton.rxbinding.view.RxView;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.hint_toast)
    Button hintToast;
    private MyToast myToast;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        //创建Toast
        myToast = new MyToast.Builder(this)
                .setMessage("自定义Toast效果！")//设置提示文字
                .setBackgroundColor(0xe9ff4587)//设置背景颜色
                .setGravity(Gravity.CENTER)//设置吐司位置
                .showIcon(true)//是否显示图标
                .build();//创建吐司
        //按钮的点击事件
        RxView.clicks(hintToast)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(aVoid -> myToast.show());
    }
}
