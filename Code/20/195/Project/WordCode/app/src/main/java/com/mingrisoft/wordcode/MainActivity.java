package com.mingrisoft.wordcode;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mingrisoft.wordcode.data.WordUtils;
import com.mingrisoft.wordcode.utils.CodeImage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Author LYJ
 * Created on 2017/1/24.
 * Time 08:43
 */

public class MainActivity extends AppCompatActivity {

    private final int INPUT_CODE_COUNT = 4;//验证码文字的数量
    private final int DISTURB_CODE_COUNT = 5;//干扰文字的数量
    private final int ALL_CODE_COUNT = 9;//干扰文字的数量
    @BindView(R.id.test)
    Button test;
    @BindViews(value = {R.id.input_one, R.id.input_two, R.id.input_three, R.id.input_four})
    List<TextView> inputViewList;//输入验证码
    @BindViews(value = {R.id.code_1, R.id.code_2, R.id.code_3, R.id.code_4, R.id.code_5,
            R.id.code_6, R.id.code_7, R.id.code_8, R.id.code_9})
    List<Button> codeViewList;//选择输入的文字按钮
    @BindView(R.id.input_cancel)
    ImageButton inputCancel;//清空输入内容
    @BindView(R.id.show_code)
    TextView showCode;//显示验证码
    @BindView(R.id.show_reset)
    TextView showReset;//重置验证码
    @BindView(R.id.image_code)
    ImageView imageCode;

    private char[] wordData;//验证文字数据
    private CompositeDisposable disposables = new CompositeDisposable();
    private Random mRandom = new Random();//用于生成随机数
    //用来储存验证码的汉字在书中的索引值
    private ArrayList<Integer> codeWordIndexInArrayList = new ArrayList<>();
    private ArrayList<Integer> showWordIndexInArrayList = new ArrayList<>();
    private ArrayList<String> showWordList = new ArrayList<>();//抽取的汉字集合
    private String inputCodeWord;//用被用来验证的数据
    private ArrayList<String> selectedWord = new ArrayList<>();//选择出来的字符串

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        getWordData();//加载数据
    }

    /**
     * 获取数据
     */
    private void getWordData() {
        Flowable<char[]> observable = Flowable.create(e ->
                e.onNext(WordUtils.GetWordData()), BackpressureStrategy.LATEST);
        disposables.add(observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())//线程切换
                .map(chars -> {
                    if (null != chars) wordData = chars;//获取数据
                    return true;
                }).subscribe(aBoolean -> {
                    if (aBoolean) getCodeWord();//生成验证码
                }));//获取数据
    }

    /**
     * 获取验证码文字
     */
    private void getCodeWord() {
        reset();
        //首先随机取四个汉字作为验证码
        getInputCodeWordFromData();
        //随后从数组中取出五个汉字并且不跟验证码重复
        getDisturbCodeWordFromData();
        //将这九个汉字重排序显示在界面中
        sortCodeWord();
        //将排序后的字显示在按钮上
        showWordInView();
        //清空输入框中的字
        clearInputWorld();
    }

    /**
     * 清空之前输入的验证码
     */
    private void clearInputWorld() {
        for (TextView textView : inputViewList) {
            textView.setText("");
        }
    }

    /**
     * 将字显示在控件上
     */
    private void showWordInView() {
        for (int i = 0; i < ALL_CODE_COUNT; i++) {
            codeViewList.get(i).setText(showWordList.get(i));
        }
    }


    /**
     * 从数据中获取四个验证汉字组成验证码
     */
    private void getInputCodeWordFromData() {
        StringBuilder sb = new StringBuilder();
        int count = 0;//设置计数器
        while (count < INPUT_CODE_COUNT) {
            //获取随机数
            int index = mRandom.nextInt(wordData.length);
            if (detection(index, codeWordIndexInArrayList)) {
                codeWordIndexInArrayList.add(index);//储存索引
                sb.append(wordData[index]);//获取文字
                count++;//计数器自增
            }
        }
        inputCodeWord = sb.toString();//文字验证码
        showCode.setText(inputCodeWord);//显示验证码
        imageCode.setBackground(new CodeImage(this,inputCodeWord));
    }

    /**
     * 生成五个干扰码
     */
    private void getDisturbCodeWordFromData() {
        int count = 0;//计数器
        while (count < DISTURB_CODE_COUNT) {
            int index = mRandom.nextInt(wordData.length);
            if (detection(index, codeWordIndexInArrayList)) {
                codeWordIndexInArrayList.add(index);//储存索引
                count++;
            }
        }
    }

    /**
     * 汉字的排序
     */
    private void sortCodeWord() {
        int count = 0;
        while (count < ALL_CODE_COUNT) {
            //获取随机数
            int index = mRandom.nextInt(ALL_CODE_COUNT);
            int codeIndex = codeWordIndexInArrayList.get(index);
            if (detection(codeIndex, showWordIndexInArrayList)) {
                showWordIndexInArrayList.add(codeIndex);//储存索引
                showWordList.add(String.valueOf(wordData[codeIndex]));//储存汉字
                count++;
            }
        }
    }

    /**
     * 重置
     */
    private void reset() {
        codeWordIndexInArrayList.clear();
        showWordIndexInArrayList.clear();
        showWordList.clear();
        selectedWord.clear();
        inputCodeWord = null;
        showCode.setText("");
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

    /**********************************************************************************************
     **********************************************************************************************
     **********************************************************************************************/
    /**
     * 验证按钮
     */
    @OnClick(R.id.test)
    public void onClick() {
        if (selectedWord.size() > 0) {
            StringBuilder sb = new StringBuilder();
            for (TextView textView : inputViewList) {
                //获取选择的验证文字
                sb.append(textView.getText().toString());
            }
            String result = TextUtils.equals(inputCodeWord,
                    sb.toString()) ? "发布成功！" : "验证码错误！";
            showToast(result);//提示验证结果
        } else {
            showToast("请输入验证码！");
        }
    }

    /**
     * 重置验证码
     */
    @OnClick(value = R.id.show_reset)
    public void resetCode() {
        getCodeWord();
    }

    /**
     * 清空输入的验证码
     */
    @OnClick(value = R.id.input_cancel)
    public void clearInputCode() {
        selectedWord.clear();
        clearInputWorld();
    }

    /**
     * 选择字
     */
    @OnClick(value = {R.id.code_1, R.id.code_2, R.id.code_3, R.id.code_4, R.id.code_5,
            R.id.code_6, R.id.code_7, R.id.code_8, R.id.code_9})
    public void selectWordBtn(View view) {
        int index = 0;
        switch (view.getId()) {
            case R.id.code_1:
                index = 0;
                break;
            case R.id.code_2:
                index = 1;
                break;
            case R.id.code_3:
                index = 2;
                break;
            case R.id.code_4:
                index = 3;
                break;
            case R.id.code_5:
                index = 4;
                break;
            case R.id.code_6:
                index = 5;
                break;
            case R.id.code_7:
                index = 6;
                break;
            case R.id.code_8:
                index = 7;
                break;
            case R.id.code_9:
                index = 8;
                break;
        }
        if (selectedWord.size() < INPUT_CODE_COUNT && null != selectedWord) {
            selectedWord.add(showWordList.get(index));
            setWordToView();//显示输入验证码
        }
    }

    /**
     * 将文字添加在控件中并显示出来
     */
    private void setWordToView() {
        for (int i = 0; i < selectedWord.size(); i++) {
            inputViewList.get(i).setText(selectedWord.get(i));
        }
    }
    /**********************************************************************************************
     **********************************************************************************************
     **********************************************************************************************/
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
