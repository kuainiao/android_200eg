package com.mingrisoft.refreshandloading;

import android.app.Application;
import android.content.Context;

/**
 * Created by wangzeqi on 2016/5/27.
 */
public class BaseApplication extends Application {


    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }
    public static Context getContext() {
        return context;
    }

}
