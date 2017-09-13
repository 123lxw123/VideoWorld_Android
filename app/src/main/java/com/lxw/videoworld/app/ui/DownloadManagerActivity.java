package com.lxw.videoworld.app.ui;

import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lxw.videoworld.R;
import com.lxw.videoworld.app.service.DownloadManager;
import com.lxw.videoworld.app.widget.DownloadManagerDialog;
import com.lxw.videoworld.framework.base.BaseActivity;
import com.lxw.videoworld.framework.util.ValueUtil;
import com.lxw.videoworld.framework.widget.NumberProgressBar;
import com.xunlei.downloadlib.parameter.XLTaskInfo;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


public class DownloadManagerActivity extends BaseActivity {

    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.txt_download_manager)
    TextView txtDownloadManager;
    @BindView(R.id.recyclerview_download_task)
    RecyclerView recyclerviewDownloadTask;

    private BaseQuickAdapter<XLTaskInfo, BaseViewHolder> downloadManagerAdapter;
    private Disposable disposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_manager);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initView() {
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        downloadManagerAdapter = new BaseQuickAdapter<XLTaskInfo, BaseViewHolder>(R.layout.item_download_manager, DownloadManager.xLTaskInfos) {
            @Override
            protected void convert(BaseViewHolder helper, final XLTaskInfo item) {
                helper.setText(R.id.txt_download_type, item.mFileName.split("\\.")[item.mFileName.split("\\.").length -1]);
                helper.setText(R.id.txt_download_title, item.mFileName);
                if (item.mFileSize > 0){
                    ((NumberProgressBar)helper.getView(R.id.txt_download_progress)).setProgress((int) Math.floor(item.mDownloadSize * 100 / item.mFileSize));
                }else {
                    ((NumberProgressBar)helper.getView(R.id.txt_download_progress)).setProgress(0);
                }
                setDownloadViewWithStatus(helper, item);
                helper.setOnClickListener(R.id.img_start_pause, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DownloadManager.stopTask(item.mTaskId);
                    }
                });
            }
        };
        recyclerviewDownloadTask.setLayoutManager(new LinearLayoutManager(this));
        recyclerviewDownloadTask.setAdapter(downloadManagerAdapter);
    }

    private void initData() {
        if(DownloadManager.xLTaskInfos != null) {
            downloadManagerAdapter.setNewData(DownloadManager.xLTaskInfos);
            refreshData();
        }
    }

    private void refreshData(){
        disposable = Observable.interval(0, 1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
            @Override
            public void accept(@NonNull Long aLong) throws Exception {
                if(DownloadManager.xLTaskInfos != null) downloadManagerAdapter.setNewData(DownloadManager.xLTaskInfos);
            }
        });
    }

    private void setDownloadViewWithStatus(BaseViewHolder helper, final XLTaskInfo xlTaskInfo){
        final CardView layout = (CardView) helper.getView(R.id.cardview_download_item);
        final ImageView statusIcon = (ImageView) helper.getView(R.id.img_start_pause);
        switch (xlTaskInfo.mTaskStatus) {
            case 0:
                if(xlTaskInfo.mDownloadSize == xlTaskInfo.mFileSize && xlTaskInfo.mFileSize > 0){// 已完成
                    helper.setText(R.id.txt_download_info, ValueUtil.formatFileSize(xlTaskInfo.mFileSize));
                    statusIcon.setImageResource(R.drawable.ic_complete);
                    View.OnClickListener listener = new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DownloadManagerDialog dialog = new DownloadManagerDialog(DownloadManagerActivity.this, xlTaskInfo);
                            dialog.show();
                        }
                    };
                    statusIcon.setOnClickListener(listener);
                    layout.setOnClickListener(listener);
                }else {// 连接中
                    helper.setText(R.id.txt_download_info, ValueUtil.formatFileSize(xlTaskInfo.mDownloadSpeed)+ "\n" +
                            ValueUtil.formatFileSize(xlTaskInfo.mDownloadSize) + "\n" + ValueUtil.formatFileSize(xlTaskInfo.mFileSize));
                    statusIcon.setImageResource(R.drawable.ic_start);
                    statusIcon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DownloadManager.startTask(xlTaskInfo.mTaskId);
                        }
                    });
                    layout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DownloadManagerDialog dialog = new DownloadManagerDialog(DownloadManagerActivity.this, xlTaskInfo);
                            dialog.show();
                        }
                    });
                }
                break;
            case 1:
                helper.setText(R.id.txt_download_info, ValueUtil.formatFileSize(xlTaskInfo.mDownloadSpeed)+ "\n" +
                        ValueUtil.formatFileSize(xlTaskInfo.mDownloadSize) + "\n" + ValueUtil.formatFileSize(xlTaskInfo.mFileSize));
                statusIcon.setImageResource(R.drawable.ic_pause);
                statusIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DownloadManager.stopTask(xlTaskInfo.mTaskId);
                    }
                });
                layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DownloadManagerDialog dialog = new DownloadManagerDialog(DownloadManagerActivity.this, xlTaskInfo);
                        dialog.show();
                    }
                });
                break;
            case 2:
                helper.setText(R.id.txt_download_info, ValueUtil.formatFileSize(xlTaskInfo.mFileSize));
                statusIcon.setImageResource(R.drawable.ic_complete);
                View.OnClickListener listener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DownloadManagerDialog dialog = new DownloadManagerDialog(DownloadManagerActivity.this, xlTaskInfo);
                        dialog.show();
                    }
                };
                statusIcon.setOnClickListener(listener);
                layout.setOnClickListener(listener);
                break;
            case 3:
                helper.setText(R.id.txt_download_info, ValueUtil.formatFileSize(xlTaskInfo.mDownloadSize) + "\n" + ValueUtil.formatFileSize(xlTaskInfo.mFileSize));
                statusIcon.setImageResource(R.drawable.ic_error);
                View.OnClickListener listener1 = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DownloadManagerDialog dialog = new DownloadManagerDialog(DownloadManagerActivity.this, xlTaskInfo);
                        dialog.show();
                    }
                };
                statusIcon.setOnClickListener(listener1);
                layout.setOnClickListener(listener1);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (disposable != null) disposable.dispose();
    }
}
