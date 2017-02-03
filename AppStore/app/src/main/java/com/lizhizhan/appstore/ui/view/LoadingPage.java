package com.lizhizhan.appstore.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.lizhizhan.appstore.R;
import com.lizhizhan.appstore.manager.ThreadManager;
import com.lizhizhan.appstore.utils.UIUtils;

/**
 * 未加载-加载中-加载失败-加载为空-加载成功
 * Created by lizhizhan on 2016/10/21.
 */

public abstract class LoadingPage extends FrameLayout {
    //未加载
    private static final int STATE_LOAD_UNDO = 1;
    //加载中
    private static final int STATE_LOAD_LOADING = 2;
    //加载失败
    private static final int STATE_LOAD_ERROR = 3;
    //加载为空
    private static final int STATE_LOAD_EMPTY = 4;
    //加载成功
    private static final int STATE_LOAD_SUCCESS = 5;
    //当前状态
    private int mCurrentState = STATE_LOAD_UNDO;
    private View mLoadingPager;
    private View mErrorPager;
    private View mEmptyPager;
    private View mSuccessPager;

    public LoadingPage(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }


    public LoadingPage(Context context) {
        super(context);
        initView();
    }

    public LoadingPage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    /**
     * FrameLayout填充View
     */
    private void initView() {
        //初始话加载中的布局
        if (mLoadingPager == null) {
            //初始化布局
            mLoadingPager = UIUtils.inflate(R.layout.page_loading);
            addView(mLoadingPager);
        }
        //        初始化加载失败布局
        if (mErrorPager == null) {
            mErrorPager = UIUtils.inflate(R.layout.page_error);
            Button btnRetry = (Button) mErrorPager.findViewById(R.id.btn_retry);
            btnRetry.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    LoadData();
                }
            });
            addView(mErrorPager);
        }
        //初始化空数据布局
        if (mEmptyPager == null) {
            mEmptyPager = UIUtils.inflate(R.layout.page_empty);
            addView(mEmptyPager);
        }


        showRightPage();
    }

    /**
     * 根据当前状态更新UI
     */
    private void showRightPage() {
        mLoadingPager.setVisibility((mCurrentState == STATE_LOAD_UNDO || mCurrentState == STATE_LOAD_LOADING) ? View.VISIBLE : View.GONE);
        mErrorPager.setVisibility(mCurrentState == STATE_LOAD_ERROR ? View.VISIBLE : View.GONE);
        mEmptyPager.setVisibility(mCurrentState == STATE_LOAD_EMPTY ? View.VISIBLE : View.GONE);
        if (mSuccessPager == null && mCurrentState == STATE_LOAD_SUCCESS) {
            mSuccessPager = onCreatSuccessView();
            if (mSuccessPager != null) {
                addView(mSuccessPager);
            }
        }
        if (mSuccessPager != null) {
            mSuccessPager.setVisibility(mCurrentState == STATE_LOAD_SUCCESS ? View.VISIBLE : View.GONE);
        }
    }

    public void LoadData() {
        //判断当前状态是否等于Loading,如果当前没有加载，就开始加载
        if (mCurrentState != STATE_LOAD_LOADING) {
            mCurrentState = STATE_LOAD_LOADING;
//            new Thread() {
//                @Override
//                public void run() {
//                    final ResultState resultState = initData();
//                    //更新UI要在主线程
//                    UIUtils.RunOnUiThread(
//                            new Runnable() {
//                                @Override
//                                public void run() {
//                                    if (resultState != null) {
//                                        //当前状态
//                                        mCurrentState = resultState.getState();
//                                        showRightPage();
//                                    }
//                                }
//                            }
//                    );
//
//
//                }
//            }.start();
            ThreadManager.getThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    final ResultState resultState = initData();
                    //                    //更新UI要在主线程
                    UIUtils.RunOnUiThread(
                            new Runnable() {
                                @Override
                                public void run() {
                                    if (resultState != null) {
                                        //当前状态
                                        mCurrentState = resultState.getState();
                                        showRightPage();
                                    }
                                }
                            }
                    );
                }
            });
        }

    }

    /**
     * 初始化成功布局
     *
     * @return
     */
    public abstract View onCreatSuccessView();

    /**
     * 加载网络数据,通过返回的值。更新UI
     *
     * @return
     */
    public abstract ResultState initData();

    //枚举
    public enum ResultState {
        STATE_SUCCESS(STATE_LOAD_SUCCESS),
        STATE_EMPTY(STATE_LOAD_EMPTY),
        STATE_ERROR(STATE_LOAD_ERROR);
        int state;

        private ResultState(int state) {
            this.state = state;
        }

        public int getState() {
            return state;
        }

    }
}
