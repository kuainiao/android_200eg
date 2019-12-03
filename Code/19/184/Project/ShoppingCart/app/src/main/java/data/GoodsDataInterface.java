package data;

import android.content.Context;

/**
 *商品信息接口
 */
public interface GoodsDataInterface {
    /** 添加和删除购物的数量 */
    int saveGoodsNumber(Context context, int menupos, int goodsid, String goodsnum, String goodsprice);

    /** 根据下标得到 对应购物的数量 */
    int getGoodsNumber(Context context, int menupos, int goodsid);

    /** 根据下标 得到所有购物数量 */
    int getGoodsNumberAll(Context context, int menupos);

    /** 根据下标 得到所有购物的价格 */
    int getGoodsPriceAll(Context context, int menupos);
    /** 删除所有的购物数据 */
    void deleteAll(Context context);

}
