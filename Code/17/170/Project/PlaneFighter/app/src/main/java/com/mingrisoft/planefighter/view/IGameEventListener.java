package com.mingrisoft.planefighter.view;

/**
 * Created by zerob13 on 12/7/13.
 */
public interface IGameEventListener {
	void onGameStart();

	void onGameFinish(int score);

}
