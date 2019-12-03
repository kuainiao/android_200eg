package com.mingrisoft.mywhiteblock;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
//接口类
public abstract class BaseProperty {
	//画笔
	public Paint paint;
	//宽
	int screen_width=MainActivity.screen_width;
	//高
	int screen_height=MainActivity.screen_height;
	//手势处理接口
	public abstract void onTouch(MotionEvent event);
	
}
