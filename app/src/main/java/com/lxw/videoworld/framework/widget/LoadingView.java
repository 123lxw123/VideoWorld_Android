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
 * Created by lxw9047 on 2017/5/29.
 */

public class LoadingView extends BaseLoadingView {

    protected int count;

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
        if (StringUtil.isEmpty(titleText)) {
            titleText = "";
            marginSpace = 0;
        }

        // 启动动画
        new Thread() {
            public void run() {
                while (true) {
                    count++;
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
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if(StringUtil.isNotEmpty(titleText)){
            paint.getTextBounds(titleText + "...", 0, (titleText + "...").length(), textBound);
            if (textBound.width() > width - getPaddingLeft() - getPaddingRight()) {
                TextPaint textPaint = new TextPaint(paint);
                String msg = TextUtils.ellipsize((titleText + "..."), textPaint, (float) width - getPaddingLeft() - getPaddingRight(),
                        TextUtils.TruncateAt.END).toString();
                String string = titleText.substring(0, msg.length() - 3) + "...";
                paint.getTextBounds(string, 0, string.length(), textBound);
            }else{
                String string = titleText + "...";
                paint.getTextBounds(string, 0, string.length(), textBound);
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 绘制标题
        paint.setColor(titleTextColor);
        paint.setStyle(Paint.Style.FILL);
        /**
         * 当前设置的宽度小于字体需要的宽度，将字体改为xxx...
         */
        if (StringUtil.isNotEmpty(titleText)) {
            //正常情况，将字体居中
            int unit = 1000 / progressSleep;
            int flag = count % (unit * 2);
            String string = titleText;
            if (flag < unit / 2) {
                string = titleText;
            } else if (flag >= unit / 2 && flag < unit) {
                string = titleText + ".";
            } else if (flag >= unit && flag < unit * 3 / 2) {
                string = titleText + "..";
            }else if(flag >= unit * 3 / 2 && flag < unit * 2){
                string = titleText + "...";
            }
            canvas.drawText(string, width / 2 - textBound.width() * 1.0f / 2, height - getPaddingBottom() - progressHeight - marginSpace, paint);
        }
        // 绘制加载条
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
