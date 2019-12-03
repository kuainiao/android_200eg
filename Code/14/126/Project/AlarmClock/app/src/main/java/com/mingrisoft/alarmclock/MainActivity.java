package com.mingrisoft.alarmclock;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Canvas;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mingrisoft.alarmclock.greendao.DaoMaster;
import com.mingrisoft.alarmclock.greendao.DaoSession;
import com.mingrisoft.alarmclock.greendao.TimeEntity;
import com.mingrisoft.alarmclock.greendao.TimeEntityDao;
import com.mingrisoft.alarmclock.scroll.MyScrollLayout;
import com.mingrisoft.alarmclock.scroll.TimeInDay;
import com.mingrisoft.alarmclock.scroll.ViewColorGenerator;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private MyScrollLayout scrollLayout;
    private RelativeLayout relativeLayout;
    private TextView timeText;
    private ImageView fengChe;
    private View viewBack;
    private LinearLayout checkLayout;
    private Button okBtn;
    private CheckBox checkMon, checkTus, checkWed, checkThur, checkFri, checkSat, checkSun;

    private AlarmManager alarmManager = null;


    // 数据库
    private SQLiteDatabase db;
    // 管理者
    private DaoMaster mDaoMaster;
    // 会话
    private DaoSession mDaoSession;
    // 对应的表,由java代码生成的,对数据库内相应的表操作使用此对象
    private TimeEntityDao timeEntityDao;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private int dbHour, dbMin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        relativeLayout = (RelativeLayout) findViewById(R.id.activity_main);
        init();
        getSysTime(); //获取系统的当前时间
    }

    private void getSysTime() {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        int hour = c.get(Calendar.HOUR_OF_DAY);  //获取小时
        int min = c.get(Calendar.MINUTE);         //获取分钟
        scrollLayout.setItem(hour * 60 + min + 1); //设置当前显示的时间
    }

    private void init() {
        // 此DevOpenHelper类继承自SQLiteOpenHelper,第一个参数Context,第二个参数数据库名字,第三个参数CursorFactory
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "daodemo.db", null);
        db = helper.getWritableDatabase();
        mDaoMaster = new DaoMaster(db);
        mDaoSession = mDaoMaster.newSession();
        timeEntityDao = mDaoSession.getTimeEntityDao();
        sharedPreferences = getSharedPreferences("AlarmClockShare", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        scrollLayout = new MyScrollLayout(this, relativeLayout);
        fengChe = (ImageView) findViewById(R.id.wind_fan);
        timeText = (TextView) findViewById(R.id.time_tv);
        timeText.setOnClickListener(this);
        viewBack = this.findViewById(R.id.back_view);
        checkMon = (CheckBox) findViewById(R.id.check_mon);
        checkTus = (CheckBox) findViewById(R.id.check_tues);
        checkWed = (CheckBox) findViewById(R.id.check_wed);
        checkThur = (CheckBox) findViewById(R.id.check_thur);
        checkFri = (CheckBox) findViewById(R.id.check_fri);
        checkSat = (CheckBox) findViewById(R.id.check_sat);
        checkSun = (CheckBox) findViewById(R.id.check_sun);

        checkLayout = (LinearLayout) findViewById(R.id.check_layout);
        okBtn = (Button) findViewById(R.id.ok_button);
        okBtn.setOnClickListener(this);
        fengChe.setOnClickListener(this);
        scrollLayout.setOnTimeChange(new MyScrollLayout.OnTimeChange() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void elapse(TimeInDay time) {
                refreshCurTime(time);
                refreshBackground(time);
                getTime(time);
            }
        });
        scrollLayout.setOnFanChange(new MyScrollLayout.OnFanChange() {
            @Override
            public void change(int item) {
                final int curAngle = (item % 60) * 6;
                fengChe.setRotation(curAngle);

            }
        });
        /*
        * 设置界面的滑动监听
        * */
        scrollLayout.setOnSwipListener(new MyScrollLayout.OnSwipListener() {
            @Override
            public void setVisible() {   //当滑动停止时
                checkLayout.setVisibility(View.VISIBLE);  //显示星期
                okBtn.setVisibility(View.VISIBLE);        //显示确认按钮
            }

            @Override
            public void setInvisible() {  //当界面滑动时
                checkLayout.setVisibility(View.INVISIBLE);  //隐藏星期
                okBtn.setVisibility(View.INVISIBLE);        //隐藏确认按钮
            }
        });
    }

    private void getTime(TimeInDay time) {
        dbHour = time.getHour();
        dbMin = time.getMin();
    }

    /**
     * 刷新时间
     */
    private void refreshCurTime(TimeInDay time) {
        timeText.setText(time.toString());
    }

    /**
     * 刷新背景颜色
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void refreshBackground(TimeInDay time) {
        int[] skyColor = ViewColorGenerator.getTopBtmColor(time);
        GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, skyColor);
        viewBack.setBackground(gd);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ok_button:
                addAlarm();
                Intent intent1 = new Intent(this, AlarmListActivity.class);
                startActivity(intent1);
                setAlarm();//设置闹铃
                break;
            case R.id.time_tv:
                Intent intent = new Intent(this, AlarmListActivity.class);
                startActivity(intent);
                break;
        }

    }

    /**
     * 将要提醒的时间添加到闹钟里
     * */
    private void setAlarm() {
        List<TimeEntity> list = new ArrayList<>();
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Calendar c = Calendar.getInstance();//获取日期对象
        c.setTimeInMillis(System.currentTimeMillis()); //设置Calendar对象
        list = timeEntityDao.queryBuilder().list();
        if (list != null && list.size() > 0)
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getTf()){
                c.set(Calendar.HOUR, list.get(i).getHour()); //设置闹钟小时数
                c.set(Calendar.MINUTE, list.get(i).getMin()); //设置闹钟的分钟数
                c.set(Calendar.SECOND, 0); //设置闹钟的秒数
                c.set(Calendar.MILLISECOND, 0); //设置闹钟的毫秒数
                Intent intent = new Intent(MainActivity.this, AlarmReceiver.class); //创建Intent对象
                PendingIntent pi = PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0); //创建PendingIntent
                alarmManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pi); //设置闹钟
                }
            }
    }

    /**
     * 将闹钟添加到
     * */
    private void addAlarm() {
        StringBuffer stringBuffer = new StringBuffer();
        String date;
        if (checkMon.isChecked()) {
            stringBuffer.append("、一");
        }
        if (checkTus.isChecked()) {
            stringBuffer.append("、二");
        }
        if (checkWed.isChecked()) {
            stringBuffer.append("、三");
        }
        if (checkThur.isChecked()) {
            stringBuffer.append("、四");
        }
        if (checkFri.isChecked()) {
            stringBuffer.append("、五");
        }
        if (checkSat.isChecked()) {
            stringBuffer.append("、六");
        }
        if (checkSun.isChecked()) {
            stringBuffer.append("、日");
        }
        if (stringBuffer.length() > 0) {
            stringBuffer.substring(1);
            date = stringBuffer.toString().substring(1);
        } else {
            date = "一次";
        }
        TimeEntity timeEntity = new TimeEntity(sharedPreferences.getLong("dataid", 0), sharedPreferences.getLong("dataid", 0),
                timeText.getText().toString(), date, dbHour, dbMin, true);
        editor.putLong("dataid", sharedPreferences.getLong("dataid", 0) + 1);
        editor.commit();
        timeEntityDao.insert(timeEntity);
    }
}
