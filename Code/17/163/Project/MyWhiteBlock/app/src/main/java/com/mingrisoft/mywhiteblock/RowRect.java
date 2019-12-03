package com.mingrisoft.mywhiteblock;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;
import com.mingrisoft.mywhiteblock.GameMenu.changeState;

public class RowRect extends BaseProperty {

	int top, bottom, left, right;
	MyRect myrect[] = new MyRect[4];
	int speed = 1;
	int y = 0;// 块的起始位置 speed
	int l = screen_height / 4;// 快之间的高端间隔
	int i = 0;// 代表第几行
	Rect rects[] = new Rect[4];
	int a = 0;// 随机数 方块为黑色
	int k=-1;//点击第几块
	public boolean die = false;// 方块的生命，不见即为die
	public boolean click = false;// 方块是否被点击了
	public boolean GAME_STATE = false;// false 游戏未死亡
	private Paint paintLine;
	private changeState change;
	public RowRect(int i) {
		init(i);
	}
	
	public void setChange(changeState change){
		this.change=change;
	}

	// 绘制初始化，构造4个块的位置
	public void init(int i) {

		paintLine = new Paint();
		paintLine.setColor(Color.GRAY);
		paintLine.setStrokeWidth(1);

		y = screen_height / 4 * (i - 1);

		a = (int) (Math.random() * 4);
		top = y + l * i;
		bottom = y + l * (i + 1);
		left = 0;
		right = screen_width / 4;

		for (int p = 0; p < rects.length; p++) {
			rects[p] = new Rect(left + p * screen_width / 4, top, left + p
					* screen_width / 4 + screen_width / 4, top + l);
		}

		for (int m = 0; m < 4; m++) {
			myrect[m] = new MyRect();
		}
	}

	// 设置第几行，重新构造块的位置
	public void setI(int i) {
		this.i = i;
		init(i);
	}

	// 开始绘制块
	public void ondraw(Canvas canvas) {
		// TODO Auto-generated method stub
		for (int j = 0; j < 4; j++) {
			rects[j].top = y + l * i;
			rects[j].bottom = y + l * (i + 1);
			//没有点击的块的画法
			if (a == j) {
				myrect[j].ondraws(canvas, rects[j], 1);
			} else {
				myrect[j].ondraws(canvas, rects[j], 2);
			}
			//点击的块的画法
			if(click){
				//黑色的块被点击,画灰色
				if(a==k){
					myrect[k].ondraws(canvas, rects[k], 3);
				}else{
					//白色的块被点击,画红色
					myrect[k].ondraws(canvas, rects[k], 4);
					//画红色代表游戏结束
					GAME_STATE=true;
				}
			}

		}
		drawline(canvas);
	}

	private void drawline(Canvas canvas) {
		// TODO Auto-generated method stub
		// 竖线
		canvas.drawLine(screen_width / 4, 0, screen_width / 4, screen_height,
				paintLine);
		canvas.drawLine(screen_width * 2 / 4, 0, screen_width * 2 / 4,
				screen_height, paintLine);
		canvas.drawLine(screen_width * 3 / 4, 0, screen_width * 3 / 4,
				screen_height, paintLine);
		// 横线
		canvas.drawLine(0, y + l * i, screen_width, y + l * i, paintLine);
	}

	// 移动下滑
	public void moveY(int speed) {
		this.y = y + speed;
		// 判断是否出屏
		if (y >= screen_height) {
			if(!click){
				//画红色代表游戏结束
				GAME_STATE=true;
			}
			click = false;
			setDie(true);
		}
	}

	@Override
	public void onTouch(MotionEvent event) {
		// TODO Auto-generated method stub
				// 点击位置在此行 Y内
				if (TouchRect_y(event.getY(), rects[a].top, rects[a].bottom)) {
					click = true;
					k=TouchRect_x(event.getX());
					//分数
					GameScore.SCORE=GameScore.SCORE+1;
				}
	}

	// 判断是否是点击的这一行
	public boolean TouchRect_y(float f, int y_top, int y_botton) {
		if (f > y_top && f < y_botton) {
			return true;
		}
		return false;
	}

	// 判断点击的那一块
	public int TouchRect_x(float x) {
		if (0 < x && x < screen_width * 1 / 4) {
			return 0;
		} else if (screen_width * 1 / 4 < x && x < screen_width * 2 / 4) {
			return 1;
		} else if (screen_width * 2 / 4 < x && x < screen_width * 3 / 4) {
			return 2;
		} else if (screen_width * 3 / 4 < x && x < screen_width * 4 / 4) {
			return 3;
		} 
			return 0;
	}

	public boolean isDie() {
		return die;
	}

	public void setDie(boolean die) {
		this.die = die;
	}

	public boolean isClick() {
		return click;
	}

	public void setClick(boolean click) {
		this.click = click;
	}

	public boolean isGAME_STATE() {
		return GAME_STATE;
	}

	public void setGAME_STATE(boolean gAME_STATE) {
		GAME_STATE = gAME_STATE;
	}
}
