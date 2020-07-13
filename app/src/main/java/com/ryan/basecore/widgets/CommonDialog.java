package com.ryan.basecore.widgets;


import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentManager;

import com.ryan.basecore.R;

import java.lang.ref.WeakReference;
import java.util.Objects;

/**
 * @Author: Ryan
 * @Date: 2020/4/17 10:48
 * @Description: 常规弹窗，标题  内容 确认 取消
 */
public class CommonDialog extends BaseDialogFragment implements View.OnClickListener {
    private static WeakReference<CommonDialog> commonDialogWeakReference;
    private TextView tv_title;
    private TextView tv_content;
    private TextView tv_sure;
    private TextView tv_cancel;
    private static String title_msg;
    private static String content_msg;
    private static CallBack callBack;

    public static void showDialog(@NonNull FragmentManager fragmentManager, String title, String content, @NonNull CallBack back) {
        callBack = back;
        title_msg = title;
        content_msg = content;
        if (commonDialogWeakReference == null)
            commonDialogWeakReference = new WeakReference<>(new CommonDialog());
        Bundle bundle = new Bundle();
        bundle.putString("message", "test");
        commonDialogWeakReference.get().setArguments(bundle);
        if (!commonDialogWeakReference.get().isAdded())
            commonDialogWeakReference.get().show(fragmentManager, CommonDialog.class.getName());
    }

    private void init() {
        tv_title.setOnClickListener(this);
        tv_content.setOnClickListener(this);
        tv_sure.setOnClickListener(this);
        tv_cancel.setOnClickListener(this);

        tv_title.setText(title_msg);
        tv_content.setText(content_msg);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onStart() {
        super.onStart();
        Window window = null;
        window = Objects.requireNonNull(getDialog()).getWindow();
        WindowManager.LayoutParams params = Objects.requireNonNull(window).getAttributes();
        params.gravity = Gravity.CENTER;
        params.width = 800;
        params.height = params.width / 4 * 3;
        //   params.windowAnimations = R.style.dialog_anim_all_style;
        window.setAttributes(params);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Objects.requireNonNull(getDialog()).requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.dialog_common, container, false);
        tv_title = view.findViewById(R.id.tv_title);
        tv_content = view.findViewById(R.id.tv_content);
        tv_sure = view.findViewById(R.id.tv_sure);
        tv_cancel = view.findViewById(R.id.tv_cancel);
        init();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (commonDialogWeakReference.get() != null) {
            commonDialogWeakReference.get().dismiss();
            commonDialogWeakReference = null;
            callBack = null;
//            try {//利用反射机制获取到DialogFragment的mHandler对象 再清空消息队列 避免其引起的内存泄漏
//                Class dialog = Class.forName("com.ryan.sdkj.widget.dialog.BaseDialogFragment");
//                Field mHandler = dialog.getDeclaredField("mHandler");
//                mHandler.setAccessible(true);//允许访问私有对象
//                Class handlerClass = Class.forName(mHandler.getType().getName());
//                Method handlerMethod = handlerClass.getDeclaredMethod("removeCallbacksAndMessages", Object.class);
//                handlerMethod.setAccessible(true);
//                handlerMethod.invoke(mHandler.get(this), (Object) null);//指明是哪个对象调用removeCallbacksAndMessages的方法
//                LogUtil.e("This dialog has destroyed!");
//            } catch (ClassNotFoundException | NoSuchFieldException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
//                ALog.e(e.toString());
//            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_cancel:
                commonDialogWeakReference.get().dismiss();
                callBack.failCall();
                break;
            case R.id.tv_sure:
                commonDialogWeakReference.get().dismiss();
                callBack.sureCall();
                break;
        }
    }

    public interface CallBack {
        void sureCall();

        void failCall();
    }
}