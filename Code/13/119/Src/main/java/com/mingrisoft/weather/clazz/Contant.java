package com.mingrisoft.weather.clazz;

/**
 * Author LYJ
 * Created on 2016/12/5.
 * Time 15:55
 */

public interface Contant {

    //    cityname	string	是	要查询的城市，如：温州、上海、北京，需要utf8 urlencode
    //    key	string	是	应用APPKEY(应用详细页查询)
    //    dtype	string	否	返回数据的格式,xml或json，默认json
    String APP_KEY = "06ba330de85cf5484fedbcd1c2247e28";
    String HTTP_URL = "http://op.juhe.cn/onebox/weather/query";
}
