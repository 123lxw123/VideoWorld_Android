package com.lxw.videoworld.framework.widget;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;

import com.lxw.videoworld.R;

import me.drakeet.materialdialog.MaterialDialog;

/**
 * Created by Zion on 2017/7/1.
 */

public class CustomDialog {
    private MaterialDialog mMaterialDialog;

    public CustomDialog(Activity context, String title, String message) {
        this(context, title, message, null, null);
    }

    public CustomDialog(Activity context, String title, String message, String okText, String cancelText) {
        this(context, title, message, okText, cancelText, true);
    }

    public CustomDialog(Activity context, String title, String message, String okText, String cancelText, boolean flag_cancel) {
        mMaterialDialog = new MaterialDialog(context);
        // 不能返回键取消，不能点击对话框外部取消
        if(!flag_cancel){
            mMaterialDialog.setCanceledOnTouchOutside(false);
        }
        String okText1, cancelText1;
        if (!TextUtils.isEmpty(title)) {
            mMaterialDialog.setTitle(title);
        }
        if (!TextUtils.isEmpty(message)) {
            mMaterialDialog.setMessage(message);
        }
        if (!TextUtils.isEmpty(okText)) {
            okText1 = okText;
        } else {
            okText1 = context.getString(R.string.ok);
        }
        if (!TextUtils.isEmpty(cancelText)) {
            cancelText1 = cancelText;
        } else {
            cancelText1 = context.getString(R.string.cancel);
        }
        mMaterialDialog.setPositiveButton(okText1, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ok();
            }
        })
                .setNegativeButton(cancelText1, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cancel();
                    }
                });

    }

    public void ok() {
        mMaterialDialog.dismiss();
    }

    public void cancel() {
        mMaterialDialog.dismiss();
    }

    public void show() {
        mMaterialDialog.show();
    }
}
