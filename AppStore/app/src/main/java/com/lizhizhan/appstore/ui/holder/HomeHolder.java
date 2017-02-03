package com.lizhizhan.appstore.ui.holder;

import android.text.format.Formatter;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.lizhizhan.appstore.R;
import com.lizhizhan.appstore.domain.AppInfo;
import com.lizhizhan.appstore.domain.DownloadInfo;
import com.lizhizhan.appstore.http.HttpHelper;
import com.lizhizhan.appstore.manager.mDownloadManager;
import com.lizhizhan.appstore.ui.view.ProgressArc;
import com.lizhizhan.appstore.utils.BitmapHelper;
import com.lizhizhan.appstore.utils.UIUtils;

/**
 * Created by lizhizhan on 2016/10/24.
 */

public class HomeHolder extends BaseHolder<AppInfo> implements View.OnClickListener, mDownloadManager.DownloadObserver {


    private TextView tvAppName;
    private TextView tvSize;
    private ImageView ivIcon;
    private RatingBar rbStar;
    private TextView tvDec;
    private BitmapUtils bitmapUtils;
    private FrameLayout flProgress;
    private TextView tvDownload;
    private ProgressArc pbProgress;
    private mDownloadManager mDM;
    private int mCurrentState;
    private float mProgress;


    @Override
    public View initView() {
        View view = UIUtils.inflate(R.layout.list_item_home);
        tvAppName = (TextView) view.findViewById(R.id.tv_appName);
        tvSize = (TextView) view.findViewById(R.id.tv_size);
        ivIcon = (ImageView) view.findViewById(R.id.iv_icon);
        rbStar = (RatingBar) view.findViewById(R.id.rb_star);
        tvDec = (TextView) view.findViewById(R.id.tv_dec);
        bitmapUtils = BitmapHelper.getmBitmapUtils();
        //初始化进度条
        flProgress = (FrameLayout) view.findViewById(R.id.fl_progress);
        tvDownload = (TextView) view.findViewById(R.id.tv_download);

        //初始化圆形进度条
        pbProgress = new ProgressArc(UIUtils.getContext());
        //设置圆形进度条直径
        pbProgress.setArcDiameter(UIUtils.dip2px(26));
        //设置进度条颜色
        pbProgress.setProgressColor(UIUtils.getColor(R.color.progress));
        //设置进度条宽高布局参数
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                UIUtils.dip2px(27), UIUtils.dip2px(27));

        flProgress.addView(pbProgress, params);
        flProgress.setOnClickListener(this);

        mDM = mDownloadManager.getInstance();
        mDM.registerObserver(this);
        return view;
    }

    @Override
    public void refreshView(AppInfo data) {
        tvAppName.setText(data.name);
        tvSize.setText(Formatter.formatFileSize(UIUtils.getContext(), data.size));
        tvDec.setText(data.des);
        bitmapUtils.display(ivIcon, HttpHelper.URL + "image?name=" + data.iconUrl);
        rbStar.setRating(data.stars);

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
        refreshUI(mCurrentState, mProgress, data.id);
    }

    /**
     * 刷新界面
     *
     * @param state
     * @param progress
     * @param id
     */
    private void refreshUI(int state, float progress, String id) {
        //ListView 的刷新机制，确保重用前是同一个运用
        if (!getData().id.equals(id)) {
            return;
        }

        mCurrentState = state;
        mProgress = progress;
        switch (state) {
            case mDownloadManager.STATE_UNDO:
                pbProgress.setBackgroundResource(R.drawable.ic_download);
                pbProgress.setStyle(ProgressArc.PROGRESS_STYLE_NO_PROGRESS);
                tvDownload.setText("下载");
                break;
            case mDownloadManager.STATE_WAITING:
                pbProgress.setBackgroundResource(R.drawable.ic_download);
                pbProgress.setStyle(ProgressArc.PROGRESS_STYLE_WAITING);
                tvDownload.setText("等待");
                break;
            case mDownloadManager.STATE_DOWNLOADING:
                pbProgress.setBackgroundResource(R.drawable.ic_pause);
                pbProgress.setStyle(ProgressArc.PROGRESS_STYLE_DOWNLOADING);
                pbProgress.setProgress(progress, true);
                tvDownload.setText((int) (progress * 100) + "%");
                break;
            case mDownloadManager.STATE_PAUSE:
                pbProgress.setBackgroundResource(R.drawable.ic_resume);
                pbProgress.setStyle(ProgressArc.PROGRESS_STYLE_NO_PROGRESS);
                break;
            case mDownloadManager.STATE_ERROR:
                pbProgress.setBackgroundResource(R.drawable.ic_redownload);
                pbProgress.setStyle(ProgressArc.PROGRESS_STYLE_NO_PROGRESS);
                tvDownload.setText("失败");
                break;
            case mDownloadManager.STATE_SUCCESS:
                pbProgress.setBackgroundResource(R.drawable.ic_install);
                pbProgress.setStyle(ProgressArc.PROGRESS_STYLE_NO_PROGRESS);
                tvDownload.setText("安装");
                break;

            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fl_progress:
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
                    refreshUI(downloadInfo.currentState, downloadInfo.getProgress(), downloadInfo.id);
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
