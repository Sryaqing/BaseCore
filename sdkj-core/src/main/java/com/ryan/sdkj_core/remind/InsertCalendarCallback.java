package com.ryan.sdkj_core.remind;


/**
 * @Author: Ryan
 * @Date: 2020/7/7 15:05
 * @Description: java类作用描述
 */
public interface InsertCalendarCallback {
    /**
     * 添加日历成功
     */
    void insertCalendarSuccess();

    /**
     * 添加日历失败
     */
    void insertCalendarFailure();
}
