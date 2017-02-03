package com.lizhizhan.appstore.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.View;

import com.lizhizhan.appstore.global.AppStoreApplication;

/**
 * Created by lizhizhan on 2016/10/21.
 */

public class UIUtils {
    public static Context getContext() {
        return AppStoreApplication.getContext();
    }

    public static Handler getHandler() {
        return AppStoreApplication.getHandler();
    }

    public static int getMainThreadId() {
        return AppStoreApplication.getMainThreadid();
    }
    ///////////加载资源文件////////////

    /**
     * 获取字符串
     *
     * @param id
     * @return
     */

    public static String getString(int id) {
        return getContext().getResources().getString(id);
    }

    /**
     * 获取字符串数组
     *
     * @param id
     * @return
     */
    public static String[] getStringArray(int id) {
        return getContext().getResources().getStringArray(id);
    }

    /**
     * 获取图片
     *
     * @param id
     * @return
     */
    public static Drawable getDrawable(int id) {
        return getContext().getResources().getDrawable(id);
    }

    /**
     * 获取颜色
     *
     * @param id
     * @return
     */
    public static int getColor(int id) {
        return getContext().getResources().getColor(id);
    }

    /**
     * 根据id获取颜色的状态选择器
     *
     * @param id
     * @return
     */

    public static ColorStateList getColorStateList(int id) {
        return getContext().getResources().getColorStateList(id);
    }

    /**
     * 获取尺寸，具体像素值
     *
     * @param id
     * @return
     */
    public static int getDimen(int id) {
        return getContext().getResources().getDimensionPixelSize(id);
    }

    public static int dip2px(float dip) {
        //设备密度
        float density = getContext().getResources().getDisplayMetrics().density;
        return (int) (dip * density + 0.5f);
    }

    public static float px2dip(int px) {
        //设备密度
        float density = getContext().getResources().getDisplayMetrics().density;
        return px / density;
    }

    /**
     * 加载布局文件
     *
     * @param id
     * @return
     */
    public static View inflate(int id) {
        return View.inflate(getContext(), id, null);
    }

    /**
     * 判断是否在主线程运行
     *
     * @return
     */
    public static boolean isRunOnUiThread() {

        return getMainThreadId() == android.os.Process.myTid();
    }

    public static void RunOnUiThread(Runnable r) {
        if (isRunOnUiThread()) {
            r.run();
        } else {
            //如果在子线程，借助Handler让其运行在主线程
            getHandler().post(r);
        }
    }


}
