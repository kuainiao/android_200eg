package com.mingrisoft.smarthome.tool;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.mingrisoft.smarthome.home.BaseApplication;



public class QueueSingleton {

    private volatile static QueueSingleton instance;
    private RequestQueue queue;

    public static QueueSingleton getInstance() {
        if (instance == null) {
            synchronized (QueueSingleton.class) {
                if (instance == null) {
                    instance = new QueueSingleton();
                }
            }
        }
        return instance;
    }

    public RequestQueue getQueue() {
        if (queue == null) {
            queue = Volley.newRequestQueue(BaseApplication.getContext());
        }
        return queue;
    }

}
