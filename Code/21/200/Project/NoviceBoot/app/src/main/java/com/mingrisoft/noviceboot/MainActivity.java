package com.mingrisoft.noviceboot;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ActivityChooserView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TopView v3;
    int w, h;    //获取屏幕的宽高
    private Button nextBtn;
    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        /**
         * 获取屏幕的宽高
         * */
        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        w = wm.getDefaultDisplay().getWidth();  //获取屏幕宽
        h = wm.getDefaultDisplay().getHeight(); //获取屏幕高
        v3 = new TopView(this);
        v3 = (TopView) findViewById(R.id.top_view_three);
        v3.setAlpha((float) 0.7); //设置覆盖最上方的view的透明度
        v3.getWindowWH(w, h);     //将手机屏幕的宽高传给画布
        v3.getMyRect(130, 170, 10, 330, "点击进行投票");
        nextBtn = (Button) findViewById(R.id.i_know_btn);
        nextBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.i_know_btn:
                /**
                 * 进行判断
                 * */
                if (i == 1) {
                    /**
                     * 如果提示信息展示完成后，使view和按钮消失
                     * */
                    v3.setVisibility(View.INVISIBLE);
                    nextBtn.setVisibility(View.INVISIBLE);
                    break;
                }
                v3.getMyRect(w - 215, 170, w - 470, 330, "再次点击取消投票");
                i++;
                break;
        }
    }
}
