package com.lizhizhan.appstore.utils;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;

/**
 * 生成图像的工具类
 *
 * @author Kevin
 */
public class DrawableUtils {

    /**
     * 创建圆角矩形
     *
     * @param rgb    颜色值
     * @param radius 圆角半径
     * @return
     */
    public static GradientDrawable getGradientDrawable(int rgb, int radius) {
        // 初始化对象
        GradientDrawable shap = new GradientDrawable();
        // 矩形类型
        shap.setShape(GradientDrawable.RECTANGLE);
        // 设置圆角半径
        shap.setCornerRadius(radius);
        // 设置颜色
        shap.setColor(rgb);
        return shap;
    }

    /**
     * 返回状态选择器对象(selector)
     *
     * @param normal  normal  默认图像
     * @param pressed 按下图像
     * @return
     */
    public static StateListDrawable getSelector(Drawable normal,
                                                Drawable pressed) {
        StateListDrawable selector = new StateListDrawable();
        selector.addState(new int[]{android.R.attr.state_pressed}, pressed);
        selector.addState(new int[]{}, normal);
        return selector;
    }

    /**
     * 返回状态选择器对象(selector)
     *
     * @param normalColor  默认颜色
     * @param pressedColor 按下颜色
     * @param radius       圆角半径
     * @return
     */
    public static StateListDrawable getSelector(int normalColor,
                                                int pressedColor, int radius) {
        GradientDrawable normal = getGradientDrawable(normalColor, radius);
        GradientDrawable pressed = getGradientDrawable(pressedColor, radius);
        return getSelector(normal, pressed);
    }
}
