package com.lizhizhan.appstore.ui.holder;

import android.view.View;

/**
 * Created by lizhizhan on 2016/10/24.
 */

public abstract class BaseHolder<T> {
    private View mRootView;//一个Item 的根布局
    private T data;

    public BaseHolder() {
        mRootView = initView();
        mRootView.setTag(this);
    }

    public View getmRootView() {
        return mRootView;
    }

    /**
     * 设置数据，更新布局
     * @param data
     */
    public void setData(T data) {
        this.data = data;
        refreshView(data);
    }

    public T getData() {
        return data;
    }

    /**
     * 加载布局文件，
     * fingviewbyid
     *
     * @return
     */
    public abstract View initView();

    /**
     * 根据数据刷新布局
     */
    public abstract void refreshView(T data);
}
