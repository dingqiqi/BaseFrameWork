package com.dingqiqi.baseframework.manager;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 请求线程池
 * Created by dingqiqi on 2016/12/30.
 */
public class ExecutorManager {

    private static ExecutorManager mInstance = new ExecutorManager();

    private ExecutorService mService = Executors.newFixedThreadPool(1);

    public static ExecutorManager getInstance() {
        return mInstance;
    }

    public void request(Runnable mRunnable) {
        if (mService.isShutdown()) {
            mService = Executors.newFixedThreadPool(1);
        }
        mService.execute(mRunnable);
    }

    public void stopRequest() {
        mService.shutdown();
    }

}
