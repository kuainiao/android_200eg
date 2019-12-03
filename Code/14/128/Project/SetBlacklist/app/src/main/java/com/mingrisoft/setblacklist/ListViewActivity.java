package com.mingrisoft.setblacklist;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mingrisoft.setblacklist.entity.DaoMaster;
import com.mingrisoft.setblacklist.entity.DaoSession;
import com.mingrisoft.setblacklist.entity.PersonEntity;
import com.mingrisoft.setblacklist.entity.PersonEntityDao;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/27.
 */

public class ListViewActivity extends AppCompatActivity {
    // 数据库
    private SQLiteDatabase db;
    // 管理者
    private DaoMaster mDaoMaster;
    // 会话
    private DaoSession mDaoSession;
    private PersonEntityDao entityDao;
    private ListView listView;
    private MyAdapter adapter;
    private List<PersonEntity> list;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view_activity);
        init();
    }

    private void init() {
        sharedPreferences = getSharedPreferences("sp",CONTEXT_RESTRICTED);
        listView = (ListView) findViewById(R.id.list_view);
        adapter = new MyAdapter(this);
        listView.setAdapter(adapter);
        list = new ArrayList<>();
        // 此DevOpenHelper类继承自SQLiteOpenHelper,第一个参数Context,第二个参数数据库名字,第三个参数CursorFactory
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "persondao.db", null);
        db = helper.getWritableDatabase();
        mDaoMaster = new DaoMaster(db);
        mDaoSession = mDaoMaster.newSession();
        entityDao = mDaoSession.getPersonEntityDao();
        list = entityDao.queryBuilder().list();
        adapter.addData(list);
    }

    class MyAdapter extends BaseAdapter {
        private Context context;
        private List<PersonEntity> entityList;

        public MyAdapter(Context context) {
            this.context = context;
            entityList = new ArrayList<>();
        }

        public void addData(List<PersonEntity> list) {
            entityList = list;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return entityList.size();
        }

        @Override
        public Object getItem(int i) {
            return entityList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            view = LayoutInflater.from(context).inflate(R.layout.list_view_item, null);
            final TextView textView = (TextView) view.findViewById(R.id.item_text);
            textView.setText(entityList.get(i).getNumber());
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showDia(entityList.get(i).getTag(), textView.getText().toString());  //展示提示框
                }


            });
            return view;
        }


    }

    private void showDia(final Long q, final String num) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("是否将" + num + "移除黑名单");
        builder.setPositiveButton("否", null);
        builder.setNegativeButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.d("-*-*-*", "entityDao" + entityDao.count());
                entityDao.deleteByKey(q);
                list = entityDao.queryBuilder().list();
                adapter.addData(list);
                Toast.makeText(ListViewActivity.this, "已将" + num + "移除黑名单", Toast.LENGTH_SHORT).show();
            }
        });
        builder.show();
    }

}
