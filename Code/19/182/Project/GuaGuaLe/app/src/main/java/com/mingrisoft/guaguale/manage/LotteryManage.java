package com.mingrisoft.guaguale.manage;

import com.mingrisoft.guaguale.bean.LotteryInfo;
import java.util.Random;

//设置中奖比例 以及中奖显示信息
public class LotteryManage {
	public static LotteryInfo getRandomLottery() {
		LotteryInfo info = null;
		//声明随机类
		Random r = new Random();
		//获取1000以内随机数
		int num = r.nextInt(1000);
		if (num < 10) {//判断随机数小于10
			info = new LotteryInfo(LotteryInfo.TYPE_FIRST, "一等奖");
		} else if (num < 30) {//判断随机数小于30
			info = new LotteryInfo(LotteryInfo.TYPE_SECOND, "二等奖");
		} else if (num < 100) {//判断随机数小于100
			info = new LotteryInfo(LotteryInfo.TYPE_THIRD, "三等奖");
		} else {//其他随机数
			info = new LotteryInfo(LotteryInfo.TYPE_NONE, "再接再厉");
		}
		return info;
	}
}
