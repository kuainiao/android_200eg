package com.mingrisoft.list.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.mingrisoft.list.R;

import java.util.List;

/**
 * Author LYJ
 * Created on 2016/12/19.
 * Time 08:55
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyHolder>{

    private LayoutInflater inflater;//布局填充器
    private List<Integer> list;//数据集合
    private Context context;//上下文

    /**
     * 构造方法
     * @param list
     * @param context
     */
    public MyAdapter(List<Integer> list, Context context) {
        this.list = list;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    /**
     * 创建item
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //填充布局
        View view = inflater.inflate(R.layout.list_item,parent,false);
        //创建复用类
        MyHolder holder = new MyHolder(view);
        return holder;
    }

    /**
     * 绑定控件
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        //下载图片
        Glide.with(context)
                .load(list.get(position))
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .fitCenter()
                .centerCrop()
                .transform(new GlideRoundTransform(context))
                .into(holder.pic);
    }

    /**
     * 获取数据条数
     * @return
     */
    @Override
    public int getItemCount() {
        return list.size();
    }

    /**
     * 复用类
     */
    class MyHolder extends RecyclerView.ViewHolder{
        ImageView pic;
        public MyHolder(View itemView) {
            super(itemView);
            pic = (ImageView) itemView.findViewById(R.id.food);
        }
    }

    /**
     * 圆角效果
     */
    public static class GlideRoundTransform extends BitmapTransformation {

        private static float radius = 0f;

        public GlideRoundTransform(Context context) {
            this(context, 4);
        }

        public GlideRoundTransform(Context context, int dp) {
            super(context);
            this.radius = Resources.getSystem().getDisplayMetrics().density * dp;
        }

        @Override
        protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
            return roundCrop(pool, toTransform);
        }

        private static Bitmap roundCrop(BitmapPool pool, Bitmap source) {
            if (source == null) return null;

            Bitmap result = pool.get(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
            if (result == null) {
                result = Bitmap.createBitmap(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
            }

            Canvas canvas = new Canvas(result);
            Paint paint = new Paint();
            paint.setShader(new BitmapShader(source, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
            paint.setAntiAlias(true);
            RectF rectF = new RectF(0f, 0f, source.getWidth(), source.getHeight());
            canvas.drawRoundRect(rectF, radius, radius, paint);
            return result;
        }

        @Override
        public String getId() {
            return getClass().getName() + Math.round(radius);
        }
    }
}
