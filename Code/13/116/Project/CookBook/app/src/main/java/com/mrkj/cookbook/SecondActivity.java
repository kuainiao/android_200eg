package com.mrkj.cookbook;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mrkj.cookbook.application.MineApplication;
import com.mrkj.cookbook.bean.MineList;
import com.mrkj.cookbook.service.MyService;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SecondActivity extends AppCompatActivity {

    @BindView(R.id.text1)
    TextView text1;
    @BindView(R.id.image2)
    ImageView image2;
    private MyService myService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        MineList.ResultEntity entity = (MineList.ResultEntity) intent.getSerializableExtra("show");
        myService = MineApplication.getMyService();
        if (entity == null) {
            Toast.makeText(this, "获取对象为空！", Toast.LENGTH_SHORT).show();
            return;
        }
        myService.getShow(initRequest(entity.getId()))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mineShow -> {
                    text1.setText(Html.fromHtml(mineShow.getResult().getMessage()));
                    Glide.with(this).load(mineShow.getResult().getImg()).into(image2);
                });
    }

    private Map<String, Object> initRequest(int id) {
        Map<String, Object> map = new HashMap<>();
        map.put("key", MyService.APP_KEY);
        map.put("id", id);
        return map;
    }
}
