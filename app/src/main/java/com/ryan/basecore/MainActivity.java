package com.ryan.basecore;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mob.pushsdk.MobPush;
import com.mob.pushsdk.MobPushCustomMessage;
import com.mob.pushsdk.MobPushNotifyMessage;
import com.mob.pushsdk.MobPushReceiver;
import com.ryan.basecore.utils.QMUIStatusBarHelper;
import com.ryan.basecore.utils.StatusBarUtil;
import com.ryan.basecore.utils.permission.PermissionsUtil;
import com.ryan.basecore.utils.permission.PermissionsUtils;
import com.ryan.sdkj_core.ocr.ZxingScanActivity;
import com.ryan.sdkj_core.remind.InsertCalendarCallback;
import com.ryan.sdkj_core.remind.RyanCalendar;
import com.ryan.sdkj_core.voice.RyanVoice;


/**
 * @Author: Ryan
 * @Date: 2020/5/6 15:30
 * @Description: Main
 */
public class MainActivity extends AppCompatActivity {
    TextView tv_translation;
    TextView tv_remind;
    TextView tv_scan;
    EditText et_text;
    private MobPushReceiver mobPushReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // QMUIStatusBarHelper.translucent(this, Color.YELLOW);
        QMUIStatusBarHelper.setStatusBarLightMode(this);
        StatusBarUtil.setColor(this, getResources().getColor(R.color.themeYellow));
        init();
    }

    private void init() {
        tv_translation = findViewById(R.id.tv_translation);
        et_text = findViewById(R.id.et_text);
        tv_remind = findViewById(R.id.tv_remind);
        tv_scan = findViewById(R.id.tv_scan);
        //语音引擎初始化
        RyanVoice.getInstance().initRyanVoice(getApplicationContext());

        tv_translation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (RyanVoice.initRyanVoiceResult == 1)
                    RyanVoice.getInstance().playVoice(et_text.getText().toString().trim());
                else
                    Toast.makeText(v.getContext(), et_text.getText().toString().trim(), Toast.LENGTH_LONG).show();
            }
        });

        tv_remind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                PermissionsUtils.getInstance().chekPermissions(MainActivity.this, PermissionsUtil.PERMISSION_CALENDAR, new PermissionsUtils.IPermissionsResult() {
                    @Override
                    public void passPermissons() {
                        RyanCalendar.getInstance().addCalendarEvent(getApplicationContext(),
                                "提醒任务测试", "提醒任务测试，提醒任务测试，提醒任务测试，提醒任务测试，提醒任务测试",
                                System.currentTimeMillis() + 5 * 60 * 1000, 0, new InsertCalendarCallback() {
                                    @Override
                                    public void insertCalendarSuccess() {
                                        Toast.makeText(v.getContext(), "添加提醒成功", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void insertCalendarFailure() {
                                        Toast.makeText(v.getContext(), "添加提醒失败", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }

                    @Override
                    public void forbitPermissons() {
                        Toast.makeText(v.getContext(), "请打开相关权限，避免App功能异常", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        tv_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PermissionsUtils.getInstance().chekPermissions(MainActivity.this, PermissionsUtil.PERMISSION_CAMERA_WRITE, new PermissionsUtils.IPermissionsResult() {
                    @Override
                    public void passPermissons() {
                        startActivity(new Intent(MainActivity.this, ZxingScanActivity.class));
                        // ZxingScanActivity.startScanActivity(MainActivity.this,false);
                    }

                    @Override
                    public void forbitPermissons() {

                    }
                });

            }
        });
        //推送消息回调监听
        mobPushReceiver = new MobPushReceiver() {
            @Override
            public void onCustomMessageReceive(Context context, MobPushCustomMessage mobPushCustomMessage) {
                //接收到自定义消息（透传消息）
                Log.e("onCustomMessageReceive", mobPushCustomMessage.getContent());
            }

            @Override
            public void onNotifyMessageReceive(Context context, MobPushNotifyMessage mobPushNotifyMessage) {
                //接收到通知消息
                Log.e("onNotifyMessageReceive", mobPushNotifyMessage.getContent());
                Toast.makeText(context, mobPushNotifyMessage.getContent(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNotifyMessageOpenedReceive(Context context, MobPushNotifyMessage mobPushNotifyMessage) {
                //通知被点击事件
                Log.e("OpenedReceive", mobPushNotifyMessage.getContent());
            }

            @Override
            public void onTagsCallback(Context context, String[] strings, int i, int i1) {
                //标签操作回调
                Log.e("onTagsCallback", strings.toString());
            }

            @Override
            public void onAliasCallback(Context context, String s, int i, int i1) {
                //别名操作回调
                Log.e("onAliasCallback", s);
            }
        };
        MobPush.addPushReceiver(mobPushReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RyanVoice.getInstance().stopVoice();
        MobPush.removePushReceiver(mobPushReceiver);
    }

}
