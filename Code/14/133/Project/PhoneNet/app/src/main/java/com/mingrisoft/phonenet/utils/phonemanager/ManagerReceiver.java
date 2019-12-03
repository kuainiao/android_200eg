package com.mingrisoft.phonenet.utils.phonemanager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ManagerReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent arg1) {
		context.startService(new Intent(context, ManagerService.class));

	}

}
