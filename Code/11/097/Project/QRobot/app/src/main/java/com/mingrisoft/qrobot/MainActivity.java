package com.mingrisoft.qrobot;

import org.json.JSONException;
import org.json.JSONObject;

import turing.os.http.core.ErrorMessage;
import turing.os.http.core.HttpConnectionListener;
import turing.os.http.core.RequestResult;

import android.Manifest;
import android.app.Activity;
import android.app.IntentService;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.turing.androidsdk.InitListener;
import com.turing.androidsdk.SDKInit;
import com.turing.androidsdk.SDKInitBuilder;
import com.turing.androidsdk.TuringApiManager;
import com.turing.androidsdk.asr.VoiceRecognizeManager;
import com.turing.androidsdk.asr.VoiceRecognizeListener;
import com.turing.androidsdk.tts.TTSListener;
import com.turing.androidsdk.tts.TTSManager;

public class MainActivity extends MPermissionsActivity {

    private final String TAG = MainActivity.class.getSimpleName();  //用于调试log的TAG
    private TTSManager ttsManager;   //图灵的工具类
    private VoiceRecognizeManager recognizerManager;   //语音识别工具类
    private TuringApiManager mTuringApiManager;  //图灵
    //	private TextView mStatus;
    private ImageView robotImOne, robotImTwo;  //用于显示等待指令和聊天是的gif图
    /**
     * 返回结果，开始说话
     */
    public final int SPEECH_START = 0;
    /**
     * 开始识别
     */
    public final int RECOGNIZE_RESULT = 1;
    /**
     * 开始识别
     */
    public final int RECOGNIZE_START = 2;

    /**
     * 聚合数据的请求接口及key
     */
    String url = "http://op.juhe.cn/robot/index?info=";
    String key = "&key=4ded35e43b05f528d5b57b8121835822";
    private RequestQueue queue;
    /**
     * 申请的turing的apikey
     **/
    private final String TURING_APIKEY = "fcb8745868e045c4b573d4a76c6d23ec";
    /**
     * 申请的secret
     **/
    private final String TURING_SECRET = "da88fd34c17a9ef9";
    /**
     * 填写一个任意的标示，没有具体要求，，但一定要写，
     **/
    private final String UNIQUEID = "131313131";
    //百度key
    private final String BD_APIKEY = "jqorGbkGOXr59HkuE9OncnlR";
    //百度screte
    private final String BD_SECRET = "b2345cc91d1fb59299587f1f797a0a56";

