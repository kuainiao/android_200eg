package com.mingrisoft.news.listener;

/**
 * Author LYJ
 * Created on 2016/12/6.
 * Time 12:38
 */

public interface Contant {


//    名称	类型	必填	说明
//    key	String	是	应用APPKEY
//    page	Int	否	请求页数，默认page=1
//    rows	Int	否	返回记录条数，默认rows=20,最大50
//    dtype	String	否	返回结果格式：可选JSON/XML，默认为JSON
//    format	Boolean	否	当返回结果格式为JSON时，是否对其进行格式化，为了节省流量默认为false，测试时您可以传入true来熟悉返回内容
    String APPKEY = "796e5ebaab5e42638dd0fab33bf0b256";
    String HTTPURL = "http://api.avatardata.cn/GuoNeiNews/Query";
}
