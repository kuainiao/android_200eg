package com.mingrisoft.smarthome.detail;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.mingrisoft.smarthome.R;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.mingrisoft.smarthome.tool.QueueSingleton;
import com.mingrisoft.smarthome.tool.SwitchView;

/**
 * Created by Administrator on 2017/2/17.
 */

public class DetailAdapter extends BaseAdapter {
    private Context context;
    private String detailBtn[], title;
    private ProgressDialog pd;               //用于登录时弹出的dialog等待
    private boolean oneBL = true, twoBL = true, threeBL = true;
    private boolean progressShow = true;
    private static final String HOST_SERVER_PORT = "http://192.168.2.116:8000/?";//应该与服务器端口相同


    public DetailAdapter(Context context) {
        this.context = context;
        detailBtn = new String[]{};
        pd = new ProgressDialog(context);

    }

    public void addBtn(String button[], String title) {
        this.detailBtn = button;
        this.title = title;
    }

    @Override
    public int getCount() {
        return detailBtn.length;
    }

    @Override
    public Object getItem(int position) {
        return detailBtn.length;
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_detail_item, null);
            holder = new ViewHolder();
            holder.switchView = (SwitchView) convertView.findViewById(R.id.switch_btn);
            holder.textView = (TextView) convertView.findViewById(R.id.detail_btn_text);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.textView.setText(detailBtn[position]);
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        holder.switchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLog("正在连接开关");      //显示等待动画
                if (title.equals("客厅")) {
                    switch (position) {
                        case 0:   //继电器开关一
                            if (oneBL) {
                                oneBL = false;
                                holder.switchView.setChecked(true); //设置开关按钮开启
                                holder.switchView.initAnimator();   //刷新动画
                                dataInternet("on1");                //向服务器传递数据
                            } else {
                                oneBL = true;
                                holder.switchView.setChecked(false);//设置开关按钮关闭
                                holder.switchView.initAnimator();   //刷新动画
                                dataInternet("off1");               //向服务器传递数据
                            }
                            break;
                        case 1:   //继电器开关二
                            if (twoBL) {
                                twoBL = false;
                                holder.switchView.setChecked(true);
                                holder.switchView.initAnimator();
                                dataInternet("on2");
                            } else {
                                twoBL = true;
                                holder.switchView.setChecked(false);
                                holder.switchView.initAnimator();
                                dataInternet("off2");
                            }
                            break;
                        case 2:   //继电器开关三
                            if (threeBL) {
                                threeBL = false;
                                holder.switchView.setChecked(true);
                                holder.switchView.initAnimator();
                                dataInternet("on3");
                            } else {
                                threeBL = true;
                                holder.switchView.setChecked(false);
                                holder.switchView.initAnimator();
                                dataInternet("off3");
                            }
                            break;
                    }
                }
            }
        });
        return convertView;
    }

    /**
     * 该方法是弹出登录时的等待框
     **/
    private void showLog(String msg) {
        pd.setCanceledOnTouchOutside(false);
        pd.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                progressShow = false;
            }
        });
        pd.setMessage(msg + "....");
        pd.show();   //显示等待框

    }

    /**
     * 向服务器请求数据的方法
     **/
    private void dataInternet(String code) {
        //把用户名和密码传递给服务器
        StringRequest request = new StringRequest(
                HOST_SERVER_PORT + code,  //网址的拼接
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equals("success")) {
                            pd.dismiss();       //等待框消失
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "网络错误", Toast.LENGTH_SHORT).show();
                pd.dismiss();       //等待框消失
            }
        });
        QueueSingleton.getInstance().getQueue().add(request);
    }

    class ViewHolder {
        SwitchView switchView;
        TextView textView;
    }
}
