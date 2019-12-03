package com.mingrisoft.friendcircle.adapter;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Administrator on 2017/1/24.
 */

public class MyPagerAdapter extends PagerAdapter {
    private List<View> list;

    public MyPagerAdapter(List<View> list) {
        this.list = list;
    }



    @Override
        public int getCount() {

            if (list != null && list.size() > 0) {
                return list.size();
            } else {
                return 0;
            }
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }


    @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            container.addView(list.get(position));

            return list.get(position);
        }
//
//        @Override
//        public int getItemPosition(Object object) {
//            return POSITION_NONE;
//    }
}
