package com.mingrisoft.musicplayer.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Author LYJ
 * Created on 2017/1/16.
 * Time 13:28
 */

public class ToastUtils {

    /**
     * 弹出短时间的吐司
     * @param context
     * @param message
     */
    public static void Short(Context context,String message){
        Toast.makeText(context.getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    /**
     * 弹出长时间的吐司
     * @param context
     * @param message
     */
    public static void Long(Context context,String message){
        Toast.makeText(context.getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }
}
