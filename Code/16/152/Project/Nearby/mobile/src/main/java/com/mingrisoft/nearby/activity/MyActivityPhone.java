package com.mingrisoft.nearby.activity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.Wearable;
import com.mingrisoft.nearby.R;
import com.mingrisoft.nearby.utils.LocationListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MyActivityPhone extends MPermissionsActivity implements GoogleApiClient.ConnectionCallbacks{
    private GoogleApiClient mGoogleApiClient;//谷歌服务
    private BroadcastReceiver mResultReceiver;//谷歌接收器
    private PoiSearch mPoiSearch;//poi检索
    private MapView mMapView = null;//百度地图控件
    private BaiduMap mBaiduMap;//百度地图设置
    private LocationClient locationClient;//位置服务
    private LatLng mPoint;//经纬度数据
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_my_phone);
        //获取地图控件引用
        mMapView = (MapView) findViewById(R.id.bmapView);
        locationClient = new LocationClient(getApplicationContext());
        initLocation();//初始化
        locationClient.registerLocationListener(locationListener);
        mBaiduMap = mMapView.getMap();
        mMapView.showScaleControl(true);//显示比例尺
        mMapView.showZoomControls(true);//显示缩放按钮
        //设置缩放，确保屏幕内有我
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(18));
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addApi(Wearable.API)
                .build();
        mGoogleApiClient.connect();//创建连接
        mResultReceiver = createBroadcastReceiver();
        LocalBroadcastManager.getInstance(this).registerReceiver(
                mResultReceiver, new IntentFilter("phone.localIntent"));
        mPoiSearch = PoiSearch.newInstance();//获取检索对向
        mPoiSearch.setOnGetPoiSearchResultListener(poiListener);//何止检索监听
        requestPermission(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,//申请权限
            Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_PHONE_STATE},1000);
    }

    /**
     * 权限申请成功
     * @param requestCode
     */
    @Override
    public void permissionSuccess(int requestCode) {
        super.permissionSuccess(requestCode);
        if(1000 == requestCode){
            locationClient.start();//开启定位服务
        }
    }

    /**
     * 权限申请失败
     * @param requestCode
     */
    @Override
    public void permissionFail(int requestCode) {
        super.permissionFail(requestCode);
        if(1000 == requestCode){
            Toast.makeText(this, "未获取相关权限！", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     *
     * 向手表发送POI检索数据
     * @param dataMapList
     */
    private void sendDataToWear(ArrayList<DataMap> dataMapList){
        PutDataMapRequest dataMapRequest = PutDataMapRequest.create("/POI");
        DataMap dataMap = dataMapRequest.getDataMap();//获取消息的载体
        dataMap.putLong("time" , System.currentTimeMillis());//保证Wear设备能接收到消息
        dataMap.putDataMapArrayList("data",dataMapList);//检索数据集合
        Wearable.DataApi//发送数据
                .putDataItem(mGoogleApiClient,dataMapRequest.asPutDataRequest());
    }
    /**
     * 执行附近检索
     * @param key
     */
    private void POI (String key){
        mPoiSearch.searchNearby(new PoiNearbySearchOption()
                .keyword(key)//美食、饮品、购物、休闲
                .location(mPoint)//检索位置
                .radius(1000)//检索范围
        );
    }

    /**
     * 检索数据的回调
     */
    OnGetPoiSearchResultListener poiListener = new OnGetPoiSearchResultListener(){
        public void onGetPoiResult(PoiResult result){
            //获取POI检索结果
            Log.i("onGetPoiResult", "onGetPoiResult: ");
            int resultCount = result.getTotalPoiNum();//检索总数
            Log.i("检索总数", resultCount + "");
            List<PoiInfo> resultAllPoi = result.getAllPoi();//检索数据总数
            Log.w("检索总数", resultAllPoi.size() + "");
            Iterator<PoiInfo> iterator = resultAllPoi.iterator();
            ArrayList<DataMap> dataMapList = new ArrayList<>();
            DataMap dataMap;
            while (iterator.hasNext()){
                PoiInfo poiInfo = iterator.next();
                dataMap = new DataMap();
                dataMap.putString("address" ,  poiInfo.address);//地址
                dataMap.putString("city" ,  poiInfo.city);//城市
                dataMap.putString("name" ,  poiInfo.name);//名称
                dataMap.putString("phoneNum" ,  poiInfo.phoneNum);//电话
                dataMapList.add(dataMap);//将数据添加进集合
            }
            sendDataToWear(dataMapList);//发送数据
        }
        public void onGetPoiDetailResult(PoiDetailResult result){
            //获取Place详情页检索结果
            Log.i("PoiDetailResult", "onGetPoiResult: ");
        }

        @Override
        public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {
            //室内检索结果
            Log.i("onGetPoiIndoorResult", "onGetPoiResult: ");
        }
    };
    private BDLocationListener locationListener = new LocationListener() {
        @Override
        public void onReceiveLocation(BDLocation location) {
            super.onReceiveLocation(location);
            mBaiduMap.clear();
            //开始移动
            MapStatusUpdate mapLatlng = MapStatusUpdateFactory.
                    newLatLng(new LatLng(location.getLatitude(),location.getLongitude()));
            mBaiduMap.setMapStatus(mapLatlng);
            //定义Maker坐标点
            LatLng point = new LatLng(location.getLatitude(), location.getLongitude());
            mPoint = point;
            //构建Marker图标
            BitmapDescriptor bitmap = BitmapDescriptorFactory
                    .fromResource(R.mipmap.location);
            //构建MarkerOption，用于在地图上添加Marker
            OverlayOptions option = new MarkerOptions()
                    .position(point)
                    .icon(bitmap);
            //在地图上添加Marker，并显示
            mBaiduMap.addOverlay(option);
        }
    };

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");
        //可选，默认gcj02，设置返回的定位结果坐标系
        int span = 1000;
        option.setScanSpan(span);
        //可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);
        //可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);
        //可选，默认false,设置是否使用gps
        option.setLocationNotify(true);
        //可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);
        //可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);
        //可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);
        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);
        //可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);
        //可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
        locationClient.setLocOption(option);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        locationClient.start();//开启定位服务
    }

    @Override
    protected void onResume() {
        mMapView.onResume();//运行地图的绘制
        super.onResume();
    }
    @Override
    protected void onPause() {
        mMapView.onPause();//暂停地图的绘制
        super.onPause();
    }
    @Override
    protected void onDestroy() {
        mMapView.onDestroy();//释放资源
        mPoiSearch.destroy();//释放资源
        if (mResultReceiver != null) {//取消广播接收器的注册
            LocalBroadcastManager.getInstance(this).unregisterReceiver(mResultReceiver);
        }
        locationClient.stop();//停止定位服务
        super.onDestroy();
    }

    /**
     * 广播接收器
     * @return
     */
    private BroadcastReceiver createBroadcastReceiver() {
        return new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String result = intent.getStringExtra("result");
                if (!TextUtils.isEmpty(result)){
                    POI(result);
                }
            }
        };
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.i("phone", "正在连接");
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.w("phone", "断开连接");
    }
}
