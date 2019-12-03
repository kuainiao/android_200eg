package com.mingrisoft.list;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Menu;
import android.view.MenuItem;

import com.mingrisoft.list.adapter.MyAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.list_show);
        //创建适配器
        MyAdapter adapter = new MyAdapter(initData(), this);
        recyclerView.setAdapter(adapter);//绑定适配器
        //设置recyclerView的展示效果
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
    /**
     * 初始化图片URL集合
     *
     * @return
     */
    private List<Integer> initData() {
        List<Integer> list = new ArrayList<>();
        for (int i=0;i<20;i++){
            list.add(R.mipmap.ic_launcher);
        }
        return list;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.vertical://纵向列表
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                break;
            case R.id.horizontal://横向列表
                recyclerView.setLayoutManager(new StaggeredGridLayoutManager(4,LinearLayoutManager.HORIZONTAL));
                break;
            case R.id.grid://表格列表
                recyclerView.setLayoutManager(new GridLayoutManager(this,2));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
