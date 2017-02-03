package com.lizhizhan.appstore.manager;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池
 * Created by lizhizhan on 2016/11/5.
 */

public class ThreadManager {

    private static threadPool mThreadPool;

    public static threadPool getThreadPool() {
        if (mThreadPool == null) {
            synchronized (ThreadManager.class) {
                if (mThreadPool == null) {
                    //int cpuCount = Runtime.getRuntime().availableProcessors();//获取Cpu数量的
                    int threadCount = 10;
                    mThreadPool = new threadPool(threadCount, threadCount, 1L);
                }
            }
        }
        return mThreadPool;
    }


    public static class threadPool {

        private int corePoolSize;//核心线程
        private int maximumPoolSize;//最大线程
        private long keepAliveTime;//休息时间
        private ThreadPoolExecutor executor;

        private threadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime) {
            this.corePoolSize = corePoolSize;
            this.maximumPoolSize = maximumPoolSize;
            this.keepAliveTime = keepAliveTime;
        }

        public void execute(Runnable r) {
            //参 5 线程队列、6生产线程的工程，7 线程异常处理
            if (executor == null) {
                executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize,
                        keepAliveTime, TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>(), Executors.defaultThreadFactory()
                        , new ThreadPoolExecutor.AbortPolicy());
            }
            executor.execute(r);
        }
    }
}
