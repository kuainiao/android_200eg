package com.mingrisoft.bottomdialog;

import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    /**
     * 显示
     */
    public void show() {
        //加载弹窗视图布局
        View bottom = getLayoutInflater().inflate(R.layout.bottom_layout, null);
        //创建弹窗对象
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(bottom);//设置弹窗布局
        //设置弹窗的状态栏半透明
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //显示弹窗
        dialog.show();
        //设置弹窗的里布局的按钮点击事件
        LinearLayout txt1 = (LinearLayout) bottom.findViewById(R.id.txt1);
        LinearLayout txt2 = (LinearLayout) bottom.findViewById(R.id.txt2);
        LinearLayout txt3 = (LinearLayout) bottom.findViewById(R.id.txt3);
        LinearLayout txt4 = (LinearLayout) bottom.findViewById(R.id.txt4);
        LinearLayout txt5 = (LinearLayout) bottom.findViewById(R.id.txt5);
        txt1.setOnClickListener(new MineOnClick(dialog));
        txt2.setOnClickListener(new MineOnClick(dialog));
        txt3.setOnClickListener(new MineOnClick(dialog));
        txt4.setOnClickListener(new MineOnClick(dialog));
        txt5.setOnClickListener(new MineOnClick(dialog));
    }


    /**
     * 点击事件
     */
    class MineOnClick implements View.OnClickListener{
        private BottomSheetDialog dialog;
        public MineOnClick(BottomSheetDialog dialog) {
            this.dialog = dialog;
        }
        @Override
        public void onClick(View v) {
             dialog.dismiss();//关闭弹窗
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.share){
            show();//显示弹窗
        }
        return super.onOptionsItemSelected(item);
    }
}
