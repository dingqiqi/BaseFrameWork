package com.dingqiqi.baseframework.application;

import android.app.Activity;
import android.app.Application;
import android.graphics.Bitmap;
import android.support.v4.app.FragmentActivity;

import com.dingqiqi.baseframework.util.LogUtil;
import com.dingqiqi.baseframework.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Application
 * Created by 丁奇奇 on 2016/12/26.
 */
public class CustomApplication extends Application {
    /**
     * 当前对象
     */
    private static CustomApplication mInstance;
    /**
     * 管理Activity列表
     */
    private static List<Activity> mList = new ArrayList<>();
    /**
     * 不裁减返回的图片路径
     */
    public static String mFilePath;
    /**
     * 裁剪返回的图片bitmap
     */
    public static Bitmap mFileBitmap;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        //日志初始化
        LogUtil.init(this, false);
        ToastUtil.init(this);
        //异常处理
        ApplicationCrashHandler.getInstance().init(this);
    }

    public static CustomApplication getInstance() {
        return mInstance;
    }

    public void addActivity(FragmentActivity activity) {
        mList.add(activity);
    }

    public void removeActivity(FragmentActivity activity) {
        mList.remove(activity);
    }

    /**
     * 移除所有活动
     */
    public void finishAllActivity() {
        if (mList.size() == 0) {
            return;
        }

        for (int i = mList.size() - 1; i >= 0; i--) {
            if (!mList.get(i).isFinishing()) {
                mList.get(i).finish();
            }
            mList.remove(i);
        }
    }

    /**
     * 获取最上层的Activity，即当前顶层可见的Activity。
     *
     * @return
     */
    public Activity getTopActivity() {
        if (mList.size() == 0) {
            return null;
        }

        return mList.get(mList.size() - 1);
    }

    /**
     * 退出应用
     * 关闭应用的所有的Activity
     *
     * @param exitCode 退出码
     */
    public void exitApplication(int exitCode) {
        finishAllActivity();
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(exitCode);
    }

}
