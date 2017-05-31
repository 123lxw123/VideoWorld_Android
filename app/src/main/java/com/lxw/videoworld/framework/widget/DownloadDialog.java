package com.lxw.videoworld.framework.widget;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import com.lxw.videoworld.R;
import com.lxw.videoworld.framework.application.BaseApplication;

/**
 * Created by Zion on 2017/5/30.
 */

public class DownloadDialog extends BaseLoadingDialog{
    protected Context context;
    protected DownloadView downloadView;

    public DownloadDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(getContext(), R.layout.layout_download, null);
        downloadView = (DownloadView) view.findViewById(R.id.download);
        setContentView(view);
    }

    public void updateProgress(int progress){
        downloadView.updateProgress(progress);
    }
}