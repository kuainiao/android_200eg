package com.mingrisoft.friendcircle;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.mingrisoft.friendcircle.adapter.MyListViewAdapter;

public class MainActivity extends AppCompatActivity {

    private ListView listView;    //定义listview
    private MyListViewAdapter listViewAdapter;   //给listview的适配器
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findID();  //绑定id
        init();  //初始化
    }

    private void init() {
        listViewAdapter = new MyListViewAdapter(this);  //初始化适配器
        listView.setAdapter(listViewAdapter);   // 给listview绑定适配器
    }

    private void findID() {
        listView = (ListView) findViewById(R.id.list_view);  //listview绑定id
    }
}
