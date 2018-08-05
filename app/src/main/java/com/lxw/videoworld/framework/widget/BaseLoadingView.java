package com.lxw.videoworld.framework.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import com.lxw.videoworld.R;
import com.lxw.videoworld.framework.util.StringUtil;


/**
 * Created by lxw9047 on 2017/5/29.
 */

public class BaseLoadingView extends View {
    protected int width;
    protected int height;
    protected static final int minWidth = 100;
    protected Paint paint;// 画笔
    protected Rect textBound;
    protected int progressCount;
    protected int progressTop;
    protected int progressBottom;
    // 自定义属性
    protected String titleText;// 标题文案
    protected int titleTextSize;// 标题字体大小
    protected int titleTextColor;// 标题字体颜色
    protected int progressColor;// 加载条颜色
    protected int progressBackground;// 加载条背景色
    protected int progressWidth;// 加载条每段的宽度
    protected int progressHeight;// 加载条的高度
    protected int progressSpeed;// 加载速度
    protected int progressSleep;// 加载间隔时间
    protected int marginSpace;// 加载条与标题的间距
    // 自定义属性默认值
    protected static final int TITLE_TEXT_SIZE = 14;
    protected static final int TITLE_TEXT_COLOR = Color.GRAY;
    protected static final int PROGRESS_COLOR = Color.BLUE;
    protected static final int PROGRESS_BACKGROUND_COLOR = Color.GRAY;
    protected static final int PROGRESS_WIDTH = 50;
    protected static final int PROGRESS_HEIGHT = 3;
    protected static final int MARGIN_SPACE = 10;
    protected static final int PROGRESS_SLEEP = 20;
    protected static final int PROGRESS_SPEED = 5;

    public BaseLoadingView(Context context) {
        this(context, null);
    }

    public BaseLoadingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseLoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public BaseLoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LoadingView, 0, 0);
        titleText = a.getString(R.styleable.LoadingView_titleText);
        titleTextSize = a.getDimensionPixelSize(R.styleable.LoadingView_titleTextSize, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                TITLE_TEXT_SIZE, getResources().getDisplayMetrics()));
        titleTextColor = a.getColor(R.styleable.LoadingView_titleTextColor, TITLE_TEXT_COLOR);
        progressColor = a.getColor(R.styleable.LoadingView_progressColor, PROGRESS_COLOR);
        progressBackground = a.getColor(R.styleable.LoadingView_progressBackground, PROGRESS_BACKGROUND_COLOR);
        progressWidth = a.getDimensionPixelSize(R.styleable.LoadingView_progressWidth, (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, PROGRESS_WIDTH, getResources().getDisplayMetrics()));
        progressHeight = a.getDimensionPixelSize(R.styleable.LoadingView_progressHeight, (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, PROGRESS_HEIGHT, getResources().getDisplayMetrics()));
        marginSpace = a.getDimensionPixelSize(R.styleable.LoadingView_marginSpace, (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, MARGIN_SPACE, getResources().getDisplayMetrics()));
        progressSpeed = a.getInt(R.styleable.LoadingView_progressSpeed, PROGRESS_SPEED);
        progressSleep = a.getInt(R.styleable.LoadingView_progressSleep, PROGRESS_SLEEP);
        a.recycle();
        paint = new Paint();
        textBound = new Rect();
        paint.setTextSize(titleTextSize);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        /**
         * 设置宽度
         */
        int specMode = MeasureSpec.getMode(widthMeasureSpec);
        int specSize = MeasureSpec.getSize(widthMeasureSpec);

        if (specMode == MeasureSpec.EXACTLY)// match_parent , accurate
        {
            Log.e("xxx", "EXACTLY");
            width = specSize;
        } else {
            // 由字体决定的宽
            int desireByTitle = getPaddingLeft() + getPaddingRight() + textBound.width();

            if (specMode == MeasureSpec.AT_MOST)// wrap_content
            {
                int desire = Math.max(minWidth, desireByTitle);
                width = Math.min(desire, specSize);
                Log.e("xxx", "AT_MOST");
            }
        }

        /***
         * 设置高度
         */

        specMode = MeasureSpec.getMode(heightMeasureSpec);
        specSize = MeasureSpec.getSize(heightMeasureSpec);
        if (specMode == MeasureSpec.EXACTLY)// match_parent , accurate
        {
            height = specSize;
        } else {
            int desire = getPaddingTop() + getPaddingBottom() + progressHeight + textBound.height() + marginSpace;
            if (specMode == MeasureSpec.AT_MOST)// wrap_content
            {
                height = Math.min(desire, specSize);
            }
        }
        setMeasuredDimension(width, height);
        // 如果加载条每段长度过长，则设置为默认值
        if(progressWidth >= width - getPaddingRight() - getPaddingLeft()){
            progressWidth = PROGRESS_WIDTH;
        }
        // 计算了描绘字体需要的范围
        paint.getTextBounds(titleText, 0, titleText.length(), textBound);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        // 绘制加载条背景
        paint.setColor(progressBackground);
        progressTop = height - getPaddingBottom() - progressHeight;
        progressBottom = height - getPaddingBottom();
        canvas.drawRect(getPaddingLeft(), progressTop, width - getPaddingRight(), progressBottom, paint);
    }
}
