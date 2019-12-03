package com.mingrisoft.starrating;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    //声明分数显示控件
    TextView fs1, fs2, fs3, fs4;
    //声明星级滑块控件
    RatingBar ratingBar1, ratingBar2, ratingBar3, ratingBar4;
    //声明输入评价控件
    EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //引入布局
        setContentView(R.layout.activity_main);
        //初始化分数显示控件
        fs1 = (TextView) findViewById(R.id.fs1);
        fs2 = (TextView) findViewById(R.id.fs2);
        fs3 = (TextView) findViewById(R.id.fs3);
        fs4 = (TextView) findViewById(R.id.fs4);
        //初始化评价输入控件
        editText = (EditText) findViewById(R.id.et);
        //初始化星级滑块控件
        ratingBar1 = (RatingBar) findViewById(R.id.room_ratingbar1);
        ratingBar2 = (RatingBar) findViewById(R.id.room_ratingbar2);
        ratingBar3 = (RatingBar) findViewById(R.id.room_ratingbar3);
        ratingBar4 = (RatingBar) findViewById(R.id.room_ratingbar4);
        //默认星级
        ratingBar1.setRating(0);
        ratingBar2.setRating(0);
        ratingBar3.setRating(0);
        ratingBar4.setRating(0);
        //设置每次更改的最小长度
        ratingBar1.setStepSize((float) 0.5);
        ratingBar2.setStepSize((float) 0.5);
        ratingBar3.setStepSize((float) 0.5);
        ratingBar4.setStepSize((float) 0.5);

        //绑定星级滑块控件改变事件
        ratingBar1.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                //设置描述相符分数
                fs1.setText(rating + "分");
            }
        });
        ratingBar2.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                //设置物流服务分数
                fs2.setText(rating + "分");
            }
        });
        ratingBar3.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                //设置发货速度分数
                fs3.setText(rating + "分");
            }

        });
        ratingBar4.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                //设置服务态度分数
                fs4.setText(rating + "分");
            }
        });

    }

    public void onShow(View v) {
        //初始化AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(
                MainActivity.this);
        //设置显示标题
        builder.setTitle("服务评价");
        //设置显示内容
        builder.setMessage(editText.getText().toString() + "\n" + "\n" +
                "描述相符分数：" + fs1.getText().toString() + "\n" +
                "物流服务分数：" + fs2.getText().toString() + "\n" +
                "发货速度分数：" + fs3.getText().toString() + "\n" +
                "服务态度分数：" + fs4.getText().toString());
        //设置显示图片
        builder.setIcon(R.mipmap.ic_launcher);
        //设置按返回键是否可以退出AlertDialog
        builder.setCancelable(false);
        //设置dialog底部按钮
        builder.setPositiveButton("知道了！", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //关闭AlertDialog
                dialog.cancel();
            }
        });
        //创建显示AlertDialog
        builder.create().show();
    }
}
