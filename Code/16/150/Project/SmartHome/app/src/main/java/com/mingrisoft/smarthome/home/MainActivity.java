package com.mingrisoft.smarthome.home;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.mingrisoft.smarthome.R;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private MainListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findID();  //绑定id
        init();    //初始化控件
    }

    private void findID() {
        listView = (ListView) findViewById(R.id.main_list_view);
    }

    private void init() {
        /**
         * 绑定adapter
         * */
        adapter = new MainListAdapter(this);
        listView.setAdapter(adapter);
    }


}
