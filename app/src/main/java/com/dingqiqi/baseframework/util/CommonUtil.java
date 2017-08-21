package com.dingqiqi.baseframework.util;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonUtil {

    /**
     * 判断网络连接
     *
     * @param context
     * @return
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo info = cm.getActiveNetworkInfo();
            if (info != null) {
                return info.isAvailable();
            }
        }
        return false;
    }

    /**
     * 判断service是否运行
     *
     * @param context
     * @param servicePath
     * @return
     */
    public static boolean isServiceWorked(Context context, String servicePath) {
        ActivityManager myManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ArrayList<RunningServiceInfo> runningService = (ArrayList<RunningServiceInfo>) myManager.getRunningServices(30);
        for (int i = 0; i < runningService.size(); i++) {
            RunningServiceInfo serviceInfo = runningService.get(i);
            if (serviceInfo != null && servicePath.equals(serviceInfo.
                    service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取版本名
     */
    public static String getVersion(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            Log.e("CommonUtil", "error", e);
            return null;
        }
    }

    /**
     * app is running
     */
    public static boolean isRunningForeground(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> rap = am.getRunningAppProcesses();
        for (RunningAppProcessInfo appProcess : rap) {
            if (appProcess.processName.equals(context.getPackageName())) {
                /*
                BACKGROUND=400 EMPTY=500 FOREGROUND=100 
                GONE=1000 PERCEPTIBLE=130 SERVICE=300 ISIBLE=200 
				 */
                Log.i(context.getPackageName(), "此appimportace ="
                        + appProcess.importance
                        + ",context.getClass().getName()="
                        + context.getClass().getName());
                if (appProcess.importance != RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    Log.i(context.getPackageName(), "处于后台"
                            + appProcess.processName);
                    return false;
                } else {
                    Log.i(context.getPackageName(), "处于前台"
                            + appProcess.processName);
                    return true;
                }
            }
        }

        return false;
    }


    /**
     * 隐藏软键盘
     *
     * @param context 上下文
     * @param v       view
     */
    public static void hideInputMethod(Context context, View v) {
        if (context != null) {
            // 得到InputMethodManager的实例
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            // 隐藏软键盘
            imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
        }
    }

    /**
     * 判断某个界面是否在前台
     *
     * @param context   上下文
     * @param className 某个界面名称
     */
    public static boolean isActivityForeground(Context context, String className) {
        if (context == null || TextUtils.isEmpty(className)) {
            return false;
        }

        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> list = am.getRunningTasks(1);
        if (list != null && list.size() > 0) {
            ComponentName cpn = list.get(0).topActivity;
            if (className.equals(cpn.getClassName())) {
                return true;
            }
        }

        return false;
    }

    /**
     * 检查输入的内容是否只包含数字和字母
     *
     * @param input 输入内容
     * @return true false
     */
    public static boolean checkOnlyNumberAndEnglish(String input) {
        String patter = "[a-zA-Z0-9]*";
        if (input != null) {
            Pattern p = Pattern.compile(patter);
            Matcher m = p.matcher(input);
            return m.matches();
        }
        return true;
    }

    /**
     * 是否为正确邮箱
     */
    public static boolean isEmail(String email) {
        if (TextUtils.isEmpty(email)) {
            return false;
        }

        String regEx = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}";
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    /**
     * 设置系统语言
     */
    public static void setAppLanguage(Context context, String languageCode) {
        Configuration config = context.getResources().getConfiguration();
        if ("en".equals(languageCode)) {//英语
            config.locale = Locale.ENGLISH;
        } else if ("jp".equals(languageCode)) {//日语
            config.locale = Locale.JAPANESE;
        } else if ("zh-rCN".equals(languageCode)) {//中文简体
            config.locale = Locale.SIMPLIFIED_CHINESE;
        } else {
            config.locale = Locale.getDefault();
        }

        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
    }


}