    /**
     * Handler传递回调结果
     * */
    private Handler myHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case SPEECH_START:   //机器人开始讲话时
                    ttsManager.startTTS((String) msg.obj);   //获取到识别的结果
                    robotImOne.setVisibility(View.INVISIBLE); //隐藏第一幅gif图
//				mStatus.setText("开始讲话：" + (String) msg.obj);
                    break;
                case RECOGNIZE_RESULT:   //识别结果
//				mStatus.setText("识别结果：" + msg.obj);
                    break;
                case RECOGNIZE_START:  //开始识别用户指令
//				mStatus.setText("开始识别");
                    robotImOne.setVisibility(View.VISIBLE);  //当机器人等待指令时，显示第一张gif图
                    break;
                default:
                    break;
            }
        }

        ;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);  //绑定布局
        requestPermission(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO,
                Manifest.permission.READ_CONTACTS}, 1000);//申请权限
        //加载微笑机器人的图片
        robotImOne = (ImageView) findViewById(R.id.robot_image_one);
        //加载或说话机器人的图片
        robotImTwo = (ImageView) findViewById(R.id.robot_image_two);
        //把微笑动态图加载到图片上
        Glide.with(MainActivity.this).load(R.drawable.robot_a).into(robotImOne);
        //把说话的动态图加载到图片上
        Glide.with(MainActivity.this).load(R.drawable.robot_b).into(robotImTwo);
        init();   //初始化

    }

    /**
     * 初始化turingSDK、识别和tts
     */
    private void init() {
        initProgress();   //初始化等待条
        queue = Volley.newRequestQueue(this);   //queue单例模式
        recognizerManager = new VoiceRecognizeManager(this, BD_APIKEY, BD_SECRET);  //初始化语音回调
        ttsManager = new TTSManager(this, BD_APIKEY, BD_SECRET);   //初始化图灵
        recognizerManager.setVoiceRecognizeListener(myVoiceRecognizeListener);    //语音回调监听
        ttsManager.setTTSListener(myTTSListener);   //设置播放语音的监听
        // turingSDK初始化
        SDKInitBuilder builder = new SDKInitBuilder(this)
                .setSecret(TURING_SECRET).setTuringKey(TURING_APIKEY).setUniqueId(UNIQUEID);
        SDKInit.init(builder, new InitListener() {
            @Override
            public void onFail(String error) {
                Log.d(TAG, error);
            }
            @Override
            public void onComplete() {
                // 获取userid成功后，才可以请求Turing服务器，需要请求必须在此回调成功，才可正确请求
                mTuringApiManager = new TuringApiManager(MainActivity.this);
                mTuringApiManager.setHttpListener(myHttpConnectionListener);
                ttsManager.startTTS("您好！很高兴为您服务。");  //首次进入时播放的语音
            }
        });
    }

    /**
     * 显示加载等待对话框
     * */
    private void initProgress() {
        final ProgressDialog pd = new ProgressDialog(MainActivity.this);  //初始化等待动画
        pd.setCanceledOnTouchOutside(false);
        pd.setProgressStyle(R.style.progress);  //设置等待条的风格
        pd.setMessage("正在初始化机器人....");  //等待动画的标题
        pd.show();  //显示等待动画

        new Thread(new Runnable() {
            public void run() {
                //在此处睡眠四秒
                try {
                    Thread.sleep(4000);  //在此处睡眠四秒
                } catch (InterruptedException e) {
                }
                /**
                 * 四秒之后
                 * */
                pd.dismiss();    //等待条消失
            }
        }).start();  //开始线程
    }
    /**
     * 网络请求回调
     */
    HttpConnectionListener myHttpConnectionListener = new HttpConnectionListener() {
        @Override
        public void onSuccess(RequestResult result) {   //成功的回调请求
            if (result != null) {  //如果数据不为空
                try {
                    Log.d(TAG, result.getContent().toString());
                    /**
                     * Json解析放回的数据
                     * */
                    JSONObject result_obj = new JSONObject(result.getContent().toString());  //解析数据
                    if (result_obj.has("text")) {
                        Log.d(TAG, result_obj.get("text").toString());
//						myHandler.obtainMessage(SPEECH_START,
//								result_obj.get("text")).sendToTarget();
                    }
                } catch (JSONException e) {
                    Log.d(TAG, "JSONException:" + e.getMessage());
                }
            }
        }

        /**
         * 网络请求错误回调
         * */
        @Override
        public void onError(ErrorMessage errorMessage) {
            Log.d(TAG, errorMessage.getMessage());
        }
    };

    /**
     * 语音识别回调
     */
    VoiceRecognizeListener myVoiceRecognizeListener = new VoiceRecognizeListener() {

        @Override
        public void onVolumeChange(int volume) {
            // 仅讯飞回调
        }

        @Override
        public void onStartRecognize() {
            // 仅针对百度回调
        }

        @Override
        public void onRecordStart() {

        }

        @Override
        public void onRecordEnd() {

        }

        /**
         * 识别结果
         * */
        @Override
        public void onRecognizeResult(String result) {
            Log.d(TAG, "识别结果：" + result);
            if (result == null) {
                recognizerManager.startRecognize();
                myHandler.sendEmptyMessage(RECOGNIZE_START);
                return;
            }
            getMessage(result);  //将识别结果传递给指定的方法

        }

        @Override
        public void onRecognizeError(String error) {
            Log.e(TAG, "识别错误：" + error);
            recognizerManager.startRecognize();
            myHandler.sendEmptyMessage(RECOGNIZE_START);

        }
    };

    /**
     * 将识别结果传给聚合数据，获取结果
     * */
    private void getMessage(String msg) {
        String path = url + msg + key;  //进行数据请求
        StringRequest request = new StringRequest(path, new Response.Listener<String>() {
            @Override
            public void onResponse(Request<String> request, String s) {
                Log.e("-------", s);
                Gson gson = new Gson(); //Gson解析数据
                UrlEntity urlEntity = gson.fromJson(s, UrlEntity.class); //解析数据的实体类
                if (urlEntity.getResult() == null ||
                        urlEntity.getResult().getUrl() == null) {  //如果返回的数据没有网址
                    Log.e("-----", "没有网址的");
                    myHandler.obtainMessage(SPEECH_START, urlEntity.getResult().getText()).sendToTarget(); //传递给Handler
                } else {  //返回的数据有网址
                    Log.e("-----", "有网址的");
                    myHandler.obtainMessage(SPEECH_START, urlEntity.getResult().getText()).sendToTarget(); //传递给Handler
                    Intent intent = new Intent(MainActivity.this, WebViewActivity.class);//打开webview
                    intent.putExtra("url", urlEntity.getResult().getUrl()); //将网址传递过去
                    startActivity(intent); //执行
                }

            }
        }, new Response.ErrorListener() {  //请求错误执行的操作
            @Override
            public void onErrorResponse(Request request, VolleyError volleyError) {
                Toast.makeText(MainActivity.this, "网络错误", Toast.LENGTH_SHORT).show();

            }
        });
        QueueSingleton.getInstance().getQueue().add(request);   //单例模式
    }

    /**
     * TTS回调
     */
    TTSListener myTTSListener = new TTSListener() {

        @Override
        public void onSpeechStart() {
            Log.d(TAG, "TTS Start!");
        }

        @Override
        public void onSpeechProgressChanged() {

        }

        @Override
        public void onSpeechPause() {
            Log.d(TAG, "TTS Pause!");
        }

        @Override
        public void onSpeechFinish() {  //当讲话停止时
            recognizerManager.startRecognize();  //开始进行语音识别
            myHandler.obtainMessage(RECOGNIZE_START).sendToTarget(); //传递给Handler
        }

        @Override
        public void onSpeechError(int errorCode) {
            Log.d(TAG, "TTS错误，错误码：" + errorCode);
        }


        @Override
        public void onSpeechCancel() {
            Log.d(TAG, "TTS Cancle!");
        }
    };
}
