package com.mingrisoft.gouldtrajectory;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;



/**
 * 实现绘制移动轨迹
 */
public class MainActivity extends Activity implements LocationSource,AMapLocationListener {
    private AMap aMap;              //地图控制器
    private MapView mMapView;       //定义地图控件
    private LatLng oldLocation;     //过去的位置
    private boolean mFirstFix;     //记录是否第一次定位
    List<Integer> colorList;        //渐变色数组
    PolylineOptions options;        //线段选项类

    private LocationSource.OnLocationChangedListener mListener;
    private AMapLocationClient mlocationClient;                 //定位服务类
    private AMapLocationClientOption mLocationOption;           //设置定位参数
    private final int SDK_PERMISSION = 1;                //申请权限
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //获取地图控件引用
        mMapView = (MapView) findViewById(R.id.map);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mMapView.onCreate(savedInstanceState);
        init();                                             //初始化控件
        getPersimmions();                                   //获取定位动态权限
    }
    private  void init(){
        if (aMap == null) {
            aMap = mMapView.getMap();             //地图控制
        }
        //定位
        aMap.setLocationSource(this);// 设置定位监听
        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        // 设置定位的类型为定位模式 ，可以由定位 LOCATION_TYPE_LOCATE、跟随 LOCATION_TYPE_MAP_FOLLOW 或地图根据面向方向旋转 LOCATION_TYPE_MAP_ROTATE
        aMap.setMyLocationType(AMap.LOCATION_TYPE_MAP_ROTATE);
        //设置地图缩放级别
        aMap.moveCamera(CameraUpdateFactory.zoomTo(16));
        //使用 aMap.setMapTextZIndex(2) 可以将地图底图文字设置在添加的覆盖物之上
        aMap.setMapTextZIndex(2);
        //用一个数组来存放颜色，渐变色，
        colorList = new ArrayList<Integer>();
        colorList.add(Color.RED);
        colorList.add(Color.YELLOW);
    }
    /**
     * 添加定位动态权限
     */
    private void getPersimmions() {
        /***
         * 定位权限为必须权限，用户如果禁止，则每次进入都会申请
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ArrayList<String> permissions = new ArrayList<String>();
            // 定位精确位置
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
            }
            if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
            }
            if (permissions.size() > 0) {
                requestPermissions(permissions.toArray(new String[permissions.size()]), SDK_PERMISSION);
            }
        }
    }
    /**
     *动态权限的回调方法
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        activate(mListener);         //调用激活定位
    }

    /**
     *根据过去的位置和新的位置绘制移动轨迹路线
     */
    private void setUpMap(LatLng oldData,LatLng newData ) {
        options = new PolylineOptions();        //创建线段选项
        options.add(oldData,newData);           //设置位置
        options.width(20);                      //设置宽度
        //加入对应的颜色,使用colorValues 即表示使用多颜色，使用color表示使用单色线
        options.colorValues(colorList);
        //加上这个属性，表示使用渐变线
        options.useGradient(true);
        aMap.addPolyline(options);              //地图中添加线段
    }


    /**
     * 定位成功后回调函数
     */
    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (mListener != null && amapLocation != null) {
            if (amapLocation != null
                    && amapLocation.getErrorCode() == 0) {
                mListener.onLocationChanged(amapLocation);// 显示系统默认定位图标
                //保存经纬度
                LatLng newLocation = new LatLng(amapLocation.getLatitude(),
                        amapLocation.getLongitude());
                if(mFirstFix){
                    //记录第一次的定位信息
                    oldLocation = newLocation;
                    mFirstFix = false;
                }
                //位置有变化时
                if(oldLocation != newLocation){
                    //设置新的位置信息
                    setUpMap( oldLocation , newLocation );
                    //记录新的位置信息
                    oldLocation = newLocation;
                }
            } else {
                String errText = "定位失败," + amapLocation.getErrorCode()+ ": " + amapLocation.getErrorInfo();
                Log.e("AmapErr", errText);
                if(mFirstFix){
                    Toast.makeText(this, errText, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    /**
     * 激活定位
     */
    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;
        if (mlocationClient == null) {  //如果定位服务为空
            mlocationClient = new AMapLocationClient(this);     //创建定位服务类
            mLocationOption = new AMapLocationClientOption();   //创建设置定位参数类
            //设置定位监听
            mlocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mlocationClient.startLocation();
        }

    }

    /**
     * 停止定位
     */
    @Override
    public void deactivate() {
        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }
    /**
     * 界面获取焦点时同时获取地图焦点
     */
    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    /**
     * 界面停止时停止地图，并停止定位
     */
    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    /**
     * 界面销毁时销毁地图并销毁定位服务
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
        if(null != mlocationClient){
            mlocationClient.onDestroy();
        }
    }
}
