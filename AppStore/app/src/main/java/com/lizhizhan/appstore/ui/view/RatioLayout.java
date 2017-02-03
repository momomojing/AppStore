package com.lizhizhan.appstore.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * 自定义控件，根据比例来觉得控件高度
 * Created by lizhizhan on 2016/10/28.
 */

public class RatioLayout extends FrameLayout {

    private float ratio;

    public RatioLayout(Context context) {
        super(context);
    }

    //有样式的调用这个
    public RatioLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    //属性的调用这个
    public RatioLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        //获取属性值
        ratio = attrs.getAttributeFloatValue("http://schemas.android.com/apk/com.lizhizhan.appstore", "ratio", -1);
        //        另一种方法
        //        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RatioLayout);
        //        float aFloat = typedArray.getFloat(R.styleable.RatioLaout_ratio, -1);
        //        typedArray.recycle();
        //        System.out.println("+" + aFloat);
        System.out.println(ratio + "+");
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //1获取到宽度，
        // 2根据宽度和比例来确定控件的高度
        // 3 重新测量控件

        //        MeasureSpec.AT_MOST;   至多
        //        MeasureSpec.EXACTLY;   确定
        //        MeasureSpec.UNSPECIFIED;  不确定
        int width = MeasureSpec.getSize(widthMeasureSpec);//宽度值
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);//宽度模式
        int height = MeasureSpec.getSize(heightMeasureSpec);//高度值
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);//高度模式

        //宽度确定，高度不确定,有比例值就是合法，可以根据比例确定高度了
        if (widthMode == MeasureSpec.EXACTLY && heightMode != MeasureSpec.EXACTLY && ratio > 0) {
            //图片宽度=控件宽度-左右padding
            int imageWidth = width - getPaddingLeft() - getPaddingRight();
            //图片高度=图片宽度、宽高比例
            int imageHeight = (int) (imageWidth / ratio + 0.5f);
            //控件高度=图片高度+上下padding值
            height = imageHeight + getPaddingTop() + getPaddingBottom();
            //根据最新的高度来确定heightMeasureSpec 。高度模式已经确定
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        }


        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }
}
