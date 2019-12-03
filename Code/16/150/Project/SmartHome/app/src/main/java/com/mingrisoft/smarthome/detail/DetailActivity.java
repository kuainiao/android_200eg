package com.mingrisoft.smarthome.detail;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.mingrisoft.smarthome.R;

/**
 * Created by Administrator on 2017/2/17.
 */

public class DetailActivity extends AppCompatActivity {

    private ListView listView;
    private DetailAdapter adapter;
    private String masterRoom[] = {"吸顶灯", "床头灯"};    //主卧的开关
    private String secondRoom[] = {"吸顶灯", "床头灯"};    //次卧的开关
    private String livingRoom[] = {"吸顶灯", "氛围灯","电视机"};      //客厅的开关
    private String studyRoom[] = {"吸顶灯", "护眼灯"};        //书房的开关
    private String toilet[] = {"吸顶灯", "排气扇"};       //卫生间的开关
    private String kitchen[] = {"吸顶灯", "油烟机"};      //厨房的开关
    private int resids[];          //用于加载图片
    private LinearLayout linearLayout;
    private ImageButton imageBtn;  //返回按钮
    private TextView titleText;  //标题

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        findID();
        init();
    }


    //绑定id
    private void findID() {
        listView = (ListView) findViewById(R.id.detail_list_view);
        linearLayout = (LinearLayout) findViewById(R.id.detail_layout);
        imageBtn = (ImageButton) findViewById(R.id.detail_back_btn);
        titleText = (TextView) findViewById(R.id.title_text);
        imageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    //初始化
    private void init() {
        adapter = new DetailAdapter(this);
        listView.setAdapter(adapter);
        TypedArray ar = getResources().obtainTypedArray(R.array.imgBackGround);
        int len = ar.length();
        resids = new int[len];
        for (int i = 0; i < len; i++)
            resids[i] = ar.getResourceId(i, 0);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = getIntent();   //获取传递过来的信息
        titleText.setText(intent.getStringExtra("name"));
        switch (intent.getStringExtra("name")) {
            case "主卧":
                linearLayout.setBackgroundResource(resids[0]);
                adapter.addBtn(masterRoom,titleText.getText().toString());
                break;
            case "次卧":
                linearLayout.setBackgroundResource(resids[1]);
                adapter.addBtn(secondRoom,titleText.getText().toString());
                break;
            case "客厅":
                linearLayout.setBackgroundResource(resids[2]);
                adapter.addBtn(livingRoom,titleText.getText().toString());
                break;
            case "书房":
                linearLayout.setBackgroundResource(resids[3]);
                adapter.addBtn(studyRoom,titleText.getText().toString());
                break;
            case "卫生间":
                linearLayout.setBackgroundResource(resids[4]);
                adapter.addBtn(toilet,titleText.getText().toString());
                break;
            case "厨房":
                linearLayout.setBackgroundResource(resids[5]);
                adapter.addBtn(kitchen,titleText.getText().toString());
                break;
        }
    }
}
