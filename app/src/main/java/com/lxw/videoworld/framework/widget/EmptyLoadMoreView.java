package com.lxw.videoworld.framework.widget;

import com.chad.library.adapter.base.loadmore.LoadMoreView;
import com.lxw.videoworld.R;

/**
 * 加载更多 空布局
 * Created by lxw9047 on 2017/6/12.
 */

public final class EmptyLoadMoreView extends LoadMoreView {

    @Override public int getLayoutId() {
        return R.layout.layout_empty;
    }

    /**
     * 如果返回true，数据全部加载完毕后会隐藏加载更多
     * 如果返回false，数据全部加载完毕后会显示getLoadEndViewId()布局
     */
    @Override public boolean isLoadEndGone() {
        return true;
    }

    @Override protected int getLoadingViewId() {
        return R.id.view_empty;
    }

    @Override protected int getLoadFailViewId() {
        return R.id.view_empty;
    }

    /**
     * isLoadEndGone()为true，可以返回0
     * isLoadEndGone()为false，不能返回0
     */
    @Override protected int getLoadEndViewId() {
        return 0;
    }
}
