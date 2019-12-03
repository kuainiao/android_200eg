package com.mingrisoft.progressbar;

import android.support.v7.app.AppCompatActivity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
public class MainActivity extends AppCompatActivity implements OnClickListener {
    private Button anim;//动画进度条按钮
    private Button color;//颜色进度条按钮
    private Button icon;//图片进度条按钮
    private Dialog mDialog;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化控件
        init();
        //绑定点击事件
        setClickListener();
    }

    private void init() {
        //初始化控件
        anim = (Button) findViewById(R.id.anim);
        color = (Button) findViewById(R.id.color);
        icon = (Button) findViewById(R.id.icon);
    }

    private void setClickListener() {
        //绑定点击事件
        anim.setOnClickListener(this);
        color.setOnClickListener(this);
        icon.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //动画进度条
            case R.id.anim:
                //显示动画进度条
                showRoundProcessDialog(this, R.layout.loading_process_dialog_anim);
                break;
            //颜色进度条
            case R.id.color:
                //显示颜色进度条
                showRoundProcessDialog(this, R.layout.loading_process_dialog_color);
                break;
            //图片进度条
            case R.id.icon:
                //显示图片进度条
                showRoundProcessDialog(this, R.layout.loading_process_dialog_icon);
                break;
            default:
                break;
        }
    }

    public void showRoundProcessDialog(Context mContext, int layout) {
        //初始化进度条
        mDialog = new AlertDialog.Builder(mContext).create();
        //显示进度条
        mDialog.show();
        // 注意此处要放在show之后 否则会报异常
        mDialog.setContentView(layout);
        // mDialog.setCancelable(false); //false设置点击其他地方不能取消进度条
    }

}