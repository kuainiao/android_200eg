package com.mingrisoft.topbutton;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private ListAdapter adapter;
    private ImageButton imageBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.list_view);
        adapter = new ListAdapter(this);
        listView.setAdapter(adapter);
        imageBtn = (ImageButton) findViewById(R.id.up_btn);
        imageBtn.setOnClickListener(new View.OnClickListener() {  //置顶按钮的点击事件
            @Override
            public void onClick(View v) {
                listView.smoothScrollToPosition(0);  //是ListView置顶
            }
        });
        add();
        listView.setOnTouchListener(new MyGestureListener(listView, imageBtn, this));

    }

    private void add() {
        TypedArray ar = getResources().obtainTypedArray(R.array.listTitle); //获取数据数组
        int len = ar.length();              //获取数组的长度
        List list = new ArrayList<>();     //初始化list
        for (int i = 0; i < len; i++) {     //for循环添加数据
            list.add(i, ar.getString(i));
        }     //向ListView中添加数据
        adapter.addData(list);              //将数据传给adapter
    }

    public class MyGestureListener extends GestureListener {
        public MyGestureListener(ListView listview, ImageButton button, Context context) {
            super(listview, button, context);
        }

        @Override
        public boolean up(ImageButton imageBtn) {
            return super.up(imageBtn);
        }

        @Override
        public boolean down(ImageButton button) {
            return super.down(button);
        }
    }
}
