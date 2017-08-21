package com.dingqiqi.baseframework.util;

import android.app.Activity;

import com.dingqiqi.baseframework.view.WaitingDialog;

/**
 * Created by dingqiqi on 2017/1/18.
 */
public class DialogUtil {

    /**
     * 等待框
     */
    private static WaitingDialog mWaitingDialog;

    /*
    * 显示等待框
    */
    public static void showWaitingDialog(Activity activity) {
        showWaitingDialog(activity, "加载中...");
    }

    /**
     * 显示等待框
     */
    public static void showWaitingDialog(Activity activity, String content) {
        initWaitingDialog(activity, content);
        mWaitingDialog.show();
    }

    /**
     * 初始化WaitingDialog
     */
    private static void initWaitingDialog(Activity activity, String content) {
        mWaitingDialog = null;
        mWaitingDialog = new WaitingDialog(activity, content);
        mWaitingDialog.setCanceledOnTouchOutside(false);
        mWaitingDialog.setCancelable(false);

//        mWaitingDialog.setOnCancelListener();
    }

    /**
     * 隐藏等待框
     */
    public static void dismissWaitingDialog() {
        if (mWaitingDialog != null && mWaitingDialog.isShowing()) {
            try {
                mWaitingDialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
