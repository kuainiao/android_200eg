package com.wedding.voicesendsms;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
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
import com.wedding.voicesendsms.tool.CharacterParser;
import com.wedding.voicesendsms.tool.PinyinComparator;
import com.wedding.voicesendsms.tool.SortModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SMSActivity extends MPermissionsActivity implements SynthesizerListener, View.OnClickListener {
    String string = "";
    String name = "";
    private ImageButton button;
    //合成对象
    private SpeechSynthesizer speechSynthesizer;
    private ProgressDialog dialog;
    private List<PersionEntity> list;
    private List<SortModel> SourceDateList;
    List<SortModel> mSortList;
    private PinyinComparator pinyinComparator;
    //识别窗口
    private RecognizerDialog recognizerDialog;
    /**
     * 汉字转换成拼音的类
     */
    private CharacterParser characterParser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestPermission(new String[]{Manifest.permission.RECORD_AUDIO,
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.SEND_SMS,
                Manifest.permission.READ_PHONE_STATE}, 1000);
        //appid换成自己申请的
        SpeechUser.getUser().login(SMSActivity.this, null, null, "appid=573a7bf0", listener);
        init();
        setParam();
        list = new ArrayList<>();
        getPhoneContacts();

    }

    public void init() {
        button = (ImageButton) findViewById(R.id.star);
        button.setOnClickListener(this);
        SourceDateList = new ArrayList<>();
        // 根据a-z进行排序源数据
        Collections.sort(SourceDateList, pinyinComparator);
        //实例化汉字转拼音类
        characterParser = CharacterParser.getInstance();
        pinyinComparator = new PinyinComparator();
        mSortList = new ArrayList<SortModel>();

    }

    public void setParam() {
        speechSynthesizer = SpeechSynthesizer.createSynthesizer(this);
        speechSynthesizer.setParameter(SpeechConstant.VOICE_NAME, "xiaoyan");
        speechSynthesizer.setParameter(SpeechConstant.SPEED, "50");
        speechSynthesizer.setParameter(SpeechConstant.VOLUME, "50");
        speechSynthesizer.setParameter(SpeechConstant.PITCH, "50");
    }

    public void setDialog() {
        recognizerDialog = new RecognizerDialog(this);
        recognizerDialog.setParameter(SpeechConstant.DOMAIN, "iat");
        recognizerDialog.setParameter(SpeechConstant.SAMPLE_RATE, "16000");
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
            string = string + text;
            if (arg1) { //识别结束
                String namePY = string.substring(4, string.indexOf("，"));
                //汉字转换成拼音
                String pinyin = characterParser.getSelling(namePY);
                filterData(pinyin);
            }

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
            case R.id.star:
                setDialog();
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

    private void getPhoneContacts() {
        final String[] PHONES_PROJECTION = new String[]{
                Phone.DISPLAY_NAME, Phone.NUMBER};
        ContentResolver resolver = getContentResolver();
        try {
            // 获取手机联系人
            Cursor phoneCursor = resolver.query(Phone.CONTENT_URI,
                    PHONES_PROJECTION, null, null, null);
            if (phoneCursor != null) {

                while (phoneCursor.moveToNext()) {

                    // 得到手机号码
                    String phoneNumber = phoneCursor
                            .getString(1);
                    // 当手机号码为空的或者为空字段 跳过当前循环
                    if (TextUtils.isEmpty(phoneNumber))
                        continue;
                    // 得到联系人名称
                    String contactName = phoneCursor
                            .getString(0);

                    PersionEntity mContact = new PersionEntity(contactName,
                            phoneNumber);
                    list.add(mContact);
                }
                phoneCursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        String strings[] = new String[list.size()];
        for (int i = 0, j = list.size(); i < j; i++) {
            strings[i] = list.get(i).getName();

        }

        SourceDateList = filledData(strings);
        Collections.sort(mSortList, pinyinComparator);
    }

    /**
     * 为ListView填充数据
     *
     * @param date
     * @return
     */
    private List<SortModel> filledData(String[] date) {

        for (int i = 0; i < date.length; i++) {
            SortModel sortModel = new SortModel();
            sortModel.setName(date[i]);
            //汉字转换成拼音
            String pinyin = characterParser.getSelling(date[i]);
            String sortString = pinyin.substring(0, 1).toUpperCase();

            // 正则表达式，判断首字母是否是英文字母
            if (sortString.matches("[A-Z]")) {
                sortModel.setSortLetters(sortString.toUpperCase());
            } else {
                sortModel.setSortLetters("#");
            }

            mSortList.add(sortModel);
        }
        return mSortList;

    }

    /**
     * 根据输入框中的值来过滤数据并更新ListView
     *
     * @param filterStr
     */


    private void filterData(String filterStr) {
        List<SortModel> filterDateList = new ArrayList<SortModel>();

        if (TextUtils.isEmpty(filterStr)) {
            filterDateList = SourceDateList;
        } else {
            filterDateList.clear();
            for (SortModel sortModel : SourceDateList) {
                String name = sortModel.getName();
                if (name.indexOf(filterStr.toString()) != -1 || characterParser.getSelling(name).startsWith(filterStr.toString())) {
                    filterDateList.add(sortModel);
                }
            }
        }
        Collections.sort(filterDateList, pinyinComparator);
        if (filterDateList != null && filterDateList.size() > 0) {
            sendSms(filterDateList.get(0).getName());
        } else {
            Toast.makeText(this, "没有对应的联系人", Toast.LENGTH_SHORT).show();
            string = "";
        }

    }

    private void sendSms(String text) {

        String smsText = string.substring(string.indexOf("是") + 2);
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getName().equals(text)) {
                String num = list.get(i).getNum();
                //获取短信管理器
                SmsManager smsManager = SmsManager.getDefault();
                //如果汉字大于70个
                if (smsText.length() > 70) {
                    //返回多条短信
                    List<String> contents = smsManager.divideMessage(string);
                    for (String sms : contents) {
                        //1.目标地址：电话号码 2.原地址：短信中心服号码3.短信内容4.意图
                        smsManager.sendTextMessage(num, null, sms, null, null);
                    }
                } else {
                    smsManager.sendTextMessage(num, null, smsText, null, null);
                }
                Toast.makeText(this, "短息成功发送给" + text, Toast.LENGTH_SHORT).show();
                string = "";
                break;
            }
        }
    }
}