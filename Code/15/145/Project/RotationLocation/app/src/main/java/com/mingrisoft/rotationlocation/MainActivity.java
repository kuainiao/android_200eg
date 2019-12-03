package com.mingrisoft.rotationlocation;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.Circle;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;

import java.util.ArrayList;

import util.SensorEventHelper;

public class MainActivity extends Activity implements LocationSource,
        AMapLocationListener {
    private AMap aMap;                                      //定义地图控制器
    private MapView mapView;                                //定义地图控件
    private AMapLocationClient mlocationClient;           //定位服务类
    private AMapLocationClientOption mLocationOption;     //设置定位参数
    private final int SDK_PERMISSION = 1;                //申请权限

    private TextView mLocationErrText;                    //显示定位失败信息
    //定位精度圈颜色
    private static final int STROKE_COLOR = Color.argb(180, 3, 145, 255);
    private static final int FILL_COLOR = Color.argb(10, 0, 0, 180);

    private boolean mFirstFix = false;                    //记录是否第一次定位
    private Marker mLocMarker;                             //绘制图标
    private SensorEventHelper mSensorHelper;              //使用方向传感器模拟指南针
    private Circle mCircle;                               //定义地图绘制圆形


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);     //不显示程序的标题栏
        setContentView(R.layout.activity_main);
        mapView = (MapView) findViewById(R.id.map);         //获取地图控件
        mapView.onCreate(savedInstanceState);               //此方法必须重写
        init();                                             //调用初始化方法
        getPersimmions();                                   //获取定位动态权限
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
        activate(null);         //调用激活定位
    }

    /**
     * 初始化
     */
    private void init() {
        if (aMap == null) {
            aMap = mapView.getMap();        //获取地图控制器
            setUpMap();                      //调用设置地图属性方法
        }
        mSensorHelper = new SensorEventHelper(this);
        if (mSensorHelper != null) {
            mSensorHelper.registerSensorListener();     //注测方向传感器监听
        }
        //获取显示定位失败信息的文本框控件
        mLocationErrText = (TextView) findViewById(R.id.location_errInfo_text);
        mLocationErrText.setVisibility(View.GONE);     //隐藏该控件
    }

    /**
     * 设置一些amap的属性
     */
    private void setUpMap() {
        aMap.setLocationSource(this);// 设置定位监听
//        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        // 设置定位的类型为定位模式 ，可以由定位、跟随或地图根据面向方向旋转几种
        aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);      //只在第一次定位移动到地图中心点
    }

    /**
     * 获取焦点进行定位
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
        if (mSensorHelper != null) {
            mSensorHelper.registerSensorListener();
        } else {
            mSensorHelper = new SensorEventHelper(this);
            if (mSensorHelper != null) {
                mSensorHelper.registerSensorListener();

                if (mSensorHelper.getCurrentMarker() == null && mLocMarker != null) {
                    mSensorHelper.setCurrentMarker(mLocMarker);
                }
            }
        }
    }

    /**
     *界面停止时取消定位
     */
    @Override
    protected void onPause() {
        super.onPause();
        if (mSensorHelper != null) {
            mSensorHelper.unRegisterSensorListener();
            mSensorHelper.setCurrentMarker(null);
            mSensorHelper = null;
        }
        mapView.onPause();      //停止地图
        deactivate();            //停止定位
        mFirstFix = false;     //设置定位状态
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 销毁定位
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        if (null != mlocationClient) {
            mlocationClient.onDestroy();
        }
    }

    /**
     * 定位成功后回调函数
     */
    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
            if (amapLocation != null
                    && amapLocation.getErrorCode() == 0) {
                mLocationErrText.setVisibility(View.GONE);      //定位成功隐藏显示错误的控件
                //保存经纬度
                LatLng location = new LatLng(amapLocation.getLatitude(),
                        amapLocation.getLongitude());
                if (!mFirstFix) {
                    addCircle(location, amapLocation.getAccuracy());//添加定位精度圆
                    addMarker(location);//添加定位图标
                    mSensorHelper.setCurrentMarker(mLocMarker);//定位图标旋转
                    mFirstFix = true;
                } else {
                    mCircle.setCenter(location);                    //设置圆心的经纬度坐标
                    mCircle.setRadius(amapLocation.getAccuracy());  //设置圆形半径
                    mLocMarker.setPosition(location);               //设置图标经纬度位置
                }
                //更新可视区域位置
                aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 18));
            } else {
                //显示定位失败的原因
                String errText = "定位失败," + amapLocation.getErrorCode() + ": " + amapLocation.getErrorInfo();
                Log.e("AmapErr", errText);
                mLocationErrText.setVisibility(View.VISIBLE);
                mLocationErrText.setText(errText);
            }
    }

    /**
     * 激活定位
     */
    @Override
    public void activate(OnLocationChangedListener listener) {
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(this);
            mLocationOption = new AMapLocationClientOption();
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
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }
    //添加定位精度圆
    private void addCircle(LatLng latlng, double radius) {
        CircleOptions options = new CircleOptions();        //圆形选项对象
        options.strokeWidth(1f);                            //设置精度圈宽度
        options.fillColor(FILL_COLOR);                     //设置填充颜色
        options.strokeColor(STROKE_COLOR);                 //设置边框颜色
        options.center(latlng);                             //设置圆心经纬度坐标
        options.radius(radius);                             //圆形半径
        mCircle = aMap.addCircle(options);                 //在地图上添加精度圈
    }

    /**
     * 添加图标覆盖物
     * @param latlng
     */
    private void addMarker(LatLng latlng) {
        if (mLocMarker != null) {
            return;
        }
        //设置定位图标
		BitmapDescriptor des = BitmapDescriptorFactory.fromResource(R.drawable.navi_map_gps_locked);
        MarkerOptions options = new MarkerOptions();            //覆盖物选项
        options.icon(des);                                      //设置覆盖物定位图标
        options.anchor(0.5f, 0.5f);                             //制定图标锚点
        options.position(latlng);                               //设置覆盖物经纬度
        mLocMarker = aMap.addMarker(options);           //添加地图覆盖物
    }
}
