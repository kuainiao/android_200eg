package com.mingrisoft.dzglthreeld;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements OnClickListener {
    //声明所在地区显示控件
    private TextView tv_city1;
    //声明城市实体类
    private City city ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //装载布局
        setContentView(R.layout.activity_main);
        //选择城市所在地区
        tv_city1 = (TextView) findViewById(R.id.tv_city1);
        //绑定点击事件
        tv_city1.setOnClickListener(this);
        //实例化城市实体类
        city = new City();
    }
    //点击事件
    @Override
    public void onClick(View v) {
        if(v == tv_city1){
            //跳转到城市选择页面
            Intent in = new Intent(this, CitySelect1Activity.class);
            in.putExtra("city", city);
            //回调“1”
            startActivityForResult(in, 1);
        }
    }
    // 接受回调信息
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 8){
            //接收返回地区信息
            if(requestCode == 1){
                //获取城市数据
                city = data.getParcelableExtra("city");
                //设置显示城市位置
                tv_city1.setText(city.getProvince()+"-"+city.getCity()+"-"+city.getDistrict());
            }
        }
    }
}

