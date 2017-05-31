package com.lxw.videoworld.framework.widget;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.lxw.videoworld.R;

/**
 * Created by Zion on 2017/5/30.
 */

public class LoadingDialog extends BaseLoadingDialog{

    public LoadingDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_loading);
    }
}