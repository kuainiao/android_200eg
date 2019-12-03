package com.mingrisoft.contentprovider;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.mingrisoft.contentprovider.MyImageView.OnMeasureListener;
import com.mingrisoft.contentprovider.NativeImageLoader.NativeImageCallBack;

public class GroupAdapter extends BaseAdapter{
	//图片信息数组
	private List<ImageBean> list;
	//用来封装ImageView的宽和高的对象
	private Point mPoint = new Point(0, 0);
	//显示图片信息的列表
	private GridView mGridView;
	protected LayoutInflater mInflater;

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public GroupAdapter(Context context, List<ImageBean> list, GridView mGridView){
		this.list = list;
		this.mGridView = mGridView;
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;
		ImageBean mImageBean = list.get(position);
		String path = mImageBean.getTopImagePath();
		if(convertView == null){
			viewHolder = new ViewHolder();
			//设置布局
			convertView = mInflater.inflate(R.layout.grid_group_item, null);
			viewHolder.mImageView = (MyImageView) convertView.findViewById(R.id.group_image);
			viewHolder.mTextViewTitle = (TextView) convertView.findViewById(R.id.group_title);
			viewHolder.mTextViewCounts = (TextView) convertView.findViewById(R.id.group_count);

			//用来监听ImageView的宽和高
			viewHolder.mImageView.setOnMeasureListener(new OnMeasureListener() {
				@Override
				public void onMeasureSize(int width, int height) {
					mPoint.set(width, height);
				}
			});
			convertView.setTag(viewHolder);
		}else{
			//添加标记
			viewHolder = (ViewHolder) convertView.getTag();
			//设置默认图片
			viewHolder.mImageView.setImageResource(R.drawable.friends_sends_pictures_no);
		}
		//设置图片文件夹名称
		viewHolder.mTextViewTitle.setText(mImageBean.getFolderName());
		//设置图片数量
		viewHolder.mTextViewCounts.setText(Integer.toString(mImageBean.getImageCounts()));
		//给ImageView设置路径Tag,这是异步加载图片的小技巧
		viewHolder.mImageView.setTag(path);


		//利用NativeImageLoader类加载本地图片
		Bitmap bitmap = NativeImageLoader.getInstance().loadNativeImage(path, mPoint, new NativeImageCallBack() {

			@Override
			public void onImageLoader(Bitmap bitmap, String path) {
				ImageView mImageView = (ImageView) mGridView.findViewWithTag(path);
				if(bitmap != null && mImageView != null){
					//设置图片
					mImageView.setImageBitmap(bitmap);
				}
			}
		});

		if(bitmap != null){
			//设置图片
			viewHolder.mImageView.setImageBitmap(bitmap);
		}else{
			viewHolder.mImageView.setImageResource(R.drawable.friends_sends_pictures_no);
		}
		return convertView;
	}

	public static class ViewHolder{
		public MyImageView mImageView;
		public TextView mTextViewTitle;
		public TextView mTextViewCounts;
	}





}
