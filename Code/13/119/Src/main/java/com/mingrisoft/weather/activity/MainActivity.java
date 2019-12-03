package com.mingrisoft.weather.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.mingrisoft.weather.R;
import com.mingrisoft.weather.app.MyApplication;
import com.mingrisoft.weather.clazz.Bean;
import com.mingrisoft.weather.clazz.Contant;
import com.mingrisoft.weather.utils.GetLocation;
import com.mingrisoft.weather.view.MyWeatherDialog;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity implements GetLocation.Callback {

    private static final String TAG = MainActivity.class.getSimpleName();
    private RequestQueue requestQueue;
    private TextView cityName, gDate, nDate, weekDay, temperature, weather, pm25, quality;
    private TextView jianYi, chuanyi, ganmao, kongtiao, xiche, yundong, ziwaixian;
    private TextView chuanyiJY, ganmaoJY, kongtiaoJY, xicheJY, yundongJY, ziwaixianJY;
    private boolean show;
    private MyWeatherDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //网络请求队列
        requestQueue = MyApplication.getRequestQueue(this);
        //开启定位获取城市名称
        new GetLocation(this).addCallback(this);
        findID();//获取控件
        dialog = new MyWeatherDialog(this);//弹窗

    }


    /**
     * 未来五天
     */
    public void open(View view) {
        if (show) dialog.show();
    }

    /**
     * 初始化控件
     */
    private void findID() {
        cityName = (TextView) findViewById(R.id.city_name);
        gDate = (TextView) findViewById(R.id.g_date);
        nDate = (TextView) findViewById(R.id.n_date);
        weekDay = (TextView) findViewById(R.id.week_day);
        temperature = (TextView) findViewById(R.id.temperature);
        weather = (TextView) findViewById(R.id.weather);
        pm25 = (TextView) findViewById(R.id.pm_25);
        quality = (TextView) findViewById(R.id.quality);
        jianYi = (TextView) findViewById(R.id.jianyi);
        //******************************************
        chuanyi = (TextView) findViewById(R.id.chuanyi);
        ganmao = (TextView) findViewById(R.id.ganmao);
        kongtiao = (TextView) findViewById(R.id.kongtiao);
        xiche = (TextView) findViewById(R.id.xiche);
        yundong = (TextView) findViewById(R.id.yundong);
        ziwaixian = (TextView) findViewById(R.id.ziwaixian);
        chuanyiJY = (TextView) findViewById(R.id.chuanyi_jy);
        ganmaoJY = (TextView) findViewById(R.id.ganmao_jy);
        kongtiaoJY = (TextView) findViewById(R.id.kongtiao_jy);
        xicheJY = (TextView) findViewById(R.id.xiche_jy);
        yundongJY = (TextView) findViewById(R.id.yundong_jy);
        ziwaixianJY = (TextView) findViewById(R.id.ziwaixian_jy);
    }

    /**
     * 获取数据
     */
    private void getData(final String name) {
        Log.i("=========", "getData");
        StringRequest request = new StringRequest(Request.Method.POST, Contant.HTTP_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i(TAG, "onResponse: " + response);
                //解析返回的数据
                Bean bean = new Gson().fromJson(response, Bean.class);

                if (TextUtils.equals("successed!", bean.getReason()) || TextUtils.equals("查询成功!", bean.getReason())) {
                    show = true;
                    cityName.setText(bean.getResult().getData().getRealtime().getCity_name());
                    gDate.setText(bean.getResult().getData().getRealtime().getDate());
                    nDate.setText(bean.getResult().getData().getRealtime().getMoon());
                    weekDay.setText(weekStr(bean.getResult().getData().getRealtime().getWeek()));
                    temperature.setText(bean.getResult().getData().getRealtime().getWeather().getTemperature() + "℃");
                    weather.setText(bean.getResult().getData().getRealtime().getWeather().getInfo());
                    pm25.setText("PM2.5：" + bean.getResult().getData().getPm25().getPm25().getPm25());
                    quality.setText("空气质量：" + bean.getResult().getData().getPm25().getPm25().getQuality());
                    jianYi.setText("外出建议：" + bean.getResult().getData().getPm25().getPm25().getDes());
                    //加载指数信息
                    addDsc(bean.getResult().getData());
                    dialog.setWeather(bean.getResult().getData().getWeather());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "onErrorResponse: " + error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("cityname", name);//设置键值对
                map.put("key", Contant.APP_KEY);//设置键值对
                return map;
            }
        };
        requestQueue.add(request);
    }

    private String weekStr(int week) {
        String weekStr = "数据异常";
        switch (week) {
            case 1:
                weekStr = "星期一";
                break;
            case 2:
                weekStr = "星期二";
                break;
            case 3:
                weekStr = "星期三";
                break;
            case 4:
                weekStr = "星期四";
                break;
            case 5:
                weekStr = "星期五";
                break;
            case 6:
                weekStr = "星期六";
                break;
            case 7:
                weekStr = "星期七";
                break;
        }
        return weekStr;
    }

    /**
     * 获取设备所在的城市名称
     *
     * @param cityName
     */
    @Override
    public void getCityName(String cityName) {
        Log.i("=========", "getCityName");
        getData(cityName);//获取天气预报数据
    }

    private void addDsc(Bean.ResultEntity.DataEntity dataEntity) {
        chuanyi.setText("穿衣指数：" + dataEntity.getLife().getInfo().getChuanyi().get(0));
        ganmao.setText("感冒指数：" + dataEntity.getLife().getInfo().getGanmao().get(0));
        kongtiao.setText("空调指数：" + dataEntity.getLife().getInfo().getKongtiao().get(0));
        xiche.setText("洗车指数：" + dataEntity.getLife().getInfo().getXiche().get(0));
        yundong.setText("运动指数：" + dataEntity.getLife().getInfo().getYundong().get(0));
        ziwaixian.setText("紫外线指数：" + dataEntity.getLife().getInfo().getZiwaixian().get(0));
        chuanyiJY.setText("建议：" + dataEntity.getLife().getInfo().getChuanyi().get(1));
        ganmaoJY.setText("建议：" + dataEntity.getLife().getInfo().getGanmao().get(1));
        kongtiaoJY.setText("建议：" + dataEntity.getLife().getInfo().getKongtiao().get(1));
        xicheJY.setText("建议：" + dataEntity.getLife().getInfo().getXiche().get(1));
        yundongJY.setText("建议：" + dataEntity.getLife().getInfo().getYundong().get(1));
        ziwaixianJY.setText("建议：" + dataEntity.getLife().getInfo().getZiwaixian().get(1));
    }

}
