package com.mingrisoft.blowingfog;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import utils.AudioRecordManger;
import view.DrawView;

public class MainActivity extends Activity {
    private DrawView drawView;//覆盖层
    private static final int RECORD = 2;//监听话筒
    private AudioRecordManger audioRecordManger;//调用话筒实现类

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //不显示系统的标题栏,
        getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN );
        audioRecordManger = new AudioRecordManger(handler,RECORD);//实例化话筒实现类
        audioRecordManger.getNoiseLevel();//打开话筒监听声音
        drawView = (DrawView) findViewById(R.id.draw_glasses);  //获得布局文件中擦屏组件
        drawView.setOnGuaGuaKaCompleteListener(new DrawView.OnGuaGuaKaCompleteListener() {
            @Override
            public void complete() {
                audioRecordManger.getNoiseLevel();//获取声音级别
                drawView.setVisibility(View.GONE);//设置控件visibility属性
            }
        });//画布监听
    }
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case RECORD://监测话筒
                    double soundValues = (double) msg.obj;
                    getSoundValues(soundValues);//获得话筒声音后，屏幕重绘起雾
                    break;
                default:
                    break;
            }
            return false;
        }
    });
    /**
     * 获取声音，显示起雾动画
     * @param values
     */
    private void getSoundValues(double values){
        //话筒分贝大于50，屏幕起雾
        if (values >60){

            Animation animation = AnimationUtils.loadAnimation(this, R.anim.in_window);//加载动画文件
            drawView.setAnimation(animation);           //动画设置在自定义控件中
            drawView.setVisibility(View.VISIBLE);       //显示控件
            audioRecordManger.isGetVoiceRun = false;//设置话筒停止运行
        }
    }
}
