package com.lizhizhan.appstore.domain;

import android.os.Environment;

import com.lizhizhan.appstore.manager.mDownloadManager;

import java.io.File;

/**
 * Created by lizhizhan on 2016/11/5.
 */

public class DownloadInfo {
    public String id;
    public String name;
    public String packageName;
    public String downloadUrl;
    public long size;
    public String path;
    public long currentPos;//当前位置
    public int currentState;

    public static final String GOOGLE_MARKET = "GOOGLE_MARKET";
    public static final String DOWNLOAD = "download";

    public float getProgress() {
        if (size == 0) {
            return 0;
        }
        float progress = currentPos / (float) size;

        return progress;
    }

    public static DownloadInfo copy(AppInfo appInfo) {
        DownloadInfo downloadInfo = new DownloadInfo();
        downloadInfo.size = appInfo.size;
        downloadInfo.name = appInfo.name;
        downloadInfo.packageName = appInfo.packageName;
        downloadInfo.downloadUrl = appInfo.downloadUrl;
        downloadInfo.id = appInfo.id;

        downloadInfo.currentPos = 0;
        downloadInfo.currentState = mDownloadManager.STATE_UNDO;
        downloadInfo.path = downloadInfo.getFilePath();
        return downloadInfo;
    }

    // 获取文件下载路径
    public String getFilePath() {
        StringBuffer sb = new StringBuffer();
        String sdcard = Environment.getExternalStorageDirectory()
                .getAbsolutePath();
        sb.append(sdcard);
        // sb.append("/");
        sb.append(File.separator);
        sb.append(GOOGLE_MARKET);
        sb.append(File.separator);
        sb.append(DOWNLOAD);

        if (createDir(sb.toString())) {
            // 文件夹存在或者已经创建完成
            return sb.toString() + File.separator + name + ".apk";// 返回文件路径
        }

        return null;
    }

    /**
     * 注意if里面的判断条件
     *
     * @param dir
     * @return
     */
    public static boolean createDir(String dir) {
        File file = new File(dir);
        //不存在或者，不是一个文件夹
        if (!file.exists() || !file.isDirectory()) {
            return file.mkdirs();
        }
        return true;
    }
}
