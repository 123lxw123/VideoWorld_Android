package com.lxw.videoworld.framework.base;

import android.content.Context;

import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zion on 2016/11/16.
 */

public class BaseMultiItemTypeAdapter<T> extends MultiItemTypeAdapter<T> {

    public BaseMultiItemTypeAdapter(Context context, List<T> datas) {
        super(context, datas);
    }

    public void setData(List<T> datas){
        this.mDatas = new ArrayList<>(datas);
    }

    public List<T> getData(){
        return this.mDatas;
    }
}
