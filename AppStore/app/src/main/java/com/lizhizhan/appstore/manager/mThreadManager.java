package com.lizhizhan.appstore.manager;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by lizhizhan on 2016/11/5.
 */

public class mThreadManager {

    private static ThreadPool threadPool;

    public static ThreadPool getThreadPool() {
        if (threadPool == null) {
            synchronized (mThreadManager.class) {
                if (threadPool == null) {
                    int threadCount = 10;
                    threadPool = new ThreadPool(threadCount, threadCount, 0);
                }
            }

        }
        return threadPool;
    }

    public static class ThreadPool {
        private int corePoolSize;
        private int maximumPoolSize;
        private long keepAliveTime;
        private ThreadPoolExecutor executor;

        public ThreadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime) {
            this.corePoolSize = corePoolSize;
            this.maximumPoolSize = maximumPoolSize;
            this.keepAliveTime = keepAliveTime;

        }

        public void execute(Runnable r) {

            if (executor == null) {
                executor = new ThreadPoolExecutor(corePoolSize,
                        maximumPoolSize, keepAliveTime, TimeUnit.SECONDS
                        , new LinkedBlockingQueue<Runnable>(),
                        Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());

            }
            // 线程池执行一个Runnable对象, 具体运行时机线程池说了算
            executor.execute(r);
        }

        // 取消任务
        public void cancel(Runnable r) {
            if (executor != null) {
                // 从线程队列中移除对象
                executor.getQueue().remove(r);
            }
        }
    }
}
