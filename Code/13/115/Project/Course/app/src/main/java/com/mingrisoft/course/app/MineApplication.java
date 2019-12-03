package com.mingrisoft.course.app;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;

/**
 * Author LYJ
 * Created on 2016/12/28.
 * Time 16:48
 */

public class MineApplication extends Application{
    private static RequestQueue requestQueue;

    @Override
    public void onCreate() {
        super.onCreate();
        requestQueue = Volley.newRequestQueue(this);
        Logger.init("课程列表Demo").logLevel(LogLevel.FULL);
    }

    public static RequestQueue getRequestQueue() {
        return requestQueue;
    }
}
