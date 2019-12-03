package view;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.RelativeLayout;

import com.mingrisoft.shoopashoop.R;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/2/10.
 */

public class WaveControlLayout extends RelativeLayout {

    private static final int DEFAULT_RIPPLE_COUNT=6;        //默认的波浪数量
    private static final int DEFAULT_DURATION_TIME=3000;    //默认的持续时间
    private static final float DEFAULT_SCALE=5.0f;           //默认的波浪范围
    private static final int DEFAULT_FILL_TYPE=0;            //默认的填充类型

    private int waveColor;                                   //波浪的颜色
    private float waveStrokeWidth;                          //波浪宽度
    private float waveRadius;                               //波浪半径
    private int waveDurationTime;                          //波浪持续时间
    private int waveAmount;                                 //波浪数量
    private int waveDelay;                                  //波浪延迟
    private float waveScale;                               //波浪的范围
    private int waveType;                                  //波浪类型
    private Paint paint;                                      //画笔
    private boolean animationRunning=false;                 //动画是否运行
    private AnimatorSet animatorSet;                          //顺序播放动画对象
    private ArrayList<Animator> animatorList;                 //动画数组
    private LayoutParams waveParams;                        //波浪参数
    private ArrayList<WaveView> waveViewList=new ArrayList<WaveView>();       //波浪数组

    public WaveControlLayout(Context context) {
        super(context);
    }

    public WaveControlLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public WaveControlLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    /**
     *控件的初始化
     */
    private void init(final Context context, final AttributeSet attrs) {
        if (isInEditMode())
            return;

        if (null == attrs) {
            throw new IllegalArgumentException("属性应该提供给这个视图");
        }
        //设置自定义属性
        final TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.WaveControlLayout);
        //设置波浪的颜色
        waveColor=typedArray.getColor(R.styleable.WaveControlLayout_wa_color, getResources().getColor(R.color.rippelColor));
        //设置波浪的宽度
        waveStrokeWidth=typedArray.getDimension(R.styleable.WaveControlLayout_wa_strokeWidth, getResources().getDimension(R.dimen.waveStrokeWidth));
        //设置波浪的半径
        waveRadius=typedArray.getDimension(R.styleable.WaveControlLayout_wa_radius,getResources().getDimension(R.dimen.waveRadius));
        //设置波浪的持续时间
        waveDurationTime=typedArray.getInt(R.styleable.WaveControlLayout_wa_duration,DEFAULT_DURATION_TIME);
        //设置波浪的数量
        waveAmount=typedArray.getInt(R.styleable.WaveControlLayout_wa_rippleAmount,DEFAULT_RIPPLE_COUNT);
        //设置波浪的范围
        waveScale=typedArray.getFloat(R.styleable.WaveControlLayout_wa_scale,DEFAULT_SCALE);
        //设置波浪的类型
        waveType=typedArray.getInt(R.styleable.WaveControlLayout_wa_type,DEFAULT_FILL_TYPE);
        typedArray.recycle();                                   //进行回收
        //波浪持续的时间除以波浪数量得到，每个波浪的延迟时间
        waveDelay=waveDurationTime/waveAmount;

        paint = new Paint();                                    //创建画笔
        paint.setAntiAlias(true);                               //设置抗锯齿
        if(waveType==DEFAULT_FILL_TYPE){                    //如果波浪类型被设置为填充效果
            waveStrokeWidth=0;                               //波浪宽度设置为0
            paint.setStyle(Paint.Style.FILL);                   //设置画笔填充
        }else
            paint.setStyle(Paint.Style.STROKE);                 //非填充模式
        paint.setColor(waveColor);                            //设置画笔颜色
        //设置波浪参数
        waveParams=new LayoutParams((int)(2*(waveRadius+waveStrokeWidth)),
                (int)(2*(waveRadius+waveStrokeWidth)));
        //设置波浪中心
        waveParams.addRule(CENTER_IN_PARENT, TRUE);

        animatorSet = new AnimatorSet();        //顺序播放动画对象
        //设置动画加速减速插补器
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        //创建动画数组
        animatorList=new ArrayList<Animator>();
        //根据波浪的数量，设置显示的动画
        for(int i=0;i<waveAmount;i++){
            //创建波浪自定义控件
            WaveView waveView=new WaveView(getContext());
            //添加控件与参数
            addView(waveView,waveParams);
            //波浪数组添加控件
            waveViewList.add(waveView);
            //波浪显示横向拉伸动画
            final ObjectAnimator scaleXAnimator = ObjectAnimator.
                    ofFloat(waveView, "ScaleX", 1.0f, waveScale);
            scaleXAnimator.setRepeatCount(ObjectAnimator.INFINITE);  //无限重复
            scaleXAnimator.setRepeatMode(ObjectAnimator.RESTART);    //动画重新启动
            scaleXAnimator.setStartDelay(i * waveDelay);           //波浪延迟的时间
            scaleXAnimator.setDuration(waveDurationTime);         //波浪持续的时间
            animatorList.add(scaleXAnimator);                       //横向拉伸动画添加进动画数组
            //波浪显示纵向拉伸动画
            final ObjectAnimator scaleYAnimator = ObjectAnimator.
                    ofFloat(waveView, "ScaleY", 1.0f, waveScale);
            scaleYAnimator.setRepeatCount(ObjectAnimator.INFINITE);   //无限重复
            scaleYAnimator.setRepeatMode(ObjectAnimator.RESTART);     //动画重新启动
            scaleYAnimator.setStartDelay(i * waveDelay);              //波浪延迟的时间
            scaleYAnimator.setDuration(waveDurationTime);            //波浪持续的时间
            animatorList.add(scaleYAnimator);                        //纵向拉伸动画添加进动画数组
            //波浪消失动画
            final ObjectAnimator alphaAnimator = ObjectAnimator.
                    ofFloat(waveView, "Alpha", 1.0f, 0f);
            alphaAnimator.setRepeatCount(ObjectAnimator.INFINITE);  //无限重复
            alphaAnimator.setRepeatMode(ObjectAnimator.RESTART);    //动画重新启动
            alphaAnimator.setStartDelay(i * waveDelay);             //波浪延迟的时间
            alphaAnimator.setDuration(waveDurationTime);           //波浪持续的时间
            animatorList.add(alphaAnimator);                       //消失动画添加进动画数组
        }

        animatorSet.playTogether(animatorList);         //设置播放所有动画
    }

    /**
     * 波浪控件
     */
    private class WaveView extends View {

        public WaveView(Context context) {
            super(context);
            this.setVisibility(View.INVISIBLE);     //波浪默认看不见
        }

        @Override
        protected void onDraw(Canvas canvas) {
            int radius=(Math.min(getWidth(),getHeight()))/2;            //计算半径
            //绘制圆
            canvas.drawCircle(radius,radius,radius-waveStrokeWidth,paint);
        }
    }

    /**
     * 启动波浪动画
     */
    public void startWaveAnimation(){
        if(!isWaveAnimationRunning()){                         //如果动画没有运行
            for(WaveView waveView:waveViewList){              //便利波浪数组
                waveView.setVisibility(VISIBLE);               //显示波浪
            }
            animatorSet.start();                                //启动动画
            animationRunning=true;                              //设置动画处于显示状态
        }
    }


    /**
     *波浪动画是否运行
     */
    public boolean isWaveAnimationRunning(){
        return animationRunning;
    }
}
