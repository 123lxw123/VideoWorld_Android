package com.lxw.videoworld.app.widget;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import com.lxw.videoworld.R;
import com.lxw.videoworld.app.service.DownloadManager;
import com.lxw.videoworld.framework.util.ToastUtil;

import java.util.ArrayList;

//分享的dialog
public class SourceLinkDialog extends AlertDialog {

    private Activity context;
    private String link;
    private ArrayList<String> links;
    private boolean isPlayVideo;
    // 取消按钮

    public SourceLinkDialog(Activity context, int theme) {
        super(context, theme);
    }

    public SourceLinkDialog(Activity context) {
        super(context);
    }

    public SourceLinkDialog(Activity context, String link, boolean isPlayVideo) {
        this(context, link, isPlayVideo, new ArrayList<String>());
    }

    public SourceLinkDialog(Activity context, String link, boolean isPlayVideo, ArrayList<String> links) {
        super(context);
        this.context = context;
        this.link = link;
        this.isPlayVideo = isPlayVideo;
        this.links = links;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!TextUtils.isEmpty(link)) {
            setContentView(R.layout.dialog_link_action);
            LinearLayout ll_thunder = (LinearLayout) this.findViewById(R.id.ll_thunder);
            LinearLayout ll_copy_link = (LinearLayout) this.findViewById(R.id.ll_copy_link);
            LinearLayout ll_cancel = (LinearLayout) this.findViewById(R.id.ll_cancel);
            LinearLayout ll_play_video = (LinearLayout) this.findViewById(R.id.ll_play_video);
            ll_play_video.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SourceLinkDialog.this.dismiss();
                    DownloadManager.addNormalTask(context, link, isPlayVideo, false, links);
                }
            });
            ll_thunder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SourceLinkDialog.this.dismiss();
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                        intent.addCategory("android.intent.category.DEFAULT");
                        context.startActivity(intent);
                    } catch (ActivityNotFoundException e) {
                        e.printStackTrace();
                        ToastUtil.showMessage("您还没安装手机迅雷");
                    }

                }
            });
            ll_copy_link.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SourceLinkDialog.this.dismiss();
                    //获取剪贴板管理器：
                    ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                    // 创建普通字符型ClipData
                    ClipData mClipData = ClipData.newPlainText("downloadLink", link);
                    // 将ClipData内容放到系统剪贴板里。
                    cm.setPrimaryClip(mClipData);
                    ToastUtil.showMessage("已复制到剪切板");
                }
            });
            ll_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SourceLinkDialog.this.dismiss();
                }
            });
        }
    }

    @Override
    public void show() {
        super.show();
    }
}
