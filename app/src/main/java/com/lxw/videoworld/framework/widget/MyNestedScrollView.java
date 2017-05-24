package com.lxw.videoworld.framework.widget;

import android.content.Context;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by lxw9047 on 2016/11/24.
 */

public class MyNestedScrollView extends NestedScrollView {

    private final String TAG = this.getClass().getSimpleName();
    private MyNestedScrollViewListener myNestedScrollViewListener;

    public MyNestedScrollView(Context context) {
        super(context);
    }

    public MyNestedScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyNestedScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setMyNestedScrollViewListener(MyNestedScrollViewListener myNestedScrollViewListener) {
        this.myNestedScrollViewListener = myNestedScrollViewListener;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);

        if (myNestedScrollViewListener != null) {
            myNestedScrollViewListener.onMyNestedScrollChanged(this, l, t, oldl, oldt);

            //====================================================

            if (!scrollerTaskRunning) {
                startScrollerTask(this, l, t, oldl, oldt);
            }

            // ====================================================

            if (t - oldt > 0) {
                myNestedScrollViewListener.onMyNestedScrollDown(this, l, t, oldl, oldt);
//                Log.i(TAG, "is scrolling down");
            } else {
                myNestedScrollViewListener.onMyNestedScrollUp(this, l, t, oldl, oldt);
//                Log.i(TAG, "is scrolling up");
            }

            //====================================================

            if (getScrollY() <= 0) {
                myNestedScrollViewListener.onMyNestedScrollTop(this, l, t, oldl, oldt);
//                Log.i(TAG, "the top has beenreached");
            }

            // ====================================================

            View view = (View) getChildAt(getChildCount() - 1);// We take the last son in the scrollview
            int diff = (view.getBottom() - (getHeight() + getScrollY()));

            if (diff == 0) {
                myNestedScrollViewListener.onMyNestedScrollBottom(this, l, t, oldl, oldt);
//                Log.i(TAG, "the bottom has beenreached");
            }
        }
    }

    private Runnable scrollerTask;
    private int initialPosition;
    private int newCheck = 50;
    private boolean scrollerTaskRunning = false;

    private void startScrollerTask(final MyNestedScrollView scrollView, final int x, final int y, final int oldx, final int oldy) {
        if (!scrollerTaskRunning) {
            myNestedScrollViewListener.onMyNestedScrollStart(this, x, y, oldx, oldy);
//            Log.i(TAG, "scroll start");
        }

        scrollerTaskRunning = true;

        if (scrollerTask == null) {
            scrollerTask = new Runnable() {
                public void run() {
                    int newPosition = getScrollY();

                    if (initialPosition - newPosition == 0) {
                        if (myNestedScrollViewListener != null) {
                            scrollerTaskRunning = false;

                            myNestedScrollViewListener.onMyNestedScrollStop(scrollView, x, y, oldx, oldy);
//                            Log.i(TAG, "scroll stop");
                            return;
                        }
                    } else {
                        startScrollerTask(scrollView, x, y, oldx, oldy);
                    }
                }
            };
        }

        initialPosition = getScrollY();
        postDelayed(scrollerTask, newCheck);
    }
}
