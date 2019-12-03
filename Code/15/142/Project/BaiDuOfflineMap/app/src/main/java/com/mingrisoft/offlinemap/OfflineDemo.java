package com.mingrisoft.offlinemap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.offline.MKOLSearchRecord;
import com.baidu.mapapi.map.offline.MKOLUpdateElement;
import com.baidu.mapapi.map.offline.MKOfflineMap;
import com.baidu.mapapi.map.offline.MKOfflineMapListener;

import java.util.ArrayList;


/* 此Demo用来演示离线地图的下载和显示 */
public class OfflineDemo extends Activity implements MKOfflineMapListener {

    private MKOfflineMap mOffline = null;       //定义离线地图类
    private TextView cidView;                   //定义显示地图ID控件
    private TextView stateView;                 //定义显示下载进度
    private EditText cityNameView;              //定义填写搜索地区的编辑框
    private Button btn_start;         //获取下载按钮
    private boolean start = true;      //设置下载按钮状态true为开始下载，false为暂停下载
    /**
     * 已下载的离线地图信息列表
     */
    private ArrayList<MKOLUpdateElement> localMapList = null;
    private LocalMapAdapter lAdapter = null;    //已下载城市列表适配器


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_offline);
        mOffline = new MKOfflineMap();          //创建离线地图对象
        mOffline.init(this);                    //初始化离线地图
        initView();                              //调用获取相关控件方法

    }

    /**
     * 获取显示信息的相关控件
     */
    private void initView() {

        cidView = (TextView) findViewById(R.id.cityid);     //获取显示地图ID的文本框
        cityNameView = (EditText) findViewById(R.id.city);  //获取填写搜索地区名称的编辑框
        stateView = (TextView) findViewById(R.id.state);    //获取显示下载进度的文本框
        btn_start = (Button) findViewById(R.id.start);       //获取开始下载的按钮

        // 获取已下过的离线地图信息
        localMapList = mOffline.getAllUpdateInfo();     //获取各城市离线地图更新信息
        if (localMapList == null) {                     //如果离线地图数组为空
            //创建离线地图的数组列表
            localMapList = new ArrayList<MKOLUpdateElement>();
        }
        //获取显示已下载城市的ListView列表
        ListView localMapListView = (ListView) findViewById(R.id.localmaplist);
        lAdapter = new LocalMapAdapter();   //创建已下载城市列表适配器
        localMapListView.setAdapter(lAdapter);
        //设置当填写搜索地区的编辑框获取焦点也就是单击后默认文字将消失
        cityNameView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    cityNameView.setHint(null);
                }
            }
        });
    }


    /**
     * 搜索离线城市
     *
     * @param view
     */
    public void search(View view) {
        ArrayList<MKOLSearchRecord> records = mOffline.searchCity(cityNameView
                .getText().toString());
        if (records == null || records.size() != 1) {
            return;     //返回离线城市信息
        }
        //显示离线城市ID
        cidView.setText(String.valueOf(records.get(0).cityID));
    }

    /**
     * 开始下载
     *
     * @param view
     */
    public void start(View view) {
        if (cidView.getText().toString().equals("")) {      //搜索地区是否为空
            Toast.makeText(this, "请先搜索需要下载的省或市的名称！", Toast.LENGTH_SHORT).show();
        } else {
            if (start == true) {     //按钮状态为开始下载
                start = false;       //修改按钮状态
                btn_start.setBackgroundResource(R.drawable.pause_btn);
                //获取城市ID
                int cityid = Integer.parseInt(cidView.getText().toString());
                mOffline.start(cityid);     //根据ID下载离线城市地图
                Toast.makeText(this, "开始下载离线地图. cityid: " + cityid, Toast.LENGTH_SHORT)
                        .show();
                updateView();               //更新显示
            } else {                        //暂停下载
                start = true;
                btn_start.setBackgroundResource(R.drawable.start_btn);
                int cityid = Integer.parseInt(cidView.getText().toString());
                mOffline.pause(cityid);
                Toast.makeText(this, "暂停下载离线地图. cityid: " + cityid, Toast.LENGTH_SHORT)
                        .show();
                updateView();
            }
        }
    }




    /**
     * 更新显示
     */
    public void updateView() {
        localMapList = mOffline.getAllUpdateInfo();
        if (localMapList == null) {
            localMapList = new ArrayList<MKOLUpdateElement>();
        }
        lAdapter.notifyDataSetChanged();        //刷新数据
    }

    /**
     * 界面停止时，停止离线下载
     */
    @Override
    protected void onPause() {
        int cityid = Integer.parseInt(cidView.getText().toString());
        MKOLUpdateElement temp = mOffline.getUpdateInfo(cityid);
        if (temp != null && temp.status == MKOLUpdateElement.DOWNLOADING) {
            mOffline.pause(cityid);
        }
        super.onPause();
    }

    /**
     * 退出时，销毁离线地图模块
     */
    @Override
    protected void onDestroy() {
        mOffline.destroy();
        super.onDestroy();
    }

    @Override
    public void onGetOfflineMapState(int type, int state) {
        switch (type) {
            case MKOfflineMap.TYPE_DOWNLOAD_UPDATE: {
                MKOLUpdateElement update = mOffline.getUpdateInfo(state);
                // 处理下载进度更新提示
                if (update != null) {
                    stateView.setText(String.format("%s : %d%%", update.cityName,
                            update.ratio));
                    //下载进度条
                    ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
                    progressBar.setProgress(update.ratio);
                    progressBar.invalidate();
                    updateView();   //更新显示
                }
            }
            break;
            case MKOfflineMap.TYPE_NEW_OFFLINE:
                // 有新离线地图安装
                Log.d("OfflineDemo", String.format("add offlinemap num:%d", state));
                break;
            case MKOfflineMap.TYPE_VER_UPDATE:
                // 版本更新提示
                // MKOLUpdateElement e = mOffline.getUpdateInfo(state);

                break;
            default:
                break;
        }

    }

    /**
     * 离线地图管理列表适配器
     */
    public class LocalMapAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return localMapList.size();
        }

        @Override
        public Object getItem(int index) {
            return localMapList.get(index);
        }

        @Override
        public long getItemId(int index) {
            return index;
        }

        @Override
        public View getView(int index, View view, ViewGroup arg2) {
            //获取离线地图信息
            MKOLUpdateElement e = (MKOLUpdateElement) getItem(index);
            //加载显示显示控件
            view = View.inflate(OfflineDemo.this,
                    R.layout.offline_localmap_list, null);
            initViewItem(view, e);
            return view;
        }
        //离线地图管理列表中显示信息的控件
        void initViewItem(View view, final MKOLUpdateElement e) {
            Button remove = (Button) view.findViewById(R.id.remove);        //删除按钮
            TextView title = (TextView) view.findViewById(R.id.title);      //标题文字
            TextView update = (TextView) view.findViewById(R.id.update);    //显示更新
            TextView ratio = (TextView) view.findViewById(R.id.ratio);      //下载进度
            ratio.setText(e.ratio + "%");
            title.setText(e.cityName);
            if (e.update) {
                update.setText("可更新");
            } else {
                update.setText("最新");
            }
            //删除离线地图
            remove.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    mOffline.remove(e.cityID);
                    updateView();
                }
            });
        }

    }

}