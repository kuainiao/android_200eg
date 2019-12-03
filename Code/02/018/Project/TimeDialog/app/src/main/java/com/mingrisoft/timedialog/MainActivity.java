package com.mingrisoft.timedialog;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private final int ONE = 1;
    private final int TWO = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化点击事件
        View.OnClickListener timeBtnListener =
                new BtnOnClickListener(ONE);
        View.OnClickListener timeBtnListener1 =
                new BtnOnClickListener(TWO);
        //初始化控件
        Button btnTime = (Button) findViewById(R.id.btnTime);
        Button btnTime1 = (Button) findViewById(R.id.btnTime1);
        //绑定点击事件
        btnTime.setOnClickListener(timeBtnListener);
        btnTime1.setOnClickListener(timeBtnListener1);
        //显示当前日期控件
        TextView text = (TextView) findViewById(R.id.text);
        //设置文本格式
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日    HH:mm:ss");
        //获取当前系统时间
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        //转换时间为设置格式
        String str = formatter.format(curDate);
        //设置显示文字
        text.setText(str);
    }

    protected Dialog onCreateDialog(int id) {
        //用来获取日期和时间的
        Calendar calendar = Calendar.getInstance();
        Dialog dialog = null;
        switch (id) {
            case ONE:
                //时间选择弹窗回调函数
                TimePickerDialog.OnTimeSetListener timeListener =
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timerPicker,
                                                  int hourOfDay, int minute) {
                                //初始化显示设置时间控件
                                TextView textV =
                                        (TextView) findViewById(R.id.textV);
                                //小时
                                String hourOfDays;
                                //分
                                String minutes;
                                if (minute < 10) {
                                    //为了美观 小于10补0
                                    minutes = "0" + minute;
                                } else {
                                    minutes = minute + "";
                                }
                                if (hourOfDay < 10) {
                                    //为了美观 小于10补0
                                    hourOfDays = "0" + hourOfDay;
                                } else {
                                    hourOfDays = "" + hourOfDay;
                                }
                                //设置控件显示选择时间
                                textV.setText(hourOfDays + ":" +
                                        minutes);
                            }
                        };
                //时间选择弹窗
                dialog = new TimePickerDialog(this, timeListener,
                        calendar.get(Calendar.HOUR_OF_DAY),
                        calendar.get(Calendar.MINUTE),
                        //是否为二十四制
                        false);
                break;
            case TWO:
                //时间选择弹窗
                TimePickerDialog.OnTimeSetListener timeListener1 =
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timerPicker,
                                                  int hourOfDay, int minute) {
                                //初始化显示设置时间控件
                                TextView textV1 =
                                        (TextView) findViewById(R.id.textV1);
                                //小时
                                String hourOfDays;
                                //分
                                String minutes;
                                if (minute < 10) {
                                    //为了美观 小于10补0
                                    minutes = "0" + minute;
                                } else {
                                    minutes = minute + "";
                                }
                                if (hourOfDay < 10) {
                                    //为了美观 小于10补0
                                    hourOfDays = "0" + hourOfDay;
                                } else {
                                    hourOfDays = "" + hourOfDay;
                                }
                                //设置控件显示选择时间
                                textV1.setText(hourOfDays + ":" +
                                        minutes);
                            }
                        };
                //是否为二十四制
                dialog = new TimePickerDialog(this, timeListener1,
                        calendar.get(Calendar.HOUR_OF_DAY),
                        calendar.get(Calendar.MINUTE),
                        true);

                break;
            default:
                break;
        }
        return dialog;
    }

    // 成员内部类,此处为提高可重用性，也可以换成匿名内部类
    private class BtnOnClickListener implements View.OnClickListener {
        //默认为0则不显示对话框
        private int dialogId = 0;

        public BtnOnClickListener(int dialogId) {
            this.dialogId = dialogId;
        }

        @Override
        public void onClick(View view) {
            showDialog(dialogId);
        }
    }

}
