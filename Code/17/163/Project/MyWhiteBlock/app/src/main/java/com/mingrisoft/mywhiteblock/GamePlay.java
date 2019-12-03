package com.mingrisoft.mywhiteblock;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;
import com.mingrisoft.mywhiteblock.GameMenu.changeState;

public class GamePlay extends BaseProperty {

	List<RowRect> rowrects;
	int k = -1;// 判断第几个被点击
	int SPEED = 1;//游戏的速度
	private changeState change;
	public boolean can_click = true;// 方块是否可以被点击

	public GamePlay() {

	}
	public void init(){
		// 创建5行，每一行中4个块，i代表第几行
		rowrects = new ArrayList<RowRect>();
		for (int i = 0; i < 5; i++) {
			rowrects.add(new RowRect(i));
		}
		//初始化速度为1
		SPEED=1;
		//定时增加速度
		GameScore.SCORE=0;
		
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				SPEED=SPEED+1;
			}
		}, 3000, 3000);//在1秒后执行此任务,每次间隔2秒,如果传递一个Data参数,就可以在某个固定的时间执行这个任务.
	}

	public void setchange(changeState change) {
		this.change = change;
	}

	public void ondraw(Canvas canvas) {
		// TODO Auto-generated method stub
		// 对行进行绘画
		for (int i = 0; i < rowrects.size(); i++) {
			rowrects.get(i).ondraw(canvas);
		}

	}

	@Override
	public void onTouch(MotionEvent event) {
		// TODO Auto-generated method stub
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			if (can_click) {
				for (int i = 0; i < rowrects.size(); i++) {
					rowrects.get(i).onTouch(event);
				}
				can_click=false;
			}
			break;

		case MotionEvent.ACTION_UP:
			can_click = true;
			break;
		}

	}

	public void logic() {
		for (int i = 0; i < rowrects.size(); i++) {
			// 判断游戏是否结束
			if (rowrects.get(i).isGAME_STATE()) {
				change.execute(GameState.STATE_OVER);
			}
			// 移动每一行Y值
			rowrects.get(i).moveY(SPEED);
			// 判断行是否出屏，出屏死亡
			if (rowrects.get(i).isDie()) {
				// 死亡后取出此行，
				RowRect rowrect = rowrects.get(i);
				// 吧此行从list集合中移除
				rowrects.remove(i);
				// 重置此行为第0行
				rowrect.setI(0);
				// 重置此行生命为false
				rowrect.setDie(false);
				// 重置此行点击为false
				rowrect.setClick(false);
				// 重新添加进list集合进行绘制
				rowrects.add(0, rowrect);

			}

		}
	}
}
