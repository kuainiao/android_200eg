package com.mingrisoft.selectimagecode.app;

import android.app.Application;

import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;

/**
 * Author LYJ
 * Created on 2017/1/23.
 * Time 09:06
 */

public class App extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        Logger.init("A5").logLevel(LogLevel.FULL);
    }
}
