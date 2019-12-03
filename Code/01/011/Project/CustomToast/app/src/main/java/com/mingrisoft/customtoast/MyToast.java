package com.mingrisoft.customtoast;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Author LYJ
 * Created on 2017/1/26.
 * Time 10:20
 */

public final class MyToast {

    public final Toast toast;//Toast对象
    public final View view;//Toast的UI效果
    public final ImageView icon;//图标
    public final TextView message;//内容

    public MyToast(Context context) {
        toast = new Toast(context);
        view = LayoutInflater.from(context).inflate(R.layout.toast_layout,null);
        icon = (ImageView) view.findViewById(R.id.toast_icon);
        message = (TextView) view.findViewById(R.id.toast_message);
    }

    /**
     * 显示
     */
    public void show(){
        this.toast.show();
    }

    public static class Builder{
        private Bitmap icon;//图标图片
        private int iconID = R.mipmap.ic_launcher;//图标资源ID
        private String message;//内容
        private int backgroundColor = 0x56000000;//背景颜色
        private Context mContext;//上下文
        private int duration = Toast.LENGTH_SHORT;//设置时间
        private MyToast mine;
        private int gravity = Gravity.NO_GRAVITY;//设置位置
        private int offsetX = 0;//设置偏移度X
        private int offsetY = 0;//设置偏移度Y
        private boolean isShowIcon;//是否显示图标
        public Builder(Context context) {
            this.mContext = context;
        }
        /**
         * 设置ICON
         * @param bitmap
         * @return
         */
        public Builder setIcon(Bitmap bitmap){
            this.icon = bitmap;
            return this;
        }
        public Builder setIcon(@DrawableRes int resId){
            this.iconID = resId;
            return this;
        }

        public Builder showIcon(boolean showIcon){
            this.isShowIcon = showIcon;
            return this;
        }
        /**
         * 设置内容
         */
        public Builder setMessage(String hintMessage){
            this.message = hintMessage;
            return this;
        }

        /**
         * 设置吐司时长
         */
        public Builder setDuration(int type){
            this.duration = type;
            return this;
        }
        /**
         * 设置背景
         */
        public Builder setBackgroundColor(@ColorInt int color){
            this.backgroundColor = color;
            return this;
        }
        /**
         * 设置位置
         */
        public Builder setGravity(int gravity){
            this.gravity = gravity;
            return this;
        }
        /**
         * 偏移量
         */
        public Builder setOffsetX(int x){
            this.offsetX = x;
            return this;
        }
        public Builder setOffsetY(int y){
            this.offsetY = y;
            return this;
        }
        /**
         * 创建MyToast对象
         */
        public MyToast build(){
            if (null == mine){
                mine = new MyToast(mContext);//创建对象
            }
            if (isShowIcon){
                //隐藏图标
                mine.icon.setVisibility(View.VISIBLE);
                if (null != icon){//判断是否显示图标
                    mine.icon.setImageBitmap(icon);//设置图片
                }else {
                    //设置图片
                    mine.icon.setBackgroundResource(iconID);
                }
            }
            if (!message.isEmpty()){//判断内容是否为空
                mine.message.setText(message);
            }else {
                mine.message.setText("");
            }
            //设置背景
            mine.view.setBackground(new BackgroundDrawable(backgroundColor,mContext));
            mine.toast.setDuration(duration);//设置时长
            mine.toast.setView(mine.view);//添加自定义效果
            mine.toast.setGravity(gravity,offsetX,offsetY);//设置偏移量
            return mine;
        }
    }

}
