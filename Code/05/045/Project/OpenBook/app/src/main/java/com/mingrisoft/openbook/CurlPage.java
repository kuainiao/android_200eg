/*
   Copyright 2012 Harri Smatt

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

package com.mingrisoft.openbook;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.RectF;

public class CurlPage {

	public static final int SIDE_BACK = 2;
	public static final int SIDE_BOTH = 3;
	public static final int SIDE_FRONT = 1;

	private int mColorBack;
	private int mColorFront;
	private Bitmap mTextureBack;
	private Bitmap mTextureFront;
	private boolean mTexturesChanged;

	/**
	 * 构造函数
	 */
	public CurlPage() {
		reset();
	}

	/**
	 * 获取颜色
	 */
	public int getColor(int side) {
		switch (side) {
		case SIDE_FRONT:
			return mColorFront;
		default:
			return mColorBack;
		}
	}

	/**
	 * 计算给定整数的两个的下一个最高功率。
	 */
	private int getNextHighestPO2(int n) {
		n -= 1;
		n = n | (n >> 1);
		n = n | (n >> 2);
		n = n | (n >> 4);
		n = n | (n >> 8);
		n = n | (n >> 16);
		n = n | (n >> 32);
		return n + 1;
	}

	/**
	 * 生成位图的两个大小的位图的最近的功率。返回此
	 * 使用默认返回语句+原始纹理坐标的新位图
	 * 存储在rectf。
	 */
	private Bitmap getTexture(Bitmap bitmap, RectF textureRect) {
		// 原始位图的大小。
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		// 位图的大小扩展到两个的下一个功率。这是由于
		// 在许多设备上的要求，纹理宽度和高度应
		// 是两个力量的力量。
		int newW = getNextHighestPO2(w);
		int newH = getNextHighestPO2(h);


		Bitmap bitmapTex = Bitmap.createBitmap(newW, newH, bitmap.getConfig());
		Canvas c = new Canvas(bitmapTex);
		c.drawBitmap(bitmap, 0, 0, null);

		// 计算最终的纹理坐标。
		float texX = (float) w / newW;
		float texY = (float) h / newH;
		textureRect.set(0f, 0f, texX, texY);

		return bitmapTex;
	}

	/**
	 * 吸气剂的纹理。创建位图大小到最接近的两个电源，拷贝
	 * 原始位图到它，并返回它。给定的参数是rectf
	 * 在这个充满新的放大的纹理实际纹理坐标
	 * 位图。
	 */
	public Bitmap getTexture(RectF textureRect, int side) {
		switch (side) {
		case SIDE_FRONT:
			return getTexture(mTextureFront, textureRect);
		default:
			return getTexture(mTextureBack, textureRect);
		}
	}

	/**
	 * 如果纹理改变了，返回真。
	 */
	public boolean getTexturesChanged() {
		return mTexturesChanged;
	}

	/**
	 * 返回true如果背线纹理存在，它不同于前
	 * 面对一个。
	 */
	public boolean hasBackTexture() {
		return !mTextureFront.equals(mTextureBack);
	}

	/**
	 * 回收和释放潜在的位图
	 */
	public void recycle() {
		if (mTextureFront != null) {
			mTextureFront.recycle();
		}
		mTextureFront = Bitmap.createBitmap(1, 1, Bitmap.Config.RGB_565);
		mTextureFront.eraseColor(mColorFront);
		if (mTextureBack != null) {
			mTextureBack.recycle();
		}
		mTextureBack = Bitmap.createBitmap(1, 1, Bitmap.Config.RGB_565);
		mTextureBack.eraseColor(mColorBack);
		mTexturesChanged = false;
	}

	/**
	 * 重置初始状态
	 */
	public void reset() {
		mColorBack = Color.WHITE;
		mColorFront = Color.WHITE;
		recycle();
	}

	/**
	 * 设置混合色。
	 */
	public void setColor(int color, int side) {
		switch (side) {
		case SIDE_FRONT:
			mColorFront = color;
			break;
		case SIDE_BACK:
			mColorBack = color;
			break;
		default:
			mColorFront = mColorBack = color;
			break;
		}
	}

	/**
	 * 设置纹理。
	 */
	public void setTexture(Bitmap texture, int side) {
		if (texture == null) {
			texture = Bitmap.createBitmap(1, 1, Bitmap.Config.RGB_565);
			if (side == SIDE_BACK) {
				texture.eraseColor(mColorBack);
			} else {
				texture.eraseColor(mColorFront);
			}
		}
		switch (side) {
		case SIDE_FRONT:
			if (mTextureFront != null)
				mTextureFront.recycle();
			mTextureFront = texture;
			break;
		case SIDE_BACK:
			if (mTextureBack != null)
				mTextureBack.recycle();
			mTextureBack = texture;
			break;
		case SIDE_BOTH:
			if (mTextureFront != null)
				mTextureFront.recycle();
			if (mTextureBack != null)
				mTextureBack.recycle();
			mTextureFront = mTextureBack = texture;
			break;
		}
		mTexturesChanged = true;
	}

}
