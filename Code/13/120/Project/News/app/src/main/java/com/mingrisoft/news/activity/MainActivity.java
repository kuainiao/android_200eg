package com.mingrisoft.news.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListView;

import com.mingrisoft.news.R;
import com.mingrisoft.news.adapter.NewsAdapter;
import com.mingrisoft.news.bean.Bean;
import com.mingrisoft.news.bean.Reasult;
import com.mingrisoft.news.listener.Contant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    private static final String TAG = MainActivity.class.getSimpleName();
    private OkHttpClient okHttpClient;//网络请求对象
    private MineCallBack callBack;//请求回调
    private int page = 1;//页数
    private int row = 20;//每次请求的数据条数
    private ListView newList;//数据列表
    private boolean isLast;//是否滑动到结尾
    private List<Reasult> reasults;//请求数据的集合
    private NewsAdapter adapter;//数据加载适配器
    private int mVisibleItemCount;//当前数据条数
    private boolean isFirst = true;//是否初次进入界面
    private boolean isRefresh;//刷新
    private boolean isLoading;//加载
    private ProgressDialog dialog;//进度弹窗
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    Log.w(TAG, "获取网络数据成功: " + msg.obj.toString());
                    Bean bean = (Bean) msg.obj;
                    if (isRefresh){
                        reasults.clear();//清空数据
                        //添加数据
                        reasults.addAll(bean.getReasults());
                        //通知适配器更新
                        adapter.notifyDataSetChanged();
                        isRefresh = false;
                        dialog.dismiss();
                        return false;
                    }
                    if (isLoading){
                        reasults.addAll(bean.getReasults());
                        adapter.notifyDataSetChanged();
                        isLoading = false;
                        dialog.dismiss();
                        return false;
                    }
                    reasults = bean.getReasults();//获取数据
                    //将数据绑定给适配器
                    adapter = new NewsAdapter(MainActivity.this, reasults);
                    //让列表绑定适配器
                    newList.setAdapter(adapter);
                    break;
                case 2:
                    Log.w(TAG, "获取网络数据失败: " + msg.obj);
                    break;
            }
            return false;
        }
    });

    /**
     * 初始化
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        okHttpClient = new OkHttpClient();//网络请求对象
        callBack = new MineCallBack();//网路请求回调
        getDataFromNet(page);//POST网络请求
        //实例化控件
        newList = (ListView) findViewById(R.id.news_list);
        //设置列表的滑动事件
        newList.setOnScrollListener(new MyScrollListener());
        //设置列表的item点击事件
        newList.setOnItemClickListener(this);
        dialog = new ProgressDialog(this);//创建弹窗
    }

    /**
     * item点击事件
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        startActivity(new Intent(this,WebPageActivity.class)//跳转界面
                .putExtra("url",reasults.get(position).getUrl()));
    }

    /**
     * listView的滑动监听
     */
    private class MyScrollListener implements AbsListView.OnScrollListener {

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            Log.d(TAG, "===================================================\n");
            int lastItemCount;
            lastItemCount = newList.getLastVisiblePosition();
            Log.i(TAG, "最后一条：" + lastItemCount);
            switch (scrollState) {
                case SCROLL_STATE_IDLE:
                    Log.w(TAG, "SCROLL_STATE_IDLE: ");
                    /*
                    滑到顶部
                     */
                    if (lastItemCount == mVisibleItemCount - 1 && view.getChildAt(0).getTop() >= 0) {
//                        Toast.makeText(MainActivity.this, "滑到顶部", Toast.LENGTH_SHORT).show();
                        dialog.setTitle("正在刷新，请等待！");
                        dialog.show();
                        page = 1;
                        //重新加载
                        getDataFromNet(page);
                        isRefresh = true;
                        return;
                    }
                    /*
                    滑到底部
                     */
                    if (isLastItemVisible() && isLast ){
//                        Toast.makeText(MainActivity.this, "滑到底部", Toast.LENGTH_SHORT).show();
                        dialog.setTitle("正在加载，请等待！");
                        dialog.show();
                        //加载更多
                        getDataFromNet(++page);
                        isLoading = true;
                    }
                    break;
            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            if (isFirst = true) {
                mVisibleItemCount = visibleItemCount;
                isFirst = false;
            }
            if (firstVisibleItem + visibleItemCount == totalItemCount) {
                isLast = true;
            }else {
                isLast = false;
            }
        }
    }

    /**
     * 获取网络数据
     */
    private void getDataFromNet(int getPage) {
        //请求键值对
        Map<String, String> map = new HashMap<>();
        map.put("page", getPage + "");//页数
        map.put("rows", row + "");//个数
        map.put("key", Contant.APPKEY);//key值
        FormBody.Builder builder = new FormBody.Builder();
        //简历map集合
        Iterator<Map.Entry<String, String>> iter = map.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<String, String> entry = iter.next();
            String key = entry.getKey();
            String val = entry.getValue();
            builder.add(key, val);//添加请求参数
        }
        okHttpClient.newCall(new Request.Builder().
                url(Contant.HTTPURL).//设置网络请求的链接
                post(builder.build()).//设置post请求
                build()).enqueue(callBack);//异步请求
    }

    /**
     * 下载结果回调
     */
    private class MineCallBack implements Callback {

        /**
         * 获取网络数据失败
         *
         * @param call
         * @param e
         */
        @Override
        public void onFailure(Call call, IOException e) {
            Message message = Message.obtain();
            message.what = 2;//设置标识
            message.obj = e.getMessage();//获取错误信息
            handler.sendMessage(message);//发送消息
        }

        /**
         * 获取网络数据成功
         *
         * @param call
         * @param response
         * @throws IOException
         */
        @Override
        public void onResponse(Call call, Response response) throws IOException {
            try {
                Message message = Message.obtain();
                message.what = 1;//设置标识
                ///获取数据
                message.obj = getBean(response.body().string());
                handler.sendMessage(message);//传递消息
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取JSON数据解析对象
     *
     * @param str
     * @return
     */
    private Bean getBean(String str) throws JSONException {
        Bean bean = new Bean();//创建对象用于保存解析出的数据
        JSONObject jsonObject = new JSONObject(str);//获取JSONObject独享
        bean.setReason(jsonObject.getString("reason"));//解析reason数据
        bean.setErrorCode(jsonObject.getInt("error_code"));//解析error_code数据
        if (TextUtils.equals("Succes", bean.getReason())) {//判断是否成功
            JSONArray jsonArray = jsonObject.getJSONArray("result");//解析返回数据
            List<Reasult> list = new ArrayList<>();//创建集合用于储存对象
            Reasult reasult;
            //将数据全部解析出来并储存到list集合中
            for (int i = 0; i < jsonArray.length(); i++) {
                reasult = new Reasult();//创建返回结果对象用于保存返回结果
                //获取JSONArray中的第i条数据
                JSONObject obj = (JSONObject) jsonArray.get(i);
                //解析ctime
                reasult.setCtime(obj.getString("ctime"));
                //解析title
                reasult.setTitle(obj.getString("title"));
                //解析description
                reasult.setDescription(obj.getString("description"));
                //解析picUrl
                reasult.setPicUrl(obj.getString("picUrl"));
                //解析url
                reasult.setUrl(obj.getString("url"));
                //存入集合
                list.add(reasult);
            }
            bean.setReasults(list);
        }
        return bean;
    }

    /**
     * 滑到listView底部
     * @return
     */
    protected boolean isLastItemVisible() {
        final Adapter adapter = newList.getAdapter();
        if (null == adapter || adapter.isEmpty()) {
            return true;
        }
        final int lastItemPosition = adapter.getCount() - 1;
        final int lastVisiblePosition = newList.getLastVisiblePosition();
        if (lastVisiblePosition >= lastItemPosition - 1) {
            final int childIndex = lastVisiblePosition - newList.getFirstVisiblePosition();
            final int childCount = newList.getChildCount();
            final int index = Math.min(childIndex, childCount - 1);
            final View lastVisibleChild = newList.getChildAt(index);
            if (lastVisibleChild != null) {
                return lastVisibleChild.getBottom() <= newList.getBottom();
            }
        }
        return false;
    }
}
