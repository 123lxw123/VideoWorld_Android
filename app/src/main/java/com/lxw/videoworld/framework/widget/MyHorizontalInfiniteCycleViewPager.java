package com.lxw.videoworld.framework.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.gigamole.infinitecycleviewpager.HorizontalInfiniteCycleViewPager;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by lxw9047 on 2017/6/15.
 */

public class MyHorizontalInfiniteCycleViewPager extends HorizontalInfiniteCycleViewPager {

    private Context context;
    private Timer timer;
    private final int TIME_SCROLL_DURATION = 3000;// 翻转周期

    public MyHorizontalInfiniteCycleViewPager(Context context) {
        super(context);
    }

    public MyHorizontalInfiniteCycleViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        if (visibility == View.VISIBLE) {
            //开始某些任务
            startAutoScroll(true);
        } else if (visibility == INVISIBLE || visibility == GONE) {
            //停止某些任务
            stopAutoScroll();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                stopAutoScroll();
                break;
            case MotionEvent.ACTION_UP:
                startAutoScroll(true);
                break;
        }
        return super.onTouchEvent(ev);
    }

    @Override
    public void stopAutoScroll() {
//        super.stopAutoScroll();
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    @Override
    public void startAutoScroll(final boolean isAutoScrollPositive) {
//        super.startAutoScroll(isAutoScrollPositive);
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                MyHorizontalInfiniteCycleViewPager.this.post(new Runnable() {
                    @Override
                    public void run() {
                        MyHorizontalInfiniteCycleViewPager.this.getmInfiniteCycleManager().getmViewPageable().setCurrentItem(MyHorizontalInfiniteCycleViewPager.this.getmInfiniteCycleManager().getRealItem() + +(isAutoScrollPositive ? 1 : -1));
                    }
                });
            }
        }, TIME_SCROLL_DURATION, TIME_SCROLL_DURATION);
    }
}
