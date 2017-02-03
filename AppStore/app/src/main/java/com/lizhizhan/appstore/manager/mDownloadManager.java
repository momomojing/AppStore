package com.lizhizhan.appstore.manager;

import android.content.Intent;
import android.net.Uri;

import com.lizhizhan.appstore.domain.AppInfo;
import com.lizhizhan.appstore.domain.DownloadInfo;
import com.lizhizhan.appstore.http.HttpHelper;
import com.lizhizhan.appstore.utils.IOUtils;
import com.lizhizhan.appstore.utils.UIUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by lizhizhan on 2016/11/5.
 */

public class mDownloadManager {

    public static final int STATE_UNDO = 1;
    public static final int STATE_WAITING = 2;
    public static final int STATE_DOWNLOADING = 3;
    public static final int STATE_PAUSE = 4;
    public static final int STATE_ERROR = 5;
    public static final int STATE_SUCCESS = 6;


    private static mDownloadManager mDM = new mDownloadManager();
    //观察者集合
    private ArrayList<DownloadObserver> mObservers = new ArrayList<DownloadObserver>();
    // Concurrent并发。  线程安全的HashMap
    private ConcurrentHashMap<String, DownloadInfo> downloadInfoHashMap = new ConcurrentHashMap<String, DownloadInfo>();
    private ConcurrentHashMap<String, DownloadTask> downloadTaskList = new ConcurrentHashMap<String, DownloadTask>();

    //一旦被调用就执行
    public mDownloadManager() {

    }


    public static mDownloadManager getInstance() {
        return mDM;
    }

    /**
     * 注册观察者
     */
    public void registerObserver(DownloadObserver observer) {
        if (observer != null && !mObservers.contains(observer)) {
            mObservers.add(observer);
        }
    }

    /**
     * 注销观察者
     */
    public void unRegisterObserver(DownloadObserver observer) {
        if (observer != null && mObservers.contains(observer)) {
            mObservers.remove(observer);
        }
    }

    /**
     * 通知下载状态改变
     *
     * @param downloadInfo
     */
    public void notifynDownloadStateChanged(DownloadInfo downloadInfo) {
        for (DownloadObserver observer : mObservers) {
            observer.onDownloadStateChanged(downloadInfo);
        }
    }

    /**
     * 下载进度改变
     */
    public void notifyDownloadProgressChanged(DownloadInfo downloadInfo) {
        for (DownloadObserver observer : mObservers) {
            observer.onDownloadProgressChanged(downloadInfo);
        }
    }

    /**
     * 下载
     */
    public synchronized void download(AppInfo appInfo) {

        //如果是第一次下载，就从头下载。否则断点续传
        DownloadInfo downloadInfo = downloadInfoHashMap.get(appInfo.id);

        if (downloadInfo == null) {
            downloadInfo = DownloadInfo.copy(appInfo);//生成一个下载对象
        }

        downloadInfo.currentState = STATE_WAITING;//改变状态
        System.out.println(downloadInfo.name + "准备下载了");
        notifynDownloadStateChanged(downloadInfo);//通知观察者，状态改变了

        downloadInfoHashMap.put(downloadInfo.id, downloadInfo);

        DownloadTask task = new DownloadTask(downloadInfo);
        mThreadManager.getThreadPool().execute(task);

        downloadTaskList.put(downloadInfo.id, task);
    }

    class DownloadTask implements Runnable {


        private final DownloadInfo downloadInfo;

        public DownloadTask(DownloadInfo downloadInfo) {
            this.downloadInfo = downloadInfo;
        }

