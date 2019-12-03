package com.mingrisoft.videodialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by Administrator on 2017/2/21.
 */

public class VideoDialog extends Dialog {

    Context context;             //上下文
    private View dialogView;    //对话框控件

    public VideoDialog(Context context) {
        super(context);
        this.context = context;
    }

    public VideoDialog(Context context, int theme){
        super(context, theme);
        this.context = context;
        LayoutInflater inflater= LayoutInflater.from(context);
        //加载对话框布局文件
        dialogView = inflater.inflate(R.layout.video_play_dialog_layout, null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(dialogView);
    }

    @Override
    public View findViewById(int id) {
        //重写findViewById方法获取对话框中控件
        return super.findViewById(id);
    }

    public View getDialogView() {
        //获得对话框view
        return dialogView;
    }

}

