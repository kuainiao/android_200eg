package com.mingrisoft.refreshandloading;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;


public class PullableListViewActivity extends Activity {
    private ListView listView;
    private PullToRefreshLayout ptrl;
    MyAdapter adapter;
    List<NewsEntity> items, loadList;  //加载数据的集合
    int datasize;//数据的条数
    View footView;//脚布局
    int count = 0;

    public PullableListViewActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview);

        ptrl = ((PullToRefreshLayout) findViewById(R.id.refresh_view));
        ptrl.setOnRefreshListener(new MyListener()); ///设置刷新的监听

        listView = (ListView) findViewById(R.id.content_view);
        addFoot();  //添加listView的脚布局
    }

    /**
     * 添加listview的脚布局
     */
    private void addFoot() {
        footView = View.inflate(this, R.layout.load_foot, null);
        listView.addFooterView(footView);
    }


    @Override
    protected void onStart() {
        super.onStart();
        getMessage();
        /**
         * listview滚动监听
         * */
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // 当不滚动时
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    // 判断是否滚动到底部
                    if (view.getLastVisiblePosition() == view.getCount() - 1) {
                        //加载更多功能的代码
                        try {
                            Thread.sleep(1000);  //睡眠1s
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        /**
                         * 添加加载出来的数据
                         //                         * */
//                        for (int i = 0; i <= count; i++) {
//                            if (items.get(i) != null) {
//                                loadList.add(i, items.get(i));
//                            }
//                        }
                        if (count < 40) {
                            adapter.addData(count);  //把数据用适配器显示出来
                            count += 10;
                        } else {
                            footView.setVisibility(View.GONE);
                            Toast.makeText(PullableListViewActivity.this, "数据全部加载完!", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }

    public void getMessage() {
        footView.setVisibility(View.VISIBLE);
        items = new ArrayList<>();   //初始化数组
        loadList = new ArrayList<>();
        count = 10;
        StringRequest request = new StringRequest(getURL(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson(); //Gson解析数据
                NewsEntity entity = gson.fromJson(response, NewsEntity.class);
                items.add(entity);
                datasize = items.size();
                adapter = new MyAdapter(PullableListViewActivity.this, items);   //初始化adapter
                listView.setAdapter(adapter);
                adapter.addData(count);
                count += 10;
                Log.e("--------", response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PullableListViewActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
            }
        });
        QueueSingleton.getInstance().getQueue().add(request);
    }

    public void showToast() {
        Toast.makeText(PullableListViewActivity.this, "已经是最新数据了", Toast.LENGTH_SHORT).show();

    }

    private String getURL() {

        String[] listType = new String[]{"tiyu", "guonei", "guoji", "yule", "junshi", "keji", "caijing", "shishang", "shehui"};
        Random random = new Random();
        random.nextInt(9);
        /**
         * 聚合数据的请求接口及key
         * http://v.juhe.cn/toutiao/index?type=top&key=APPKEY
         */
        String url = "http://v.juhe.cn/toutiao/index?type=" + listType[random.nextInt(9)] + "&key=6a8326a4cf4e32f7550c45a56035fd5f";
        return url;
    }

    class MyListener  implements PullToRefreshLayout.OnRefreshListener {


        /**
         * 刷新方法
         */
        @Override
        public void onRefresh(final PullToRefreshLayout pullToRefreshLayout) {
            // 下拉刷新操作
            new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    // 千万别忘了告诉控件刷新完毕了哦！
                    pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                    getMessage();
                }
            }.sendEmptyMessageDelayed(0, 2000);
        }
    }
}
