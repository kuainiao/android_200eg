package com.mingrisoft.weather.app;

import android.app.Activity;
import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Author LYJ
 * Created on 2016/12/5.
 * Time 15:45
 */

public class MyApplication extends Application{
    private static RequestQueue queue;//网络请求队列
    /**
     * 获取请求队列
     * @param activity
     * @return
     */
    public static RequestQueue getRequestQueue(Activity activity){
        if (queue == null){
            queue = Volley.newRequestQueue(activity.getApplicationContext());
        }
        return queue;
    }
}
