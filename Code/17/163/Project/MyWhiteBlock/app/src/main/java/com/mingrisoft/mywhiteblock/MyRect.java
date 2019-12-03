package com.mingrisoft.mywhiteblock;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;

public class MyRect extends BaseProperty {

	private Paint paintwite;
	private Paint paintgray;
	private Paint paintred;
	int  flag=2;
	public MyRect(){
		flag=2;
		paintwite=new Paint();
		paintwite.setColor(Color.WHITE);
		paintwite.setStyle(paintwite.getStyle().FILL);
		
		paintgray=new Paint();
		paintgray.setColor(Color.GRAY);
		paintgray.setStyle(paintwite.getStyle().FILL);
		
		paintred=new Paint();
		paintred.setColor(Color.RED);
		paintred.setStyle(paintred.getStyle().FILL);
		
		paint=new Paint();
		paint.setColor(Color.BLACK);
		paint.setStyle(paint.getStyle().FILL);
	}
	public void setFlag(int flag){
		this.flag=flag;
		
	}

	public void ondraws(Canvas canvas,Rect rect,int flag) {
		// TODO Auto-generated method stub
		switch (flag) {
		//黑色
		case 1:
			canvas.drawRect(rect, paint);
			break;
		//白色
		case 2:
			canvas.drawRect(rect, paintwite);
			break;
		//灰色			
		case 3:
			canvas.drawRect(rect, paintgray);
			break;
		//红色			
		case 4:
			canvas.drawRect(rect, paintred);
			break;
		}	
	}

	@Override
	public void onTouch(MotionEvent event) {
		// TODO Auto-generated method stub

	}

}
