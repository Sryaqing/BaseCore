package com.ryan.sdkj_core.voice;

import android.speech.tts.TextToSpeech;


/**
 * @Author: Ryan
 * @Date: 2020/7/6 16:35
 * @Description: 检查语音初始化
 */
public abstract class CheckVoiceInit implements TextToSpeech.OnInitListener {
    private InitCallBack initCallBack;
    public CheckVoiceInit checkVoiceInit = this;

    /**
     * 检查是否支持中文
     *
     * @return
     */
    protected abstract int isSupportChinese();

    public InitCallBack getInitCallBack() {
        return initCallBack;
    }

    public void setInitCallBack(InitCallBack initCallBack) {
        this.initCallBack = initCallBack;
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS && isSupportChinese() != TextToSpeech.LANG_MISSING_DATA) {
            initCallBack.successInit(status);
        } else {
            initCallBack.failureInit(status);
        }
    }
}
