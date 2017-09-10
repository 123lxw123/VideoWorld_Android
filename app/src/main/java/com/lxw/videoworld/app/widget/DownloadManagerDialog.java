package com.lxw.videoworld.app.widget;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import com.lxw.videoworld.R;
import com.lxw.videoworld.app.service.DownloadManager;
import com.lxw.videoworld.app.ui.PlayVideoActivity;
import com.lxw.videoworld.framework.util.FileUtil;
import com.lxw.videoworld.framework.util.ToastUtil;
import com.xunlei.downloadlib.parameter.XLTaskInfo;

import static com.lxw.videoworld.app.config.Constant.PATH_OFFLINE_DOWNLOAD;

public class DownloadManagerDialog extends AlertDialog {

    private Activity context;
    private XLTaskInfo xlTaskInfo;
    // 取消按钮

    public DownloadManagerDialog(Activity context, int theme) {
        super(context, theme);
    }

    public DownloadManagerDialog(Activity context) {
        super(context);
    }

    public DownloadManagerDialog(Activity context, XLTaskInfo xlTaskInfo) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (xlTaskInfo != null) {
            setContentView(R.layout.dialog_download_manager);
            LinearLayout ll_play_video = (LinearLayout) this.findViewById(R.id.ll_play_video);
            LinearLayout ll_reset_download = (LinearLayout) this.findViewById(R.id.ll_reset_download);
            LinearLayout ll_remove_task = (LinearLayout) this.findViewById(R.id.ll_remove_task);
            LinearLayout ll_delete_task = (LinearLayout) this.findViewById(R.id.ll_delete_task);
            LinearLayout ll_open_folder = (LinearLayout) this.findViewById(R.id.ll_open_folder);
            LinearLayout ll_copy_link = (LinearLayout) this.findViewById(R.id.ll_copy_link);
            LinearLayout ll_cancel = (LinearLayout) this.findViewById(R.id.ll_cancel);
            ll_play_video.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DownloadManagerDialog.this.dismiss();
                    String url = DownloadManager.getLoclUrl(PATH_OFFLINE_DOWNLOAD + xlTaskInfo.mFileName);
                    Intent intent = new Intent(context, PlayVideoActivity.class);
                    intent.putExtra("url", url);
                    context.startActivity(intent);
                }
            });
            ll_reset_download.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DownloadManagerDialog.this.dismiss();
                    DownloadManager.removeDownloadUrl(xlTaskInfo.sourceUrl);
                    DownloadManager.deleteTask(xlTaskInfo.mTaskId, PATH_OFFLINE_DOWNLOAD);
                    if(xlTaskInfo.index >= 0){
                        DownloadManager.removeDownloadIndex(xlTaskInfo.sourceUrl, xlTaskInfo.index);
                        DownloadManager.addTorrentTask(xlTaskInfo.torrentUrl, xlTaskInfo.sourceUrl, PATH_OFFLINE_DOWNLOAD, new int[] {xlTaskInfo.index});
                    }else {
                        DownloadManager.addNormalTask(context, xlTaskInfo.sourceUrl, false);
                    }
                }
            });
            ll_remove_task.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DownloadManagerDialog.this.dismiss();
                    DownloadManager.removeDownloadUrl(xlTaskInfo.sourceUrl);
                    if(xlTaskInfo.index >= 0){
                        DownloadManager.removeDownloadIndex(xlTaskInfo.sourceUrl, xlTaskInfo.index);
                    }
                    DownloadManager.stopTask(xlTaskInfo.mTaskId);
                }
            });
            ll_delete_task.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DownloadManagerDialog.this.dismiss();
                    DownloadManager.removeDownloadUrl(xlTaskInfo.sourceUrl);
                    DownloadManager.deleteTask(xlTaskInfo.mTaskId, PATH_OFFLINE_DOWNLOAD);
                    if(xlTaskInfo.index >= 0){
                        DownloadManager.removeDownloadIndex(xlTaskInfo.sourceUrl, xlTaskInfo.index);
                    }
                }
            });
            ll_open_folder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FileUtil.openFolder(context, PATH_OFFLINE_DOWNLOAD);
                }
            });
            ll_copy_link.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DownloadManagerDialog.this.dismiss();
                    //获取剪贴板管理器：
                    ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                    // 创建普通字符型ClipData
                    ClipData mClipData = null;
                    if(!TextUtils.isEmpty(xlTaskInfo.torrentUrl)){
                        mClipData = ClipData.newPlainText("downloadLink", xlTaskInfo.torrentUrl);
                    }else {
                        mClipData = ClipData.newPlainText("downloadLink", xlTaskInfo.sourceUrl);
                    }
                    // 将ClipData内容放到系统剪贴板里。
                    cm.setPrimaryClip(mClipData);
                    ToastUtil.showMessage("已复制到剪切板");
                }
            });
            ll_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DownloadManagerDialog.this.dismiss();
                }
            });
        }
    }

    @Override
    public void show() {
        super.show();
    }
}
