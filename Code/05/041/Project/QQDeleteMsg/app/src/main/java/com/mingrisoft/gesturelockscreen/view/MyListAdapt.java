package com.mingrisoft.gesturelockscreen.view;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.mingrisoft.gesturelockscreen.R;
import com.mingrisoft.gesturelockscreen.view.view.DrawCircle;
import com.mingrisoft.gesturelockscreen.view.view.DrawCircleListener;


import java.util.ArrayList;


/**
 * Created by Root on 2016/6/22.
 */


public class MyListAdapt extends BaseAdapter {

    private ArrayList<String> mData;

    Activity mContext;

    private ArrayList<Integer> mRmoveList;


    public MyListAdapt(Context context, ArrayList<String> data) {

        mData = data;
        mContext = (Activity) context;
        mRmoveList = new ArrayList<Integer>();        //存放移除的消息

    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    ViewHolder mViewHolder;
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_list, null);
            mViewHolder = new ViewHolder(convertView);

            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();

        }
        addData(mViewHolder,mData,position);


        final int temp = position + 1;

        boolean isVisable = !mRmoveList.contains(temp);
        mViewHolder.msgPoint.setVisibility(isVisable ? View.VISIBLE : View.INVISIBLE);
        if (isVisable) {

            mViewHolder.msgPoint.setText("" + temp);
            mViewHolder.msgPoint.setTag(temp);

            DrawCircleListener listener = new DrawCircleListener(mContext, mViewHolder.msgPoint) {

                @Override
                public void onDisappear(DrawCircle dragBall, float x, float y) {
                    super.onDisappear(dragBall, x, y);

                    mRmoveList.add(temp);

                    showToast("条目" + temp + "消息小球被移除");
                }

                @Override
                public void onReset() {
                    super.onReset();

                    showToast("条目" + temp + "消息小球被回到原位");
                }
            };
            mViewHolder.msgPoint.setOnTouchListener(listener);
        }

        return convertView;
    }

    private void addData(ViewHolder mViewHolder, ArrayList<String> mData, int position) {
        mViewHolder.name.setText(mData.get(position));

        switch (position){
            case 0:
                mViewHolder.name.setText("小红");
                mViewHolder.msg.setText("祝你生日快乐，永远开心。");
                mViewHolder.time.setText("上午12:21");
                mViewHolder.imageView.setImageResource(R.mipmap.a);
                break;

            case 1:
                mViewHolder.name.setText("小刚");
                mViewHolder.msg.setText("你妈叫你回家吃饭去呢，找不到你，让我转告你");
                mViewHolder.time.setText("上午9:20");
                mViewHolder.imageView.setImageResource(R.mipmap.b);
                break;

            case 2:
                mViewHolder.name.setText("兰兰");
                mViewHolder.msg.setText("好的有时间咱们一起聚一聚，地点就在你家");
                mViewHolder.time.setText("上午11:23");
                mViewHolder.imageView.setImageResource(R.mipmap.c);
                break;

            case 3:
                mViewHolder.name.setText("一帆");
                mViewHolder.msg.setText("后天咱们去老张那，还有小眉毛也在呢");
                mViewHolder.time.setText("下午2:40");
                mViewHolder.imageView.setImageResource(R.mipmap.d);
                break;

            case 4:
                mViewHolder.name.setText("海风");
                mViewHolder.msg.setText("周一一起去滑雪吧，我都买好票了。");
                mViewHolder.time.setText("下午11:22");
                mViewHolder.imageView.setImageResource(R.mipmap.image);
                break;

        }
    }


    private void showToast(String string) {
        Toast.makeText(mContext, string, Toast.LENGTH_SHORT).show();
    }

    public class ViewHolder {
        public  TextView name,msg,time;
        public  View root;
        public  TextView msgPoint;
        public ImageView imageView;

        public ViewHolder(View root) {

            this.root = root;
            /**
             * 设置item的控件id
             * */
            name = (TextView) root.findViewById(R.id.name);
            msg = (TextView) root.findViewById(R.id.msg);
            time = (TextView) root.findViewById(R.id.text_time);
            msgPoint = (TextView) root.findViewById(R.id.msg_point);
            imageView = (ImageView) root.findViewById(R.id.person_image);


        }
    }
}
