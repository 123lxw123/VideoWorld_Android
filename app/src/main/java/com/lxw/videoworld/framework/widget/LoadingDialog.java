package com.lxw.videoworld.framework.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.KeyEvent;

import com.lxw.videoworld.R;

/**
 * Created by Zion on 2017/5/30.
 */

public class LoadingDialog extends Dialog{
    private Context context;

    public LoadingDialog(@NonNull Context context) {
        super(context, R.style.TransDialog);
        OnKeyListener keylistener = new DialogInterface.OnKeyListener(){
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode==KeyEvent.KEYCODE_BACK&&event.getRepeatCount()==0) {
                    return true;
                }
                else {
                    return false;
                }
            }
        } ;
        this.context = context;
        this.setCanceledOnTouchOutside(false);
        this.setCancelable(false);
        this.setOnKeyListener(keylistener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_loading);
    }
}