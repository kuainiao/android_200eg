package com.mingrisoft.imagechoose;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;


public class TemplateFragment extends Fragment {
	private Handler handler;
	private MyImageLoading imageLoading;
	private View leftShake, rightShake;
	private View leftClickArea, rightClickArea;
	private OnClickListener btnListener;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.template_layout, null);

		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				initAnimations();
			}
		};

		leftShake = rootView.findViewById(R.id.left_shake);
		rightShake = rootView.findViewById(R.id.right_shake);
		leftClickArea = rootView.findViewById(R.id.left_click_area);
		rightClickArea = rootView.findViewById(R.id.right_click_area);
		imageLoading = (MyImageLoading) rootView.findViewById(R.id.image_loading);
		/**
		 * 定义左右按钮的点击监听事件
		 * */
		btnListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				int type = 0;
				/**
				 * 点击不同不同按钮执行不同的操作，并传递相关的参数
				 * */
				if (v.getId() == leftClickArea.getId()) {
					type = -1;  //如果点击左侧的按钮
					imageLoading.leftBtn(getActivity(),type); //调用imageLoading类中的方法
				} else if (v.getId() == rightClickArea.getId()) {
					type = 1;   //如果点击右侧的按钮
					imageLoading.rightBtn(getActivity(),type);  //调用imageLoading类中的方法
				}
			}
		};
		
		leftClickArea.setOnClickListener(btnListener);
		rightClickArea.setOnClickListener(btnListener);

		delayShowSlidePanel();
		return rootView;
	}

	private void initAnimations() {
		Animation animationLeft = AnimationUtils.loadAnimation(getActivity(),
				R.anim.left_shake);
		Animation animationRight = AnimationUtils.loadAnimation(getActivity(),
				R.anim.right_shake);
		animationLeft.setInterpolator(new OvershootInterpolator(3));
		animationRight.setInterpolator(new OvershootInterpolator(3));
		leftShake.startAnimation(animationLeft);
		rightShake.startAnimation(animationRight);
		leftShake.setVisibility(View.VISIBLE);
		rightShake.setVisibility(View.VISIBLE);
	}

	private void delayShowSlidePanel() {

		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				handler.sendEmptyMessage(1);
			}
		}, 1200);

	}
}
