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

public class LoadingView extends View {
    private int width;
    private int height;
    private static final int minWidth = 100;
    private Paint paint;// 画笔
    private Rect textBound;
    private int progressCount;
    private int progressTop;
    private int progressBottom;
    // 自定义属性
    private String titleText;// 标题文案
    private int titleTextSize;// 标题字体大小
    private int titleTextColor;// 标题字体颜色
    private int progressColor;// 加载条颜色
    private int progressBackground;// 加载条背景色
    private int progressWidth;// 加载条每段的宽度
    private int progressHeight;// 加载条的高度
    private int progressSpeed;// 加载速度
    private int progressSleep;// 加载间隔时间
    private int marginSpace;// 加载条与标题的间距
    // 自定义属性默认值
    private static final int TITLE_TEXT_SIZE = 16;
    private static final int TITLE_TEXT_COLOR = Color.GRAY;
    private static final int PROGRESS_COLOR = Color.BLUE;
    private static final int PROGRESS_BACKGROUND_COLOR = Color.GRAY;
    private static final int PROGRESS_WIDTH = 50;
    private static final int PROGRESS_HEIGHT = 10;
    private static final int MARGIN_SPACE = 10;
    private static final int PROGRESS_SLEEP = 20;
    private static final int PROGRESS_SPEED = 1;

    public LoadingView(Context context) {
        this(context, null);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
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
        if (StringUtil.isEmpty(titleText)) {
            titleText = "";
            marginSpace = 0;
        }
        // 加载条每段宽度不能超过或等于加载条宽度
        if (progressWidth >= width) {
            progressWidth = width / 2;
        }
        paint = new Paint();
        textBound = new Rect();
        paint.setTextSize(titleTextSize);
        // 计算了描绘字体需要的范围
        paint.getTextBounds(titleText, 0, titleText.length(), textBound);
        // 启动动画
        new Thread() {
            public void run() {
                while (true) {
                    progressCount++;
                    postInvalidate();
                    try {
                        Thread.sleep(progressSleep);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
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

    }

    @Override
    protected void onDraw(Canvas canvas) {
        // 绘制标题
        paint.setColor(titleTextColor);
        paint.setStyle(Paint.Style.FILL);
        /**
         * 当前设置的宽度小于字体需要的宽度，将字体改为xxx...
         */
        if (textBound.width() > width) {
            TextPaint textPaint = new TextPaint(paint);
            String msg = TextUtils.ellipsize(titleText, textPaint, (float) width - getPaddingLeft() - getPaddingRight(),
                    TextUtils.TruncateAt.END).toString();
            canvas.drawText(msg, getPaddingLeft(), height - getPaddingBottom() - progressHeight - marginSpace, paint);
        } else {
            //正常情况，将字体居中
            canvas.drawText(titleText, width / 2 - textBound.width() * 1.0f / 2, height - getPaddingBottom() - progressHeight - marginSpace, paint);
        }

        // 绘制加载条背景
        paint.setColor(progressBackground);
        progressTop = height - getPaddingBottom() - progressHeight;
        progressBottom = height - getPaddingBottom();
        canvas.drawRect(getPaddingLeft(), progressTop, width - getPaddingRight(), progressBottom, paint);
        paint.setColor(progressColor);
        int distance = progressCount * progressSpeed;
        if (distance <= progressWidth) {
            canvas.drawRect(getPaddingLeft(), progressTop, distance + getPaddingLeft(), progressBottom, paint);
        } else if (distance > progressWidth && distance <= width - getPaddingLeft() - getPaddingRight()) {
            canvas.drawRect(distance - progressWidth + getPaddingLeft(), progressTop, distance + getPaddingLeft(), progressBottom, paint);
        } else if (distance > width - getPaddingLeft() - getPaddingRight() && distance <= width - getPaddingLeft() - getPaddingRight() + progressWidth) {
            canvas.drawRect(distance - progressWidth + getPaddingLeft(), progressTop, width - getPaddingRight(), progressBottom, paint);
        } else {
            progressCount = 1;
            canvas.drawRect(getPaddingLeft(), progressTop, progressSpeed, progressBottom, paint);
        }
    }
}
