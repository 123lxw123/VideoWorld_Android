package com.lxw.videoworld.framework.widget;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;

import com.lxw.videoworld.R;
import com.xunlei.downloadlib.parameter.TorrentInfo;

//分享的dialog
public class DownloadTorrentDialog extends AlertDialog {

    private Activity context;
    private TorrentInfo torrentInfo;
    // 取消按钮

    public DownloadTorrentDialog(Activity context, int theme) {
        super(context, theme);
    }

    public DownloadTorrentDialog(Activity context) {
        super(context);
    }

    public DownloadTorrentDialog(Activity context, TorrentInfo torrentInfo) {
        super(context);
        this.context = context;
        this.torrentInfo = torrentInfo;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (torrentInfo != null && torrentInfo.mFileCount > 0) {
            setContentView(R.layout.dialog_download_torrent);
            
        }
    }

    @Override
    public void show() {
        super.show();
    }
}
