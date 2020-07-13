package com.ryan.sdkj_core.remind;

import android.content.Context;

/**
 * @Author: Ryan
 * @Date: 2020/7/7 14:36
 * @Description:  往系统日历添加日程
 */
public interface ICalendarWarn {
    /**
     * 检查是否已经添加了日历账户，如果没有添加先添加一个日历账户再查询
     * 获取账户成功返回账户id，否则返回-1
     */
    int checkAndAddCalendarAccount(Context context);

    /**
     * 检查是否存在现有账户，存在则返回账户id，否则返回-1
     */
    int checkCalendarAccount(Context context);


    /**
     * 添加日历账户，账户创建成功则返回账户id，否则返回-1
     */
    long addCalendarAccount(Context context);

    /**
     * 添加日历
     *
     * @param context
     * @param title
     * @param description
     * @param reminderTime
     * @param previousDate
     */
    void addCalendarEvent(Context context, String title, String description, long reminderTime, int previousDate, InsertCalendarCallback insertCalendarCallback);

    /**
     * 删除日历
     *
     * @param context
     * @param title
     */
    void deleteCalendarEvent(Context context, String title);
}
