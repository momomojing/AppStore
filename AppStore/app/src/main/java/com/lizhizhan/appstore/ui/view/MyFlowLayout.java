package com.lizhizhan.appstore.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.lizhizhan.appstore.utils.UIUtils;

import java.util.ArrayList;

/**
 * 排行页面的自定义View
 * Created by lizhizhan on 2016/11/3.
 */

public class MyFlowLayout extends ViewGroup {
    //当前行已使用的宽度
    private int mUsedWidth;
    private int mHorizontalSpacing = UIUtils.dip2px(6);
    private int mVerticalSpacing = UIUtils.dip2px(8);
    private Line mLine;//当前行使用对象
    private ArrayList<Line> mLineList = new ArrayList<Line>();
    private static final int MAX_LINE = 100;

    public MyFlowLayout(Context context) {
        super(context);
    }

    public MyFlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyFlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int left = l + getPaddingLeft();
        int top = t + getPaddingTop();
        for (int i = 0; i < mLineList.size(); i++) {
            Line line = mLineList.get(i);
            line.layout(left, top);
            top += line.mMaxHeight + mVerticalSpacing;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //获取到有效宽度
        int width = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight();
        //获取有效高度
        int heigh = MeasureSpec.getSize(heightMeasureSpec) - getPaddingTop() - getPaddingBottom();
        //得到模式
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heighMode = MeasureSpec.getMode(heightMeasureSpec);

        //得到所有的孩子
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            //如果父控件是确定模式，子控件包裹内容，否则子控件模式和父控件一样
            int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(width, (widthMode == MeasureSpec.EXACTLY) ? MeasureSpec.AT_MOST : widthMode);
            int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(heigh, (heighMode == MeasureSpec.EXACTLY) ? MeasureSpec.AT_MOST : heighMode);
            //开始测量
            childView.measure(childWidthMeasureSpec, childHeightMeasureSpec);

            if (mLine == null) {
                mLine = new Line();
            }
            int childWidth = childView.getMeasuredWidth();
            mUsedWidth += childWidth;
            if (mUsedWidth < width) {//判断是否超出了边界
                mLine.addView(childView);
                mUsedWidth += mHorizontalSpacing;//加一个水平间距
                if (mUsedWidth > width) {
                    //增加水平间距后超出边距，换行
                    if (!newLine()) {
                        //到最大了。添加失败，结束循环
                        break;
                    }
                }
            } else {
                //已经超出边界
                //1，没有任何元素
                if (mLine.getChildCount() == 0) {
                    mLine.addView(childView);
                    if (!newLine()) {//换行
                        break;
                    }
                } else {
                    //已经有了，添加后超出，换行
                    if (!newLine()) {
                        break;
                    }
                    mLine.addView(childView);
                    mUsedWidth += childWidth + mHorizontalSpacing;//更新已经使用的宽度
                }
            }
        }
        //添加最后一行对象
        if (mLine != null && mLine.getChildCount() != 0 && !mLineList.contains(mLine)) {
            mLineList.add(mLine);
        }
        int totalWidth = MeasureSpec.getSize(widthMeasureSpec);//控件的整体宽度

        int totalHeight = 0;
        for (int j = 0; j < mLineList.size(); j++) {
            Line line = mLineList.get(j);
            totalHeight += line.mMaxHeight;
        }
        //整个自定义View的宽高
        totalHeight += (mLineList.size() - 1) * mVerticalSpacing;
        totalHeight += getPaddingTop() + getPaddingBottom();
        setMeasuredDimension(totalWidth, totalHeight);
        //        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    //换行
    public boolean newLine() {
        mLineList.add(mLine);//保存上一行数据
        if (mLineList.size() < MAX_LINE) {
            //可以继续添加
            mLine = new Line();
            mUsedWidth = 0;//已经使用宽度清零
            return true;//创建成功
        }
        return false;
    }

    //每一行的对象封装
    class Line {
        private int mTotalWidth;//当前所有控件总宽度
        public int mMaxHeight;
        private ArrayList<View> mChildViewList = new ArrayList<View>();

        //添加一个子控件
        public void addView(View view) {
            mChildViewList.add(view);
            //            总宽度增加
            mTotalWidth += view.getMeasuredWidth();

            int height = view.getMeasuredHeight();
            mMaxHeight = mMaxHeight < height ? height : mMaxHeight;
        }

        public int getChildCount() {
            return mChildViewList.size();
        }

        /**
         * 子控件摆放位置
         */
        public void layout(int left, int top) {
            int childCount = getChildCount();
            //将剩余空间分配给每个子控件
            int validWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();//屏幕有效宽度

            int surplusWidth = validWidth - mTotalWidth - (childCount - 1) * mHorizontalSpacing;
            if (surplusWidth >= 0) {

                int space = (int) ((float) surplusWidth / childCount + 0.5f);
                //重新测量子控件
                for (int i = 0; i < childCount; i++) {
                    View mChildView = mChildViewList.get(i);
                    int measuredWidth = mChildView.getMeasuredWidth();
                    int measuredHeight = mChildView.getMeasuredHeight();
                    measuredWidth += space;
                    int widthMeasureSpec = MeasureSpec.makeMeasureSpec(measuredWidth, MeasureSpec.EXACTLY);
                    int heightMeasureSpec = MeasureSpec.makeMeasureSpec(measuredHeight, MeasureSpec.EXACTLY);
                    //重修测量控件
                    mChildView.measure(widthMeasureSpec, heightMeasureSpec);
                    int topOffset = (mMaxHeight - measuredHeight) / 2;
                    if (topOffset < 0) {
                        topOffset = 0;
                    }
                    //设置每一行里面的子控件位置
                    mChildView.layout(left, top + topOffset, left + measuredWidth, top + topOffset + measuredHeight);

                    left += measuredWidth + mHorizontalSpacing;//更新Left的值，下一个View
                }
            } else {
                View view = mChildViewList.get(0);
                view.layout(left, top, left + getMeasuredWidth(), top + getMeasuredHeight());
            }
        }
    }
}
