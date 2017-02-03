package com.lizhizhan.appstore.global;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

/**
 * 自定义Application,进行全局初始化
 * Created by lizhizhan on 2016/10/21.
 */

public class AppStoreApplication extends Application {

    private static Context context;
    private static Handler handler;
    private static int mainThreadid;

    @Override
    public void onCreate() {
        super.onCreate();

        context = getApplicationContext();
        handler = new Handler();
        //当前线程id，此处是主线程id
        mainThreadid = android.os.Process.myTid();
    }

    public static Context getContext() {
        return context;
    }

    public static Handler getHandler() {
        return handler;
    }

    public static int getMainThreadid() {
        return mainThreadid;
    }
}
