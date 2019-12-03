package com.mrkj.cookbook;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.mrkj.cookbook.adapter.MineAdapter;
import com.mrkj.cookbook.application.MineApplication;
import com.mrkj.cookbook.bean.MineList;
import com.mrkj.cookbook.service.MyService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    private MyService myService;//下载服务
    private MineAdapter adapter;//适配器
    private List<MineList.ResultEntity> reasult;
    @BindView(R.id.lists)
    ListView lists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        //获取网络下载数据对象
        myService = MineApplication.getMyService();
        //列表的Item点击事件
        lists.setOnItemClickListener((parent, view, position, id) ->
                //跳转界面
                startActivity(new Intent(this,SecondActivity.class)
                        .putExtra("show", reasult.get(position))));
        //进行网络请求
        myService.getList(initRequest())
                //网络请求在子线程中执行
                .subscribeOn(Schedulers.newThread())
                //线程切换到主线程
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mineList -> {
                    reasult = mineList.getResult();
                    //创建适配器
                    adapter = new MineAdapter(reasult,this);
                    //绑定数据
                    lists.setAdapter(adapter);
                });
    }

    /**
     * 设置网络数据参数键值对
     * @return
     */
    private Map<String, Object> initRequest() {
        Map<String, Object> map = new HashMap<>();
        map.put("key", MyService.APP_KEY);
        map.put("page", 2);
        map.put("rows", 20);
        return map;
    }
}
