package com.mingrisoft.nearby.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.wearable.view.WatchViewStub;

import com.mingrisoft.nearby.R;
import com.mingrisoft.nearby.adapter.MyAdapter;
import com.mingrisoft.nearby.entity.Result;

import java.util.ArrayList;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final ArrayList<Result> list = getIntent().getParcelableArrayListExtra("data");
        final MyAdapter adapter = new MyAdapter(this, list);
        final RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                RecyclerView listView = (RecyclerView) findViewById(R.id.list);
                listView.setLayoutManager(manager);
                listView.setAdapter(adapter);
            }
        });
    }
}
