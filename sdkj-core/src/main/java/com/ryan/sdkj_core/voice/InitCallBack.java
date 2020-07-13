package com.ryan.sdkj_core.voice;


/**
 * @Author: Ryan
 * @Date: 2020/7/6 16:46
 * @Description: voice初始化回调
 */
public interface InitCallBack {
    /**
     * 初始化成功
     */
    void successInit (int result);

    /**
     * 初始化失败
     */
    void failureInit(int result);

}
