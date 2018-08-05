package com.lxw.videoworld.framework.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.lxw.videoworld.framework.util.StringUtil;

/**
 * Created by lxw9047 on 2017/5/31.
 */

public class DownloadView extends BaseLoadingView {

    protected int progress;

    public DownloadView(Context context) {
        this(context, null);
    }

    public DownloadView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DownloadView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public DownloadView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        if (StringUtil.isEmpty(titleText)) {
            titleText = "0";
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (StringUtil.isNotEmpty(titleText) && textBound.width() > width - getPaddingLeft() - getPaddingRight()) {
            TextPaint textPaint = new TextPaint(paint);
            String msg = TextUtils.ellipsize(titleText, textPaint, (float) width - getPaddingLeft() - getPaddingRight(),
                    TextUtils.TruncateAt.END).toString();
            titleText = msg;
            // 计算了描绘字体需要的范围
            paint.getTextBounds(titleText, 0, titleText.length(), textBound);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 绘制标题
        paint.setColor(titleTextColor);
        paint.setStyle(Paint.Style.FILL);
        //正常情况，将字体居中
        canvas.drawText(titleText, width / 2 - textBound.width() * 1.0f / 2, height - getPaddingBottom() - progressHeight - marginSpace, paint);
        // 绘制进度条
        paint.setColor(progressColor);
        int distance = (width - getPaddingLeft() - getPaddingRight()) * progress / 100;
        canvas.drawRect(getPaddingLeft(), progressTop, distance + getPaddingLeft(), progressBottom, paint);
    }

    public void updateProgress(int progress) {
        if (progress < 0) {
            progress = 0;
        } else if (progress > 100) {
            progress = 100;
        }
        this.progress = progress;
        if (progress == 0) {
            titleText = progress + "";
        } else {
            titleText = progress + " %";
        }
        postInvalidate();
    }
}
