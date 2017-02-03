package com.lizhizhan.appstore.ui.holder;

import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.lizhizhan.appstore.R;
import com.lizhizhan.appstore.domain.AppInfo;
import com.lizhizhan.appstore.domain.DownloadInfo;
import com.lizhizhan.appstore.manager.mDownloadManager;
import com.lizhizhan.appstore.ui.view.ProgressHorizontal;
import com.lizhizhan.appstore.utils.UIUtils;

/**
 * Created by lizhizhan on 2016/11/4.
 */

public class DetailDownloadHolder extends BaseHolder<AppInfo> implements mDownloadManager.DownloadObserver, View.OnClickListener {

    private Button btnDownload;
    private FrameLayout flDownload;
    private ProgressHorizontal progressHorizontal;

    private mDownloadManager mDM;
    private int mCurrentState;
    private float mProgress;

    @Override
    public View initView() {
        View view = UIUtils.inflate(R.layout.layout_detail_download);
        btnDownload = (Button) view.findViewById(R.id.btn_download);
        flDownload = (FrameLayout) view.findViewById(R.id.fl_download);

        progressHorizontal = new ProgressHorizontal(UIUtils.getContext());
        progressHorizontal.setBackgroundResource(R.drawable.progress_bg);
        progressHorizontal.setProgressResource(R.drawable.progress_normal);
        progressHorizontal.setProgressTextColor(Color.WHITE);
        progressHorizontal.setProgressTextSize(UIUtils.dip2px(16));
        //控件大小
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        flDownload.addView(progressHorizontal, layoutParams);

        btnDownload.setOnClickListener(this);
        flDownload.setOnClickListener(this);
        mDM = mDownloadManager.getInstance();
        mDM.registerObserver(this);
        return view;
    }

    @Override
    public void refreshView(AppInfo data) {
        DownloadInfo downloadInfo = mDM.getDownloadInfo(getData());
        //        获取到当前状态
        if (downloadInfo != null) {
            //        之前下载过
            mCurrentState = downloadInfo.currentState;
            mProgress = downloadInfo.getProgress();
        } else {
            //没有下载过
            mCurrentState = mDownloadManager.STATE_UNDO;
            mProgress = 0;
        }
        System.out.println("mProgress" + mCurrentState + "ll" + mProgress);
        refreshUi(mCurrentState, mProgress);
    }

    /**
     * 根据当前进度和状态来更新UI
     *
     * @param CurrentState
     * @param Progress
     */
    private void refreshUi(int CurrentState, float Progress) {
        System.out.println("刷新UI了" + CurrentState);
        mCurrentState = CurrentState;
        mProgress = Progress;
        switch (CurrentState) {
            case mDownloadManager.STATE_UNDO:
                flDownload.setVisibility(View.GONE);
                btnDownload.setVisibility(View.VISIBLE);
                btnDownload.setText("下载");
                break;
            case mDownloadManager.STATE_WAITING:
                flDownload.setVisibility(View.GONE);
                btnDownload.setVisibility(View.VISIBLE);
                btnDownload.setText("等待下载...");
                break;
            case mDownloadManager.STATE_DOWNLOADING:
                flDownload.setVisibility(View.VISIBLE);
                btnDownload.setVisibility(View.GONE);
                progressHorizontal.setCenterText("");
                progressHorizontal.setProgress(mProgress);
                break;
            case mDownloadManager.STATE_PAUSE:
                flDownload.setVisibility(View.VISIBLE);
                btnDownload.setVisibility(View.GONE);
                progressHorizontal.setProgress(mProgress);
                progressHorizontal.setCenterText("暂停");
                break;
            case mDownloadManager.STATE_ERROR:
                flDownload.setVisibility(View.GONE);
                btnDownload.setVisibility(View.VISIBLE);
                btnDownload.setText("下载失败");
                break;
            case mDownloadManager.STATE_SUCCESS:
                flDownload.setVisibility(View.GONE);
                btnDownload.setVisibility(View.VISIBLE);
                btnDownload.setText("安装");
                break;
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_download:
            case R.id.fl_download:
                //根据状态来进行下一步动作
                if (mCurrentState == mDownloadManager.STATE_UNDO || mCurrentState == mDownloadManager.STATE_PAUSE
                        || mCurrentState == mDownloadManager.STATE_ERROR) {
                    mDM.download(getData());
                } else if (mCurrentState == mDownloadManager.STATE_DOWNLOADING || mCurrentState == mDownloadManager.STATE_WAITING) {
                    mDM.pause(getData());
                } else if (mCurrentState == mDownloadManager.STATE_SUCCESS) {
                    mDM.install(getData());
                }
                break;


        }
    }


    private void refreshUiOnMainThread(final DownloadInfo downloadInfo) {
        AppInfo appInfo = getData();
        //过滤被的下载
        if (appInfo.id.equals(downloadInfo.id)) {
            UIUtils.RunOnUiThread(new Runnable() {
                @Override
                public void run() {
                    refreshUi(downloadInfo.currentState, downloadInfo.getProgress());
                }
            });
        }

    }

    @Override
    public void onDownloadStateChanged(DownloadInfo downloadInfo) {
        refreshUiOnMainThread(downloadInfo);
    }

    @Override
    public void onDownloadProgressChanged(DownloadInfo downloadInfo) {
        refreshUiOnMainThread(downloadInfo);
    }


}
