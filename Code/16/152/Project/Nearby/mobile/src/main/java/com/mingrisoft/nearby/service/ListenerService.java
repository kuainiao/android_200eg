package com.mingrisoft.nearby.service;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.LocalBroadcastManager;

import com.google.android.gms.common.data.FreezableUtils;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.WearableListenerService;

import java.util.List;

public class ListenerService extends WearableListenerService {

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        super.onDataChanged(dataEvents);
        final List<DataEvent> events = FreezableUtils.freezeIterable(dataEvents);
        for(DataEvent event : events) {
            final Uri uri = event.getDataItem().getUri();//获取消息的uri
            final String path = uri!=null ? uri.getPath() : null;//获取标识
            if("/KEY".equals(path)) {//判断标识
                final DataMap map = DataMapItem//获取DataMap对象
                        .fromDataItem(event.getDataItem()).getDataMap();
                String key = map.get("keyword");//获取消息内容
                Intent localIntent = new Intent("phone.localIntent");//发送广播意图
                localIntent.putExtra("result", key);//添加附加内容
                //发送广播
                LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);
            }
        }
    }
}