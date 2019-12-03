package com.mrkj.cookbook.application;

import android.app.Application;

import com.mrkj.cookbook.service.MyService;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Author LYJ
 * Created on 2016/12/7.
 * Time 20:32
 */

public class MineApplication extends Application {
    private static Retrofit retrofit;
    private static MyService myService;

    /**
     * 获取Retrofit对象
     */
    public static Retrofit getRetrofit() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(MyService.HTTP_URL)//设置网址
                    //gson转换器
                    .addConverterFactory(GsonConverterFactory.create())
                    //rxJava转换器
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();//创建网络请求对象
        }
        return retrofit;
    }

    /**
     * 获取服务
     *
     * @return
     */
    public static MyService getMyService() {
        if (retrofit == null) {
            getRetrofit();
        }
        if (myService == null) {
            myService = retrofit.create(MyService.class);
        }
        return myService;
    }
}
