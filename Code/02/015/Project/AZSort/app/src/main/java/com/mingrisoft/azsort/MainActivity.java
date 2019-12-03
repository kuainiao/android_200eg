package com.mingrisoft.azsort;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements MySideBar.OnTouchingLetterChangedListener{
	//显示城市列表
	private ListView mainList;
	//城市数据源
	private List<String> data;
	//字母位置
	private List<Integer> letterPositionList;
	//字母Char
	private List<Integer> letterCharList;
	//自定义View显示右侧字母
	private MySideBar myView;
	//列表上方固定文字
	private TextView tv01;
	//右侧显示内容
	private String[] title = {"热门城市", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
	//城市数据源二维数组
	private String[][] ary = {
//			热门城市
			{"北京", "上海", "广州","深圳", "重庆", "三亚","天津"},
//			A
			{"阿坝","阿拉善","阿里","安康","安庆","鞍山","安顺","安阳","澳门"},
//			B
			{"北京","白银","保定","宝鸡","保山","包头","巴中","北海","蚌埠","本溪","毕节","滨州","百色","亳州"},
//			C
			{"重庆","成都","长沙","长春","沧州","常德","昌都","长治","常州","巢湖","潮州","承德","郴州","赤峰","池州","崇左","楚雄","滁州","朝阳"},
//			D
			{"大连","东莞","大理","丹东","大庆","大同","大兴安岭","德宏","德阳","德州","定西","迪庆","东营"},
//			E
			{"鄂尔多斯","恩施","鄂州"},
//			F
			{"福州","防城港","佛山","抚顺","抚州","阜新","阜阳"},
//			G
			{"广州","桂林","贵阳","甘南","赣州","甘孜","广安","广元","贵港","果洛"},
//			H
			{"杭州","哈尔滨","合肥","海口","呼和浩特","海北","海东","海南","海西","邯郸","汉中","鹤壁","河池","鹤岗","黑河","衡水","衡阳","河源","贺州","红河","淮安","淮北","怀化","淮南","黄冈","黄南","黄山","黄石","惠州","葫芦岛","呼伦贝尔","湖州","菏泽"},
//			I
			{"暂无"},
//			J
			{"济南","佳木斯","吉安","江门","焦作","嘉兴","嘉峪关","揭阳","吉林","金昌","晋城","景德镇","荆门","荆州","金华","济宁","晋中","锦州","九江","酒泉"},
//			K
			{"昆明","开封"},
//			L
			{"兰州","拉萨","来宾","莱芜","廊坊","乐山","凉山","连云港","聊城","辽阳","辽源","丽江","临沧","临汾","临夏","临沂","林芝","丽水","六安","六盘水","柳州","陇南","龙岩","娄底","漯河","洛阳","泸州","吕梁"},
//			M
			{"马鞍山","茂名","眉山","梅州","绵阳","牡丹江"},
//			N
			{"南京","南昌","南宁","宁波","南充","南平","南通","南阳","那曲","内江","宁德","怒江"},
//			O
			{"暂无"},
//			P
			{"盘锦","攀枝花","平顶山","平凉","萍乡","莆田","濮阳"},
//			Q
			{"青岛","黔东南","黔南","黔西南","庆阳","清远","秦皇岛","钦州","齐齐哈尔","泉州","曲靖","衢州"},
//			R
			{"日喀则","日照"},
//			S
			{"上海","深圳","苏州","沈阳","石家庄","三门峡","三明","三亚","商洛","商丘","上饶","山南","汕头","汕尾","韶关","绍兴","邵阳","十堰","朔州","四平","绥化","遂宁","随州","宿迁","宿州"},
//			T
			{"天津","太原","泰安","泰州","台州","唐山","天水","铁岭","铜川","通化","通辽","铜陵","铜仁","台湾"},
//			U
			{"暂无"},
//			V
			{"暂无"},
//			W
			{"武汉","乌鲁木齐","无锡","威海","潍坊","文山","温州","乌海","芜湖","乌兰察布","武威","梧州"},
//			X
			{"厦门","西安","西宁","襄樊","湘潭","湘西","咸宁","咸阳","孝感","邢台","新乡","信阳","新余","忻州","西双版纳","宣城","许昌","徐州","香港","锡林郭勒","兴安"},
//			Y
			{"银川","雅安","延安","延边","盐城","阳江","阳泉","扬州","烟台","宜宾","宜昌","宜春","营口","益阳","永州","岳阳","榆林","运城","云浮","玉树","玉溪","玉林"},
//			Z
			{"杂多县","赞皇县","枣强县","枣阳市","枣庄","泽库县","增城市","曾都区","泽普县","泽州县","札达县","扎赉特旗","扎兰屯市","扎鲁特旗","扎囊县","张北县","张店区","章贡区","张家港","张家界","张家口","漳平市","漳浦县","章丘市","樟树市","张湾区","彰武县","漳县","张掖","漳州","长子县","湛河区","湛江","站前区","沾益县","诏安县","召陵区","昭平县","肇庆","昭通","赵县","昭阳区","招远市","肇源县","肇州县","柞水县","柘城县","浙江","镇安县","振安区","镇巴县","正安县","正定县","正定新区","正蓝旗","正宁县","蒸湘区","正镶白旗","正阳县","郑州","镇海区","镇江","浈江区","镇康县","镇赉县","镇平县","振兴区","镇雄县","镇原县","志丹县","治多县","芝罘区","枝江市","芷江侗族自治县","织金县","中方县","中江县","钟楼区","中牟县","中宁县","中山","中山区","钟山区","钟山县","中卫","钟祥市","中阳县","中原区","周村区","周口","周宁县","舟曲县","舟山","周至县","庄河市","诸城市","珠海","珠晖区","诸暨市","驻马店","准格尔旗","涿鹿县","卓尼","涿州市","卓资县","珠山区","竹山县","竹溪县","株洲","株洲县","淄博","子长县","淄川区","自贡","秭归县","紫金县","自流井区","资溪县","资兴市","资阳"}
	};
	private int lastFirstVisibleItem;
	private TextView overlay;
	public static TextView cityTV;
	private Handler handler;
	private OverlayThread overlayThread;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //用于显示城市列表
        mainList = (ListView) findViewById(R.id.mainlist);
		//显示右侧字母列表
        myView = (MySideBar) findViewById(R.id.myview);
		//城市列表上方固定文字
        tv01 = (TextView) findViewById(R.id.main_tv01);
		//选择的城市
        cityTV = (TextView) findViewById(R.id.cityTV);
        //绑定滑动监听
        myView.setOnTouchingLetterChangedListener(this);
        data = new ArrayList<String>();
        letterCharList = new ArrayList<Integer>();
        letterPositionList = new ArrayList<Integer>();
		//消息通知
        handler = new Handler();
		//隐藏提示信息线程
		overlayThread = new OverlayThread();
        initOverlay();
        int index = 0, position = 0;
        letterCharList.add(index);
		//循环城市数据源
        for(int i = 0; i < ary.length; i++){
        	for(int j = 0; j < ary[i].length; j++){
        		if(i == 0 && j == 0){
        			index++;
        			letterPositionList.add(position);
        		}else if(j == 0){
        			letterCharList.add(index);
        			letterPositionList.add(position);
        			index++;
        		}else{
        			letterCharList.add(-1);
        		}
        		position++;
				//添加数据到集合
        		data.add(ary[i][j]);
        	}
        }
		//声明适配器
        MyAdapter adapter = new MyAdapter(this, data, letterCharList, title);
		//设置适配器
        mainList.setAdapter(adapter);
        //判断listview滚动设置城市列表上方固定文字
        mainList.setOnScrollListener(new OnScrollListener() {
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}
			//滑动的时候触发
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				if(letterCharList.get(firstVisibleItem) >= 0){
					//设置显示文字
					tv01.setText(title[letterCharList.get(firstVisibleItem)]);
					lastFirstVisibleItem = firstVisibleItem;
				}else{
					if(lastFirstVisibleItem > firstVisibleItem){
						//设置显示文字
						tv01.setText(title[letterCharList.get(lastFirstVisibleItem) - 1]);
					}
				}
			}
		});
       
    }
	//加载提示选择的字母
	private void initOverlay() {
		//设置选择字母后提示信息显示位置
		LayoutInflater inflater = LayoutInflater.from(this);
		overlay = (TextView) inflater.inflate(R.layout.overlay, null);
		overlay.setVisibility(View.INVISIBLE);
		//设置提示信息显示的位置
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.TYPE_APPLICATION,
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
						| WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
				PixelFormat.TRANSLUCENT);
		WindowManager windowManager = (WindowManager) this
				.getSystemService(Context.WINDOW_SERVICE);
		//添加提示信息到布局 lp的位置
		windowManager.addView(overlay, lp);
	}

	//触摸监听
	public void onTouchingLetterChanged(int s) {
		//切换到list指定的item位置
		mainList.setSelection(letterPositionList.get(s));
		//设置提示的字母
		overlay.setText(title[s]);
		//显示提示窗
		overlay.setVisibility(View.VISIBLE);
        //移除以前开启的线程
		handler.removeCallbacks(overlayThread);
		//1.5秒后执行overlayThread线程 隐藏选择字母提示
		handler.postDelayed(overlayThread, 1500);
	}
	//关闭提示信息线程
	private class OverlayThread implements Runnable {
		@Override
		public void run() {
			//隐藏提示信息
			overlay.setVisibility(View.GONE);
		}
	}

}