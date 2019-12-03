package com.mingrisoft.nfcread;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;

public class AppListView extends ListActivity implements OnItemClickListener {
	private List<String> mPackages = new ArrayList<String>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		PackageManager packageManager = getPackageManager();
		List<PackageInfo> packageInfos = packageManager
				.getInstalledPackages(PackageManager.GET_ACTIVITIES);
		for (PackageInfo packageInfo : packageInfos) {
            //当选择应用后，会回调并传回
			mPackages.add(packageInfo.applicationInfo.loadLabel(packageManager)
					+ "\n" + packageInfo.packageName);
		}
         //适配器装载应用列表信息
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, android.R.id.text1,
				mPackages);
		setListAdapter(arrayAdapter);
		getListView().setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		//回传信息
		Intent intent = new Intent();
		intent.putExtra("package_name", mPackages.get(position));
		setResult(1, intent);
		finish();
	}

}
