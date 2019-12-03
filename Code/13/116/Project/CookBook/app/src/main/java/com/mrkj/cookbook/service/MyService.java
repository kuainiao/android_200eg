package com.mrkj.cookbook.service;

import com.mrkj.cookbook.bean.MineList;
import com.mrkj.cookbook.bean.MineShow;

import java.util.Map;

import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Author LYJ
 * Created on 2016/12/7.
 * Time 20:01
 */

public interface MyService {
    String APP_KEY = "380dfd6393bc4cd0b609b27e0ae0f8e8";//AppKey
    String HTTP_URL = "http://api.avatardata.cn/Cook/";//接口地址
    String LIST_URL = "List";//菜谱列表
    String SHOW_URL = "Show";//菜谱信息
    @POST(MyService.LIST_URL)//用于获取菜谱分类
    Observable<MineList> getList(@QueryMap Map<String,Object> map);
    @POST(MyService.SHOW_URL)//用于获取菜谱详情
    Observable<MineShow> getShow(@QueryMap Map<String,Object> map);
}
