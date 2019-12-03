package com.mingrisoft.toastmessage;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private Boolean isExit = false;     //定义记录是否退出
    private Timer timer;                //定义计时器
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     *该方法判断是否单击了返回按钮
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK){
           exit();              //调用退出方法
        }
        return false;
    }

    /**
     * 该方法实现，两秒内单击两次返回按钮将实现退出功能
     */
    private void exit() {
        if (isExit==false){     //如果没有退出的情况下
            isExit=true;        //准备退出
            //显示提示信息
            Toast.makeText(this, "再按一次退出高德地图", Toast.LENGTH_SHORT).show();
            timer=new Timer();   //实例化计时器
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit=false;   //取消退出
                }
            },2000);                 //两秒内再次单击返回按钮，将退出该应用
        }else {
            finish();                //退出当前界面
            System.exit(0);          //释放内存
        }
    }
}
