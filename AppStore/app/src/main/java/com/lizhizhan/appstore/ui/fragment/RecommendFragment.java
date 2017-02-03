package com.lizhizhan.appstore.ui.fragment;

import android.graphics.Color;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.lizhizhan.appstore.protocol.RecommendProtocol;
import com.lizhizhan.appstore.ui.view.LoadingPage;
import com.lizhizhan.appstore.ui.view.fly.ShakeListener;
import com.lizhizhan.appstore.ui.view.fly.StellarMap;
import com.lizhizhan.appstore.utils.UIUtils;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by lizhizhan on 2016/10/21.
 */

public class RecommendFragment extends BaseFragment {

    private ArrayList<String> data;

    @Override
    public View OnCreatSuccessView() {
        final StellarMap stellarMap = new StellarMap(UIUtils.getContext());
        stellarMap.setAdapter(new RecommendAdapter());
        //随机方式.9行6列
        stellarMap.setRegularity(6, 9);
        //默认的初始页面
        stellarMap.setGroup(0, true);
        int padding = UIUtils.dip2px(10);
        //边距
        stellarMap.setInnerPadding(padding, padding, padding, padding);
        ShakeListener listener = new ShakeListener(UIUtils.getContext());
        listener.setOnShakeListener(new ShakeListener.OnShakeListener() {
            @Override
            public void onShake() {
                stellarMap.zoomIn();
            }
        });


        return stellarMap;
    }

    @Override
    public LoadingPage.ResultState onLoad() {
        RecommendProtocol recommendProtocol = new RecommendProtocol();
        data = recommendProtocol.getData(0);
        return check(data);
    }

    class RecommendAdapter implements StellarMap.Adapter {

        @Override
        public int getGroupCount() {
            return 2;
        }

        @Override
        public int getCount(int group) {
            int count = data.size() / getGroupCount();
            if (group == getGroupCount() - 1) {
                count += data.size() % getGroupCount();
            }
            return count;
        }

        @Override
        public View getView(int group, int position, View convertView) {
            position += (group) * getCount(group - 1);
            final String value = data.get(position);
            TextView view = new TextView(UIUtils.getContext());
            view.setText(value);
            //随机大小
            Random random = new Random();
            int size = 16 + random.nextInt(10);
            view.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);

            //随机颜色
            //r g b .0 -255
            int r = 30 + random.nextInt(200);
            int g = 30 + random.nextInt(200);
            int b = 30 + random.nextInt(200);
            view.setTextColor(Color.rgb(r, g, b));

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(UIUtils.getContext(), value, Toast.LENGTH_SHORT).show();
                }
            });

            return view;
        }

        @Override
        public int getNextGroupOnZoom(int group, boolean isZoomIn) {
            System.out.println("iszoomin" + isZoomIn);
            if (isZoomIn) {
                //ture,往下滑。上一页
                if (group > 0) {
                    group--;
                } else {
                    //跳到最后一页
                    group = getGroupCount() - 1;
                }
            } else {
                if (group < getGroupCount()) {
                    group++;
                } else {
                    group = 0;
                }
            }
            return group;
        }
    }

}
