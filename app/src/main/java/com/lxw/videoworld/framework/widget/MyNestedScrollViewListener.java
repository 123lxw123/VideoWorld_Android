package com.lxw.videoworld.framework.widget;

/**
 * Created by lxw9047 on 2016/11/24.
 */

public abstract class MyNestedScrollViewListener {
    /**
     * onMyScrollChanged : 当滚动时
     * onMyScrollStart : 当开始滚动时
     * onMyScrollStop : 当滚动停止时
     * onMyScrollTop : 当滚动到到顶部时
     * onMyScrollBottom : 当滚动到底部时
     * onMyScrollUp : 当向上滚动时
     * onMyScrollDown : 当向下滚动时
     */
    public void onMyNestedScrollChanged(MyNestedScrollView scrollView, int x, int y, int oldx, int oldy) {
    }

    public void onMyNestedScrollStart(MyNestedScrollView scrollView, int x, int y, int oldx, int oldy) {
    }

    public void onMyNestedScrollStop(MyNestedScrollView scrollView, int x, int y, int oldx, int oldy) {
    }

    public void onMyNestedScrollTop(MyNestedScrollView scrollView, int x, int y, int oldx, int oldy) {
    }

    public void onMyNestedScrollBottom(MyNestedScrollView scrollView, int x, int y, int oldx, int oldy) {
    }

    public void onMyNestedScrollUp(MyNestedScrollView scrollView, int x, int y, int oldx, int oldy) {
    }

    public void onMyNestedScrollDown(MyNestedScrollView scrollView, int x, int y, int oldx, int oldy) {
    }
}
