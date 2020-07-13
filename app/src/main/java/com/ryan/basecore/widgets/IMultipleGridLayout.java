package com.ryan.basecore.widgets;

import java.util.List;

/**
 * @Author: Ryan
 * @Date: 2020/7/20 11:38
 * @Description: 多图展示View 基础功能
 */
public interface IMultipleGridLayout {


    /**
     * 生成子view
     *
     * @param i
     * @param url
     * @return
     */
    ShadeRatioImageView createImageView(final int i, final String url);

    /**
     * 布局子view
     *
     * @param imageView
     * @param i
     * @param url
     * @param isShowExtraNum
     */
    void layoutChildView(ShadeRatioImageView imageView, int i, String url, boolean isShowExtraNum);


    /**
     * 设置图片间隔
     *
     * @param spacing
     */
    void setSpacing(float spacing);

    /**
     * 设置数据源
     *
     * @param urlList
     */
    void setData(List<String> urlList);


    /**
     * 刷新数据
     */
    void notifyDataSetChanged();
}
