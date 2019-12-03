package com.mingrisoft.smarthome;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.mingrisoft.smarthome.tool.QueueSingleton;

public class SocketAndroidActivity extends Activity {
    private Button oneBtn, twoBtn, threeBtn;
    private String HOST_SERVER_PORT = "192.168.2.116:8000/?";//应该与服务器端口相同
    private static final int SERVER_PORT = 8000;
    private boolean oneBL = false, twoBL = false, threeBL = false;
    private String msg = null;
    private StringRequest request = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        oneBtn = (Button) findViewById(R.id.btn_one);
        oneBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                msg ="on1";
                dataInternet(msg);
            }
        });

    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String val = data.getString("value");
            if (val.equals("success")) {
                Toast.makeText(SocketAndroidActivity.this, "操作执行成功", Toast.LENGTH_SHORT).show();
            }
        }
    };

    /**
     * 向服务器请求数据的方法
     **/
    private void dataInternet(String msg) {

        //把用户名和密码传递给服务器
         request =
                 new StringRequest("http://192.168.2.116:8000/", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
//                signedEntity = gson.fromJson(response, SignedEntity.class);
                //把解析的数据显示出来
//                setGsonData(signedEntity);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SocketAndroidActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
            }
        });

//        String url = "http://apis.juhe.cn/idcard/index?key=bb97bfce9edee938aeac99cb503b76db&cardno=430524199106158690";
//       StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
//               @Override
//               public void onResponse(String s) {
//                       Toast.makeText(SocketAndroidActivity.this, s, Toast.LENGTH_LONG).show();
//                       Log.i("aa", "get请求成功" + s);
//                   }
//           }, new Response.ErrorListener() {
//               @Override
//               public void onErrorResponse(VolleyError volleyError) {
//                       Toast.makeText(SocketAndroidActivity.this, volleyError.toString(), Toast.LENGTH_LONG).show();
//                       Log.i("aa", "get请求失败" + volleyError.toString());
//                   }
//           });
        QueueSingleton.getInstance().getQueue().add(request);
    }


}