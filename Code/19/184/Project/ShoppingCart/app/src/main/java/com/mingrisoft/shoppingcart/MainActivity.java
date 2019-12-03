package com.mingrisoft.shoppingcart;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import animation.GoodsAnimation;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import data.Data;
import data.GoodsDataInterface;
import data.GoodsOperationMethod;
import adapter.RecyclerViewAdapter;

public class MainActivity extends Activity {

    /**
     * 列表菜单
     */
    @InjectView(R.id.recyclerView)
    RecyclerView recyclerView;
    /**
     * 总价
     */
    @InjectView(R.id.total_tv)
    TextView displayTotal;
    /**
     * 物品总数
     */
    @InjectView(R.id.sum_tv)
    TextView sum;
    /**
     * 内容数据适配器
     */
    private RecyclerViewAdapter recyclerViewAdapter = null;
    private Context mContext;
    /**
     * 存储数据
     */
    private List<String> contentList = new ArrayList<String>();
    public static int SELECTPOSITION = 0;
    /**
     * 数据操作接口
     */
    GoodsDataInterface goodsDataInterface = null;
    /**
     * 购物车布局
     */
    @InjectView(R.id.car_layout)
    RelativeLayout carLayout;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_main);
        //全屏
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,
                WindowManager.LayoutParams. FLAG_FULLSCREEN);

        ButterKnife.inject(MainActivity.this);
        mContext = this;
        initView();
        setRecyclerView();
        initHttp();
    }
    private void initView() {
        goodsDataInterface = GoodsOperationMethod.getInstance();
        //清空数据库缓存
        goodsDataInterface.deleteAll(mContext);
    }
    /**
     * 模拟网络请求数据
     */
    private void initHttp() {

        for (int i = 0; i < 4; i++) {
            contentList.add("2222");
        }
        setContentCommonadapter();
    }

    /**
     * 设置RecyclerView的布局方式
     */
    private void setRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
    }

    /**
     * 商品种类列表    数据填充
     */
    private void setContentCommonadapter() {
        recyclerViewAdapter = new RecyclerViewAdapter(mContext,contentList);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerViewAdapter.ViewHolder holder) {}
            @Override
            public void onItemLongClick(RecyclerViewAdapter.ViewHolder holder) {}
            /** 添加 */
            @Override
            public void onItemJiaClick(RecyclerViewAdapter.ViewHolder holder) {
                String numText = holder.number.getText().toString().trim();
                /** 点击加号之前还没有数据的时候 */
                if (numText.isEmpty() || numText.equals("0")) {
                    Log.e("TAG", "点击获取信息：SELECTPOSITION--" + SELECTPOSITION + "  DemoData.ListMenu_GOODSID[position]--" + Data.ListMenu_FOODID[holder.getPosition()]);
                    holder.imgreduce.setVisibility(View.VISIBLE);
                    holder.number.setText(goodsDataInterface.saveGoodsNumber(mContext, SELECTPOSITION, Data.ListMenu_FOODID[holder.getPosition()], "1", Data.ListMenu_PRICE[holder.getPosition()]) + "");
                    holder.number.setVisibility(View.VISIBLE);
                }/** 点击加号之前有数据的时候 */
                else {
                    holder.number.setText(goodsDataInterface.saveGoodsNumber(mContext, SELECTPOSITION, Data.ListMenu_FOODID[holder.getPosition()], String.valueOf(Integer.parseInt(numText) + 1), Data.ListMenu_PRICE[holder.getPosition()]) + "");
                }
                /** 动画 */
                GoodsAnimation.setAnim(MainActivity.this, holder.imgAdd, carLayout);
                GoodsAnimation.setOnEndAnimListener(new onEndAnim());
                /** 统计购物总数和购物总价 */
            }
            /** 减少 */
            @Override
            public void onItemJianClick(RecyclerViewAdapter.ViewHolder holder) {
                String numText = holder.number.getText().toString().trim();
                holder.number.setText(goodsDataInterface.saveGoodsNumber(mContext, SELECTPOSITION, Data.ListMenu_FOODID[holder.getPosition()], String.valueOf(Integer.parseInt(numText) - 1), Data.ListMenu_PRICE[holder.getPosition()]) + "");
                numText = holder.number.getText().toString().trim();
                /** 减完之后  数据为0 */
                if (numText.equals("0")) {
                    holder.number.setVisibility(View.GONE);
                    holder.imgreduce.setVisibility(View.GONE);
                }
                setAll();
            }
        });
    }
    /**
     * 动画结束后，更新所有数量和所有价格
     */
    class onEndAnim implements GoodsAnimation.OnEndAnimListener {
        @Override
        public void onEndAnim() {
            setAll();
        }
    }
    /**
     * 点击加号和减号的时候设置总数和总价格
     */
    private void setAll() {
        //设置所有购物数量
        if (goodsDataInterface.getGoodsNumberAll(mContext, SELECTPOSITION) == 0) {
            sum.setVisibility(View.GONE);
            displayTotal.setText("￥0");
            sum.setText("0");
        } else {
            displayTotal.setText("￥" + goodsDataInterface.getGoodsPriceAll(mContext, SELECTPOSITION) + "");
            sum.setText(goodsDataInterface.getGoodsNumberAll(mContext, SELECTPOSITION) + "");
            sum.setVisibility(View.VISIBLE);
        }
    }
}