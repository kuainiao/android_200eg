package com.mingrisoft.nfcread;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {

	private Button button;
	private String appPackageName;
	private NfcAdapter nfcAdapter;
	private PendingIntent pendingIntent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		button = (Button) findViewById(R.id.button);
		nfcAdapter = NfcAdapter.getDefaultAdapter(this);
		//Intent是立即使用的，而PendingIntent可以等到事件发生后触发，PendingIntent可以cancel
		//Intent在程序结束后即终止，而PendingIntent在程序结束后依然有效
		//PendingIntent自带Context，而Intent需要在某个Context内运行
		// Intent在原task中运行，PendingIntent在新的task中运行
		//一旦截获NFC消息，就会通过PendingIntent调用窗口
		pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,
				getClass()), 0);
	}
	//先将nfc标签纸放入手机背面会执行 写入内容 如果不为空，就将所选择的应用写入标签纸
	//然后退出应用后，将标签纸放入手机背面就会自动执行所选择的应用程序
	//因为当前是singleTop模式，所以当重新靠近NFC标签设备的时候，不会走onCreate函数，会响应onNewIntent函数
	public void onNewIntent(Intent intent) {
		if (appPackageName == null)
			return;
		//获取标签实例进行读写
		Tag detectedTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
		//写入标签内容方法
		writeNFCTag(detectedTag);
	}

	public void onResume() {
		super.onResume();
        //设置处理优于所有其他NFC的处理
		if (nfcAdapter != null)
			// 开始监听NFC设备是否连接，如果连接就发pi意图
			nfcAdapter
					.enableForegroundDispatch(this, pendingIntent, null, null);
	}

	public void onPause() {
		super.onPause();
//		设置NFCAdapter 恢复默认状态
		if (nfcAdapter != null)
			nfcAdapter.disableForegroundDispatch(this);
	}

	public void onClick(View view) {
		//跳转到应用选择页
		Intent intent = new Intent(this, AppListView.class);
		startActivityForResult(intent, 0);
	}

	/**
	 * 往标签写数据的方法
	 *
	 * @param tag
	 */
	public void writeNFCTag(Tag tag) {
		if (tag == null) {
			return;
		}
        //封装标签消息
		//NdefRecord.createApplicationRecord("要打开应用包名");//打开某个APP
		//NdefRecord.createUri("网址");//打开网页
		NdefMessage ndefMessage = new NdefMessage(
				new NdefRecord[] { NdefRecord
						.createApplicationRecord(appPackageName) });
		//转换成字节获得大小
		int size = ndefMessage.toByteArray().length;
		try {
			//判断NFC标签的数据类型（通过Ndef.get方法）
			Ndef ndef = Ndef.get(tag);
			//判断是否为NDEF标签
			if (ndef != null) {
				ndef.connect();
				//判断是否支持可写
				if (!ndef.isWritable()) {
					return;
				}
				//判断标签的容量是否够用
				if (ndef.getMaxSize() < size) {
					return;
				}
				//写入数据
				ndef.writeNdefMessage(ndefMessage);
				Toast.makeText(this, "写入成功，请退出应用后再扫描标签", Toast.LENGTH_LONG).show();
			}else {//当我们买回来的NFC标签是没有格式化的，或者没有分区的执行此步
				//Ndef格式类
				NdefFormatable format = NdefFormatable.get(tag);
				//判断是否获得了NdefFormatable对象，有一些标签是只读的或者不允许格式化的
				if (format != null) {
					//连接
					format.connect();
					//格式化并将信息写入标签
					format.format(ndefMessage);
					Toast.makeText(this, "写入成功", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(this, "写入失败", Toast.LENGTH_SHORT).show();
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		//接收回调信息 ，当选择应用后，会回调并传回
		if (resultCode == 1) {
			button.setText(data.getExtras().getString("package_name"));
			String temp = button.getText().toString();
			appPackageName = temp.substring(temp.indexOf("\n") + 1);
		}
	}
}
