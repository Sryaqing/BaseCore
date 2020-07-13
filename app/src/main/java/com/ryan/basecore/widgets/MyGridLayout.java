package com.ryan.basecore.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;

import com.bumptech.glide.Glide;

import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;

/**
 * @Author: Ryan
 * @Date: 2020/7/16 14:20
 * @Description: java类作用描述
 */
public class MyGridLayout extends MultipleGridLayout {

    public MyGridLayout(Context context) {
        super(context);
    }

    public MyGridLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected boolean displaySingleImage(ShadeRatioImageView imageView, String url, int parentWidth) {
        setSingleImageLayoutParams(imageView, 500, 700);
        Glide.with(this).asBitmap().load(url).into(imageView);
        return false;
    }

    @Override
    protected void displayImages(ShadeRatioImageView imageView, String url) {
        Glide.with(this).asBitmap().load(url).into(imageView);
    }

    @Override
    protected void onClickImage(int position, String url, List<String> urlList) {

    }

    @Override
    protected void onClickMore(int position, String url, List<String> urlList) {
        Log.e(TAG, "onClickMore: -*-*-*-*-*-*--------------444444444044" + urlList.toString());
    }
}
