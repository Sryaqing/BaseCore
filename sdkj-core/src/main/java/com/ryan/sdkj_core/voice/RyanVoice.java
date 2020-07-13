package com.ryan.sdkj_core.voice;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.Locale;

/**
 * @Author: Ryan
 * @Date: 2020/7/6 16:19
 * @Description: 具体功能实现
 */
public class RyanVoice extends CheckVoiceInit implements ITextToVoice {
    private TextToSpeech textToSpeech;
    private static RyanVoice ryanVoice;
    public static int initRyanVoiceResult = 0;//初始化标志:默认0，成功1，失败-1
    private int language = -1;//语言是否支持，默认-1

    public static RyanVoice getInstance() {
        if (ryanVoice == null) {
            synchronized (RyanVoice.class) {
                if (ryanVoice == null) {
                    ryanVoice = new RyanVoice();
                }
            }
        }
        return ryanVoice;
    }

    @Override
    public void initRyanVoice(@NonNull final Context context) {
        textToSpeech = new TextToSpeech(context, checkVoiceInit);
        //设置语音播报的语言
        language = textToSpeech.setLanguage(Locale.CHINESE);
        // 设置音调,值越大声音越尖（女生），值越小则变成男声,1.0是常规
        textToSpeech.setPitch(0.9f);
        // 设定语速,默认1.0正常语速
        textToSpeech.setSpeechRate(0.8f);
        //设置初始化监听
        super.setInitCallBack(new InitCallBack() {
            @Override
            public void successInit(int result) {
                initRyanVoiceResult = 1;
            }

            @Override
            public void failureInit(int result) {
                Toast.makeText(context, "语音引擎初始化失败", Toast.LENGTH_LONG).show();
                Log.e("RyanVoice", "初始化失败，错误码：" + result);
                initRyanVoiceResult = -1;
                stopVoice();
            }
        });
    }

    @Override
    public void playVoice(@NonNull String text) {
        if (textToSpeech != null && initRyanVoiceResult == 1)
            textToSpeech.speak(text, TextToSpeech.QUEUE_ADD, null);
        else
            Log.e("RyanVoice", "播放失败，当前系统版本暂不支持，错误码： " + initRyanVoiceResult);
    }

    @Override
    public void stopVoice() {
        textToSpeech.stop(); // 不管是否正在朗读TTS都被打断
        textToSpeech.shutdown(); // 关闭，释放资源
    }

    @Override
    protected int isSupportChinese() {
        return language;
    }
}
