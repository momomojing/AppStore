package com.lizhizhan.appstore.ui.fragment;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.lizhizhan.appstore.ui.view.LoadingPage;
import com.lizhizhan.appstore.utils.UIUtils;

/**
 * Created by lizhizhan on 2016/10/21.
 */

public class GameFragment extends BaseFragment {

    private TextView textView;

    @Override
    public View OnCreatSuccessView() {
        textView = new TextView(UIUtils.getContext());
        textView.setTextColor(Color.BLACK);
        textView.setText("游戏");
        return textView;
    }

    @Override
    public LoadingPage.ResultState onLoad() {


        return LoadingPage.ResultState.STATE_SUCCESS;
    }
}
