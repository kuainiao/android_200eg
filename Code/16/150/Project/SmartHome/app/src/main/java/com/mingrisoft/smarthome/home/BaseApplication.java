package com.mingrisoft.smarthome.home;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by wangzeqi on 2016/5/27.
 */
public class BaseApplication extends Application {

    private static Context context;
    private static Map<String, Activity> destroyMap = new HashMap<>();
    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }
    public static Context getContext() {
        return context;
    }


    /**
     * 添加到销毁的列队
     * <p/>
     * 要销毁的activity
     */
    public static void addDestroyActiivty(Activity activity, String activityName) {
        destroyMap.put(activityName, activity);
    }


    /**
     * 销毁指定的activity
     */
    public static void destroyActivity(String activityName) {
        Set<String> keySet = destroyMap.keySet();
        for (String key : keySet) {
            destroyMap.get(key).finish();
        }
    }
}
