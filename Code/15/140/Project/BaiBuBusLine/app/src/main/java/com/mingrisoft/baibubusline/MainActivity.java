package com.mingrisoft.baibubusline;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.SupportMapFragment;
import com.baidu.mapapi.model.LatLng;

import com.baidu.mapapi.search.busline.BusLineResult;
import com.baidu.mapapi.search.busline.BusLineSearch;
import com.baidu.mapapi.search.busline.BusLineSearchOption;
import com.baidu.mapapi.search.busline.OnGetBusLineSearchResultListener;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;


import java.util.ArrayList;
import java.util.List;

/**
 * 此demo用来展示如何进行公交线路详情检索，并使用RouteOverlay在地图上绘制 同时展示如何浏览路线节点并弹出泡泡
 */
public class MainActivity extends FragmentActivity implements
        OnGetPoiSearchResultListener, OnGetBusLineSearchResultListener,
        BaiduMap.OnMapClickListener {
    private Button mBtnPre = null; //上一站
    private Button mBtnNext = null; //下一站
    private int nodeIndex = -2; //节点索引,供浏览节点时使用
    private BusLineResult route = null; //保存驾车/步行路线数据的变量，供浏览节点时使用
    private List<String> busLineIDList = null;      //公交ID数据
    private int busLineIndex = 0;                   //数据下标
    //搜索相关
    private PoiSearch mSearch = null; //搜索模块，也可去掉地图模块独立使用
    private BusLineSearch mBusLineSearch = null; //查询公交信息类
    private BaiduMap mBaiduMap = null;           //地图对象
    BusLineOverlay overlay; //公交路线绘制对象

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        CharSequence titleLable = "公交线路查询功能";
        setTitle(titleLable);
        mBtnPre = (Button) findViewById(R.id.pre);      //上一站按钮
        mBtnNext = (Button) findViewById(R.id.next);    //下一站按钮
        mBtnPre.setVisibility(View.INVISIBLE);         //隐藏按钮
        mBtnNext.setVisibility(View.INVISIBLE);
        //显示百度地图
        mBaiduMap = ((SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.bmapView)).getBaiduMap();
        mBaiduMap.setOnMapClickListener(this);      //地图单击事件
        mSearch = PoiSearch.newInstance();          //创建POI搜索
        mSearch.setOnGetPoiSearchResultListener(this);      //设置poi检索监听者
        mBusLineSearch = BusLineSearch.newInstance();       //创建公交线路搜索对象
        //设置公交线路检索监听者
        mBusLineSearch.setOnGetBusLineSearchResultListener(this);
            busLineIDList = new ArrayList<String>();        //公交ID数据
        overlay = new BusLineOverlay(mBaiduMap);            //公交查询数据类对象
        mBaiduMap.setOnMarkerClickListener(overlay);        //地图覆盖物单击事件
    }

    /**
     * 发起检索
     *
     * @param v
     */
    public void searchButtonProcess(View v) {
        busLineIDList.clear();      //清空公交ID数据
        busLineIndex = 0;           //设置公交下标
        mBtnPre.setVisibility(View.INVISIBLE);      //隐藏按钮
        mBtnNext.setVisibility(View.INVISIBLE);
        EditText editCity = (EditText) findViewById(R.id.city);     //填写城市的编辑框
        //填写公交的编辑框
        EditText editSearchKey = (EditText) findViewById(R.id.searchkey);
        // 发起poi检索，从得到所有poi中找到公交线路类型的poi，再使用该poi的uid进行公交详情搜索
        mSearch.searchInCity((new PoiCitySearchOption()).city(
                editCity.getText().toString())
                        .keyword(editSearchKey.getText().toString()));
    }

    /**
     * 查询公交线路相反方向线路
     * @param v
     */
    public void searchNextBusline(View v) {
        if (busLineIndex >= busLineIDList.size()) {
            busLineIndex = 0;
        }
        if (busLineIndex >= 0 && busLineIndex < busLineIDList.size()
                && busLineIDList.size() > 0) {
            mBusLineSearch.searchBusLine((new BusLineSearchOption()
                    .city(((EditText) findViewById(R.id.city)).getText()
                            .toString()).uid(busLineIDList.get(busLineIndex))));

            busLineIndex++;
        }

    }

    /**
     * 站点浏览示例
     *
     * @param v
     */
    public void nodeClick(View v) {

        if (nodeIndex < -1 || route == null
                || nodeIndex >= route.getStations().size()) {
            return;
        }
        TextView popupText = new TextView(this);   //创建文本框显示站名
        popupText.setBackgroundResource(R.drawable.popup);    //设置背景图
        popupText.setTextColor(0xff000000);                   //设置字体颜色
        // 上一个节点
        if (mBtnPre.equals(v) && nodeIndex > 0) {
            // 索引减
            nodeIndex--;
        }
        // 下一个节点
        if (mBtnNext.equals(v) && nodeIndex < (route.getStations().size() - 1)) {
            // 索引加
            nodeIndex++;
        }
        if (nodeIndex >= 0) {
            // 移动到指定索引的坐标
            mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(route
                    .getStations().get(nodeIndex).getLocation()));
            // 弹出泡泡
            popupText.setText(route.getStations().get(nodeIndex).getTitle());
            mBaiduMap.showInfoWindow(new InfoWindow(popupText, route.getStations()
                    .get(nodeIndex).getLocation(), 10));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        mSearch.destroy();
        mBusLineSearch.destroy();
        super.onDestroy();
    }

    /**
     * 公交信息查询结果
     * @param result
     */
    @Override
    public void onGetBusLineResult(BusLineResult result) {
        //如果数据为空，或者出现查询错误做出提示
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(MainActivity.this, "抱歉，未找到结果",
                    Toast.LENGTH_LONG).show();
            return;
        }
        mBaiduMap.clear();  //清空地图覆盖物
        route = result;     //保存浏览数据
        nodeIndex = -1;    //设置下标
        overlay.removeFromMap();    //删除地图覆盖物
        overlay.setData(result);    //设置公交数据
        overlay.addToMap();         //将公交站点添加地图上
        overlay.zoomToSpan();       //缩放地图显示所有公交站点
        mBtnPre.setVisibility(View.VISIBLE);    //显示上一站按钮
        mBtnNext.setVisibility(View.VISIBLE);
        //提示公交线路与终点信息
        Toast.makeText(MainActivity.this, result.getBusLineName(),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onGetPoiResult(PoiResult result) {

        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(MainActivity.this, "抱歉，未找到结果",
                    Toast.LENGTH_LONG).show();
            return;
        }
        // 遍历所有poi，找到类型为公交线路的poi
        busLineIDList.clear();
        for (PoiInfo poi : result.getAllPoi()) {
            if (poi.type == PoiInfo.POITYPE.BUS_LINE
                    || poi.type == PoiInfo.POITYPE.SUBWAY_LINE) {
                busLineIDList.add(poi.uid);
            }
        }
        searchNextBusline(null);
        route = null;
    }

    @Override
    public void onGetPoiDetailResult(PoiDetailResult result) {

    }

    @Override
    public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

    }

    @Override
    public void onMapClick(LatLng point) {
        mBaiduMap.hideInfoWindow();
    }

    @Override
    public boolean onMapPoiClick(MapPoi poi) {
        return false;
    }
}
