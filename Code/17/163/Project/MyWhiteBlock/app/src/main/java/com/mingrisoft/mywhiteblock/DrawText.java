package com.mingrisoft.mywhiteblock;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;

public class DrawText {

	private String Text="1000";
	private int TextSize=0;
	private int x;
	private int y;
	float x_left;
	float x_right;
	float y_top;
	float y_botton;
	private Paint  paint;
	Rect targetRect;
	public DrawText() {
		// TODO Auto-generated constructor stub
		paint=new Paint();
		paint.setTextSize(TextSize);
		paint.setColor(Color.WHITE);
		paint.setAntiAlias(true);
		//x y为text的中心点
		paint.setTextAlign(Paint.Align.CENTER);
		targetRect = new Rect();
		//返回包围整个字符串的最小的一个Rect区域
		paint.getTextBounds(Text, 0, Text.length(), targetRect); 
	}
	public void setText(String Text,int size){
		this.Text=Text;
		paint.setTextSize(size);
	}

	public void setXY(int x,int y){
		this.x=x;
		this.y=y;
		init();
	}
	public void init(){
		int textheight=targetRect.height();
		int textwidth=targetRect.width();
		x_left=x-textwidth*6;
		x_right=x+textwidth*6;
		y_top=y-textheight*6;
		y_botton=y+textheight*6;
	}
	public void ondraw(Canvas canvas){
		canvas.drawText(Text,x ,y , paint);
	}
	public boolean onTouch(MotionEvent event){
		//判断点击区域
		if(event.getX()>x_left-100&&event.getX()<x_left+100){
			if(event.getY()<y_top+100&&event.getY()>y_botton-100){
				return true;
			}
		}
		return false;
	}
	
}
