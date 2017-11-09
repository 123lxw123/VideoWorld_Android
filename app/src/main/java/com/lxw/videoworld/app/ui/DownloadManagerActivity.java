package com.lxw.videoworld.app.ui;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lxw.videoworld.R;
import com.lxw.videoworld.app.adapter.QuickFragmentPageAdapter;
import com.lxw.videoworld.app.config.Constant;
import com.lxw.videoworld.app.service.DownloadManager;
import com.lxw.videoworld.framework.base.BaseActivity;
import com.lxw.videoworld.framework.util.ToastUtil;
import com.xunlei.downloadlib.parameter.XLTaskInfo;

import java.util.ArrayList;
import java.util.List;
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
    @BindView(R.id.img_add_task)
    ImageView imgAddTask;
    @BindView(R.id.edit_source_url)
    EditText editSourceUrl;
    @BindView(R.id.btn_add_task)
    Button btnAddTask;
    @BindView(R.id.ll_add_task)
    LinearLayout llAddTask;
    @BindView(R.id.tab_download_status)
    TabLayout tabDownloadStatus;
    @BindView(R.id.viewpager_download)
    ViewPager viewpagerDownload;


    private Disposable disposable;
    private DownloadListFragment allListFragment;
    private DownloadListFragment downloadingListFragment;
    private DownloadListFragment completeListFragment;

    private List<XLTaskInfo> downloadingList = new ArrayList<>();
    private List<XLTaskInfo> completeList = new ArrayList<>();

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
        imgAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llAddTask.setVisibility(View.VISIBLE);
                editSourceUrl.requestFocus();
            }
        });
        btnAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sourceUrl = editSourceUrl.getText().toString().trim();
                if (!TextUtils.isEmpty(sourceUrl)) {
                    DownloadManager.addNormalTask(DownloadManagerActivity.this, sourceUrl, false, false);
                    if (disposable == null) initRefresh();
                    else refreshData();
                } else {
                    ToastUtil.showMessage("下载链接为空");
                }
                editSourceUrl.setText("");
                llAddTask.setVisibility(View.GONE);
            }
        });

        viewpagerDownload.setAdapter(new QuickFragmentPageAdapter(getSupportFragmentManager(), createDownLoadListFragments(),
                new String[]{"全部", "下载中", "已完成"}));
        tabDownloadStatus.setupWithViewPager(viewpagerDownload);
    }

    private void initData() {
        if (DownloadManager.xLTaskInfos != null) {
            initRefresh();
        }
    }

    private void initRefresh() {
        disposable = Observable.interval(0, 3, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(@NonNull Long aLong) throws Exception {
                        refreshData();
                    }
                });
    }

    private void refreshData() {
        downloadingList.clear();
        completeList.clear();
        if (DownloadManager.xLTaskInfos != null) {
            for (int i = 0; i < DownloadManager.xLTaskInfos.size(); i++){
                XLTaskInfo xlTaskInfo = DownloadManager.xLTaskInfos.get(i);
                if (xlTaskInfo.mTaskStatus == 2 || (xlTaskInfo.mFileSize != 0 && xlTaskInfo.mFileSize == xlTaskInfo.mDownloadSize)){
                    completeList.add(xlTaskInfo);
                } else downloadingList.add(xlTaskInfo);
            }
        }
        if (allListFragment != null) allListFragment.refreshData(DownloadManager.xLTaskInfos);
        if (downloadingListFragment != null) downloadingListFragment.refreshData(downloadingList);
        if (completeListFragment != null) completeListFragment.refreshData(completeList);
    }

    private List<DownloadListFragment> createDownLoadListFragments(){
        List<DownloadListFragment> fragments = new ArrayList<>();
        DownloadListFragment allListFragment = new DownloadListFragment();
        Bundle bundle1 = new Bundle();
        bundle1.putString(Constant.DOWNLOAD_TYPE, Constant.DOWNLOAD_TYPE_ALL);
        allListFragment.setArguments(bundle1);
        allListFragment.setDownloadType(Constant.DOWNLOAD_TYPE_ALL);
        fragments.add(allListFragment);
        this.allListFragment = allListFragment;
        DownloadListFragment downloadingListFragment = new DownloadListFragment();
        Bundle bundle2 = new Bundle();
        bundle2.putString(Constant.DOWNLOAD_TYPE, Constant.DOWNLOAD_TYPE_DOWNLOADING);
        downloadingListFragment.setArguments(bundle2);
        downloadingListFragment.setDownloadType(Constant.DOWNLOAD_TYPE_DOWNLOADING);
        fragments.add(downloadingListFragment);
        this.downloadingListFragment = downloadingListFragment;
        DownloadListFragment completeListFragment = new DownloadListFragment();
        Bundle bundle3 = new Bundle();
        bundle3.putString(Constant.DOWNLOAD_TYPE, Constant.DOWNLOAD_TYPE_COMPLETE);
        completeListFragment.setArguments(bundle3);
        completeListFragment.setDownloadType(Constant.DOWNLOAD_TYPE_COMPLETE);
        fragments.add(completeListFragment);
        this.completeListFragment = completeListFragment;
        return fragments;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (disposable != null) disposable.dispose();
    }
}
