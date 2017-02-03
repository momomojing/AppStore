package com.lizhizhan.appstore.ui.holder;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.lidroid.xutils.BitmapUtils;
import com.lizhizhan.appstore.R;
import com.lizhizhan.appstore.http.HttpHelper;
import com.lizhizhan.appstore.utils.BitmapHelper;
import com.lizhizhan.appstore.utils.UIUtils;

import java.util.ArrayList;

/**
 * 首页轮播条
 * Created by lizhizhan on 2016/10/31.
 */

public class HomeHeadHolder extends BaseHolder<ArrayList<String>> {

    private ViewPager viewPager;
    private ArrayList<String> data;
    private LinearLayout linearLayout;
    private int mPreviousPos;

    @Override
    public View initView() {
        //创建根布局，相对布局。
        RelativeLayout relativeLayout = new RelativeLayout(UIUtils.getContext());
        //初始化布局参数
        AbsListView.LayoutParams params = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, UIUtils.dip2px(140));
        relativeLayout.setLayoutParams(params);
        //初始化viewPager
        viewPager = new ViewPager(UIUtils.getContext());
        RelativeLayout.LayoutParams viewPagerParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        relativeLayout.addView(viewPager, viewPagerParams);
        //初始化线性布局，存放小圆点
        linearLayout = new LinearLayout(UIUtils.getContext());
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);//水平方向
        RelativeLayout.LayoutParams llParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        llParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);//摆放规则，右对齐
        llParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);//摆放规则，底部对齐
        int padding = UIUtils.dip2px(10);
        linearLayout.setPadding(padding, padding, padding, padding);
        linearLayout.setLayoutParams(llParams);
        relativeLayout.addView(linearLayout);
        return relativeLayout;
    }

    @Override
    public void refreshView(final ArrayList<String> data) {
        this.data = data;
        //填充viewPager的数据
        viewPager.setAdapter(new HomeHeadAdapter());
        viewPager.setCurrentItem(data.size() * 10000);
        //给线性布局添加圆点指示器
        for (int i = 0; i < data.size(); i++) {
            ImageView point = new ImageView(UIUtils.getContext());
            LinearLayout.LayoutParams poiParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            //            point.setImageResource(R.drawable.indicator_normal);
            //设置圆点间距
            if (i == 0) {
                //默认第一页是选中的状态
                point.setImageResource(R.drawable.indicator_selected);
            } else {
                point.setImageResource(R.drawable.indicator_normal);
                poiParams.leftMargin = UIUtils.dip2px(4);
            }
            point.setLayoutParams(poiParams);

            linearLayout.addView(point);
        }

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                //选中的点设置为选择状态，小白点
                position = position % data.size();
                ImageView point = (ImageView) linearLayout.getChildAt(position);
                point.setImageResource(R.drawable.indicator_selected);


                //上个点设为不选择状态
                ImageView prePoint = (ImageView) linearLayout.getChildAt(mPreviousPos);
                prePoint.setImageResource(R.drawable.indicator_normal);
                mPreviousPos = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //        UIUtils.getHandler().postDelayed()
        HomeHeaderTask homeHeaderTask = new HomeHeaderTask();
        homeHeaderTask.start();

    }

    /**
     * 轮播条
     */
    private class HomeHeaderTask implements Runnable {

        public void start() {

            UIUtils.getHandler().removeCallbacksAndMessages(null);
            UIUtils.getHandler().postDelayed(this, 3000);

        }

        @Override
        public void run() {
            int currentItem = viewPager.getCurrentItem();
            currentItem++;
            viewPager.setCurrentItem(currentItem);
            //再发一条信息，开始循环
            UIUtils.getHandler().postDelayed(this, 3000);
        }
    }

    class HomeHeadAdapter extends PagerAdapter {

        private final BitmapUtils bitmapUtils;

        public HomeHeadAdapter() {
            bitmapUtils = BitmapHelper.getmBitmapUtils();
        }

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            position = position % data.size();
            String url = data.get(position);
            ImageView imageView = new ImageView(UIUtils.getContext());

            imageView.setScaleType(ImageView.ScaleType.FIT_XY);

            bitmapUtils.display(imageView, HttpHelper.URL + "image?name=" + url);
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
