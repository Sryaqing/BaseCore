package com.ryan.sdkj_core.voice;

import android.content.Context;

import androidx.annotation.NonNull;

/**
 * @Author: Ryan
 * @Date: 2020/7/6 16:10
 * @Description: module基础功能
 */
public interface ITextToVoice {

    /**
     * 初始化标志:默认0，成功1，失败-1，播放前拿返回值做判断，可以实现自己的逻辑了
     */
    void initRyanVoice(@NonNull Context context);

    /**
     * 语音播放
     */
    void playVoice(@NonNull String text);

    /**
     * 停止播放
     */
    void stopVoice();

}
