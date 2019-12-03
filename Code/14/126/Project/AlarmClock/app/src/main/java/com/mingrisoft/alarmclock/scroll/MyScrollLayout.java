package com.mingrisoft.alarmclock.scroll;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.mingrisoft.alarmclock.R;

/**
 * Created by Administrator on 2016/12/30.
 */

public class MyScrollLayout {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    //    private static final int ITEMS_IN_VISIBLE = 33;   //当前手机屏幕可容纳的item数量
//    private static final int ITEMS_IN_DAY = 1440 + ITEMS_IN_VISIBLE;
//    private static final int TOTAL_ITEMS_IN_DAY = ITEMS_IN_DAY + 1;
    private RelativeLayout scrollContainer;
    private RelativeLayout parentLayout;
    private LayoutInflater mInflater;
    private Context context;
    private ListView lv;

    private ScrollBaseAdapter scrollAdapter;
    /**
     * 生命自定义的三个接口
     */
    private OnSwipListener onSwipListener;
    private OnFanChange onFanChange;
    private OnTimeChange onTimeChange;

    private boolean isFirst = true;


    private int hourZone;

    private TimeInDay time;

    /**
     * 初始化
     */
    public MyScrollLayout(Context context, RelativeLayout parentLayout) {

        this.context = context;
        this.parentLayout = parentLayout;
        mInflater = LayoutInflater.from(context);
        init();
    }

    private void init() {
        sharedPreferences = context.getSharedPreferences("share", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        scrollContainer = (RelativeLayout) mInflater.inflate(R.layout.scroll_layout, null); //绑定布局
        lv = (ListView) scrollContainer.findViewById(R.id.scroll_list);  //绑定id
        lv.setDivider(null);
        scrollAdapter = new ScrollBaseAdapter(context); //初始化adapter
        scrollAdapter.addData(1441+33);
        RelativeLayout.LayoutParams scrollParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        parentLayout.addView(scrollContainer, scrollParams);
        lv.setAdapter(scrollAdapter);
        lv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                Log.e("-*-*-*-", "i:" + i);
                switch (i) {
                    case SCROLL_STATE_FLING:
                        if (onSwipListener != null) {
                            onSwipListener.setInvisible();
                        }
                        break;
                    case SCROLL_STATE_IDLE:
                        if (onSwipListener != null) {
                            onSwipListener.setVisible();
                        }
                        break;
                    case SCROLL_STATE_TOUCH_SCROLL:
                        if (onSwipListener != null) {
                            onSwipListener.setInvisible();
                        }
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                Log.e("-*-*-*-", "firstVisibleItem:" + firstVisibleItem);
                Log.e("-*-*-*-", "visibleItemCount:" + visibleItemCount);
                Log.e("-*-*-*-", "totalItemCount:" + totalItemCount);
                Log.e("-*-*-*-", "++" + sharedPreferences.getInt("vic", 100));
                if (sharedPreferences.getInt("vic", 100) > visibleItemCount) {
                    editor.putInt("vic", visibleItemCount);
                    editor.commit();
                }
                int vItem = 33;
                Log.e("-*-*-*-", "--" +vItem);
                int firstItem;
                int totalItems;
                if (firstVisibleItem == 0) {
                    if (isFirst) {
                        lv.setSelection(1);
                        isFirst = false;
                    } else {
                        lv.setSelection(1441);
                    }
                } else {
                    firstItem = firstVisibleItem - 1;
                    totalItems = totalItemCount - 1;
                    Log.e("-----", "firstItem:" + firstItem);
                    Log.e("-----", "totalItems:" + totalItems);
                    Log.e("-----", "totalItems - vItem:" + (totalItems - vItem));
                    if (firstItem == totalItems - vItem) {
                        lv.setSelection(1);
                        Log.e("--------", "---------------------------");
                    } else {
                        int curHourZone = firstItem / 60;
                        if ((curHourZone != hourZone || hourZone == 0) && curHourZone < 24) {
                            hourZone = curHourZone;
                        } else {
                        }

                        time = getCurTime(firstItem);

                        if (onTimeChange != null) {
                            onTimeChange.elapse(time);
                        }

                        updateFan(firstItem);


                    }
                }

            }
        });
    }

    private void updateFan(int item) {
        if (onFanChange != null) {
            onFanChange.change(item);
        }

    }

    private TimeInDay getCurTime(int firstItem) {
        int hour = firstItem / 60;
        int min = firstItem % 60;
        TimeInDay result = new TimeInDay(hour, min);
        return result;
    }

    public void setOnSwipListener(OnSwipListener listener) {
        onSwipListener = listener;
    }

    public void setOnFanChange(OnFanChange change) {
        onFanChange = change;
    }

    public void setOnTimeChange(OnTimeChange change) {
        onTimeChange = change;
    }


    public void dispose() {
        parentLayout.removeView(scrollContainer);
    }

    public void setItem(int itemNum) {
        lv.setSelection(itemNum);
    }

    /**
     * 设置其他控件显示与隐藏的接口
     */
    public interface OnSwipListener {
        void setVisible();
        void setInvisible();
    }

    /**
     *
     * */
    public interface OnFanChange {
        void change(int item);
    }

    /**
     * 时间改变的接口
     */
    public interface OnTimeChange {
        void elapse(TimeInDay time);
    }
}
