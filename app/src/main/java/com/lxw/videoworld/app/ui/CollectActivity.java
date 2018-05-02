package com.lxw.videoworld.app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lxw.videoworld.R;
import com.lxw.videoworld.app.config.Constant;
import com.lxw.videoworld.app.model.SourceCollectModel;
import com.lxw.videoworld.app.model.SourceDetailModel;
import com.lxw.videoworld.app.util.RealmUtil;
import com.lxw.videoworld.framework.base.BaseActivity;
import com.lxw.videoworld.framework.image.ImageManager;
import com.lxw.videoworld.framework.util.StringUtil;
import com.lxw.videoworld.framework.util.ToastUtil;
import com.lxw.videoworld.framework.util.ValueUtil;
import com.lxw.videoworld.framework.widget.CustomDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmResults;

public class CollectActivity extends BaseActivity {

    @BindView(R.id.recyclerview_source_type)
    RecyclerView recyclerviewSourceType;
    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.button_delete)
    Button buttonDelete;
    private List<SourceDetailModel> sourceDetails = new ArrayList<>();
    private BaseQuickAdapter<SourceDetailModel, BaseViewHolder> sourceAdapter;
    private int picWidth;
    private int picHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect);
        ButterKnife.bind(this);
        RealmResults<SourceCollectModel> resultSourceDetails = RealmUtil.queryCollectModelByStatus(Constant.STATUS_1);
        for (int i = 0; i < resultSourceDetails.size(); i++) {
            sourceDetails.add(resultSourceDetails.get(i).getSourceDetailModel());
        }
        if (sourceDetails.isEmpty()) ToastUtil.showMessage("暂无收藏记录");
        else initView();
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initView() {
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomDialog customDialog = new CustomDialog(CollectActivity.this, "清空收藏记录", "清空收藏记录将会删除所有收藏记录，确定要清空？\n(长按可删除单条记录)", "立即清空", "取消") {
                    @Override
                    public void ok() {
                        super.ok();
                        RealmUtil.modifyCollectStatusByUrl(null, Constant.STATUS_0);
                        sourceAdapter.setNewData(null);
                        ToastUtil.showMessage("已清空收藏记录");
                    }

                    @Override
                    public void cancel() {
                        super.cancel();
                    }
                };
                customDialog.show();
            }
        });
        // 计算列表 Item 图片展示宽高 比例 4:3
        WindowManager wm = getWindowManager();
        int width = wm.getDefaultDisplay().getWidth();
        picWidth = (width - ValueUtil.dip2px(this, recyclerviewSourceType.getPaddingLeft() + recyclerviewSourceType.getPaddingRight()) -
                ValueUtil.dip2px(this, (Constant.GRIDLAYOUTMANAGER_SPANCOUNT - 1) * 10)) / Constant.GRIDLAYOUTMANAGER_SPANCOUNT;
        picHeight = picWidth * 4 / 3;

        sourceAdapter = new BaseQuickAdapter<SourceDetailModel, BaseViewHolder>(R.layout.item_source_list, sourceDetails) {
            @Override
            protected void convert(BaseViewHolder helper, final SourceDetailModel item) {
                helper.addOnLongClickListener(R.id.ll_content);
                // 设置列表 Item 图片、标题展示宽高
                FrameLayout flPicture = ((FrameLayout) helper.getView(R.id.fl_picture));
                flPicture.getLayoutParams().width = picWidth;
                flPicture.getLayoutParams().height = picHeight;
                TextView txtTitle = ((TextView) helper.getView(R.id.txt_title));
                txtTitle.getLayoutParams().width = picWidth;
                // 图片
                List<String> images = ValueUtil.string2list(item.getImages());
                if (images != null && images.size() > 0) {
                    ImageManager.getInstance().loadImage(CollectActivity.this, (ImageView) helper.getView(R.id.img_picture), images.get(0));
                }
                // 标题
                if (!TextUtils.isEmpty(item.getTitle())) {
                    helper.setText(R.id.txt_title, item.getTitle());
                } else if (!TextUtils.isEmpty(item.getName()) && StringUtil.isHasChinese(item.getName())) {
                    helper.setText(R.id.txt_title, item.getName());
                } else if (!TextUtils.isEmpty(item.getTranslateName())) {
                    helper.setText(R.id.txt_title, item.getTranslateName());
                }
                // 评分
                if (!TextUtils.isEmpty(item.getImdbScore()) && item.getImdbScore().length() == 3) {
                    helper.setText(R.id.txt_imdb, item.getImdbScore());
                    helper.setVisible(R.id.ll_score, true);
                    helper.setVisible(R.id.ll_imdb, true);
                } else {
                    helper.setVisible(R.id.ll_score, false);
                    helper.setVisible(R.id.ll_imdb, false);
                }
                if (!TextUtils.isEmpty(item.getDoubanScore()) && item.getDoubanScore().length() == 3) {
                    helper.setText(R.id.txt_douban, item.getDoubanScore());
                    helper.setVisible(R.id.ll_score, true);
                    helper.setVisible(R.id.ll_douban, true);
                } else {
                    helper.setVisible(R.id.ll_score, false);
                    helper.setVisible(R.id.ll_douban, false);
                }

            }
        };
        // item 点击事件
        sourceAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                // TODO
                Constant.detailModels = sourceAdapter.getData();
                Intent intent = new Intent(CollectActivity.this, SourceDetailActivity.class);
                intent.putExtra("url", sourceDetails.get(position).getUrl());
                intent.putExtra("sourceType", sourceDetails.get(position).getSourceType());
                intent.putExtra("isRefreshDetail", true);
                startActivity(intent);
            }
        });
        sourceAdapter.setOnItemChildLongClickListener(new BaseQuickAdapter.OnItemChildLongClickListener() {
            @Override
            public boolean onItemChildLongClick(final BaseQuickAdapter adapter, View view, final int position) {
                CustomDialog customDialog = new CustomDialog(CollectActivity.this, "删除收藏记录", "将会删除该条收藏记录，确定要删除？", "立即删除", "取消") {
                    @Override
                    public void ok() {
                        super.ok();
                        RealmUtil.modifyCollectStatusByUrl(((SourceDetailModel) adapter.getData().get(position)).getUrl(), Constant.STATUS_0);
                        sourceAdapter.getData().remove(position);
                        sourceAdapter.notifyDataSetChanged();
                        ToastUtil.showMessage("已删除收藏记录");
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
        // 动画
        sourceAdapter.openLoadAnimation();
        recyclerviewSourceType.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerviewSourceType.setAdapter(sourceAdapter);
    }
}
