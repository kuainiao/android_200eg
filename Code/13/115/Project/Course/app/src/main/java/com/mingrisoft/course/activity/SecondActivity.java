package com.mingrisoft.course.activity;

import android.content.Intent;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.mingrisoft.course.R;
import com.mingrisoft.course.base.BaseActivity;
import com.mingrisoft.course.bean.Details;
import com.mingrisoft.course.listener.Service;
import com.orhanobut.logger.Logger;
/**
 * Author LYJ
 * Created on 2016/12/28.
 * Time 16:48
 */
public class SecondActivity extends BaseActivity {
    private TextView title,time,people,name,about,message;
    private ImageView image;
    /**
     * 添加布局
     */
    @Override
    protected void addContentView() {
        setContentView(R.layout.activity_second);
    }

    /**
     * 初始化
     */
    @Override
    protected void initContentView() {
        setTitleName(R.string.second_title);
        setTitleBarBackgroundColor(R.color.colorTransparent);
        initView();
        Intent intent = getIntent();
        String course_id = intent.getStringExtra("course_id");
        String user_id = intent.getStringExtra("user_id");
        String url = String.format(Service.second_url,course_id,user_id);
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Logger.json(response);
                Details details = new Gson().fromJson(response,Details.class);
                title.setText(details.getCourse().getCourse_name());
                time.setText("课程时长："+details.getCourse().getClass_hour());
                people.setText("学习人数："+details.getCourse().getStudy_num());
                message.setText(details.getCourse().getDescription());
                name.setText(details.getCourse().getMain_teacher());
                about.setText(details.getCourse().getTeacher_info());
                Glide.with(SecondActivity.this).load(details.getCourse().getTeacher_pic()).into(image);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(request);
    }

    /**
     * 初始化控件
     */
    private void initView() {
        title = (TextView) findViewById(R.id.second_title);
        time = (TextView) findViewById(R.id.second_time);
        people = (TextView) findViewById(R.id.second_people);
        message = (TextView) findViewById(R.id.second_message);
        name = (TextView) findViewById(R.id.second_name);
        about = (TextView) findViewById(R.id.second_about);
        image = (ImageView) findViewById(R.id.second_image);
    }

    /**
     * 初始化标题栏
     * @return
     */
    @Override
    protected boolean initTitleBar() {
        return true;
    }

    /**
     * 显示返回键
     * @return
     */
    @Override
    protected boolean showBackBtn() {
        return true;
    }
}
