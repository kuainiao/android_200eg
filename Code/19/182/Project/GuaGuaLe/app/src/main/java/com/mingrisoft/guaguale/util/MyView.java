package com.mingrisoft.guaguale.util;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;
import com.mingrisoft.guaguale.R;
import com.mingrisoft.guaguale.bean.LotteryInfo;
import com.mingrisoft.guaguale.manage.LotteryManage;

public class MyView extends TextView {
	//控件宽高
	private int widget, height;
	//上下文
	private Context mContext;
	//坐标点
	private Paint mPaint;
	//画布
	private Canvas tempCanvas;
	//图片类
	private Bitmap mBitmap;
	private float x, y, ox, oy;
	//画笔类
	private Path mPath;
    //实体类
	LotteryInfo info;
	int messageCount;
    //覆盖层颜色
	int color = 0xFFD6D6D6;
	//声明控件方法
	public MyView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		init(attrs);
	}
	//在次抽奖方法
	public void againLotter() {
		messageCount = 0;
		//获取奖励信息
		info = LotteryManage.getRandomLottery();
		//设置覆盖层颜色
		tempCanvas.drawColor(color);
		//中奖信息文字
		setText(info.getText());
		//设置中奖信息文字颜色
		setTextColor(Color.BLACK);
		//设置字体加粗
		getPaint().setFakeBoldText(true);
		//拓展可设置奖区图片
//		Drawable nav_up=getResources().getDrawable(R.drawable.ic_launcher);
//		nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
//		setCompoundDrawables(null, null, nav_up, null);
		//设置背景为白色
		setBackgroundResource(R.color.colorWhite);
	}
    //
	private void init(AttributeSet attrs) {
		// 获取控件大小值
		TypedArray a = mContext.obtainStyledAttributes(attrs,
				R.styleable.lotter);
		//设置控件大小
		widget = (int) a.getDimension(R.styleable.lotter_widget, 300);
		height = (int) a.getDimension(R.styleable.lotter_height, 100);
		//销毁对象
		a.recycle();
		// 初始化路径
		mPath = new Path();

		// 初始化画笔
		mPaint = new Paint();
		//设置画笔颜色
		mPaint.setColor(mContext.getResources().getColor(R.color.view_color));
		//设置透明度
		mPaint.setAlpha(0);
		//设置相交模式
		mPaint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
        //设置防止锯齿
		mPaint.setAntiAlias(true);
		//设置画笔空心画笔
		mPaint.setStyle(Style.STROKE);
		//画笔宽度
		mPaint.setStrokeWidth(50);
		// 初始化Bitmap并且锁定到临时画布上
		mBitmap = Bitmap.createBitmap(widget, height, Bitmap.Config.ARGB_4444);
		tempCanvas = new Canvas();
		//绘制图片
		tempCanvas.setBitmap(mBitmap);
		//获取新的彩票
		againLotter();

	}
   //绘制控件
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		// 将处理过的bitmap画上去
		canvas.drawBitmap(mBitmap, 0, 0, null);
	}

   //手势判断
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int action = event.getAction();
		switch (action) {
			case MotionEvent.ACTION_DOWN://按下事件
				touchDown(event);
				break;
			case MotionEvent.ACTION_MOVE://手指移动事件
				touchMove(event);
				break;
			case MotionEvent.ACTION_CANCEL:
			case MotionEvent.ACTION_UP:
				break;
		}
		return true;
	}

	// 移动的时候
	private void touchMove(MotionEvent event) {
		//手指x坐标
		x = event.getX();
		//手机Y坐标
		y = event.getY();
		// 二阶贝塞尔曲线，实现平滑曲线；oX, oY为操作点 x,y为终点
		mPath.quadTo((x + ox) / 2, (y + oy) / 2, x, y);
		//绘制mPath进行绘制
		tempCanvas.drawPath(mPath, mPaint);
		ox = x;
		oy = y;
		//刷新控件
		invalidate();
	}
	// 按下事件
	private void touchDown(MotionEvent event) {
		ox = x = event.getX();
		oy = y = event.getY();
		//逆时针
		mPath.reset();
		//移动画笔
		mPath.moveTo(ox, oy);
	}
}
