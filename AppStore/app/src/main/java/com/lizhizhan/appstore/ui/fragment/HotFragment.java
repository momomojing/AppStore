package com.lizhizhan.appstore.ui.fragment;

import android.graphics.Color;
import android.graphics.drawable.StateListDrawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.lizhizhan.appstore.protocol.HotProtocol;
import com.lizhizhan.appstore.ui.view.LoadingPage;
import com.lizhizhan.appstore.ui.view.MyFlowLayout;
import com.lizhizhan.appstore.utils.DrawableUtils;
import com.lizhizhan.appstore.utils.UIUtils;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by lizhizhan on 2016/10/21.
 */

public class HotFragment extends BaseFragment {

    private ArrayList<String> data;

    @Override
    public View OnCreatSuccessView() {
        //        可以上下滑动的
        ScrollView scrollView = new ScrollView(UIUtils.getContext());
        MyFlowLayout flow = new MyFlowLayout(UIUtils.getContext());
        int padding = UIUtils.dip2px(10);
        flow.setPadding(padding, padding, padding, padding);
        //        flow.setHorizontalSpacing(UIUtils.dip2px(6));//水平布局
        //        flow.setVerticalSpacing(UIUtils.dip2px(8));//竖直布局
        for (int i = 0; i < 45; i++) {
            String value = data.get(i);
            TextView textView = new TextView(UIUtils.getContext());
            textView.setText(value);
            //大小
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            textView.setPadding(padding, padding, padding, padding);
            textView.setGravity(Gravity.CENTER);

            Random random = new Random();

            //随机颜色
            //r g b .0 -255
            int r = 30 + random.nextInt(200);
            int g = 30 + random.nextInt(200);
            int b = 30 + random.nextInt(200);
            int color = 0xffcecece;//摁下的颜色
            //            GradientDrawable bgNormal = DrawableUtils.getGradientDrawable(Color.rgb(r, g, b), UIUtils.dip2px(6));
            //            GradientDrawable bgPress = DrawableUtils.getGradientDrawable(color, UIUtils.dip2px(6));
            //            DrawableUtils.getSelector(bgNormal,bgPress);

            StateListDrawable selector = DrawableUtils.getSelector(Color.rgb(r, g, b), color, UIUtils.dip2px(6));

            textView.setBackground(selector);
            final int finalI = i;
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(UIUtils.getContext(), data.get(finalI), Toast.LENGTH_SHORT).show();
                }
            });
            flow.addView(textView);
        }
        scrollView.addView(flow);
        return scrollView;
    }

    @Override
    public LoadingPage.ResultState onLoad() {
        HotProtocol protocol = new HotProtocol();
        data = protocol.getData(0);
        return check(data);
    }
}
