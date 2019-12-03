package com.mingrisoft.webserver;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import org.apache.http.conn.util.InetAddressUtils;
import com.mingrisoft.webserver.server.WebService;
import com.mingrisoft.webserver.util.CopyUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class WFSActivity extends Activity implements OnCheckedChangeListener {

	private static final String TAG = "WFSActivity";
	private ToggleButton toggleBtn;
	private TextView urlText;
	private Intent intent;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		initViews();
		initFiles();

		intent = new Intent(this, WebService.class);
	}

	private void initViews() {
		toggleBtn = (ToggleButton) findViewById(R.id.toggleBtn);
		toggleBtn.setOnCheckedChangeListener(this);
		urlText = (TextView) findViewById(R.id.urlText);
	}

	
	private void initFiles() {
		new CopyUtil(this).assetsCopy();
	}
	
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		Log.e(TAG, "=============isChecked================="+isChecked);
		if (isChecked) {
			String ip = getLocalIpAddressIP4();
			if (ip == null) {
				Toast.makeText(this, R.string.msg_net_off, Toast.LENGTH_SHORT).show();
				urlText.setText("");
			} else {
				startService(intent);
				urlText.setText("http://" + ip + ":" + WebService.PORT + "/");
			}
		} else {
			stopService(intent);
			urlText.setText("");
		}
	}
	
	/** 获取当前IP地址 */
	private String getLocalIpAddress() {
		try {
			// 遍历网络接口
			for (Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces(); en.hasMoreElements();)

			{
				NetworkInterface intf = en.nextElement();
				// 遍历IP地址
				for (Enumeration<InetAddress> enumIpAddr = intf
						.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					// 非回传地址时返回
					if (!inetAddress.isLoopbackAddress()) {
						return inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (SocketException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/*
	 * 获取手机ip地址
	 */
	public String getLocalIpAddressIP4() {
		try {
			String ipv4;
			List<NetworkInterface> nilist = Collections.list(NetworkInterface.getNetworkInterfaces());
			for (NetworkInterface ni : nilist) {
				List<InetAddress> ialist = Collections.list(ni
						.getInetAddresses());
				for (InetAddress address : ialist) {
					if (!address.isLoopbackAddress()
							&& InetAddressUtils.isIPv4Address(ipv4 = address
									.getHostAddress())) {
						return ipv4;
					}
				}

			}

		} catch (SocketException ex) {
		}
		return null;
	}
	
	
	@Override
	public void onBackPressed() {
		if (intent != null) {
			stopService(intent);
		}
		super.onBackPressed();
	}

	
	
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

}