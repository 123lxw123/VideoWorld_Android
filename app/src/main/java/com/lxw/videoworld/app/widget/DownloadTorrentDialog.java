package com.lxw.videoworld.app.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lxw.videoworld.R;
import com.lxw.videoworld.app.service.DownloadManager;
import com.lxw.videoworld.framework.util.ToastUtil;
import com.xunlei.downloadlib.parameter.TorrentFileInfo;
import com.xunlei.downloadlib.parameter.TorrentInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.lxw.videoworld.app.config.Constant.PATH_OFFLINE_DOWNLOAD;

//分享的dialog
public class DownloadTorrentDialog extends AlertDialog {

    private Context context;
    private String sourceUrl;
    private TorrentInfo torrentInfo;
    private boolean isPlayVideo;
    private BaseQuickAdapter<TorrentFileInfo, BaseViewHolder> downloadTorrentAdapter;
    private RecyclerView downloadTorrentRecyclerView;
    private Button confirmButton;
    // 取消按钮

    public DownloadTorrentDialog(Context context, int theme) {
        super(context, theme);
    }

    public DownloadTorrentDialog(Context context) {
        super(context);
    }

    public DownloadTorrentDialog(Context context, String sourceUrl, TorrentInfo torrentInfo, boolean isPlayVideo) {
        super(context);
        this.context = context;
        this.sourceUrl = sourceUrl;
        this.torrentInfo = torrentInfo;
        this.isPlayVideo = isPlayVideo;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (torrentInfo != null && torrentInfo.mFileCount > 0) {
            setContentView(R.layout.dialog_download_torrent);
            initViews();
        }
    }

    private void initViews() {
        CheckBox allCheck = (CheckBox)findViewById(R.id.checkbox_selected);
        allCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                List<TorrentFileInfo> torrentFileInfos = downloadTorrentAdapter.getData();
                if(torrentFileInfos != null && torrentFileInfos.size() > 0){
                    for(int i = 0; i < torrentFileInfos.size(); i++){
                        downloadTorrentAdapter.getData().get(i).checked = isChecked;
                    }
                    downloadTorrentAdapter.notifyDataSetChanged();
                 }
            }
        });
        downloadTorrentRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_download_torrent);
        downloadTorrentRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        downloadTorrentAdapter = new BaseQuickAdapter<TorrentFileInfo,
                BaseViewHolder>(R.layout.item_download_torrent, Arrays.asList(torrentInfo.mSubFileInfo)){

            @Override
            protected void convert(final BaseViewHolder helper, TorrentFileInfo item) {
                helper.setText(R.id.txt_video_name, item.mFileName);
                helper.setChecked(R.id.checkbox_selected, item.checked);
                helper.setOnCheckedChangeListener(R.id.checkbox_selected, new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        downloadTorrentAdapter.getData().get(helper.getAdapterPosition()).checked = isChecked;
                    }
                });
            }
        };
        downloadTorrentRecyclerView.setAdapter(downloadTorrentAdapter);
        confirmButton = (Button) findViewById(R.id.btn_confirm);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<TorrentFileInfo> torrentFileInfos = downloadTorrentAdapter.getData();
                List<Integer> indexs = new ArrayList<>();
                for(int i = 0; i < torrentFileInfos.size(); i++){
                    if(torrentFileInfos.get(i).checked){
                        indexs.add(torrentFileInfos.get(i).mFileIndex);
                    }
                }
                if(indexs.size() > 0){
                    DownloadManager.addTorrentTask(sourceUrl, torrentInfo.torrentPath, PATH_OFFLINE_DOWNLOAD, indexs, false);
                    dismiss();
                }else{
                    ToastUtil.showMessage("请选择下载文件");
                }
            }
        });
    }

    @Override
    public void show() {
        super.show();
    }
}
