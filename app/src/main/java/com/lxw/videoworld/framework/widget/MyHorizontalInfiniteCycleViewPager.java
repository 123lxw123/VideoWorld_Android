package com.lxw.videoworld.framework.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.gigamole.infinitecycleviewpager.HorizontalInfiniteCycleViewPager;

/**
 * Created by lxw9047 on 2017/6/15.
 */

public class MyHorizontalInfiniteCycleViewPager extends HorizontalInfiniteCycleViewPager {
    public MyHorizontalInfiniteCycleViewPager(Context context) {
        super(context);
    }

    public MyHorizontalInfiniteCycleViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        if (visibility == View.VISIBLE){
            //开始某些任务
            MyHorizontalInfiniteCycleViewPager.this.startAutoScroll(true);
        }
        else if(visibility == INVISIBLE || visibility == GONE){
            //停止某些任务
            MyHorizontalInfiniteCycleViewPager.this.stopAutoScroll();
        }
    }
}
