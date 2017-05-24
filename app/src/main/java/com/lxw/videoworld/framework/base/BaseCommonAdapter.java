package com.lxw.videoworld.framework.base;

import android.content.Context;

import com.zhy.adapter.recyclerview.CommonAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zion on 2016/11/16.
 */

public abstract class BaseCommonAdapter<T> extends CommonAdapter<T> {
    public BaseCommonAdapter(Context context, int layoutId, List<T> datas) {
        super(context, layoutId, datas);
    }

    public void setData(List<T> datas){
        this.mDatas = new ArrayList<>(datas);
    }

    public List<T> getData(){
        return this.mDatas;
    }
}
