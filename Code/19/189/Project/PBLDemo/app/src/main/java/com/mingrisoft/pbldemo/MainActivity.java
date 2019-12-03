package com.mingrisoft.pbldemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private List<String> lists;
    private MyRecyclerAdapter adapter;
    private List<Integer>  resids;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        //列表控件
        mRecyclerView = (RecyclerView) this.findViewById(R.id.recyclerView);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        //设置RecyclerView布局管理器为2列垂直排布
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        adapter = new MyRecyclerAdapter(this,lists,resids);
        mRecyclerView.setAdapter(adapter);
        adapter.setOnClickListener(new MyRecyclerAdapter.OnItemClickListener() {
            //单击事件
            @Override
            public void ItemClickListener(View view, int postion) {
                Toast.makeText(MainActivity.this,"点击了："+postion,Toast.LENGTH_SHORT).show();
            }
            //长按事件
            @Override
            public void ItemLongClickListener(View view, int postion) {
                //长按删除
                lists.remove(postion);
                adapter.notifyItemRemoved(postion);
            }
        });
    }

    private void initData() {
        //文字信息集合
        lists = new ArrayList();
        //资源id集合
        resids= new ArrayList();
        for (int i = 0; i < 100; i++) {
            lists.add("" + i);
        }
        resids.add(R.drawable.book0);
        resids.add(R.drawable.book1);
        resids.add(R.drawable.book2);
        resids.add(R.drawable.book3);
        resids.add(R.drawable.book4);
        resids.add(R.drawable.book5);
        resids.add(R.drawable.book6);
        resids.add(R.drawable.book7);
        resids.add(R.drawable.book8);
        resids.add(R.drawable.book9);
        resids.add(R.drawable.book10);
        resids.add(R.drawable.book11);
        resids.add(R.drawable.book12);
        resids.add(R.drawable.book13);
        resids.add(R.drawable.book14);
    }
}
