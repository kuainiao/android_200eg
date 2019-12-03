package com.mingrisoft.planefighter.model;

import com.mingrisoft.planefighter.R;

/**
 * Created by zerob13 on 12/7/13.
 */
public class Bullet extends BaseModel {
	public static final int BULLET_RES = R.drawable.bullet;

	public Bullet(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void onUpdate(int step) {
		y -= step;
	}
}
