package data;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Table;

/**
 * 商品信息
 */
@Table(name = "goods")
public class GoodsInfo extends ID {
    @Column(column = "menupos")
    //菜单位置
    private int menupos;
    @Column(column = "goodsid")
    //货物的id
    private int goodsid;
    @Column(column = "goodsnum")
    //货物的数量
    private String goodsnum;
    @Column(column = "goodsprice")
    //商品的价格
    private String goodsprice;

    public int getMenupos() {
        return menupos;
    }

    public void setMenupos(int menupos) {
        this.menupos = menupos;
    }

    public int getGoodsid() {
        return goodsid;
    }

    public void setGoodsid(int goodsid) {
        this.goodsid = goodsid;
    }

    public String getGoodsnum() {
        return goodsnum;
    }

    public void setGoodsnum(String goodsnum) {
        this.goodsnum = goodsnum;
    }

    public String getGoodsprice() {
        return goodsprice;
    }

    public void setGoodsprice(String goodsprice) {
        this.goodsprice = goodsprice;
    }

    @Override
    public String toString() {
        return "GoodsInfo{" +
                "menupos='" + menupos + '\'' +
                ", goodsid='" + goodsid + '\'' +
                ", goodsnum='" + goodsnum + '\'' +
                ", goodsprice='" + goodsprice + '\'' +
                '}';
    }
}
