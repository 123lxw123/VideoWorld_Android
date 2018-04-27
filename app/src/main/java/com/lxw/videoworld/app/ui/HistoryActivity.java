package com.lxw.videoworld.app.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lxw.videoworld.R;
import com.lxw.videoworld.app.config.Constant;
import com.lxw.videoworld.app.model.SourceHistoryModel;
import com.lxw.videoworld.app.util.RealmUtil;
import com.lxw.videoworld.app.util.StringUtil;
import com.lxw.videoworld.framework.base.BaseActivity;
import com.lxw.videoworld.framework.image.ImageManager;
import com.lxw.videoworld.framework.util.ToastUtil;
import com.lxw.videoworld.framework.util.ValueUtil;
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
    private RealmResults<SourceHistoryModel> historyModels;
    private BaseQuickAdapter<SourceHistoryModel, BaseViewHolder> historyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        ButterKnife.bind(this);
        historyModels = RealmUtil.queryHistoryModelByStatus(Constant.STATUS_1);
        if (historyModels.isEmpty()) ToastUtil.showMessage("暂无播放记录");
        else setUpView();
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void setUpView() {
        historyAdapter = new BaseQuickAdapter<SourceHistoryModel, BaseViewHolder>(R.layout.item_download_manager, null) {
            @Override
            protected void convert(BaseViewHolder helper, final SourceHistoryModel item) {
                List<String> images = ValueUtil.string2list(item.getSourceDetailModel().getImages());
                if (images != null && !images.isEmpty()) ImageManager.getInstance().loadImage(HistoryActivity.this, (ImageView) helper.getView(R.id.img_picture), images.get(0));
                helper.setText(R.id.txt_title, item.getSourceDetailModel().getTitle());
                int index = 0;
                List<String> links = StringUtil.getSourceLinks(item.getSourceDetailModel().getLinks());
                for (int i = 0; i < links.size(); i++) {
                    if (item.getLink().equals(links.get(i))) index = i + 1;
                }
                String link = item.getLink();
                if (index > 0) link = "( " + index + " ) " + link;
                helper.setText(R.id.txt_link, link);
                if (item.getTotal() > 0 && item.getSeek() >= 0) {
                    ((NumberProgressBar) helper.getView(R.id.txt_progress)).setProgress((int) Math.floor(item.getSeek() / item.getTotal()));
                } else {
                    ((NumberProgressBar) helper.getView(R.id.txt_progress)).setProgress(0);
                }
            }
        };
        recyclerviewHistory.setLayoutManager(new LinearLayoutManager(this));
        recyclerviewHistory.setAdapter(historyAdapter);
    }
}
