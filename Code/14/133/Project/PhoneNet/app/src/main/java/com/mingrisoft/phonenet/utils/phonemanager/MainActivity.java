package com.mingrisoft.phonenet.utils.phonemanager;

import java.util.List;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;
import com.mingrisoft.phonenet.R;
import com.mingrisoft.phonenet.utils.TSP;
import com.mingrisoft.phonenet.utils.utils.TrafficInfo;


public class MainActivity extends ActionBarActivity {

	private TSP tsp;
	Handler mHandler;
	TrafficInfo speed;
	ManagerService service;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//全屏
		getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,
				WindowManager.LayoutParams. FLAG_FULLSCREEN);
		setContentView(R.layout.activity_main);
		tsp = (TSP) findViewById(R.id.wave_view);  //初始化画圆及画波的类
		try {
			mHandler = new Handler() {  //用handler进行传值
				@Override
				public void handleMessage(Message msg) {
					if (msg.what == 1) {
						Double a = (Double) msg.obj;
						int b = (int) (a*10);  //将网速装换成int行
						tsp.setWaveHeight(b);  //设置水波的高度
						if(service != null)
							service.setSpeed(b); //将int值传给悬浮窗
					}
					super.handleMessage(msg);
				}
			};
			speed = new TrafficInfo(this,mHandler,10035);
			speed.startCalculateNetSpeed();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Log.i("test","总流量="+speed.getTrafficInfo());
		Intent intent = new Intent(MainActivity.this, ManagerService.class);  
		bindService(intent, conn, Context.BIND_AUTO_CREATE);

	}
	
	private ServiceConnection conn = new ServiceConnection() {
        /** 获取服务对象时的操作 */
        public void onServiceConnected(ComponentName name, IBinder binder) {
        	service = ((ManagerService.ServiceBinder) binder).getService();
        }
        /** 无法获取到服务对象时的操作 */
        public void onServiceDisconnected(ComponentName name) {
            // TODO Auto-generated method stub
        	service = null;
        }
    };

	@Override
	protected void onPause() {
		super.onPause();
		tsp.stop();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		speed.stopCalculateNetSpeed();
		unbindService(conn);

	}
}
