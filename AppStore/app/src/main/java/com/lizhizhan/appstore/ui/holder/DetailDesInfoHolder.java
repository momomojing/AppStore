package com.lizhizhan.appstore.ui.holder;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.lizhizhan.appstore.R;
import com.lizhizhan.appstore.domain.AppInfo;
import com.lizhizhan.appstore.utils.UIUtils;

/**
 * 运用介绍模块
 * Created by lizhizhan on 2016/11/2.
 */

public class DetailDesInfoHolder extends BaseHolder<AppInfo> {

    private TextView tvDetailDes;
    private RelativeLayout rlDetailToggle;
    private TextView tvDetailAuthor;
    private ImageView ivArrow;
    private LinearLayout.LayoutParams tvDetailDesParams;

    @Override
    public View initView() {
        View view = UIUtils.inflate(R.layout.layout_detail_desinfo);
        tvDetailDes = (TextView) view.findViewById(R.id.tv_detail_des);
        rlDetailToggle = (RelativeLayout) view.findViewById(R.id.rl_detail_toggle);
        tvDetailAuthor = (TextView) view.findViewById(R.id.tv_detail_author);
        ivArrow = (ImageView) view.findViewById(R.id.iv_arrow);
        rlDetailToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toogle();
            }


        });
        return view;
    }

    private boolean isOpen = false;

    private void toogle() {
        int shortHeight = getShortHeight();
        int longHeight = getLongHeight();
        ValueAnimator valueAnimator = null;
        if (isOpen) {
            isOpen = false;
            if (longHeight > shortHeight) {
                valueAnimator = ValueAnimator.ofInt(longHeight, shortHeight);
                ivArrow.setImageResource(R.drawable.arrow_down);
            }
        } else {
            isOpen = true;
            if (longHeight > shortHeight) {
                valueAnimator = ValueAnimator.ofInt(shortHeight, longHeight);
                ivArrow.setImageResource(R.drawable.arrow_up);
            }
        }
        if (valueAnimator != null) {
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int height = (int) animation.getAnimatedValue();
                    tvDetailDesParams.height = height;
                    tvDetailDes.setLayoutParams(tvDetailDesParams);
                }
            });
            valueAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    final ScrollView scrollView = getScrollView();
                    //滑动到底部，为了更安全，放到消息队列中
                    scrollView.post(new Runnable() {
                        @Override
                        public void run() {
                            scrollView.fullScroll(ScrollView.FOCUS_DOWN);//滑动到底部
                        }
                    });
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            valueAnimator.setDuration(200);
            valueAnimator.start();
        }
    }

    @Override
    public void refreshView(AppInfo data) {
        tvDetailDes.setText(data.des);
        tvDetailAuthor.setText(data.author);
        //放在消息队列中运行，可以解决当值未满7行时的显示
        tvDetailDes.post(new Runnable() {
            @Override
            public void run() {
                int shortHeight = getShortHeight();
                tvDetailDesParams = (LinearLayout.LayoutParams) tvDetailDes.getLayoutParams();
                tvDetailDesParams.height = shortHeight;
                tvDetailDes.setLayoutParams(tvDetailDesParams);
            }
        });

    }

    /**
     * 获取7行TextView的高度数据
     */
    private int getShortHeight() {
        //模拟一个TextView。设置最多行数7.计算该高度，从而知道我们需要展示的高度是多少
        int width = tvDetailDes.getMeasuredWidth();
        TextView textView = new TextView(UIUtils.getContext());
        textView.setText(getData().des);//文字一致
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);//字体大小一致
        textView.setMaxLines(7);//最大行数7
        int withMeasureSpec = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY);//确切的宽度
        //包裹内容时，第一个参数表示尺寸最大值，
        // 暂时写2000.也可以写屏幕高度
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(2000, View.MeasureSpec.AT_MOST);
        //开始测量
        textView.measure(withMeasureSpec, heightMeasureSpec);
        return textView.getMeasuredHeight();
    }

    /**
     * 获取完整TextView的高度数据
     */
    private int getLongHeight() {
        //模拟一个TextView。设置最多行数7.计算该高度，从而知道我们需要展示的高度是多少
        int width = tvDetailDes.getMeasuredWidth();
        TextView textView = new TextView(UIUtils.getContext());
        textView.setText(getData().des);//文字一致
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);//字体大小一致
        int withMeasureSpec = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY);//确切的宽度
        //包裹内容时，第一个参数表示尺寸最大值，
        // 暂时写2000.也可以写屏幕高度
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(2000, View.MeasureSpec.AT_MOST);
        //开始测量
        textView.measure(withMeasureSpec, heightMeasureSpec);
        return textView.getMeasuredHeight();
    }

    /**
     * 获取控件中的ScrollView
     *
     * @return
     */
    private ScrollView getScrollView() {
        ViewParent parent = tvDetailDes.getParent();
        while (!(parent instanceof ScrollView)) {
            parent = parent.getParent();
        }
        return (ScrollView) parent;
    }
}
