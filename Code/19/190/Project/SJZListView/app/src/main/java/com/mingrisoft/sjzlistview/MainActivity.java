package com.mingrisoft.sjzlistview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ListView lv;
    private KuaiDiAdapter adapter;
    private List<KuaiDi> list;
   private TextView tv_content,tv_time;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //载入布局
        setContentView(R.layout.activity_main);
        tv_content=(TextView)findViewById(R.id.tv_content);
        tv_time=(TextView)findViewById(R.id.tv_time);
        tv_time.setText("2016-09-20 09:42:11");
        tv_content.setText("已签收");
        lv = (ListView) findViewById(R.id.lv);

        //物流信息集合
        list =new ArrayList<>();
        list.add(new KuaiDi("2016-09-19 04:42:11","到达【广东省广州邮件处理中心】"));
        list.add(new KuaiDi("2016-09-19 01:14:30","离开【深圳市处理中心】，下一站【广州市】"));
        list.add(new KuaiDi("2016-09-18 23:18:21","到达【深圳市处理中心】"));
        list.add(new KuaiDi("2016-09-18 21:26:51","离开【深圳市龙华函件中心】，下一站【深圳市】"));
        list.add(new KuaiDi("2016-09-18 21:12:53","【深圳市龙华函件中心】已收寄"));
        list.add(new KuaiDi("2016-09-19 04:42:11","到达【广东省广州邮件处理中心】"));
        list.add(new KuaiDi("2016-09-19 01:14:30","离开【深圳市处理中心】，下一站【广州市】"));
        list.add(new KuaiDi("2016-09-18 23:18:21","到达【深圳市处理中心】"));
        list.add(new KuaiDi("2016-09-18 21:26:51","离开【深圳市龙华函件中心】，下一站【深圳市】"));
        list.add(new KuaiDi("2016-09-18 21:12:53","【深圳市龙华函件中心】已收寄"));
        list.add(new KuaiDi("2016-09-18 08:51:33","您的包裹已出库"));
        list.add(new KuaiDi("2016-09-18 08:40:23","您的订单待配货"));
        list.add(new KuaiDi("2016-09-18 08:33:50","您的订单开始处理"));
        //初始化适配器
        adapter = new KuaiDiAdapter(this,list);
        //绑定适配器
        lv.setAdapter(adapter);
    }
}


