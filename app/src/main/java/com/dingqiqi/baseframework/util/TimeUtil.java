package com.dingqiqi.baseframework.util;

import android.text.TextUtils;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 时间工具类
 * Created by 丁奇奇 on 2017/8/9.
 */
public class TimeUtil {
    /**
     * 时间
     */
    public static final String[] mWeek = new String[]{"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};

    /**
     * 时间格式转换工具
     */
    public static SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());

    public static SimpleDateFormat mDateFormatSS = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());

    /**
     * 带有时分秒的时间
     * @return 时间
     */
    public static String getCurSecondData(){
        return mDateFormatSS.format(getCurDate());
    }

    /**
     * 获取当前时间
     *
     * @return 当前时间
     */
    public static String getCurTime() {
        return mDateFormat.format(getCurDate());
    }

    public static Date getCurDate() {
        return new Date();
    }

    public static Date getDate(String time) {
        if (!TextUtils.isEmpty(time)) {
            try {
                return mDateFormat.parse(time);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    /**
     * 获取当前年
     *
     * @return
     */
    public static int getCurYear() {
        Calendar mCalendar = Calendar.getInstance();
        return mCalendar.get(Calendar.YEAR);
    }

    /**
     * 获取当前月
     *
     * @return
     */
    public static int getCurMonth() {
        Calendar mCalendar = Calendar.getInstance();
        return mCalendar.get(Calendar.MONTH) + 1;
    }

    /**
     * 获取当前日
     *
     * @return
     */
    public static int getCurDay() {
        Calendar mCalendar = Calendar.getInstance();
        return mCalendar.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获取当前年
     *
     * @return
     */
    public static int getYear(Date date) {
        if (date == null) {
            return 0;
        }

        Calendar mCalendar = Calendar.getInstance();
        mCalendar.setTime(date);
        return mCalendar.get(Calendar.YEAR);
    }

    /**
     * 获取当前月
     *
     * @return
     */
    public static int getMonth(Date date) {
        if (date == null) {
            return 0;
        }

        Calendar mCalendar = Calendar.getInstance();
        mCalendar.setTime(date);
        return mCalendar.get(Calendar.MONTH) + 1;
    }

    /**
     * 获取当前日
     *
     * @return
     */
    public static int getDay(Date date) {
        if (date == null) {
            return 0;
        }

        Calendar mCalendar = Calendar.getInstance();
        mCalendar.setTime(date);
        return mCalendar.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获取某月第一天
     *
     * @param year  年
     * @param month 月
     * @return
     */
    public static Date getMonthFirstDay(int year, int month) {
        Calendar mCalendar = Calendar.getInstance();

        mCalendar.set(Calendar.YEAR, year);
        mCalendar.set(Calendar.MONTH, month - 1);
        mCalendar.set(Calendar.DAY_OF_MONTH, mCalendar.getActualMinimum(Calendar.DAY_OF_MONTH));

        return mCalendar.getTime();
    }

    /**
     * 获取某月最后一天
     *
     * @param year  年
     * @param month 月
     * @return
     */
    public static Date getMonthLastDay(int year, int month) {
        Calendar mCalendar = Calendar.getInstance();

        mCalendar.set(Calendar.YEAR, year);
        mCalendar.set(Calendar.MONTH, month - 1);
        mCalendar.set(Calendar.DAY_OF_MONTH, mCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));

        return mCalendar.getTime();
    }

    /**
     * 获取某一天是周几
     *
     * @param date 时间
     * @return
     */
    public static int getWeek(Date date) {
        if (date != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            return calendar.get(Calendar.DAY_OF_WEEK);
        }

        return -1;
    }

    /**
     * 获取星期几文字
     *
     * @param day
     * @return
     */
    public static String getWeekStr(int day) {
        if (day < 0) {
            return "";
        }
        return mWeek[day - 1];
    }

    /**
     * 获取这个月显示的上个月或下个月日期
     *
     * @param date      本月第一天
     * @param isBack    是否是前几天
     * @param addLength 一个月后几天 增加的天数 (有的一个月五行就能表示,但是还是要增加一行,统一六行)
     * @return 日期列表
     */
    public static String[] getOtherMonthDay(Date date, boolean isBack, int addLength) {
        //获取星期  星期日1 依次叠加
        int dayWeek = getWeek(date);
        if (dayWeek != -1) {
            //假如当月第一天是星期天 星期天返回的是1 -1做下标处理
            if (isBack) {
                dayWeek = dayWeek - 1;
                if (dayWeek == 0) {
                    dayWeek = dayWeek + addLength;
                }
            } else {
                dayWeek = 7 - dayWeek + addLength;
            }

            String[] array = new String[dayWeek];

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);

            for (int i = 0; i < dayWeek; i++) {
                if (isBack) {
                    calendar.add(Calendar.DAY_OF_MONTH, -1);
                    array[dayWeek - i - 1] = mDateFormat.format(calendar.getTime());
                } else {
                    calendar.add(Calendar.DAY_OF_MONTH, 1);
                    array[i] = mDateFormat.format(calendar.getTime());
                }
            }

            return array;
        }

        return new String[0];
    }

    /**
     * 获取这周的日期
     *
     * @param date 选中的哪天
     * @return
     */
    public static String[] getOtherWeekDay(Date date) {
        //获取星期  星期日1 依次叠加
        int dayWeek = getWeek(date);
        if (dayWeek != -1) {
            //假如当月第一天是星期天 星期天返回的是1 -1做下标处理
            dayWeek = dayWeek - 1;

            String[] array = new String[7];

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);

            for (int i = 0; i < dayWeek; i++) {
                calendar.add(Calendar.DAY_OF_MONTH, -1);
                array[dayWeek - i - 1] = mDateFormat.format(calendar.getTime());
            }

            array[dayWeek] = mDateFormat.format(date);

            calendar.setTime(date);
            for (int i = dayWeek + 1; i < 7; i++) {
                calendar.add(Calendar.DAY_OF_MONTH, 1);
                array[i] = mDateFormat.format(calendar.getTime());
            }

            return array;
        }

        return new String[0];
    }

    /**
     * 获取某月的时间列表
     *
     * @param date
     * @return
     */
    public static String[] getMonthDay(Date date) {
        int year = getYear(date);
        int month = getMonth(date);

        String firstTime = mDateFormat.format(getMonthFirstDay(year, month));
        String lastTime = mDateFormat.format(getMonthLastDay(year, month));

        int first = Integer.parseInt(firstTime);
        int last = Integer.parseInt(lastTime);

        String[] array = new String[last - first + 1];

        for (int i = first; i <= last; i++) {
            array[i - first] = String.valueOf(i);
        }

        return array;
    }

    /**
     * 获取距离本月几个月的日期
     *
     * @param date 本月
     * @param num  距离几个月 - 往前 +往后
     * @return
     */
    public static Date getOtherMonth(Date date, int num) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, num);

        return calendar.getTime();
    }

    /**
     * 获取距离本天相隔几天的日期
     *
     * @param date 本月
     * @param num  距离几天 - 往前 +往后
     * @return
     */
    public static Date getOtherDay(Date date, int num) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, num);

        return calendar.getTime();
    }

    /**
     * 获取时间
     *
     * @param date
     * @return
     */
    public static String getTime(Date date) {
        return mDateFormat.format(date);
    }

    /**
     * 获得两个日期直接相差的分钟数
     */
    public static long getMinutesBetweenDates(String dateStr1, String dateStr2) {
        if (dateStr1 == null || dateStr2 == null) {
            return 0;
        }
        SimpleDateFormat sFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        try {
            Date date1 = sFormat.parse(dateStr1);
            Date date2 = sFormat.parse(dateStr2);
            long dateTime1 = date1.getTime();
            long dateTime2 = date2.getTime();

            return (dateTime1 - dateTime2) / 1000 / 60;
        } catch (Exception e) {
            Log.e("CommonUtil", "error", e);
        }
        return 0;
    }
}
