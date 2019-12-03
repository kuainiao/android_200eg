package com.mingrisoft.gouldnavigation;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AMapNaviListener;
import com.amap.api.navi.model.AMapLaneInfo;
import com.amap.api.navi.model.AMapNaviCross;
import com.amap.api.navi.model.AMapNaviInfo;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.navi.model.AMapNaviPath;
import com.amap.api.navi.model.AMapNaviStaticInfo;
import com.amap.api.navi.model.AMapNaviTrafficFacilityInfo;
import com.amap.api.navi.model.AimLessModeCongestionInfo;
import com.amap.api.navi.model.AimLessModeStat;
import com.amap.api.navi.model.NaviInfo;
import com.amap.api.navi.model.NaviLatLng;
import com.amap.api.navi.view.RouteOverLay;
import com.autonavi.tbt.NaviStaticInfo;
import com.autonavi.tbt.TrafficFacilityInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends Activity implements AMapNaviListener,
        View.OnClickListener, CompoundButton.OnCheckedChangeListener,LocationSource,
        AMapLocationListener {
    private boolean congestion, cost, hightspeed, avoidhightspeed;      //筛选路线条件
    /**
     * 导航对象(单例)
     */
    private AMapNavi mAMapNavi;
    private AMap mAmap;
    /**
     * 地图对象
     */
    private MapView mRouteMapView;
    private Marker mStartMarker;
    private Marker mEndMarker;
    /**
     * 选择起点Action标志位
     */
    private boolean mapClickStartReady;
    /**
     * 选择终点Aciton标志位
     */
    private boolean mapClickEndReady;
    private NaviLatLng endLatlng = new NaviLatLng(39.955846, 116.352765);
    private NaviLatLng startLatlng = new NaviLatLng(39.925041, 116.437901);
    private List<NaviLatLng> startList = new ArrayList<NaviLatLng>();
    /**
     * 途径点坐标集合
     */
    private List<NaviLatLng> wayList = new ArrayList<NaviLatLng>();
    /**
     * 终点坐标集合［建议就一个终点］
     */
    private List<NaviLatLng> endList = new ArrayList<NaviLatLng>();
    /**
     * 保存当前算好的路线
     */
    private SparseArray<RouteOverLay> routeOverlays = new SparseArray<RouteOverLay>();

    /**
     * 当前用户选中的路线，在下个页面进行导航
     */
    private int routeIndex;
    /**路线的权值，重合路线情况下，权值高的路线会覆盖权值低的路线**/
    private int zindex = 1;
    /**
     * 路线计算成功标志位
     */
    private boolean calculateSuccess = false;       //路线计算状态
    private boolean chooseRouteSuccess = false;     //路径选择状态

    private boolean mFirstFix=false;                    //记录是否第一次定位
    private AMapLocationClient mlocationClient;           //定位服务类
    private AMapLocationClientOption mLocationOption;     //设置定位参数
    private OnLocationChangedListener mListener;
    private final int SDK_PERMISSION = 1;                //申请权限

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRouteMapView = (MapView) findViewById(R.id.navi_view);
        mRouteMapView.onCreate(savedInstanceState);
        mAmap = mRouteMapView.getMap();
        init();                                             //调用初始化控件方法
        getPersimmions();                                   //获取定位动态权限
    }

    /**
     * 初始化控件
     */
    private void init() {
        //获取布局文件中的控件
        CheckBox congestion = (CheckBox) findViewById(R.id.congestion);
        CheckBox cost = (CheckBox) findViewById(R.id.cost);
        CheckBox hightspeed = (CheckBox) findViewById(R.id.hightspeed);
        CheckBox avoidhightspeed = (CheckBox) findViewById(R.id.avoidhightspeed);
        Button calculate = (Button) findViewById(R.id.calculate);
        Button startPoint = (Button) findViewById(R.id.startpoint);
        Button endPoint = (Button) findViewById(R.id.endpoint);
        Button selectroute = (Button) findViewById(R.id.selectroute);
        Button gpsnavi = (Button) findViewById(R.id.gpsnavi);
        //设置控件的单击事件
        calculate.setOnClickListener(this);
        startPoint.setOnClickListener(this);
        endPoint.setOnClickListener(this);
        selectroute.setOnClickListener(this);
        gpsnavi.setOnClickListener(this);
        //复选框事件
        congestion.setOnCheckedChangeListener(this);
        cost.setOnCheckedChangeListener(this);
        hightspeed.setOnCheckedChangeListener(this);
        avoidhightspeed.setOnCheckedChangeListener(this);

        /**
         * 地图单击事件，实现在地图中选择起点与终点
         */
        mAmap.setOnMapClickListener(new AMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng latLng) {
                //控制选起点
                if (mapClickStartReady) {
                    //设置选中起点的坐标
                    startLatlng = new NaviLatLng(latLng.latitude, latLng.longitude);
                    mStartMarker.setPosition(latLng);           //在起点坐标位置绘制起点图标
                    startList.clear();                          //清除起点坐标
                    startList.add(startLatlng);                //保存起点坐标
                    mapClickStartReady = false;                //设置起点标志状态
                }
                //控制选终点
                if (mapClickEndReady) {
                    endLatlng = new NaviLatLng(latLng.latitude, latLng.longitude);
                    mEndMarker.setPosition(latLng);
                    endList.clear();
                    endList.add(endLatlng);
                    mapClickEndReady = false;
                }

            }
        });
        StartMarker();          //获取起点图标
        EndMarker();            //获取终点图标
        //创建导航对象
        mAMapNavi = AMapNavi.getInstance(getApplicationContext());
        mAMapNavi.addAMapNaviListener(this);    //添加导航事件回调监听
        setUpMap();                              //调用设置地图属性方法
    }
    //获取起点图标
    private void StartMarker() {
        // 获取设置起点的图标
        mStartMarker = mAmap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory
                .fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.start))));
    }
    //获取终点图标
    private void EndMarker() {
        mEndMarker = mAmap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory
                .fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.end))));
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
     * 设置一些amap的属性
     */
    private void setUpMap() {
        //定位
        mAmap.setLocationSource(this);// 设置定位监听
        mAmap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        mAmap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        // 设置定位的类型为定位模式 ，可以由定位 LOCATION_TYPE_LOCATE、跟随 LOCATION_TYPE_MAP_FOLLOW 或地图根据面向方向旋转 LOCATION_TYPE_MAP_ROTATE
        mAmap.setMyLocationType(AMap.LOCATION_TYPE_MAP_ROTATE);
    }
    /**
     * 定位成功后回调函数
     */
    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (mListener != null && amapLocation != null) {
            if (amapLocation != null
                    && amapLocation.getErrorCode() == 0) {
                mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
                //保存经纬度
                LatLng location = new LatLng(amapLocation.getLatitude(),
                        amapLocation.getLongitude());
                if (!mFirstFix) {       //判断第一次进入地图
                    //显示当前位置信息
                    Toast.makeText(this, amapLocation.getAddress() + "", Toast.LENGTH_SHORT).show();
                    mFirstFix = true;   //修改状态
                    //更新可视区域位置
                    mAmap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 16));
                }

            } else {
                String errText = "定位失败," + amapLocation.getErrorCode()+ ": " + amapLocation.getErrorInfo();
                    Toast.makeText(this, errText, Toast.LENGTH_SHORT).show();
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
        mRouteMapView.onResume();
    }

    /**
     * 界面停止时停止地图，并清理地图路线数据与线路
     */
    @Override
    protected void onPause() {
        super.onPause();
        mRouteMapView.onPause();
        startList.clear();
        wayList.clear();
        endList.clear();
        routeOverlays.clear();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mRouteMapView.onSaveInstanceState(outState);
    }

    /**
     * 销毁地图
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRouteMapView.onDestroy();
        /**
         * 当前页面只是展示地图，activity销毁后不需要再回调导航的状态
         */
        mAMapNavi.removeAMapNaviListener(this);
        //注意：不要调用这个destory方法，因为在当前页面进行算路，算路成功的数据全部存在此对象中。到另外一个activity中只需要开始导航即可。
        //如果用户是回退退出当前activity，可以调用下面的destory方法。
        //mAMapNavi.destroy();

    }

    /**
     *路线筛选条件的复选框事件处理
     */
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int id = buttonView.getId();
        switch (id) {
            case R.id.congestion:
                congestion = isChecked;             //选中躲避拥堵
                break;
            case R.id.avoidhightspeed:
                avoidhightspeed = isChecked;        //选中不走高速
                break;
            case R.id.cost:
                cost = isChecked;                    //选中躲避收费
                break;
            case R.id.hightspeed:
                hightspeed = isChecked;             //选中高速优先
                break;
            default:
                break;
        }
    }

    @Override
    public void onInitNaviSuccess() {
    }

    /**
     *多路线绘制
     */
    @Override
    public void onCalculateMultipleRoutesSuccess(int[] ints) {
        //清空上次计算的路径列表。
        routeOverlays.clear();
        HashMap<Integer, AMapNaviPath> paths = mAMapNavi.getNaviPaths();
        for (int i = 0; i < ints.length; i++) {
            AMapNaviPath path = paths.get(ints[i]);
            if (path != null) {
                drawRoutes(ints[i], path);
            }
        }
    }

    /**
     * 单路线绘制
     */
    @Override
    public void onCalculateRouteSuccess() {
        /**
         * 清空上次计算的路径列表。
         */
        routeOverlays.clear();
        AMapNaviPath path = mAMapNavi.getNaviPath();
        /**
         * 单路径不需要进行路径选择，直接传入－1即可
         */
        drawRoutes(-1, path);
    }

    @Override
    public void onCalculateRouteFailure(int arg0) {
        calculateSuccess = false;
        Toast.makeText(getApplicationContext(), "计算路线失败，errorcode＝" + arg0, Toast.LENGTH_SHORT).show();
    }

    /**
     *画出路线
     */
    private void drawRoutes(int routeId, AMapNaviPath path) {
        calculateSuccess = true;        //设置路线状态
        mAmap.moveCamera(CameraUpdateFactory.changeTilt(0));  //设置地图倾斜角度为0
        //构造导航路线图层
        RouteOverLay routeOverLay = new RouteOverLay(mAmap, path, this);
        routeOverLay.setTrafficLine(true);                    //开启交通线
        routeOverLay.addToMap();                              //添加驾车/步行路线添加到地图上显示
        routeOverlays.put(routeId, routeOverLay);           //保存当前算好的路线
    }

    /**
     * 改变路线
     */
    public void changeRoute() {
        if (!calculateSuccess) {
            Toast.makeText(this, "请先算路", Toast.LENGTH_SHORT).show();
            return;
        }
        /**
         * 计算出来的路径只有一条
         */
        if (routeOverlays.size() == 1) {
            chooseRouteSuccess = true;
            Toast.makeText(this, "导航距离:" + (mAMapNavi.getNaviPath()).getAllLength() + "m" + "\n" + "导航时间:" + (mAMapNavi.getNaviPath()).getAllTime() + "s", Toast.LENGTH_SHORT).show();
            return;
        }

        if (routeIndex >= routeOverlays.size())
            routeIndex = 0;
        int routeID = routeOverlays.keyAt(routeIndex);
        //突出选择的那条路
        for (int i = 0; i < routeOverlays.size(); i++) {
            int key = routeOverlays.keyAt(i);
            routeOverlays.get(key).setTransparency(0.7f);
        }
        routeOverlays.get(routeID).setTransparency(0);
        /**把用户选择的那条路的权值弄高，使路线高亮显示的同时，重合路段不会变的透明**/
        routeOverlays.get(routeID).setZindex(zindex++);

        //必须告诉AMapNavi 你最后选择的哪条路
        mAMapNavi.selectRouteId(routeID);
        Toast.makeText(this, "导航距离:" + (mAMapNavi.getNaviPaths())
                .get(routeID).getStrategy()+ "\n" + "导航时间:" +
                (mAMapNavi.getNaviPaths()).get(routeID).getAllTime() +
                "s", Toast.LENGTH_SHORT).show();
        routeIndex++;

        chooseRouteSuccess = true;
    }

    /**
     * 清除当前地图上算好的路线
     */
    private void clearRoute() {
        for (int i = 0; i < routeOverlays.size(); i++) {
            RouteOverLay routeOverlay = routeOverlays.valueAt(i);
            routeOverlay.removeFromMap();
        }
        routeOverlays.clear();
    }

    /**
     *按钮单击事件
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.calculate:                   //开始算路按钮
                clearRoute();                       //清除当前地图上算好的路线
                mapClickStartReady = false;      //设置起点状态
                mapClickEndReady = false;        //设置终点状态
                if (avoidhightspeed && hightspeed) {
                    Toast.makeText(getApplicationContext(), "不走高速与高速优先不能同时为true.", Toast.LENGTH_LONG).show();
                }
                if (cost && hightspeed) {
                    Toast.makeText(getApplicationContext(), "高速优先与避免收费不能同时为true.", Toast.LENGTH_LONG).show();
                }
                mStartMarker.remove();             //清除不准确的起点图标

            /*
			 * strategyFlag转换出来的值都对应PathPlanningStrategy常量，用户也可以直接传入PathPlanningStrategy常量进行算路。
			 * 如:mAMapNavi.calculateDriveRoute(mStartList, mEndList, mWayPointList,PathPlanningStrategy.DRIVING_DEFAULT);
			 */
                int strategyFlag = 0;
                try {
                    //进行算路策略转换，将传入的特定规则转换成PathPlanningStrategy的枚举值
                    strategyFlag = mAMapNavi.strategyConvert(congestion, avoidhightspeed, cost, hightspeed, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (strategyFlag >= 0) {
                    //计算驾车路径(包含起点)
                    mAMapNavi.calculateDriveRoute(startList, endList, wayList, strategyFlag);
                    Toast.makeText(getApplicationContext(), "策略:" + strategyFlag, Toast.LENGTH_LONG).show();
                }

                break;
            case R.id.startpoint:       //选择起点按钮
                Toast.makeText(this, "请在地图上点选起点", Toast.LENGTH_SHORT).show();
                mapClickStartReady = true;      //设置起点状态
                StartMarker();                     //获取起点图标
                break;
            case R.id.endpoint:
                Toast.makeText(this, "请在地图上点选终点", Toast.LENGTH_SHORT).show();
                mapClickEndReady = true;
                break;
            case R.id.selectroute:          //选择路径按钮
                changeRoute();                //改变路线方法
                break;
            case R.id.gpsnavi:              //开始导航按钮
                Intent gpsintent = new Intent(getApplicationContext(), RouteNaviActivity.class);
                gpsintent.putExtra("gps", true);
                startActivity(gpsintent);    //跳转导航界面
                this.finish();
                break;
            default:
                break;
        }
    }

    /**
     * ************************************************** 在算路页面，以下接口全不需要处理，在以后的版本中我们会进行优化***********************************************************************************************
     **/

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo arg0) {


    }

    @Override
    public void OnUpdateTrafficFacility(TrafficFacilityInfo arg0) {


    }

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo[] arg0) {


    }

    @Override
    public void hideCross() {


    }

    @Override
    public void hideLaneInfo() {


    }

    @Override
    public void notifyParallelRoad(int arg0) {


    }

    @Override
    public void onArriveDestination() {


    }

    @Override
    public void onArriveDestination(NaviStaticInfo naviStaticInfo) {

    }

    @Override
    public void onArriveDestination(AMapNaviStaticInfo aMapNaviStaticInfo) {

    }

    @Override
    public void onArrivedWayPoint(int arg0) {


    }

    @Override
    public void onEndEmulatorNavi() {


    }

    @Override
    public void onGetNavigationText(int arg0, String arg1) {


    }

    @Override
    public void onGpsOpenStatus(boolean arg0) {


    }

    @Override
    public void onInitNaviFailure() {


    }

    @Override
    public void onLocationChange(AMapNaviLocation arg0) {


    }

    @Override
    public void onNaviInfoUpdate(NaviInfo arg0) {


    }

    @Override
    public void onNaviInfoUpdated(AMapNaviInfo arg0) {


    }

    @Override
    public void onReCalculateRouteForTrafficJam() {


    }

    @Override
    public void onReCalculateRouteForYaw() {


    }

    @Override
    public void onStartNavi(int arg0) {


    }

    @Override
    public void onTrafficStatusUpdate() {


    }

    @Override
    public void showCross(AMapNaviCross arg0) {


    }

    @Override
    public void showLaneInfo(AMapLaneInfo[] arg0, byte[] arg1, byte[] arg2) {


    }

    @Override
    public void updateAimlessModeCongestionInfo(AimLessModeCongestionInfo arg0) {


    }

    @Override
    public void updateAimlessModeStatistics(AimLessModeStat arg0) {


    }


}
