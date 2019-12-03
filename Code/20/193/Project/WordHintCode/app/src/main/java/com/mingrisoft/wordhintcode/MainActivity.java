package com.mingrisoft.wordhintcode;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mingrisoft.wordhintcode.data.Code;
import com.mingrisoft.wordhintcode.data.WordUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.input)
    EditText input;
    @BindView(R.id.hint)
    TextView hint;
    @BindView(R.id.send)
    Button send;
    private Random mRandom = new Random();//用于生成随机数
    private CompositeDisposable disposables = new CompositeDisposable();
    private ArrayList<Integer> codeArrayList = new ArrayList<>();//文字
    private ArrayList<Integer> wordArrayList = new ArrayList<>();//字母
    private ArrayList<Integer> codeList = new ArrayList<>();//字母
    private String[] codeWord;//验证码索引
    private Code mCode;//数据
    private String hiitStr = "划线";//提示关键字

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        getWordData();
    }

    /**
     * 获取数据
     */
    private void getWordData() {
        Flowable<Code> observable = Flowable.create(e ->
                e.onNext(WordUtils.GetWordData()), BackpressureStrategy.LATEST);
        disposables.add(observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())//线程切换
                .map(code -> {
                    if (null != code) mCode = code;//获取数据
                    return true;
                }).subscribe(aBoolean -> {
                    if (aBoolean) getCodeWord();//生成验证码
                }));//获取数据
    }

    /**
     * 生成验证码
     */
    private void getCodeWord() {
        reset();
        //随机获取四个汉字
        getHintWordData(mCode.getCordArray(),codeArrayList);
        //随机获取四个字母
        getHintWordData(mCode.getWordArray(),wordArrayList);
        //在四个汉字中随机出来两个字作为验证码
        getCodeFromWord();
        //显示验证码输入提示文字
        //（请一次输入”龙p肥r置s应i“中”划线“的汉字）
        //拼接字符串
        String codeHintStr = addWordCode();
        String hintText = "（请依次输入”" + codeHintStr +"“中”" + hiitStr +"“的汉字）";
        int firstIndex = hintText.indexOf(codeWord[0]);
        int secondIndex = hintText.indexOf(codeWord[1]);
        Log.i("字体索引" , firstIndex + "--" + secondIndex);
        SpannableString hintStr = new SpannableString(hintText);
        //设置字体效果
        hintStr.setSpan(new ForegroundColorSpan(Color.parseColor("#008000")),
                18,20, SpannableString.SPAN_INCLUSIVE_INCLUSIVE);
        //设置下划线
        hintStr.setSpan(new UnderlineSpan(),18,20, SpannableString.SPAN_INCLUSIVE_INCLUSIVE);
        //设置字体效果
        hintStr.setSpan(new ForegroundColorSpan(Color.parseColor("#008000")),
                firstIndex,firstIndex+1, SpannableString.SPAN_INCLUSIVE_INCLUSIVE);
        //设置下划线
        hintStr.setSpan(new UnderlineSpan(),firstIndex,firstIndex+1,
                SpannableString.SPAN_INCLUSIVE_INCLUSIVE);
        //设置字体效果
        hintStr.setSpan(new ForegroundColorSpan(Color.parseColor("#008000")),
                secondIndex,secondIndex + 1, SpannableString.SPAN_INCLUSIVE_INCLUSIVE);
        //设置下划线
        hintStr.setSpan(new UnderlineSpan(),secondIndex,secondIndex + 1,
                SpannableString.SPAN_INCLUSIVE_INCLUSIVE);
        hint.setText(hintStr);
    }

    /**
     * 随机获取两个验证码
     */
    private void getCodeFromWord() {
        int count = 0;//设置计数器
        int firstIndex = mRandom.nextInt(4);
        int secondIndex = 0;
        while (count<1) {
            //获取随机数
            int index = mRandom.nextInt(4);
            if (firstIndex != index){
                secondIndex = index;
                count++;
            }
        }
        codeWord = new String[2];
        Log.i("1",firstIndex + "");
        Log.i("2",secondIndex + "");

        if (firstIndex > secondIndex){//按大小排列
            int c = firstIndex;
            firstIndex = secondIndex;
            secondIndex = c;
        }
        codeWord[0] = String.valueOf(mCode.getCordArray()[codeArrayList.get(firstIndex)]);
        codeWord[1] = String.valueOf(mCode.getCordArray()[codeArrayList.get(secondIndex)]);
        Log.w("选出字的索引", Arrays.toString(codeWord));
    }

    /**
     * 拼接字符串
     */
    private String addWordCode() {
        StringBuffer sb = new StringBuffer();
        for (int i = 0;i<4;i++){
            sb.append(mCode.getCordArray()[codeArrayList.get(i)])
                    .append(mCode.getWordArray()[wordArrayList.get(i)]);
        }
        return sb.toString();
    }


    /**
     * 重置
     */
    private void reset(){
        input.setText("");
        codeArrayList.clear();
        wordArrayList.clear();
        codeList.clear();
    }
    /**
     * 从数据中获取四个验证汉字组成验证码
     */
    private void getHintWordData(char[] array,ArrayList<Integer> list) {
        int count = 0;//设置计数器
        while (count < 4) {
            //获取随机数
            int index = mRandom.nextInt(array.length);
            if (detection(index, list)) {
                list.add(index);//储存索引
                count++;//计数器自增
            }
        }
    }
    /**
     * 检测数据是否合法
     *
     * @param num
     * @return
     */
    private boolean detection(int num, ArrayList<Integer> list) {
        for (int i = 0; i < list.size(); i++) {
            if (num == list.get(i)) return false;
        }
        return true;
    }

    /**
     * 验证验证码
     */
    @OnClick(R.id.send)
    public void onClick() {
        if (!"".equals(input.getText().toString())){
            if (TextUtils.equals(input.getText().toString(),codeWord[0] + codeWord[1])){
                showToast("验证成功!");
            }else {
                showToast("验证码有误!");
            }
            getCodeWord();
        }else {
            showToast("请输入验证码!");
        }
    }
    /**
     * 界面被销毁
     */
    @Override
    protected void onDestroy() {
        disposables.clear();
        super.onDestroy();
    }
    /**
     * 吐司
     *
     * @param message
     */
    private void showToast(String message) {
        //除了UI界面尽量避免使用Activity的Context
        Toast.makeText(getApplicationContext(), message
                , Toast.LENGTH_SHORT).show();
    }

}
