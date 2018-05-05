package com.lxw.videoworld.app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lxw.videoworld.R;
import com.lxw.videoworld.app.config.Constant;
import com.lxw.videoworld.app.model.SourceDetailModel;
import com.lxw.videoworld.app.model.SourceHistoryModel;
import com.lxw.videoworld.app.service.DownloadManager;
import com.lxw.videoworld.app.util.RealmUtil;
import com.lxw.videoworld.app.widget.DownloadManagerDialog;
import com.lxw.videoworld.framework.base.BaseFragment;
import com.lxw.videoworld.framework.util.ValueUtil;
import com.lxw.videoworld.framework.widget.NumberProgressBar;
import com.xunlei.downloadlib.parameter.XLTaskInfo;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.lxw.videoworld.app.config.Constant.PATH_OFFLINE_DOWNLOAD;

public class DownloadListFragment extends BaseFragment {

    Unbinder unbinder;
    @BindView(R.id.recyclerview_download_list)
    RecyclerView recyclerviewDownloadList;
    private View rootView;
    private String downloadType;
    private BaseQuickAdapter<XLTaskInfo, BaseViewHolder> downloadManagerAdapter;

    public DownloadListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            downloadType = getArguments().getString(Constant.DOWNLOAD_TYPE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView == null){
            rootView = inflater.inflate(R.layout.fragment_download_list, container, false);
            unbinder = ButterKnife.bind(this, rootView);
            initView();
        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        return rootView;
    }

    private void initView() {
        downloadManagerAdapter = new BaseQuickAdapter<XLTaskInfo, BaseViewHolder>(R.layout.item_download_manager, null) {
            @Override
            protected void convert(BaseViewHolder helper, final XLTaskInfo item) {
                helper.setText(R.id.txt_download_title, item.mFileName);
                if (item.mFileSize > 0) {
                    ((NumberProgressBar) helper.getView(R.id.txt_download_progress)).setProgress((int) Math.floor(item.mDownloadSize * 100 / item.mFileSize));
                } else {
                    ((NumberProgressBar) helper.getView(R.id.txt_download_progress)).setProgress(0);
                }
                setDownloadType(helper, item);
                setDownloadViewWithStatus(helper, item);
            }
        };
        recyclerviewDownloadList.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerviewDownloadList.setAdapter(downloadManagerAdapter);
    }