        @Override
        public void run() {
            System.out.println(downloadInfo.name + "我的开始下载了");
            downloadInfo.currentState = STATE_DOWNLOADING;
            notifynDownloadStateChanged(downloadInfo);
            File file = new File(downloadInfo.path);
            System.out.println("下载路径");
            System.out.println("下载路径"+downloadInfo.path);
            HttpHelper.HttpResult result;
            if (!file.exists() || downloadInfo.currentPos != file.length() || downloadInfo.currentPos == 0) {
                //从头下载
                file.delete();
                downloadInfo.currentPos = 0;
                result = HttpHelper.download(HttpHelper.URL + "download?name=" + downloadInfo.downloadUrl);

            } else {
                //断点续传"&range=" + file.length()
                result = HttpHelper.download(HttpHelper.URL + "download?name=" + downloadInfo.downloadUrl + "&range=" + file.length());
            }
            if (result != null && result.getInputStream() != null) {

                InputStream in = result.getInputStream();
                FileOutputStream out = null;
                try {
                    out = new FileOutputStream(file, true);//要加原文件上追加数据
                    int len = 0;
                    byte[] buffer = new byte[1024];
                    //判断当前状态，下载一半点暂停时触发。
                    while ((len = in.read(buffer)) != -1 && downloadInfo.currentState == STATE_DOWNLOADING) {
                        out.write(buffer, 0, len);
                        out.flush();
                        //更新下载进度
                        downloadInfo.currentPos += len;
                        notifyDownloadProgressChanged(downloadInfo);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    IOUtils.close(in);
                    IOUtils.close(out);
                }
                //文件下载成功
                if (file.length() == downloadInfo.size) {
                    downloadInfo.currentState = STATE_SUCCESS;
                    notifynDownloadStateChanged(downloadInfo);
                } else if (downloadInfo.currentState == STATE_PAUSE) {
                    //中途暂停
                    notifynDownloadStateChanged(downloadInfo);
                } else {
                    //失败
                    file.delete();
                    downloadInfo.currentState = STATE_ERROR;
                    downloadInfo.currentPos = 0;
                    notifynDownloadStateChanged(downloadInfo);
                }
            } else {
                //网络错误
                file.delete();
                downloadInfo.currentState = STATE_ERROR;
                downloadInfo.currentPos = 0;
                notifynDownloadStateChanged(downloadInfo);

            }
            //移除下载任务
            downloadTaskList.remove(downloadInfo.id);

        }
    }

    /**
     * 暂停
     */
    public synchronized void pause(AppInfo appInfo) {
        DownloadInfo downloadInfo = downloadInfoHashMap.get(appInfo.id);
        downloadInfoHashMap.get(appInfo.id);
        if (downloadInfo != null) {
            if (downloadInfo.currentState == STATE_WAITING || downloadInfo.currentState == STATE_DOWNLOADING) {
                downloadInfo.currentState = STATE_PAUSE;
                notifynDownloadStateChanged(downloadInfo);

                DownloadTask task = downloadTaskList.get(downloadInfo.id);
                if (task != null) {
                    mThreadManager.getThreadPool().cancel(task);
                }
                // 将下载状态切换为暂停
                downloadInfo.currentState = STATE_PAUSE;
                notifynDownloadStateChanged(downloadInfo);
            }
        }
    }

    /**
     * 安装
     */
    public synchronized void install(AppInfo appInfo) {
        DownloadInfo downloadInfo = downloadInfoHashMap.get(appInfo.id);
        if (downloadInfo != null) {
            // 跳到系统的安装页面进行安装
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.parse("file://" + downloadInfo.path),
                    "application/vnd.android.package-archive");
            UIUtils.getContext().startActivity(intent);
        }
    }

    /**
     * 声明观察者的接口
     */
    public interface DownloadObserver {
        /**
         * 下载状态的改变
         *
         * @param downloadInfo
         */
        public void onDownloadStateChanged(DownloadInfo downloadInfo);

        /**
         * 下载进度的改变
         *
         * @param downloadInfo
         */
        public void onDownloadProgressChanged(DownloadInfo downloadInfo);


    }

    public DownloadInfo getDownloadInfo(AppInfo appInfo) {
        return downloadInfoHashMap.get(appInfo.id);
    }
}
