package com.mingrisoft.alarmclock;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mingrisoft.alarmclock.greendao.DaoMaster;
import com.mingrisoft.alarmclock.greendao.DaoSession;
import com.mingrisoft.alarmclock.greendao.TimeEntity;
import com.mingrisoft.alarmclock.greendao.TimeEntityDao;
import com.mingrisoft.alarmclock.item.SwitchButton;
import com.mingrisoft.alarmclock.scroll.TimeInDay;
import com.mingrisoft.alarmclock.scroll.ViewColorGenerator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/1/3.
 */

public class AlarmListAdapter extends BaseAdapter {

    private Context context;
    private List<TimeEntity> list;

    private AlarmListAdapter adapter;
    // 数据库
    private SQLiteDatabase db;
    // 管理者
    private DaoMaster mDaoMaster;
    // 会话
    private DaoSession mDaoSession;
    // 对应的表,由java代码生成的,对数据库内相应的表操作使用此对象
    private TimeEntityDao timeEntityDao;

    public AlarmListAdapter(Context context) {
        this.context = context;
        // 此DevOpenHelper类继承自SQLiteOpenHelper,第一个参数Context,第二个参数数据库名字,第三个参数CursorFactory
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, "daodemo.db", null);
        db = helper.getWritableDatabase();
        mDaoMaster = new DaoMaster(db);
        mDaoSession = mDaoMaster.newSession();
        timeEntityDao = mDaoSession.getTimeEntityDao();

    }

    public void addData(List<TimeEntity> dataList) {
        list = new ArrayList<>();
        this.list = dataList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (list != null && list.size() > 0) {
            return list.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int i) {
        if (list != null && list.size() > 0) {
            return list.get(i);
        } else {
            return 0;
        }
    }

    @Override
    public long getItemId(int i) {
        if (list != null && list.size() > 0) {
            return i;
        } else {
            return 0;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(context).inflate(R.layout.alarm_item, null);
        TextView textTime = (TextView) view.findViewById(R.id.alarm_time);
        TextView textDate = (TextView) view.findViewById(R.id.alarm_date);
        final SwitchButton switchButton = (SwitchButton) view.findViewById(R.id.alarm_enable);
        textTime.setText(list.get(i).getTime());
        textDate.setText(list.get(i).getDate());
        /**
         * 判断当前的按钮状态
         * */
        if (!list.get(i).getTf()){
            switchButton.closeSwitch();
        }
        int[] skyColor = ViewColorGenerator.getTopBtmColor(new TimeInDay(list.get(i).getHour(), 00));
        GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, skyColor);
        view.setBackground(gd);
        switchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (switchButton.isSwitchOpen()) {
                    switchButton.closeSwitch();
                    insertOrReplace(false, i);
                } else {
                    switchButton.openSwitch();
                    insertOrReplace(true, i);
                }
            }
        });
        return view;
    }

    public void insertOrReplace(boolean tf, int p) {
        timeEntityDao.insertOrReplace(new TimeEntity(list.get(p).getId(),list.get(p).getID(),list.get(p).getTime(),
                list.get(p).getDate(),list.get(p).getHour(),list.get(p).getMin(),tf));
    }
}
