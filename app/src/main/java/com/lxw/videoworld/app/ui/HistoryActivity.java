package com.lxw.videoworld.app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lxw.videoworld.R;
import com.lxw.videoworld.app.config.Constant;
import com.lxw.videoworld.app.model.SourceHistoryModel;
import com.lxw.videoworld.app.service.DownloadManager;
import com.lxw.videoworld.app.util.RealmUtil;
import com.lxw.videoworld.app.util.StringUtil;
import com.lxw.videoworld.framework.base.BaseActivity;
import com.lxw.videoworld.framework.image.ImageManager;
import com.lxw.videoworld.framework.util.ToastUtil;
import com.lxw.videoworld.framework.util.ValueUtil;
import com.lxw.videoworld.framework.widget.CustomDialog;
import com.lxw.videoworld.framework.widget.NumberProgressBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmResults;

public class HistoryActivity extends BaseActivity {


    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.button_delete)
    Button buttonDelete;
    @BindView(R.id.recyclerview_history)
    RecyclerView recyclerviewHistory;
    private List<SourceHistoryModel> historyModels = new ArrayList<>();
    private BaseQuickAdapter<SourceHistoryModel, BaseViewHolder> historyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        ButterKnife.bind(this);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        historyModels.clear();
        RealmResults realmResults = RealmUtil.queryHistoryModelByStatus(Constant.STATUS_1);
        if (realmResults.isEmpty()) ToastUtil.showMessage("暂无播放记录");
        else {
            for (int i = 0; i < realmResults.size(); i++) {
                historyModels.add((SourceHistoryModel) realmResults.get(i));
            }
            setUpView();
        }
    }

    private void setUpView() {
        historyAdapter = new BaseQuickAdapter<SourceHistoryModel, BaseViewHolder>(R.layout.item_history, historyModels) {
            @Override
            protected void convert(final BaseViewHolder helper, final SourceHistoryModel item) {
                List<String> images = ValueUtil.string2list(item.getSourceDetailModel().getImages());
                if (images != null && !images.isEmpty()) ImageManager.getInstance().loadImage(HistoryActivity.this, (ImageView) helper.getView(R.id.img_picture), images.get(0));
                helper.setText(R.id.txt_title, item.getSourceDetailModel().getTitle());
                int index = 0;
                List<String> links = StringUtil.getSourceLinks(item.getSourceDetailModel());
                for (int i = 0; i < links.size(); i++) {
                    if (item.getLink().equals(links.get(i))) index = i + 1;
                }
                String link = item.getLink();
                if (index > 0) link = "( " + index + " ) " + link;
                helper.setText(R.id.txt_link, link);
                if (item.getTotal() > 0 && item.getSeek() >= 0) {
                    ((NumberProgressBar) helper.getView(R.id.txt_progress)).setMax(item.getTotal());
                    ((NumberProgressBar) helper.getView(R.id.txt_progress)).setProgress(item.getSeek());
                } else {
                    ((NumberProgressBar) helper.getView(R.id.txt_progress)).setProgress(0);
                }
                helper.getView(R.id.cardview_history_item).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String url = item.getLink();
                        if (item.getLocalFilePath() != null && item.getLocalFilePath().startsWith(Constant.PATH_SD_CARD)) {
                            url = DownloadManager.getLoclUrl(item.getLocalFilePath());
                            if (!TextUtils.isEmpty(item.getLocalUrl())) {
                                SourceHistoryModel sourceHistoryModel = RealmUtil.queryHistoryModelByLocalUrl(item.getLocalUrl());
                                if (sourceHistoryModel != null) {
                                    sourceHistoryModel.setLocalUrl(url);
                                    RealmUtil.copyOrUpdateHistoryModel(sourceHistoryModel, false);
                                }
                            }
                        } else if (item.getLocalUrl() != null)
                            url = item.getLocalUrl();
                        Intent intent = new Intent(HistoryActivity.this, PlayVideoActivity.class);
                        intent.putExtra("url", url);
                        if (!TextUtils.isEmpty(item.getLink())) intent.putExtra("sourceUrl", item.getLink());
                        startActivity(intent);
                    }
                });
                helper.getView(R.id.cardview_history_item).setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        CustomDialog customDialog = new CustomDialog(HistoryActivity.this, "删除播放记录", "将会删除该条播放记录，确定要删除？", "立即删除", "取消") {
                            @Override
                            public void ok() {
                                super.ok();
                                RealmUtil.modifyHistoryStatusByUrl(item.getLink(), Constant.STATUS_0);
                                historyAdapter.getData().remove(helper.getAdapterPosition());
                                historyAdapter.notifyDataSetChanged();
                                ToastUtil.showMessage("已删除播放记录");
                            }

                            @Override
                            public void cancel() {
                                super.cancel();
                            }
                        };
                        customDialog.show();
                        return false;
                    }
                });
            }
        };
        recyclerviewHistory.setLayoutManager(new LinearLayoutManager(this));
        recyclerviewHistory.setAdapter(historyAdapter);
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomDialog customDialog = new CustomDialog(HistoryActivity.this, "清空播放记录", "清空播放记录将会删除所有播放记录，确定要清空？\n(长按可删除单条记录)", "立即清空", "取消") {
                    @Override
                    public void ok() {
                        super.ok();
                        RealmUtil.modifyHistoryStatusByUrl(null, Constant.STATUS_0);
                        historyAdapter.setNewData(null);
                        ToastUtil.showMessage("已清空播放记录");
                    }

                    @Override
                    public void cancel() {
                        super.cancel();
                    }
                };
                customDialog.show();
            }
        });
    }
}
