package com.mingrisoft.selectimagecode;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mingrisoft.selectimagecode.code.Code;
import com.mingrisoft.selectimagecode.code.CodeDataUtils;
import com.mingrisoft.selectimagecode.code.CodeList;
import com.mingrisoft.selectimagecode.code.CodeType;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
/**
 * Author LYJ
 * Created on 2017/1/22.
 * Time 17:40
 */

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.login)
    Button test;
    @BindViews(value = {R.id.image_1, R.id.image_2, R.id.image_3, R.id.image_4,
            R.id.image_5, R.id.image_6, R.id.image_7, R.id.image_8})
    List<ImageView> imageList;
    @BindView(R.id.text)
    TextView text;
    @BindView(R.id.reset)
    ImageButton reset;
    private boolean[] isSelected = new boolean[8];//标记
    private int[] bgColor = {0xffff0000, 0xff000000, 0x00000000};//背景颜色
    private CompositeSubscription subscription = new CompositeSubscription();
    private boolean hasData;//是否有数据
    private List<Code> allCode;//所有验证图片
    private List<CodeType> typeCode;//所有类别
    private Random mRandom = new Random();//获取随机数
    private int[] indexArray = {0, 1, 2, 3, 4, 5, 6, 7};//标记数组
    private ArrayList<String> selectCode;//验证信息
    private String codeName;//验证码条件
    private ArrayList<Integer> showImageList = new ArrayList<>();//筛选数据集合
    private ArrayList<String> codeString = new ArrayList<>();//验证字符串
    private String[] codeNumberInAll = new String[2];//用于保存验证值
    private ArrayList<Code> showCodeList = new ArrayList<>();//用于显示的验证图片
    private ArrayList<Integer> indexRecordList = new ArrayList<>();//用于记录标记位

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        getImageCodeData();//获取数据
    }

    /**
     * 添加验证图片
     */
    private void addCodeImageToWindow() {
        if (hasData == false) {
            return;
        }
        //获取随机类别
        int typeNum = mRandom.nextInt(typeCode.size());
        CodeType type = typeCode.get(typeNum);//获取验证码类别
        codeName = type.getType();//获取验证码条件
        text.setText(Html.fromHtml("请点击下面所有的:"+
                "<font color = '#ff0000'>"
                + codeName + "</font>"));//显示类别
        saveCodeMessage(type.getCode());//保存数据
        getCodeImageFromAll();//获取其他干扰数据
        setCodeShowIndex();//设置图片的
        showImageInWindow();//显示图片在界面中
    }

    /**
     * 将图片显示在界面中
     */
    private void showImageInWindow() {
        for (int i = 0; i < showCodeList.size(); i++) {
            imageList.get(i).setImageBitmap(showCodeList.get(i).getBitmap());
        }
    }

    /**
     * 设置验证图片的显示位置
     */
    private void setCodeShowIndex() {
        showCodeList.clear();//清空数据
        indexRecordList.clear();//清空数据
        int count = 0;
        while (count < 8) {
            int index = mRandom.nextInt(8);
            if (detection(showImageList.get(index), indexRecordList)) {
                indexRecordList.add(showImageList.get(index));
                count++;
            }
        }
        for (int i = 0 ; i < indexRecordList.size();i++){
            showCodeList.add(allCode.get(indexRecordList.get(i)));
        }

    }

    /**
     * 获取干扰数据
     */
    private void getCodeImageFromAll() {
        int count = 0;
        while (count < 6) {
            int num = mRandom.nextInt(24);
            if (detection(num, showImageList)) {
                showImageList.add(num);
                count++;
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
     * 保存验证数据
     *
     * @param typeCodeList
     */
    private void saveCodeMessage(List<Code> typeCodeList) {
        codeString.clear();//清空数据
        showImageList.clear();//清空数据
        //遍历集合
        for (int i = 0; i < typeCodeList.size(); i++) {
            Code code = typeCodeList.get(i);
            //用于生成随机的验证图片
            showImageList.add(code.getId());
            codeNumberInAll[i] = code.getId() + "";
        }
        //用于最后的验证码判断
        codeString.add(codeNumberInAll[0] + codeNumberInAll[1]);
        codeString.add(codeNumberInAll[1] + codeNumberInAll[0]);
    }

    /**
     * 设置图片的状态属性
     *
     * @param index    start 0
     * @param position start 1
     */
    private void setImageViewState(int index, int position) {
        boolean imageState = isSelected[index] ? false : true;
        isSelected[index] = imageState;//设置状态
        int backgroundColor;
        if (isSelected[index]) {
            backgroundColor = bgColor[0];//设置背景颜色
            selectCode.add(String.valueOf(position));//存储验证值
        } else {
            backgroundColor = bgColor[1];//设置背景颜色
            selectCode.remove(String.valueOf(position));//移除验证值
        }
        //设置背景
        imageList.get(index).setBackgroundColor(backgroundColor);
    }

    /**
     * 获取验证码数据
     */
    private void getImageCodeData() {
        Observable<CodeList> codeListObservable =
                Observable.create(subscriber -> {
                    if (!subscriber.isUnsubscribed()) {
                        subscriber.onNext(CodeDataUtils.getDataList(this));
                    }
                });
        Subscription codeListSubscription = codeListObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(codeList -> {
                    if (codeList != null) {
                        allCode = codeList.getCodeList();//获取数据
                        typeCode = codeList.getTypeList();//获取类别
                        Logger.t("图片总数据数量").i(allCode.size() + "");
                        Logger.t("图片类别数量").i(typeCode.size() + "");
                        hasData = true;//标识数据以获取
                    } else {
                        hasData = false;//数据获取失败
                        showToast("初始化数据失败");
                    }
                    selectCode = new ArrayList<>();//保存验证数据
                    imageViewBackground();//初始化图片的背景
                    addCodeImageToWindow();//添加验证图片
                });
        subscription.add(codeListSubscription);
    }

    /**
     * 登录验证
     */
    @OnClick(R.id.login)
    public void onClick() {
       if (selectCode.size() > 0){
           //创建StringBuilder对象
           StringBuilder sb = new StringBuilder();
           for (int i = 0;i < selectCode.size();i++){
               //添加指定数据的验证数据
               sb.append(showCodeList.get(
                       Integer.parseInt(selectCode.get(i)) - 1).getId());
           }
           for (int i = 0;i < codeString.size();i++){
               if (TextUtils.equals(sb.toString(),codeString.get(i))){
                   showToast("登陆成功！");
                   return;
               }
           }
           showToast("登录失败，验证码有误！");
       }else {
           showToast("请选择验证码！");
       }
    }

    /**
     * 选择验证码
     *
     * @param view
     */
    @OnClick(value = {R.id.image_1, R.id.image_2, R.id.image_3, R.id.image_4,
            R.id.image_5, R.id.image_6, R.id.image_7, R.id.image_8})
    public void selectCode(View view) {
        int index = 0;
        switch (view.getId()) {
            case R.id.image_1://代表第一张图片
                index = indexArray[0];
                break;
            case R.id.image_2://代表第二张图片
                index = indexArray[1];
                break;
            case R.id.image_3://代表第三张图片
                index = indexArray[2];
                break;
            case R.id.image_4://代表第四张图片
                index = indexArray[3];
                break;
            case R.id.image_5://代表第五张图片
                index = indexArray[4];
                break;
            case R.id.image_6://代表第六张图片
                index = indexArray[5];
                break;
            case R.id.image_7://代表第七张图片
                index = indexArray[6];
                break;
            case R.id.image_8://代表第八张图片
                index = indexArray[7];
                break;
        }
        setImageViewState(index, index + 1);//保存状态属性
    }

    /**
     * 重置图片的选择状态
     */
    private void resetImageState(){
        for (int i = 0;i < isSelected.length;i++){
            isSelected[i] = false;//初始化判断值
        }
        imageViewBackground();//设置图片的背景色
    }
    /**
     * 设置图片的背景颜色
     */
    private void imageViewBackground() {
        //通过判断设置颜色
        int initBackgroundColor = hasData ? bgColor[1] : bgColor[2];
        for (int i = 0; i < imageList.size(); i++) {
            //设置图片的背景颜色
            imageList.get(i).setBackgroundColor(initBackgroundColor);
        }
    }

    /**
     * 界面被销毁
     */
    @Override
    protected void onDestroy() {
        //解除订阅事件防止内存溢出
        subscription.unsubscribe();
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

    /**
     * 重置
     */
    @OnClick(R.id.reset)
    public void reset() {
        addCodeImageToWindow();//重置图片背景
        resetImageState();//重置图片状态
        selectCode.clear();//清空图片选择值
    }
}
