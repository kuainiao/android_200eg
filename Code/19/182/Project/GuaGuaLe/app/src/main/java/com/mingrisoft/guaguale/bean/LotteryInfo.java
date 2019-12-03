package com.mingrisoft.guaguale.bean;

//抽奖信息实体类
public class LotteryInfo {
	// 中奖类型，可以叠加，也就是一等奖二等奖可以一起中,中了多个奖显示最高奖项。
	public final static int TYPE_FIRST = 1;
	public final static int TYPE_SECOND = 2;
	public final static int TYPE_THIRD = 3;
	public final static int TYPE_NONE = 4;
    //文字信息
	private String text;
	//中奖类型
	private int type;
	public LotteryInfo(int t, String text) {
		setType(t);
		setText(text);
	}
	public void setType(int t) {
		type = t ;
	}
	public int getType() {
		return type;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
}
