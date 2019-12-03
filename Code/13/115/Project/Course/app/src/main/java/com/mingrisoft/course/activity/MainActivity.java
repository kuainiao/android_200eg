package com.mingrisoft.course.activity;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.mingrisoft.course.R;
import com.mingrisoft.course.adapter.ClazzAdapter;
import com.mingrisoft.course.base.BaseActivity;
import com.mingrisoft.course.bean.Clazz;
import com.mingrisoft.course.listener.Service;

import java.util.List;
/**
 * Author LYJ
 * Created on 2016/12/28.
 * Time 16:48
 */
public class MainActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    private ListView classList;
    private List<Clazz.ResultEntity> list;

    /**
     * 添加布局
     */
    @Override
    protected void addContentView() {
        setContentView(R.layout.activity_main);
    }

    /**
     * 初始化
     */
    @Override
    protected void initContentView() {
        setTitleName(R.string.main_title);//设置标题
        setTitleBarBackgroundColor(R.color.colorWhite);//设置标题栏背景颜色
        setTitleNameColor(Color.BLACK);//设置标题颜色
        //初始化控件
        classList = (ListView) findViewById(R.id.class_list);
        //设置Item点击事件
        classList.setOnItemClickListener(this);
        //创建网络请求
        StringRequest request = new StringRequest(Service.first_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //使用Gson解析Json数据
                Clazz clazz = new Gson().fromJson(response, Clazz.class);
                list = clazz.getResult();//获取数据结合
                //创建适配器绑定数据
                ClazzAdapter adapter = new ClazzAdapter(list, MainActivity.this);
                //绑定适配器
                classList.setAdapter(adapter);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(request);//将请求添加到队列中
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Clazz.ResultEntity entity = list.get(position);
        //跳转界面
        startActivity(new Intent(this, SecondActivity.class)
                .putExtra("course_id", entity.getEntity_id())
                .putExtra("user_id", entity.getCourse_type()));
    }

    /**
     * 初始化标题栏
     *
     * @return
     */
    @Override
    protected boolean initTitleBar() {
        return true;
    }
}
