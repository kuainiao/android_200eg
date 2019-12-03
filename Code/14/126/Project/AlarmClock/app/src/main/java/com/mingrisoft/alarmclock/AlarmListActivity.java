package com.mingrisoft.alarmclock;

import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.mingrisoft.alarmclock.greendao.DaoMaster;
import com.mingrisoft.alarmclock.greendao.DaoSession;
import com.mingrisoft.alarmclock.greendao.TimeEntity;
import com.mingrisoft.alarmclock.greendao.TimeEntityDao;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/30.
 */

public class AlarmListActivity  extends AppCompatActivity {

    private ListView listView;
    private AlarmListAdapter adapter;
    // 数据库
    private SQLiteDatabase db;
    // 管理者
    private DaoMaster mDaoMaster;
    // 会话
    private DaoSession mDaoSession;
    // 对应的表,由java代码生成的,对数据库内相应的表操作使用此对象
    private TimeEntityDao timeEntityDao;
    private List<TimeEntity> list;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_list_layout);
        init();
    }

    private void init() {
        listView = (ListView) findViewById(R.id.alarm_list);
        adapter = new AlarmListAdapter(this);
        listView.setAdapter(adapter);
        // 此DevOpenHelper类继承自SQLiteOpenHelper,第一个参数Context,第二个参数数据库名字,第三个参数CursorFactory
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "daodemo.db", null);
        db = helper.getWritableDatabase();
        mDaoMaster = new DaoMaster(db);
        mDaoSession = mDaoMaster.newSession();
        timeEntityDao = mDaoSession.getTimeEntityDao();
        list = new ArrayList<>();
       getData();
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                showLog(i);
                return false;
            }

            private void showLog(final int p) {
                AlertDialog.Builder builder =new AlertDialog.Builder(AlarmListActivity.this);
                builder.setTitle("是否删除该条闹钟");
                builder.setNegativeButton("否", null);
                builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        timeEntityDao.deleteByKey(list.get(p).getID());
                        getData();

                    }
                });
                builder.show();
            }
        });
    }

    private void getData() {
        list = timeEntityDao.queryBuilder().list();
        adapter.addData(list);
    }
}
