package data;

import android.content.Context;
import android.util.Log;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;

import java.util.ArrayList;
import java.util.List;

/**
 * 商品数据库
 */
public class OperateGoodsDataBase {
    /**
     *添加和删除商品数量
     */
    public static int saveGoodsNumber(Context context, int menupos, int goodsid, String goodsnum , String goodsprice) {
        DbUtils utils = DbUtils.create(context);
        GoodsInfo goodsInfo = null;
        goodsInfo =new GoodsInfo();
        goodsInfo.setMenupos(menupos);
        goodsInfo.setGoodsid(goodsid);
        goodsInfo.setGoodsnum(goodsnum);
        goodsInfo.setGoodsprice(goodsprice);
        try {
            GoodsInfo bean = utils.findFirst(Selector.from(GoodsInfo.class).where("menupos" , "=" , menupos).and("goodsid", "=", goodsid));
            //如果有这条数据，数量直接加1；否则就插入表里面
            if(bean == null){
                Log.e("TAG", "还没有该商品");
                utils.save(goodsInfo);
                Log.e("TAG" , "该商品已经存储");
                return getGoodsNumber(context , menupos , goodsid);
            }else{
                Log.e("TAG" , "已经有该商品");
                //返回添加商品之后的商品总数
                return updateNum(context, menupos, goodsid, goodsnum);
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        Log.e("TAG" , "添加商品失败");
        utils.close();
        return 0;
    }
    /**修改数量，直接传入数量**/
    public static int updateNum(Context context , int menupos , int goodsid , String goodsnum){
        DbUtils utils = DbUtils.create(context);
        try {
            GoodsInfo bean = utils.findFirst(Selector.from(GoodsInfo.class).where("menupos", "=", menupos).and("goodsid", "=", goodsid));
            bean.setGoodsnum(goodsnum);
            utils.update(bean);
            Log.e("TAG", "该商品数量改变为：" + getGoodsNumber(context, menupos, goodsid));
            return getGoodsNumber(context , menupos , goodsid);
        } catch (DbException e) {
            e.printStackTrace();
        }
        utils.close();
        return 0;
    }
    /**
     *根据下标得到对应购物的数量
     */
    public static int getGoodsNumber(Context context , int menupos , int goodsid) {
        DbUtils utils = DbUtils.create(context);
        if(utils == null){
            Log.e("TAG" , "还没有该数据库");
            return 0;
        }
        try {
            GoodsInfo bean = utils.findFirst(Selector.from(GoodsInfo.class).where("menupos", "=", menupos).and("goodsid", "=", goodsid));
            if(bean == null){
                Log.e("TAG" , "还没有该存储商品");
                return 0;
            }else{
                Log.e("TAG" , "获取商品数量成功:" + Integer.parseInt(bean.getGoodsnum()));
                return Integer.parseInt(bean.getGoodsnum());
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        utils.close();
        Log.e("TAG", "获取商品数量失败");
        return 0;
    }
    /**
     * 根据下标 得到所有购物数量
     */
    public static int getGoodsNumberAll(Context context, int menupos) {
        //创建数据库连接
        DbUtils utils = DbUtils.create(context);
        //所有购物数量
        int mGoodsNum = 0;
        //商品类型总数
        ArrayList<GoodsInfo> mGoodsInfoList = null;
        //获取所有商品类型
        mGoodsInfoList = getGoodsTypeList(context);
        if(mGoodsInfoList == null){
            Log.e("TAG" , "获取商品类型总数失败");
            return 0;
        }
        //根据商品类型的总数计算购物车中所有商品的总数
        for(int i = 0; i < mGoodsInfoList.size() ; i++){
             //计算总数
            if(mGoodsInfoList.get(i).getMenupos() == menupos){
                mGoodsNum += Integer.parseInt(mGoodsInfoList.get(i).getGoodsnum());
            }
        }
        Log.e("TAG", " 得到所有购物数量成功：" + mGoodsNum);
        //关闭数据库
        utils.close();
        return mGoodsNum;
    }
    /**
     *根据下标 得到所有商品类型集合
     */
    public static ArrayList<GoodsInfo> getGoodsTypeList(Context context){
        //商品信息
        ArrayList<GoodsInfo> list = null;
        try {
            //获取所有商品信息
            list = (ArrayList<GoodsInfo>) DbUtils.create(context).findAll(GoodsInfo.class);
            //没有数据进行返回空
            if(list == null){
                Log.e("TAG" , "商品还没有存储数据");
                return null;
            }else{          //否则返回商品信息
                Log.e("TAG" , " 得到商品类型总数成功：" + list.size());
                return list;
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        Log.e("TAG" , " 得到商品类型总数失败");
        return null;
    }

    /**
     *根据下标 得到所有购物的价格
     */
    public static int getGoodsPriceAll(Context context, int menupos) {
        //创建数据库连接
        DbUtils utils = DbUtils.create(context);
        int mGoodsPrice = 0;
        //商品类型总数
        ArrayList<GoodsInfo> mGoodsInfoList = null;
        //获取所有商品类型
        mGoodsInfoList = getGoodsTypeList(context);
        if(mGoodsInfoList == null){
            Log.e("TAG" , "获取商品类型总数失败");
            return 0;
        }
        //根据商品类型的总数计算购物车中所有商品的总价
        for(int i = 0; i < mGoodsInfoList.size(); i++){
            if(mGoodsInfoList.get(i).getMenupos() == menupos){
                //计算总价
                mGoodsPrice += Integer.parseInt(mGoodsInfoList.get(i).getGoodsnum())
                        * Integer.parseInt(mGoodsInfoList.get(i).getGoodsprice());
            }
        }
        Log.e("TAG" , " 得到所有购物的价格成功：" + mGoodsPrice);
        utils.close();
        Log.e("TAG" , " 得到所有购物的价格失败");
        return mGoodsPrice;
    }
    /**
     *删除所有的购物数据
     */
    public static void deleteAll(Context context) {
        //创建数据库工具类
        DbUtils utils = DbUtils.create(context);
        try {
            //找到所有的商品信息
            List<GoodsInfo> records = utils.findAll(GoodsInfo.class);
            //删除所有的商品信息
            utils.deleteAll(records);
        } catch (DbException e) {
            e.printStackTrace();
        }
        //关闭数据库
        utils.close();
    }
}
