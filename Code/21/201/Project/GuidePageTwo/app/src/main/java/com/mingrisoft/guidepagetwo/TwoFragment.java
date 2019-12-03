package com.mingrisoft.guidepagetwo;

import android.support.v4.app.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Administrator on 2017/1/21.
 */

public class TwoFragment extends Fragment {
    private TextView titleTV,contentTV;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_guide, container, false);
        titleTV = (TextView) view.findViewById(R.id.tv_title);
        contentTV = (TextView) view.findViewById(R.id.tv_content);
        titleTV.setText("--TWO--");
        contentTV.setText("----疑似地上霜----");
        return view;
    }
}

