package com.mingrisoft.mywhiteblock;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;

public class GameScore extends BaseProperty {

	public static int SCORE=0;//分数
	@Override
	public void onTouch(MotionEvent event) {
		// TODO Auto-generated method stub

	}
	public GameScore(){
		paint=new Paint();
		paint.setColor(Color.RED);
		paint.setTextSize(40);
		paint.setAntiAlias(true);
	}
	public void ondraw(Canvas canvas){
		canvas.drawText("分数"+SCORE, screen_width/2-50, 50, paint);
	}

}
