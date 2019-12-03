package com.mingrisoft.gould3dmap;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.CameraPosition;

import util.Constants;


/**
 * AMapV2地图中介绍如何显示一个3D地图与卫星地图的切换
 */
public class MainActivity extends Activity implements View.OnClickListener {

    private MapView mMapView;       //定义地图控件
    private AMap aMap;              //地图控制器
    private Button basicmap;        //标准地图按钮
    private Button rsmap;           //卫星地图按钮


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //获取地图控件引用
        mMapView = (MapView) findViewById(R.id.map);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mMapView.onCreate(savedInstanceState);
        init();     //调用初始化方法

    }
    /**
     * 初始化AMap对象
     */
    private void init() {
        if (aMap == null) {
            aMap = mMapView.getMap();                       //地图控制
        }
        //调用changeCamera()方法，设置地图显示位置、角度、缩放级别
        changeCamera(
                CameraUpdateFactory.newCameraPosition(new CameraPosition(
                        Constants.BEIJING, 18, 60, 0)));
        basicmap = (Button)findViewById(R.id.basicmap);     //获取标准地图按钮控件
        basicmap.setOnClickListener(this);                  //设置单击事件监听
        rsmap = (Button)findViewById(R.id.rsmap);           //获取卫星地图按钮控件
        rsmap.setOnClickListener(this);                     //设置单击事件
    }
    /**
     * 该方法用于实现地图显示位置、角度、缩放级别
     */
    private void changeCamera(CameraUpdate update) {

        aMap.moveCamera(update);    //根据参数移动可视区域

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mMapView.onDestroy();
    }
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mMapView.onPause();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }

    /**
     * 按钮单击事件的处理
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.basicmap:
                aMap.setMapType(AMap.MAP_TYPE_NORMAL);// 标准地图模式
                break;
            case R.id.rsmap:
                aMap.setMapType(AMap.MAP_TYPE_SATELLITE);// 卫星地图模式
                break;
        }
    }
}
