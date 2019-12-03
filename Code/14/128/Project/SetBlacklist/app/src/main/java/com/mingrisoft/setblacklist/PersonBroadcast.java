package com.mingrisoft.setblacklist;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.mingrisoft.setblacklist.entity.DaoMaster;
import com.mingrisoft.setblacklist.entity.DaoSession;
import com.mingrisoft.setblacklist.entity.PersonEntityDao;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static android.content.ContentValues.TAG;

/**
 * Created by Administrator on 2016/12/27.
 */


public class PersonBroadcast extends BroadcastReceiver {
    // 数据库
    private SQLiteDatabase db;
    // 管理者
    private DaoMaster mDaoMaster;
    // 会话
    private DaoSession mDaoSession;
    private PersonEntityDao entityDao;
    private Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("action" + intent.getAction());
        this.context = context;
//如果是去电
        if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
            String phoneNumber = intent
                    .getStringExtra(Intent.EXTRA_PHONE_NUMBER);
            Log.d(TAG, "call OUT:" + phoneNumber);
        } else {
//查了下android文档，貌似没有专门用于接收来电的action,所以，非去电即来电.
//如果我们想要监听电话的拨打状况，需要这么几步 :
//            /* 第一：获取电话服务管理器TelephonyManager manager = this.getSystemService(TELEPHONY_SERVICE);
//            * 第二：通过TelephonyManager注册我们要监听的电话状态改变事件。manager.listen(new MyPhoneStateListener(),
//                    * PhoneStateListener.LISTEN_CALL_STATE);这里的PhoneStateListener.LISTEN_CALL_STATE就是我们想要
//                    * 监听的状态改变事件，初次之外，还有很多其他事件哦。
//            * 第三步：通过extends PhoneStateListener来定制自己的规则。将其对象传递给第二步作为参数。
//            * 第四步：这一步很重要，那就是给应用添加权限。android.permission.READ_PHONE_STATE
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Service.TELEPHONY_SERVICE);
            tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
//设置一个监听器
        }
        // 此DevOpenHelper类继承自SQLiteOpenHelper,第一个参数Context,第二个参数数据库名字,第三个参数CursorFactory
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, "persondao.db", null);
        db = helper.getWritableDatabase();
        mDaoMaster = new DaoMaster(db);
        mDaoSession = mDaoMaster.newSession();
        entityDao = mDaoSession.getPersonEntityDao();
    }

    PhoneStateListener listener = new PhoneStateListener() {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            //注意，方法必须写在super方法后面，否则incomingNumber无法获取到值。
            super.onCallStateChanged(state, incomingNumber);
            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE:
                    Log.e("------", "挂断");
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    Log.e("------", "接听");
                    break;
                case TelephonyManager.CALL_STATE_RINGING:
                    Log.e("------", "响铃:来电号码" + incomingNumber);
                    for (int i = 0; i < entityDao.count(); i++) {
                        //判断来电的号码是否已经加入到黑名单中
                        if (incomingNumber.equals(entityDao.queryBuilder().build().list().get(i).getNumber())) {
                            endCall(context);   //如果来电号码在黑名单中，进行挂断电话
                            break;
                        }
                    }
                    break;
            }
        }
    };

    /* 挂断电话
    * @param context
    */
    public static void endCall(Context context) {
        try {
            Object telephonyObject = getTelephonyObject(context);
            if (null != telephonyObject) {
                Class telephonyClass = telephonyObject.getClass();      //初始化手机管理者
                Method endCallMethod = telephonyClass.getMethod("endCall");  // 挂断电话
                endCallMethod.setAccessible(true);                       //允许
                endCallMethod.invoke(telephonyObject);
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

    }

    private static Object getTelephonyObject(Context context) {
        Object telephonyObject = null;
        try {
            // 初始化iTelephony
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            // Will be used to invoke hidden methods with reflection
            // Get the current object implementing ITelephony interface
            Class telManager = telephonyManager.getClass();
            Method getITelephony = telManager.getDeclaredMethod("getITelephony");
            getITelephony.setAccessible(true);
            telephonyObject = getITelephony.invoke(telephonyManager);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return telephonyObject;
    }
}
