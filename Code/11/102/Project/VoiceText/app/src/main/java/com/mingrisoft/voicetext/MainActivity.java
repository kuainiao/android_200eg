package com.mingrisoft.voicetext;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.iflytek.cloud.speech.RecognizerResult;
import com.iflytek.cloud.speech.SpeechConstant;
import com.iflytek.cloud.speech.SpeechError;
import com.iflytek.cloud.speech.SpeechListener;
import com.iflytek.cloud.speech.SpeechSynthesizer;
import com.iflytek.cloud.speech.SpeechUser;
import com.iflytek.cloud.speech.SynthesizerListener;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;

import org.w3c.dom.Text;

public class MainActivity extends Activity implements OnClickListener, SynthesizerListener {
    private EditText editText, textEnter;
    private Button button2, buttonSend;

    private RelativeLayout layoutZ, layoutS;
    private ImageView imageViewZ, imageViewS;
    //合成对象
    private SpeechSynthesizer speechSynthesizer;
    //识别窗口
    private RecognizerDialog recognizerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //appid换成自己申请的
        SpeechUser.getUser().login(MainActivity.this, null, null, "appid=573a7bf0", listener);
        init();
        setParam();

    }

    public void init() {
        recognizerDialog = new RecognizerDialog(this);
        recognizerDialog.setParameter(SpeechConstant.DOMAIN, "iat");
        recognizerDialog.setParameter(SpeechConstant.SAMPLE_RATE, "16000");
        /**
         * 初始化控件
         * */
        editText = (EditText) findViewById(R.id.editText1);
        button2 = (Button) findViewById(R.id.button2);
        buttonSend = (Button) findViewById(R.id.send_btn);
        buttonSend.setOnClickListener(this);
        imageViewS = (ImageView) findViewById(R.id.enter_s);
        imageViewZ = (ImageView) findViewById(R.id.enter_z);
        layoutS = (RelativeLayout) findViewById(R.id.rl_s);
        layoutZ = (RelativeLayout) findViewById(R.id.rl_z);
        imageViewZ.setOnClickListener(this);
        imageViewS.setOnClickListener(this);
        textEnter = (EditText) findViewById(R.id.text_top);
        button2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() ==MotionEvent.ACTION_UP){
                    final Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(2000);  //开启睡眠两秒
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    thread.start();  //开启线程
                }
                if (event.getAction() ==MotionEvent.ACTION_DOWN){
                    //当手势按下时获取音频
                    setDialog();  //显示dialog
                }
                return false;
            }
        });
        textEnter.setEnabled(false);
    }

    public void setParam() {
        speechSynthesizer = SpeechSynthesizer.createSynthesizer(this);
        speechSynthesizer.setParameter(SpeechConstant.VOICE_NAME, "xiaoyan");
        speechSynthesizer.setParameter(SpeechConstant.SPEED, "50");
        speechSynthesizer.setParameter(SpeechConstant.VOLUME, "50");
        speechSynthesizer.setParameter(SpeechConstant.PITCH, "50");
    }

    public void setDialog() {
        textEnter.setText(null);
        //显示Dialog
        recognizerDialog.setListener(dialogListener);
        recognizerDialog.show();
    }

    /**
     * 识别回调监听器
     */
    private RecognizerDialogListener dialogListener = new RecognizerDialogListener() {
        //识别结果回调
        @Override
        public void onResult(RecognizerResult arg0, boolean arg1) {
            // TODO Auto-generated method stub
            String text = JsonParser.parseIatResult(arg0.getResultString());
            textEnter.append(text);
            textEnter.setSelection(editText.length());
        }

        //识别结束回调
        @Override
        public void onError(SpeechError arg0) {
            // TODO Auto-generated method stub

        }
    };


    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.send_btn:  //当用手动输入的时候，判断输入框是都为空
                if (TextUtils.isEmpty(editText.getText().toString().trim())) {
                    Toast.makeText(this, "输入内容为空", Toast.LENGTH_SHORT).show();
                } else {
                    textEnter.append(editText.getText().toString());
                    editText.setText("");
                }
                break;
            case R.id.enter_s://设置显示语音输入的布局
                layoutZ.setVisibility(View.VISIBLE);
                layoutS.setVisibility(View.INVISIBLE);
                break;
            case R.id.enter_z:  //设置显示手动输入的布局
                layoutS.setVisibility(View.VISIBLE);
                layoutZ.setVisibility(View.INVISIBLE);
                break;
        }
    }

    //缓冲进度回调通知
    @Override
    public void onBufferProgress(int arg0, int arg1, int arg2, String arg3) {
        // TODO Auto-generated method stub
    }

    //结束回调
    @Override
    public void onCompleted(SpeechError arg0) {
        // TODO Auto-generated method stub
    }

    //开始播放
    @Override
    public void onSpeakBegin() {
        // TODO Auto-generated method stub
    }

    //暂停播放
    @Override
    public void onSpeakPaused() {
        // TODO Auto-generated method stub
    }

    //播放进度
    @Override
    public void onSpeakProgress(int arg0, int arg1, int arg2) {
        // TODO Auto-generated method stub
    }

    //继续播放
    @Override
    public void onSpeakResumed() {
        // TODO Auto-generated method stub
    }

    /**
     * 通用回调接口
     */
    private SpeechListener listener = new SpeechListener() {
        //消息回调
        @Override
        public void onEvent(int arg0, Bundle arg1) {
            // TODO Auto-generated method stub
        }

        //数据回调
        @Override
        public void onData(byte[] arg0) {
            // TODO Auto-generated method stub
        }

        //结束回调（没有错误）
        @Override
        public void onCompleted(SpeechError arg0) {
            // TODO Auto-generated method stub
        }
    };

}
