package com.mingrisoft.qrobot;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Environment;


import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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
