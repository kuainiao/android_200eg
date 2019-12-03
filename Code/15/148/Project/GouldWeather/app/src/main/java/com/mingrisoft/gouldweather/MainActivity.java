package com.mingrisoft.gouldweather;

import android.app.Activity;

import android.os.Bundle;
import android.widget.TextView;

import com.amap.api.services.core.AMapException;
import com.amap.api.services.weather.LocalDayWeatherForecast;
import com.amap.api.services.weather.LocalWeatherForecast;
import com.amap.api.services.weather.LocalWeatherForecastResult;
import com.amap.api.services.weather.LocalWeatherLive;
import com.amap.api.services.weather.LocalWeatherLiveResult;
import com.amap.api.services.weather.WeatherSearch;
import com.amap.api.services.weather.WeatherSearchQuery;

import java.util.List;

import util.ToastUtil;

public class MainActivity extends Activity implements WeatherSearch.OnWeatherSearchListener {
    private TextView forecasttv;            //定义显示预报信息文本框控件
    private TextView reporttime1;           //显示实时天气发布时间
    private TextView reporttime2;           //显示预报发布时间
    private TextView weather;               //显示天气状况
    private TextView Temperature;           //显示温度
    private TextView wind;                   //显示风向
    private TextView humidity;              //显示实时湿度
    private WeatherSearchQuery mquery;      //定义检索城市和天气类型
    private WeatherSearch mweathersearch;  //创建天气查询对象
    private LocalWeatherLive weatherlive;   //实时天气返回结果对象
    private LocalWeatherForecast weatherforecast;   //预报天气返回结果对象
    private List<LocalDayWeatherForecast> forecastlist = null;
    private String cityname="北京市";//天气搜索的城市，可以写名称或adcode；
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();                 //调用初始化控件方法
        searchliveweather();    //调用检索城市实时天气方法
        searchforcastsweather();//调用检索城市的预报天气方法
    }

    /**
     * 初始化显示控件
     */
    private void init() {
        TextView city =(TextView)findViewById(R.id.city);
        city.setText(cityname);
        forecasttv=(TextView)findViewById(R.id.forecast);         //获取显示预报信息文本框控件
        reporttime1 = (TextView)findViewById(R.id.reporttime1);  //获取显示实时天气发布时间控件
        reporttime2 = (TextView)findViewById(R.id.reporttime2);  //获取显示预报发布时间
        weather = (TextView)findViewById(R.id.weather);           //获取显示天气状况
        Temperature = (TextView)findViewById(R.id.temp);          //获取显示温度
        wind=(TextView)findViewById(R.id.wind);                    //获取显示风向
        humidity = (TextView)findViewById(R.id.humidity);         //获取显示实时湿度
    }

    /**
     * 检索城市的预报天气
     */
    private void searchforcastsweather() {
        //检索参数为城市和天气类型，预报天气为2
        mquery = new WeatherSearchQuery(cityname, WeatherSearchQuery.WEATHER_TYPE_FORECAST);
        mweathersearch=new WeatherSearch(this);                 //创建天气查询对象
        mweathersearch.setOnWeatherSearchListener(this);        //设置天气信息查询监听
        mweathersearch.setQuery(mquery);                        //设置天气查询条件
        mweathersearch.searchWeatherAsyn(); //异步搜索
    }

    /**
     * 检索城市实时天气
     */
    private void searchliveweather() {
        //检索参数为城市和天气类型，实时天气为1
        mquery = new WeatherSearchQuery(cityname, WeatherSearchQuery.WEATHER_TYPE_LIVE);
        mweathersearch=new WeatherSearch(this);                 //创建天气查询对象
        mweathersearch.setOnWeatherSearchListener(this);        //设置天气信息查询监听
        mweathersearch.setQuery(mquery);                        //设置天气查询条件
        mweathersearch.searchWeatherAsyn(); //异步搜索
    }
    /**
     * 实时天气查询回调
     */
    @Override
    public void onWeatherLiveSearched(LocalWeatherLiveResult weatherLiveResult , int rCode) {
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
            if (weatherLiveResult != null && weatherLiveResult.getLiveResult() != null) {
                weatherlive = weatherLiveResult.getLiveResult();            //获取实时天气结果
                reporttime1.setText(weatherlive.getReportTime()+"发布");  //显示发布时间
                weather.setText(weatherlive.getWeather());                 //显示天气状况
                Temperature.setText(weatherlive.getTemperature()+"°");   //显示实时温度
                //显示风力
                wind.setText(weatherlive.getWindDirection()+"风:"+weatherlive.getWindPower()+"级");
                //显示湿度
                humidity.setText("湿度:"+weatherlive.getHumidity()+"%");
            }else {
                //提示没有得到相关数据
                ToastUtil.show(MainActivity.this, R.string.no_result);
            }
        }else {
            //提示错误代码
            ToastUtil.showerror(MainActivity.this, rCode);
        }
    }
    /**
     * 天气预报查询结果回调
     * */
    @Override
    public void onWeatherForecastSearched(
            LocalWeatherForecastResult weatherForecastResult, int rCode) {
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
            if (weatherForecastResult!=null && weatherForecastResult.getForecastResult()!=null
                    && weatherForecastResult.getForecastResult().getWeatherForecast()!=null
                    && weatherForecastResult.getForecastResult().getWeatherForecast().size()>0) {
                weatherforecast = weatherForecastResult.getForecastResult();
                forecastlist= weatherforecast.getWeatherForecast();
                fillforecast();

            }else {
                //提示没有得到相关数据
                ToastUtil.show(MainActivity.this, R.string.no_result);
            }
        }else {
            ToastUtil.showerror(MainActivity.this, rCode);
        }
    }
    private void fillforecast() {
        reporttime2.setText(weatherforecast.getReportTime()+"发布");  //显示发布时间
        String forecast="";
        for (int i = 0; i < forecastlist.size(); i++) {
            LocalDayWeatherForecast localdayweatherforecast=forecastlist.get(i);
            String week = null;
            //便利星期
            switch (Integer.valueOf(localdayweatherforecast.getWeek())) {
                case 1:
                    week = "周一";
                    break;
                case 2:
                    week = "周二";
                    break;
                case 3:
                    week = "周三";
                    break;
                case 4:
                    week = "周四";
                    break;
                case 5:
                    week = "周五";
                    break;
                case 6:
                    week = "周六";
                    break;
                case 7:
                    week = "周日";
                    break;
                default:
                    break;
            }
            String temp =String.format("%-3s/%3s",
                    localdayweatherforecast.getDayTemp()+"°",       //获取白天温度
                    localdayweatherforecast.getNightTemp()+"°");    //获取夜晚温度
            String date = localdayweatherforecast.getDate();         //获取日期
            forecast+=date+"  "+week+"                       "+temp+"\n\n";
        }
        forecasttv.setText(forecast);       //显示日期、星期、度数
    }
}
