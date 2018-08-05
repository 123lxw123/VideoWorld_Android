package com.lxw.videoworld.framework.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.KeyEvent;

import com.lxw.videoworld.R;
import com.lxw.videoworld.framework.http.HttpManager;

/**
 * Created by Zion on 2017/5/30.
 */

public class LoadingDialog extends Dialog {
    public LoadingDialog(@NonNull Context context) {
        this(context, null);
    }

    public LoadingDialog(@NonNull Context context, final HttpManager httpManager) {
        super(context, R.style.TransDialog);
        this.setCanceledOnTouchOutside(false);
        this.setCancelable(false);
        OnKeyListener keylistener = new OnKeyListener(){
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                    if(httpManager != null){
                        httpManager.cancel();
                    }
                    LoadingDialog.this.dismiss();
                    return true;
                }
                else {
                    return false;
                }
            }
        } ;
        this.setOnKeyListener(keylistener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_loading);
    }
}