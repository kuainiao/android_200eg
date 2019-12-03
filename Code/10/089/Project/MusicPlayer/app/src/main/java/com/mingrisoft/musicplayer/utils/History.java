package com.mingrisoft.musicplayer.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Author LYJ
 * Created on 2017/1/17.
 * Time 10:58
 */

public class History {
    private static Context context;
    private History(){}
    private static class SingleTon{
        public static History INSTANCE = new History();
    }
    public static History getInstance(Context mContext){
        context = mContext.getApplicationContext();
        return SingleTon.INSTANCE;
    }

    /**
     * 获取记录
     * @return
     */
    public SharedPreferences getHistoryRecord(){
        return context.getSharedPreferences(context.getPackageName(),Context.MODE_PRIVATE);
    }

    /**
     * 取值
     * @param key
     * @param defaultValues
     * @return
     */
    public int getInt(String key,int defaultValues){
        return getHistoryRecord().getInt(key,defaultValues);
    }
    /**
     * 获取写入对象
     * @return
     */
    public boolean putInt(String key,int values){
        SharedPreferences.Editor editor = getHistoryRecord().edit().putInt(key,values);
        return editor.commit();
    }

}
