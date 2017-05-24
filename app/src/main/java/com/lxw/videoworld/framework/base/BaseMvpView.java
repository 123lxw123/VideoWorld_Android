package com.lxw.videoworld.framework.base;

import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by lxw9047 on 2016/10/24.
 */

public interface BaseMvpView extends MvpView {
    void initView();
    void prepareData();
    void rePrepareData();
}
