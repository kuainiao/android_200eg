package data;
import android.content.Context;

/**
 * 商品数据操作的方法
 */
public class GoodsOperationMethod implements GoodsDataInterface {

    private static GoodsOperationMethod instance  = new GoodsOperationMethod();

    public static GoodsOperationMethod getInstance(){
        return  instance;
    }

    private GoodsOperationMethod(){

    }


    /**
     *添加和删除商品数量,并得到商品数量
     */
    @Override
    public int saveGoodsNumber(Context context, int menupos, int goodsid, String goodsnum , String goodsprice) {
        return OperateGoodsDataBase.saveGoodsNumber(context , menupos , goodsid , goodsnum ,goodsprice);
    }
    /**
     *根据下标得到 对应购物的数量
     */
    @Override
    public int getGoodsNumber(Context context, int menupos, int goodsid) {
        return OperateGoodsDataBase.getGoodsNumber(context, menupos, goodsid);
    }
    /**
     * 根据下标 得到所有购物数量
     */
    @Override
    public int getGoodsNumberAll(Context context, int menupos) {
        return OperateGoodsDataBase.getGoodsNumberAll(context, menupos);
    }

    /**
     *据下标 得到所有购物的价格
     */
    @Override
    public int getGoodsPriceAll(Context context, int menupos) {
        return OperateGoodsDataBase.getGoodsPriceAll(context, menupos);
    }
    /**
     *删除所有的购物数据
     */
    @Override
    public void deleteAll(Context context) {
        OperateGoodsDataBase.deleteAll(context);
    }
}