    public void setDownloadType(BaseViewHolder helper, final XLTaskInfo xlTaskInfo){
        String downloadType = xlTaskInfo.mFileName.split("\\.")[xlTaskInfo.mFileName.split("\\.").length - 1].toLowerCase();
        for (int i = 0; i < Constant.videos.length; i++){
            if (Constant.videos[i].equals(downloadType)){
                helper.setImageResource(R.id.img_download_type, R.drawable.svg_video);
                return;
            }
        }
        for (int i = 0; i < Constant.musics.length; i++){
            if (Constant.musics[i].equals(downloadType)){
                helper.setImageResource(R.id.img_download_type, R.drawable.svg_music);
                return;
            }
        }
        for (int i = 0; i < Constant.images.length; i++){
            if (Constant.images[i].equals(downloadType)){
                helper.setImageResource(R.id.img_download_type, R.drawable.svg_image);
                return;
            }
        }
        for (int i = 0; i < Constant.documents.length; i++){
            if (Constant.documents[i].equals(downloadType)){
                helper.setImageResource(R.id.img_download_type, R.drawable.svg_document);
                return;
            }
        }
        helper.setImageResource(R.id.img_download_type, R.drawable.svg_package);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            List<XLTaskInfo> downloadingList = new ArrayList<>();
            List<XLTaskInfo> completeList = new ArrayList<>();
            if (DownloadManager.xLTaskInfos != null) {
                for (int i = 0; i < DownloadManager.xLTaskInfos.size(); i++){
                    XLTaskInfo xlTaskInfo = DownloadManager.xLTaskInfos.get(i);
                    if (xlTaskInfo.mTaskStatus == 2 || (xlTaskInfo.mFileSize != 0 && xlTaskInfo.mFileSize == xlTaskInfo.mDownloadSize)){
                        completeList.add(xlTaskInfo);
                    } else downloadingList.add(xlTaskInfo);
                }
            }
            if (downloadType != null){
                switch (downloadType){
                    case Constant.DOWNLOAD_TYPE_ALL:
                        refreshData(DownloadManager.xLTaskInfos);
                        break;
                    case Constant.DOWNLOAD_TYPE_DOWNLOADING:
                        refreshData(downloadingList);
                        break;
                    case Constant.DOWNLOAD_TYPE_COMPLETE:
                        refreshData(completeList);
                        break;
                }
            }
        }
    }

    public void refreshData(List<XLTaskInfo> datas){
        if (downloadManagerAdapter != null) downloadManagerAdapter.setNewData(datas);
    }

    private void setDownloadViewWithStatus(final BaseViewHolder helper, final XLTaskInfo xlTaskInfo) {
        final CardView layout = helper.getView(R.id.cardview_download_item);
        final TextView statusText = helper.getView(R.id.txt_download_status);
        String downloadType = xlTaskInfo.mFileName.split("\\.")[xlTaskInfo.mFileName.split("\\.").length - 1].toLowerCase();
        switch (xlTaskInfo.mTaskStatus) {
            case 0:
                if (xlTaskInfo.mDownloadSize == xlTaskInfo.mFileSize && xlTaskInfo.mFileSize > 0) {// 已完成
                    setDownloadStatus(Constant.STATUS_2, helper, xlTaskInfo);
                    View.OnClickListener listener = new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DownloadManagerDialog dialog = new DownloadManagerDialog(getActivity(), xlTaskInfo);
                            dialog.show();
                        }
                    };
                    if (isDownloadVideo(downloadType)) {
                        statusText.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String url = DownloadManager.getLoclUrl(PATH_OFFLINE_DOWNLOAD + xlTaskInfo.mFileName);
                                Intent intent = new Intent(getContext(), PlayVideoActivity.class);
                                intent.putExtra("url", url);
                                getContext().startActivity(intent);
                            }
                        });
                    } else if (downloadType.equals("png") || downloadType.equals("jpg") || downloadType.equals("jpeg")){
                        statusText.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String url = Constant.PATH_OFFLINE_DOWNLOAD + xlTaskInfo.mFileName;
                                Intent intent = new Intent(DownloadListFragment.this.getActivity(), PreViewActivity.class);
                                intent.putExtra("url", url);
                                startActivity(intent);
                            }
                        });
                    } else statusText.setOnClickListener(listener);
                    layout.setOnClickListener(listener);
                } else {// 连接中
                    setDownloadStatus(Constant.STATUS_0, helper, xlTaskInfo);
                    statusText.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DownloadManager.removeTask(xlTaskInfo.mTaskId);
                            DownloadManager.startTask(getActivity(), xlTaskInfo);
                        }
                    });
                    layout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DownloadManagerDialog dialog = new DownloadManagerDialog(getActivity(), xlTaskInfo);
                            dialog.show();
                        }
                    });
                }
                break;
            case 1:
                setDownloadStatus(Constant.STATUS_1, helper, xlTaskInfo);
                statusText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DownloadManager.stopTask(xlTaskInfo.mTaskId);
                        setDownloadStatus(Constant.STATUS_4, helper, xlTaskInfo);
                    }
                });
                layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DownloadManagerDialog dialog = new DownloadManagerDialog(getActivity(), xlTaskInfo);
                        dialog.show();
                    }
                });
                break;
            case 2:
                setDownloadStatus(Constant.STATUS_2, helper, xlTaskInfo);
                View.OnClickListener listener1 = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DownloadManagerDialog dialog = new DownloadManagerDialog(getActivity(), xlTaskInfo);
                        dialog.show();
                    }
                };
                if (isDownloadVideo(downloadType)) {
                    statusText.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            String url = DownloadManager.getLoclUrl(PATH_OFFLINE_DOWNLOAD + xlTaskInfo.mFileName);

                            SourceHistoryModel oldSourceHistoryModel = RealmUtil.queryHistoryModelByLink(xlTaskInfo.sourceUrl);
                            SourceHistoryModel sourceHistoryModel = new SourceHistoryModel();
                            sourceHistoryModel.setLink(url);
                            if (oldSourceHistoryModel != null) {
                                oldSourceHistoryModel.setStatus(Constant.STATUS_0);
                                sourceHistoryModel.setSourceDetailModel(oldSourceHistoryModel.getSourceDetailModel());
                            } else {
                                SourceDetailModel sourceDetailModel = new SourceDetailModel();
                                sourceDetailModel.setTitle(xlTaskInfo.mFileName);
                                sourceHistoryModel.setSourceDetailModel(sourceDetailModel);
                            }
                            sourceHistoryModel.setStatus(Constant.STATUS_1);
                            RealmUtil.copyOrUpdateHistoryModel(oldSourceHistoryModel, false);
                            RealmUtil.copyOrUpdateHistoryModel(sourceHistoryModel, false);

                            Intent intent = new Intent(getContext(), PlayVideoActivity.class);
                            intent.putExtra("url", url);
                            getContext().startActivity(intent);
                        }
                    });
                } else if (downloadType.equals("png") || downloadType.equals("jpg") || downloadType.equals("jpeg")) {
                    statusText.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String url = Constant.PATH_OFFLINE_DOWNLOAD + xlTaskInfo.mFileName;
                            Intent intent = new Intent(DownloadListFragment.this.getActivity(), PreViewActivity.class);
                            intent.putExtra("url", url);
                            startActivity(intent);
                        }
                    });
                } else statusText.setOnClickListener(listener1);
                layout.setOnClickListener(listener1);
                break;
            case 3:
                setDownloadStatus(Constant.STATUS_3, helper, xlTaskInfo);
                statusText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DownloadManager.removeTask(xlTaskInfo.mTaskId);
                        DownloadManager.startTask(getActivity(), xlTaskInfo);
                        setDownloadStatus(Constant.STATUS_1, helper, xlTaskInfo);
                    }
                });
                layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DownloadManagerDialog dialog = new DownloadManagerDialog(getActivity(), xlTaskInfo);
                        dialog.show();
                    }
                });
                break;
            case 4:
                setDownloadStatus(Constant.STATUS_4, helper, xlTaskInfo);
                statusText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DownloadManager.startTask(getActivity(), xlTaskInfo);
                        setDownloadStatus(Constant.STATUS_1, helper, xlTaskInfo);
                    }
                });
                layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DownloadManagerDialog dialog = new DownloadManagerDialog(getActivity(), xlTaskInfo);
                        dialog.show();
                    }
                });
                break;
            default:
                break;
        }
    }

    public void setDownloadStatus(String downloadStatus, BaseViewHolder helper, XLTaskInfo xlTaskInfo){
        final TextView statusText = helper.getView(R.id.txt_download_status);
        switch (downloadStatus){
            case Constant.STATUS_0:// 连接中
                helper.setText(R.id.txt_download_speed, "资源连接中...");
                helper.setText(R.id.txt_download_size, ValueUtil.formatFileSize(xlTaskInfo.mDownloadSize) + " / " + ValueUtil.formatFileSize(xlTaskInfo.mFileSize));
                helper.setText(R.id.txt_download_time, "");
                statusText.setBackgroundResource(R.drawable.bg_tab_select);
                statusText.setTextColor(getCustomColor(R.styleable.BaseColor_com_font_A));
                statusText.setText("重试");
                break;
            case Constant.STATUS_1:// 下载中
                helper.setText(R.id.txt_download_speed, ValueUtil.formatFileSize(xlTaskInfo.mDownloadSpeed) + "/s");
                helper.setText(R.id.txt_download_size, ValueUtil.formatFileSize(xlTaskInfo.mDownloadSize) + " / " + ValueUtil.formatFileSize(xlTaskInfo.mFileSize));
                long downloadTime;
                if (xlTaskInfo.mFileSize - xlTaskInfo.mDownloadSize > 0 && xlTaskInfo.mDownloadSpeed > 0) {
                    downloadTime = (xlTaskInfo.mFileSize - xlTaskInfo.mDownloadSize) / xlTaskInfo.mDownloadSpeed;
                    helper.setText(R.id.txt_download_time, ValueUtil.formatTime(downloadTime));
                } else helper.setText(R.id.txt_download_time, "");
                statusText.setBackgroundResource(R.drawable.bg_tab_select);
                statusText.setTextColor(getCustomColor(R.styleable.BaseColor_com_font_A));
                statusText.setText("暂停");
                break;
            case Constant.STATUS_2:// 已完成
                helper.setText(R.id.txt_download_speed, ValueUtil.formatFileSize(xlTaskInfo.mFileSize));
                helper.setText(R.id.txt_download_size, "");
                helper.setText(R.id.txt_download_time, "");
                String downloadType = xlTaskInfo.mFileName.split("\\.")[xlTaskInfo.mFileName.split("\\.").length - 1].toLowerCase();
                if (isDownloadVideo(downloadType)){
                    statusText.setTextColor(getResources().getColor(R.color.color_009587));
                    statusText.setBackgroundResource(R.drawable.bg_tab_green);
                    statusText.setText("播放");
                } else if (downloadType.equals("png") || downloadType.equals("jpg") || downloadType.equals("jpeg")){
                    statusText.setTextColor(getResources().getColor(R.color.color_009587));
                    statusText.setBackgroundResource(R.drawable.bg_tab_green);
                    statusText.setText("预览");
                } else {
                    statusText.setTextColor(getCustomColor(R.styleable.BaseColor_com_font_C));
                    statusText.setBackgroundDrawable(null);
                    statusText.setText("完成");
                }
                break;
            case Constant.STATUS_3:// 下载错误
                helper.setText(R.id.txt_download_speed, "下载失败");
                helper.setText(R.id.txt_download_size, ValueUtil.formatFileSize(xlTaskInfo.mDownloadSize) + " / " + ValueUtil.formatFileSize(xlTaskInfo.mFileSize));
                helper.setText(R.id.txt_download_time, "");
                statusText.setBackgroundResource(R.drawable.bg_tab_red);
                statusText.setTextColor(getResources().getColor(R.color.com_font_D_gold));
                statusText.setText("重试");
                break;
            case Constant.STATUS_4:// 暂停中
                helper.setText(R.id.txt_download_speed, ValueUtil.formatFileSize(xlTaskInfo.mDownloadSize) + " / " + ValueUtil.formatFileSize(xlTaskInfo.mFileSize));
                helper.setText(R.id.txt_download_size, "");
                helper.setText(R.id.txt_download_time, "");
                statusText.setBackgroundResource(R.drawable.bg_tab_select);
                statusText.setTextColor(getCustomColor(R.styleable.BaseColor_com_font_A));
                statusText.setText("继续");
                break;
        }

    }

    public boolean isDownloadVideo(String downloadType){

        boolean isVideo = false;
        for (int i = 0; i < Constant.videos.length; i++){
            if (Constant.videos[i].equals(downloadType)){
                isVideo = true;
                break;
            }
        }
        return isVideo;
    }

    public void setDownloadType(String downloadType) {
        this.downloadType = downloadType;
    }
}
