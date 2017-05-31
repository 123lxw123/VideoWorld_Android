package com.lxw.videoworld.framework.weixin;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.lxw.videoworld.R;
import com.lxw.videoworld.framework.util.ToastUtil;

//分享的dialog
public class WXShareDialog extends AlertDialog {

    private String link;
    private String wechatContent;
    private String wechatTitle;
    private String imageUrl;

    private LinearLayout share_wechat_btn;
    private LinearLayout share_link_btn;
    private LinearLayout share_wxfriend_btn;

    WXShareAction wxShareAction = null;
    // 取消按钮
    private LinearLayout ll_cancel;

    public WXShareDialog(Activity context, int theme) {
        super(context, theme);
    }

    public WXShareDialog(Activity context) {
        super(context);
    }

    public WXShareDialog(Activity context, String link, String wechatTitle, String wechatContent) {
        super(context);
        this.link = link;
        this.wechatContent = wechatContent;
        this.wechatTitle = wechatTitle;
    }

    public WXShareDialog(Activity context, String link, String wechatTitle, String wechatContent, String imageUrl) {
        super(context);
        this.link = link;
        this.wechatContent = wechatContent;
        this.wechatTitle = wechatTitle;
        this.imageUrl = imageUrl;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_weixin_share);
        ll_cancel = (LinearLayout) this.findViewById(R.id.ll_cancel);
        share_wechat_btn = (LinearLayout) this.findViewById(R.id.share_wechat_btn);
        share_link_btn = (LinearLayout) this.findViewById(R.id.share_link_btn);
        share_wxfriend_btn = (LinearLayout) this.findViewById(R.id.share_wxfriend_btn);
        ll_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WXShareDialog.this.dismiss();
            }
        });
        share_wechat_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                wxShareAction = new WXShareAction(getContext());
                WXShareImgUrlBean url = new WXShareImgUrlBean();
                url.title = wechatTitle;
                url.description = wechatContent;
                url.url = link;// 点击图片后的链接地址
                url.imageUrl = imageUrl;
                wxShareAction.sendToSession(url);
                WXShareDialog.this.dismiss();
            }
        });
        share_wxfriend_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                wxShareAction = new WXShareAction(getContext());
                WXShareImgUrlBean url = new WXShareImgUrlBean();
                url.title = wechatTitle;
                url.description = wechatContent;
                url.url = link;// 点击图片后的链接地址
                url.imageUrl = imageUrl;
                url.isFriends = true;
                wxShareAction.sendToSession(url);
                WXShareDialog.this.dismiss();
            }
        });

        share_link_btn.setOnClickListener(new View.OnClickListener() {

            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
                // 复制链接
                ClipboardManager clip = (ClipboardManager) WXShareDialog.this
                        .getContext().getSystemService(
                                Context.CLIPBOARD_SERVICE);
                clip.setText(link);
                ToastUtil.showMessage("已复制到剪切板");
                WXShareDialog.this.dismiss();
            }
        });
    }

    @Override
    public void show() {
        super.show();
    }
}
