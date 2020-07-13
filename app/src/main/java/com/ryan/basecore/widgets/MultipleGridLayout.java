package com.ryan.basecore.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;

import com.ryan.basecore.R;

import java.util.ArrayList;
import java.util.List;


/**
 * @Author: Ryan
 * @Date: 2020/7/14 15:21
 * @Description: 多张图片展示，仿朋友圈布局
 */
public abstract class MultipleGridLayout extends ViewGroup implements IMultipleGridLayout {

    private static final float DEFAULT_SPACING = 5f;
    private static final int MAX_IMAGE_COUNT = 9;//只能最大展示九张图片

    protected Context mContext;
    private float mSpacing = DEFAULT_SPACING;
    private int mColumns;
    private int mRows;
    private int mTotalWidth;

    private int singleImageWidth;//单张图片的宽度
    private int singleImageHeight;//单张图片的高度
    private int onlySingleImageW;//只有一张图片时图片的自定义宽度
    private int onlySingleImageH;//只有一张图片时图片的自定义高度
    private boolean mIsFirst = true;
    private List<String> mUrlList = new ArrayList<>();
    private int color = Color.WHITE;
    private int textSize = 18;

    public MultipleGridLayout(Context context) {
        super(context);
        init(context);
    }

    public MultipleGridLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MultipleGridLayout);
        mSpacing = typedArray.getDimension(R.styleable.MultipleGridLayout_spacing, DEFAULT_SPACING);
        typedArray.recycle();
        init(context);
    }

    /**
     * 初始化，如果数据源是空的 ，就不显示view
     *
     * @param context
     */
    private void init(Context context) {
        mContext = context;
        if (getListSize(mUrlList) == 0 || getVisibility() == GONE) {
            setVisibility(GONE);
        }
    }

    /**
     * 设置多余图片的数字样式
     *
     * @param color
     * @param textSize
     */
    public void setExtraNumStyle(@ColorInt int color, int textSize) {
        this.color = color;
        this.textSize = textSize;
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public int px2dip(float px) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureChildViewRanks(getListSize(mUrlList));
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecSize;
        if (widthSpecMode == MeasureSpec.EXACTLY) {//xml里布局的宽度 不要设置成wrap content
            mTotalWidth = widthSpecSize - getPaddingLeft() - getPaddingRight();
            singleImageWidth = (int) ((mTotalWidth - mSpacing * (mColumns - 1)) / mColumns);
            singleImageHeight = singleImageWidth;//根据view的宽度，子view设置成正方形
            if (getListSize(mUrlList) == 1) {
                setMeasuredDimension(onlySingleImageW, onlySingleImageH);
            } else {
                widthSpecSize = (int) (singleImageWidth * mColumns + mSpacing * (mColumns - 1)) + getPaddingLeft() + getPaddingRight();
                heightSpecSize = (int) (singleImageHeight * mRows + mSpacing * (mRows - 1) + getPaddingTop() + getPaddingBottom());
                setMeasuredDimension(widthSpecSize, heightSpecSize);
            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (mIsFirst) {
            notifyDataSetChanged();
            mIsFirst = false;
        }
    }


    @Override
    public ShadeRatioImageView createImageView(final int i, final String url) {
        ShadeRatioImageView imageView = new ShadeRatioImageView(mContext);
        imageView.setLayoutParams(new LinearLayout.LayoutParams(singleImageWidth, singleImageHeight));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickImage(i, url, mUrlList);
            }
        });
        return imageView;
    }

    @Override
    public void layoutChildView(ShadeRatioImageView imageView, final int i, String url, boolean isShowExtraNum) {
        int[] position = findPosition(i);
        int left = (int) ((singleImageWidth + mSpacing) * position[1]) + getPaddingLeft();
        int top = (int) ((singleImageHeight + mSpacing) * position[0]) + getPaddingTop();
        int right = left + singleImageWidth;
        int bottom = top + singleImageHeight;
        imageView.layout(left, top, right, bottom);
        addView(imageView);
        //显示超过9张的图片数量
        if (isShowExtraNum && i == MAX_IMAGE_COUNT - 1) {
            int overCount = getListSize(mUrlList) - MAX_IMAGE_COUNT;
            if (overCount > 0) {
                final TextView textView = new TextView(mContext);
                textView.setText("+" + overCount);
                textView.setTextColor(color);
                textView.setPadding(0, singleImageHeight / 2 - getFontHeight(textSize), 0, 0);
                textView.setTextSize(textSize);
                textView.setGravity(Gravity.CENTER);
                textView.setBackgroundColor(Color.BLACK);
                textView.getBackground().setAlpha(100);
                textView.layout(left, top, right, bottom);
                addView(textView);
                //点击更多的
                imageView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onClickMore(i, mUrlList.get(i), mUrlList);
                    }
                });
            }
        }
        displayImages(imageView, url);
    }

    @Override
    public void setData(List<String> urlList) {
        if (getListSize(urlList) == 0) {
            setVisibility(GONE);
            return;
        }
        setVisibility(VISIBLE);
        mUrlList.clear();
        mUrlList.addAll(urlList);
        if (!mIsFirst) {
            notifyDataSetChanged();
        }
    }

    @Override
    public void setSpacing(float spacing) {
        mSpacing = spacing;
    }

    @Override
    public void notifyDataSetChanged() {
        /*
         数据刷新在很多地方都调用到，考虑到外部调用的时候或者设置数据源的时候，可能不是在主线程，比如设置数据源的时候是在子线程里面 所以这里直接调用post方法
         */
        post(new Runnable() {
            @Override
            public void run() {
                refreshViews();
            }
        });
    }


    /**
     * 刷新view
     */
    private void refreshViews() {
        removeAllViews();
        int size = getListSize(mUrlList);
        if (size > 0) {
            setVisibility(VISIBLE);
        } else {
            setVisibility(GONE);
        }
        if (size == 1) {
            String url = mUrlList.get(0);
            ShadeRatioImageView imageView = createImageView(0, url);
            //避免在ListView中一张图未加载成功时，布局高度受其他item影响
            LayoutParams params = getLayoutParams();
            params.height = singleImageHeight;
            setLayoutParams(params);
            imageView.layout(0, 0, singleImageWidth, singleImageWidth);
            boolean isShowDefault = displaySingleImage(imageView, url, mTotalWidth);
            if (isShowDefault) {
                layoutChildView(imageView, 0, url, false);
            } else {
                addView(imageView);
            }
            return;
        }

        for (int i = 0; i < size; i++) {
            String url = mUrlList.get(i);
            ShadeRatioImageView imageView;
            //最多显示九张,多余的用数字表示
            if (i < MAX_IMAGE_COUNT - 1) {
                imageView = createImageView(i, url);
                layoutChildView(imageView, i, url, false);
            } else {
                if (i == MAX_IMAGE_COUNT - 1) {
                    imageView = createImageView(i, url);
                    layoutChildView(imageView, i, url, true);
                    break;
                }
            }
        }
    }

    /**
     * 确定每个图片的行数和列数
     *
     * @param childNum
     * @return
     */
    private int[] findPosition(int childNum) {
        int[] position = new int[2];
        for (int i = 0; i < mRows; i++) {
            for (int j = 0; j < mColumns; j++) {
                if ((i * mColumns + j) == childNum) {
                    position[0] = i;//行
                    position[1] = j;//列
                    break;
                }
            }
        }
        return position;
    }

    /**
     * 当只有一张图片时，设置其大小
     *
     * @param imageView
     * @param width
     * @param height
     */
    protected void setSingleImageLayoutParams(ShadeRatioImageView imageView, int width, int height) {
        imageView.setLayoutParams(new LayoutParams(width, height));
        imageView.layout(0, 0, width, height);
        onlySingleImageH = height;
        onlySingleImageW = width;
    }

    /**
     * 根据图片数量确定行列数量
     *
     * @param size
     */
    private void measureChildViewRanks(int size) {
        if (size <= 3) {
            mRows = 1;
            mColumns = size;
        } else if (size <= 6) {
            mRows = 2;
            mColumns = 3;
            if (size == 4) {
                mColumns = 2;
            }
        } else {
            mColumns = 3;
            mRows = 3;
        }
    }

    /**
     * 获取List的大小
     *
     * @param list
     * @return
     */
    private int getListSize(List<String> list) {
        if (list == null || list.size() == 0) {
            return 0;
        }
        return list.size();
    }

    /**
     * 根据字体大小，获取字体高度
     *
     * @param fontSize
     * @return
     */
    private int getFontHeight(float fontSize) {
        Paint paint = new Paint();
        paint.setTextSize(fontSize);
        Paint.FontMetrics fm = paint.getFontMetrics();
        return (int) Math.ceil(fm.descent - fm.ascent);
    }

    /**
     * @param imageView
     * @param url
     * @param parentWidth 父控件宽度 当只有一张图片的时候让它占满父控件
     * @return true 代表按照九宫格默认大小显示，false 代表按照自定义宽高显示
     */
    protected abstract boolean displaySingleImage(ShadeRatioImageView imageView, String url, int parentWidth);

    protected abstract void displayImages(ShadeRatioImageView imageView, String url);

    protected abstract void onClickImage(int position, String url, List<String> urlList);

    protected abstract void onClickMore(int position, String url, List<String> urlList);
}
