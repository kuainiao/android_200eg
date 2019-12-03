package com.mingrisoft.course.base;

import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.mingrisoft.course.R;
import com.mingrisoft.course.app.MineApplication;

/**
 * Author LYJ
 * Created on 2016/12/29.
 * Time 09:25
 */

public  abstract class BaseActivity extends AppCompatActivity{
    protected RequestQueue requestQueue;
    private RelativeLayout titleBar;
    private ImageButton backBtn;
    private TextView titleName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestQueue = MineApplication.getRequestQueue();
        addContentView();
        if (initTitleBar()){
            titleBar = (RelativeLayout) findViewById(R.id.title_bar);
            backBtn = (ImageButton) findViewById(R.id.back_btn);
            titleName = (TextView) findViewById(R.id.title_name);
            setTitleBarShowType();
            addListener();
        }
        initContentView();
    }

    protected abstract void addContentView();
    protected abstract void initContentView();

    /**
     * 设置标题栏显示类型
     */
    protected void setTitleBarShowType(){
        if (showBackBtn()){
            backBtn.setVisibility(View.VISIBLE);
        }else {
            backBtn.setVisibility(View.GONE);
        }
    }

    /**
     * 是否初始化标题栏
     * @return
     */
    protected abstract boolean initTitleBar();

    /**
     * 用于设置标题栏显示类型
     * @return
     */
    protected boolean showBackBtn(){
        return false;
    }

    /**
     * 设置标题栏背景颜色
     * @param color
     */
    protected void setTitleBarBackgroundColor(@ColorRes int color){
        titleBar.setBackgroundResource(color);
    }

    /**
     * 设置标题
     * @param title
     */
    protected void setTitleName(@StringRes int title){
        titleName.setText(title);
    }
    protected void setTitleNameColor(int color){
        titleName.setTextColor(color);
    }
    /**
     * 添加监听
     */
    private void addListener(){
        //设置返回按钮
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
