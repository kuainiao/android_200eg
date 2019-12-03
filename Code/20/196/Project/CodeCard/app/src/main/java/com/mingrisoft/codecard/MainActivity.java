package com.mingrisoft.codecard;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mingrisoft.codecard.data.WordUtils;
import com.mingrisoft.codecard.drawable.CodeImageDrawable;
import com.mingrisoft.codecard.view.CodeImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 作者： LYJ
 * 功能： 测试界面
 * 创建日期： 2017/3/4
 */
public class MainActivity extends AppCompatActivity implements CodeImageView.OnCodeSelectedListener {

    @BindView(R.id.code)
    CodeImageView codeView;
    @BindView(R.id.show_code)
    ImageView showCode;
    @BindView(R.id.code_reset)
    TextView codeReset;
    @BindView(R.id.validation)
    Button validation;
    private String[] wordData;//验证文字数据
    private CompositeDisposable disposables = new CompositeDisposable();
    private Random mRandom = new Random();//用于生成随机数
    private String selectCodeStr,showCodeStr;//选择的验证码,显示的验证码
    private boolean isShow;//显示标记

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        codeView.setOnCodeSelectedListener(this);//设置监听获取选择回调
        getWordData();//获取验证码数据
    }

    /**
     * 获取数据
     */
    private void getWordData() {
        Flowable<String[]> observable = Flowable.create(e ->
                e.onNext(WordUtils.getDataStringArray()), BackpressureStrategy.LATEST);
        disposables.add(observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())//线程切换
                .map(strings -> {
                    if (null != strings) wordData = strings;//获取数据
                    return true;
                }).subscribe(aBoolean -> {
                    if (aBoolean) getCodeWord();//生成验证码
                }));//获取数据
    }

    /**
     * 生成验证码
     */
    private void getCodeWord() {
        //随机从数组中获取一个字符串作为验证码的数据源
        int index = mRandom.nextInt(wordData.length);
        //随机获取一个图片作为验证码选择的背景图片
        int bitmapIndex = mRandom.nextInt(8) + 1;
        //随机生成验证码的数
        int codeCount = mRandom.nextInt(3) + 2;
        //获取验证码数据
        String selectCode = wordData[index];//数据源
        List<String> list = new ArrayList<>();//用来保存数据源
        char[] array = selectCode.toCharArray();//获取数据源数组
        for (int i = 0; i < array.length; i++) {
            list.add(String.valueOf(array[i]));//获取数据源集合
        }
        codeView.setSelectOptionsData(list, codeCount);
        Bitmap background = getRes("picture" + String.valueOf(bitmapIndex));
        codeView.setCodeBackground(background);
        //生成验证码
        generate(list, codeCount ,background);
    }

    /**
     * 生成验证码
     *
     * @param list
     */
    private void generate(List<String> list, int codeSize , Bitmap back) {
        StringBuilder stringBuilder = new StringBuilder();
        int count = 0;//用于计数
        while (count < codeSize) {
            int position = mRandom.nextInt(list.size());//获取随机数
            if (stringBuilder.length() == 0) {//如果为第一个就直接添加
                stringBuilder.append(list.get(position));//拼接字符串
            } else {
                String str = stringBuilder.toString();
                if (str.contains(list.get(position))) {
                    continue;//跳出当前循环执行下一次循环
                } else {
                    stringBuilder.append(list.get(position));//添加数据
                }
            }
            count++;//增加计数
        }
        showCodeStr = stringBuilder.toString();//获取验证码
        showCode.setBackground(new CodeImageDrawable(this,showCodeStr,back));
    }

    /**
     * 获取图片资源
     *
     * @param name
     * @return
     */
    public Bitmap getRes(String name) {
        //获取资源ID值
        int resID = getResources().getIdentifier(name, "mipmap", getPackageName());
        //获取图片
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resID);
        return bitmap;
    }

    /**
     * 获得验证码选择结果
     *
     * @param codeStr
     */
    @Override
    public void selectedCodeMessage(String codeStr) {
        selectCodeStr = codeStr;//选择的验证码
    }

    /**
     * 释放资源
     */
    @Override
    protected void onDestroy() {
        disposables.clear();
        super.onDestroy();
    }

    /**
     * 点击事件
     * @param view
     */
    @OnClick({R.id.code_reset, R.id.validation ,R.id.show_code})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.code_reset://重置验证码
                getCodeWord();//重新获取验证码数据
                codeView.reset();//刷新控件
                break;
            case R.id.validation:
                if (TextUtils.equals(showCodeStr,selectCodeStr)){
                    Toast.makeText(this, "验证成功！", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this, "验证失败！", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.show_code:
                Log.i("width", showCode.getWidth()+ "");
                Log.i("height", showCode.getHeight()+ "");
                break;
        }
    }


    /**
     * 选择是否绘制标记
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.index:
                isShow = isShow ? !isShow : !isShow;
                codeView.setShowKeyPoint(isShow);//绘制标记
                codeView.invalidate();//重绘
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
