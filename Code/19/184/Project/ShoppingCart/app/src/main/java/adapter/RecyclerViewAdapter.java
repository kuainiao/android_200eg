package adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;


import com.mingrisoft.shoppingcart.R;

import java.util.List;

import com.mingrisoft.shoppingcart.MainActivity;
import data.Data;
import data.GoodsDataInterface;
import data.GoodsOperationMethod;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{
    private LayoutInflater layoutInflater;
    protected List<String> listContentData;
    private Context lContext;
    private OnItemClickListener onItemClickListener;
    /** 数据操作接口 */
    GoodsDataInterface goodsDataInterface = null;

    //定义接口
    public interface OnItemClickListener{
        void onItemClick(ViewHolder holder);
        void onItemLongClick(ViewHolder holder);
        void onItemJiaClick(ViewHolder holder);
        void onItemJianClick(ViewHolder holder);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.onItemClickListener = listener ;
    }
    public RecyclerViewAdapter(Context context, List<String> datas){
        this.listContentData = datas;
        this.lContext = context;
        layoutInflater = LayoutInflater.from(lContext);
        goodsDataInterface = GoodsOperationMethod.getInstance();
    }
    //创建ViewHolder
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = layoutInflater.inflate(R.layout.menu_item_content,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }
    //绑定ViewHolder
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        //设置菜单图片
        holder.imageView.setImageResource(Data.ListMenu_IMG[position]);
        //设置菜单名称
        holder.title.setText(Data.ListMenu_NAME[position]);
        holder.yueSale.setText("月售" + Data.ListMenu_NUMBER[position]);
        holder.price.setText(Data.ListMenu_PRICE[position]);

        holder.mRatingBar.setRating(Float.parseFloat(Data.ListMenu_STAR[position]));
        holder.mRatingBar.getRating();

        /** 获取存储的商品数量 */
        if (goodsDataInterface.getGoodsNumber(lContext, MainActivity.SELECTPOSITION , Data.ListMenu_FOODID[holder.getPosition()]) == 0) {
            holder.number.setText("");
            holder.number.setVisibility(View.GONE);
            holder.imgreduce.setVisibility(View.GONE);
        } else {
            holder.number.setText("" + goodsDataInterface.getGoodsNumber(lContext, MainActivity.SELECTPOSITION , Data.ListMenu_FOODID[holder.getPosition()]));
            holder.number.setVisibility(View.VISIBLE);
            holder.imgreduce.setVisibility(View.VISIBLE);
        }

        setOnListtener(holder);
    }
    //触发
    protected void setOnListtener(final ViewHolder holder){
        if(onItemClickListener != null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(holder);
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    onItemClickListener.onItemLongClick(holder);
                    return true;
                }
            });
            holder.imgAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemJiaClick(holder);
                }
            });
            holder.imgreduce.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemJianClick(holder);
                }
            });
        }
    }
    @Override
    public int getItemCount() {
        return listContentData.size();
    }

    /**
     * 加载子菜单中的控件
     */
    public class ViewHolder extends RecyclerView.ViewHolder{
        //菜品图标、增加数量的图标、减少数量的图标
        public ImageView imageView , imgAdd , imgreduce;
        //显示菜名、月销售数量、价格、数量
        public TextView title , yueSale , price , number;
        public RatingBar mRatingBar;
        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.item_img);
            title = (TextView) itemView.findViewById(R.id.item_title);
            mRatingBar = (RatingBar) itemView.findViewById(R.id.item_star);
            yueSale = (TextView) itemView.findViewById(R.id.item_sale);
            price = (TextView) itemView.findViewById(R.id.item_price);
            imgAdd = (ImageView) itemView.findViewById(R.id.item_increase_icon);
            imgreduce = (ImageView) itemView.findViewById(R.id.item_reduce_icon);
            number = (TextView) itemView.findViewById(R.id.item_number);
        }
    }
}
