package com.mingrisoft.downmenu;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{


    private PopupWindow popupWindow;
    private ImageView tag_line;
    private TextView txt;
    private boolean isShow;
    private int index;
    private int oldIndex;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化菜单项
        findViewById(R.id.select_1).setOnClickListener(this);
        findViewById(R.id.select_2).setOnClickListener(this);
        findViewById(R.id.select_3).setOnClickListener(this);
        findViewById(R.id.select_4).setOnClickListener(this);
        tag_line = (ImageView) findViewById(R.id.tag_line);
    }

    @Override
    public void onClick(View v) {
        String str = null;
        switch (v.getId()) {
            case R.id.select_1://全部分类
                str = "全部分类";
                index = 1;
                break;
            case R.id.select_2://类型
                str = "类型";
                index = 2;
                break;
            case R.id.select_3://热门
                str = "热门";
                index = 3;
                break;
            case R.id.select_4://难易
                str = "难易";
                index = 4;
                break;

        }
        setMenu(str);
    }
    /**
     * 设置
     */
    private void setMenu(String message) {
        if (null == popupWindow){
            View view = getLayoutInflater().inflate(R.layout.pop_layout, null);
            txt = (TextView) view.findViewById(R.id.text);
            //创建弹窗
            popupWindow = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.MATCH_PARENT);
            popupWindow.setAnimationStyle(R.style.PopupWindowAnimation);//设置动画效果
            popupWindow.setOutsideTouchable(false);//设置点击弹窗外部不关闭弹窗
            popupWindow.setFocusable(false);//设置不获取焦点
        }
        if (isShow){
            if (index == oldIndex){
                popupWindow.dismiss();//关闭弹窗
                isShow = false;
                return;
            }
        }
        txt.setText(message);
        popupWindow.showAsDropDown(tag_line);//设置显示弹窗
        isShow = true;
        oldIndex = index;
    }
}

