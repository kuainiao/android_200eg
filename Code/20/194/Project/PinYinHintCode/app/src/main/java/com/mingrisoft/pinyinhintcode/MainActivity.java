package com.mingrisoft.pinyinhintcode;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.haley.checkcodeview.CheckCodeEditText;
import com.mingrisoft.pinyinhintcode.data.WordUtils;
import com.mingrisoft.pinyinhintcode.pin.CharacterParser;

import java.util.ArrayList;
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

    @BindView(R.id.input_code)
    CheckCodeEditText inputCode;
    @BindView(R.id.clear_code)
    Button clearCode;
    @BindView(R.id.input_hint)
    TextView inputHint;
    @BindView(R.id.validation_code)
    Button validationCode;
    @BindView(com.haley.checkcodeview.R.id.checkcode_edit)
    EditText editText;
    private Random mRandom = new Random();//用于生成随机数
    private CompositeDisposable disposables = new CompositeDisposable();
    private String[] dataCodeArray;//验证码数据
    private String inputCodeWord;//用被用来验证的数据
    private ArrayList<String> showWordList = new ArrayList<>();//抽取的汉字集合

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        //设置文本框的输入类型
        editText.setInputType(InputType.TYPE_CLASS_TEXT);
        editText.setText("");
        getWordData();//加载数据
    }

    /**
     * 获取数据
     */
    private void getWordData() {
        Flowable<String[]> observable = Flowable.create(e ->
                e.onNext(WordUtils.GetWordData()), BackpressureStrategy.LATEST);
        disposables.add(observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())//线程切换
                .map(str -> {
                    if (null != str) dataCodeArray = str;//获取数据
                    return true;
                }).subscribe(aBoolean -> {
                    if (aBoolean) getCodeWord();//生成验证码
                }));//获取数据
    }

    /**
     * 获取验证码文字
     */
    private void getCodeWord() {
        inputCodeWord = null;
        showWordList.clear();
        //首先随机取一个成语作为验证码
        getInputCodeWordFromData();
        //清空输入框中的字
        editText.setText("");
        int index = mRandom.nextInt(4);
        String word = showWordList.get(index);//获取验证码
        String pinYin = CharacterParser.getInstance().getSelling(word);
        String showMessage = inputCodeWord.replace(word, pinYin);//替换字符
        inputHint.setText(Html.fromHtml("请输入" + "<font color='#ff0000'>" +
                showMessage + "</font>" + "【提示：" + pinYin + "=" + word + "】"));
    }

    /**
     * 随机获取成语验证码
     */
    private void getInputCodeWordFromData() {
        int index = mRandom.nextInt(dataCodeArray.length);
        inputCodeWord = dataCodeArray[index].trim();//文字验证码
        for (char c : inputCodeWord.toCharArray()) {
            showWordList.add(String.valueOf(c));
        }
    }

    /**
     * 清空
     */
    @OnClick(value = R.id.clear_code)
    public void clear() {
        editText.setText("");
    }

    /**
     * 验证
     */
    @OnClick(value = R.id.validation_code)
    public void validation() {
        if (!"".equals(inputCode.getText())) {
            if (inputCode.getText().equals(inputCodeWord)){
                showToast("验证成功！");
            }else {
                showToast("验证失败！");
            }
            getCodeWord();
        } else {
            showToast("请输入验证码！");
        }
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
