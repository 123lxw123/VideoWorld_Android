package com.lxw.videoworld.framework.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.KeyEvent;
import android.view.View;

import com.lxw.videoworld.R;

/**
 * Created by Zion on 2017/5/30.
 */

public class DownloadDialog extends Dialog{
    protected Context context;
    protected DownloadView downloadView;

    public DownloadDialog(@NonNull Context context) {
        this(context, true);
    }

    public DownloadDialog(@NonNull Context context, boolean flag_cancel) {
        super(context, R.style.TransDialog);
        this.context = context;
        if(!flag_cancel){
            OnKeyListener keylistener = new OnKeyListener(){
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                        return true;
                    }
                    else {
                        return false;
                    }
                }
            } ;
            this.setCanceledOnTouchOutside(false);
            this.setCancelable(false);
            this.setOnKeyListener(keylistener);
        }
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