package com.lxw.videoworld.app.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lxw.videoworld.R;
import com.lxw.videoworld.app.api.HttpHelper;
import com.lxw.videoworld.app.config.Constant;
import com.lxw.videoworld.app.model.BaseResponse;
import com.lxw.videoworld.app.model.SourceDetailModel;
import com.lxw.videoworld.app.model.SourceListModel;
import com.lxw.videoworld.framework.base.BaseActivity;
import com.lxw.videoworld.framework.http.HttpManager;
import com.lxw.videoworld.framework.image.ImageManager;
import com.lxw.videoworld.framework.util.SharePreferencesUtil;
import com.lxw.videoworld.framework.util.StringUtil;
import com.lxw.videoworld.framework.util.ToastUtil;
import com.lxw.videoworld.framework.util.ValueUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LocalSearchActivity extends BaseActivity implements SearchView.OnQueryTextListener, View.OnClickListener {

    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.searchview)
    SearchView searchview;
    @BindView(R.id.ll_head)
    LinearLayout llHead;
    @BindView(R.id.recyclerview_keyword)
    RecyclerView recyclerviewKeyword;
    @BindView(R.id.recyclerview_result)
    RecyclerView recyclerviewResult;

    private List<String> hotwords = new ArrayList<>();
    private BaseQuickAdapter<String, BaseViewHolder> hotwordAdapter;
    private List<SourceDetailModel> sourceDetails = new ArrayList<>();
    private BaseQuickAdapter<SourceDetailModel, BaseViewHolder> sourceAdapter;
    private int picWidth;
    private int picHeight;
    private String keyword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_search);
        ButterKnife.bind(this);
        initViews();
    }

    private void initViews() {
        // 计算列表 Item 图片展示宽高 比例 4:3
        WindowManager wm = this.getWindowManager();
        int width = wm.getDefaultDisplay().getWidth();
        picWidth = (width - ValueUtil.dip2px(this, recyclerviewResult.getPaddingLeft() + recyclerviewResult.getPaddingRight()) -
                ValueUtil.dip2px(this, (Constant.GRIDLAYOUTMANAGER_SPANCOUNT - 1) * 10)) / Constant.GRIDLAYOUTMANAGER_SPANCOUNT;
        picHeight = picWidth * 4 / 3;
        imgBack.setOnClickListener(this);
        searchview.setOnQueryTextListener(this);//为搜索框设置监听事件
        // 热搜关键词
        String str = SharePreferencesUtil.getStringSharePreferences(this, Constant.KEY_SEARCH_HOTWORDS, "");
        hotwords = ValueUtil.string2list(str);
        if (hotwords != null && hotwords.size() > 0) {
            if (hotwords.size() > 8) {
                List<String> tempList = new ArrayList<>();
                for (int i = 0; i < 8; i++) {
                    tempList.add(hotwords.remove(i));
                }
                hotwords = tempList;
            }
            recyclerviewKeyword.setLayoutManager(new GridLayoutManager(this, 4));
            hotwordAdapter = new BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_hotword, hotwords) {
                @Override
                protected void convert(BaseViewHolder helper, String item) {
                    helper.setText(R.id.txt_hotword, item);
                }
            };
            // item 点击事件
            hotwordAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    // TODO
                    keyword = (String) adapter.getData().get(position);
                    searchview.setQuery(keyword, false);
                    doSearch();
                }
            });
            recyclerviewKeyword.setAdapter(hotwordAdapter);
            recyclerviewKeyword.setVisibility(View.VISIBLE);
        } else {
            recyclerviewKeyword.setVisibility(View.GONE);
        }
        // 搜索结果
        recyclerviewResult.setLayoutManager(new GridLayoutManager(this, 3));
        sourceAdapter = new BaseQuickAdapter<SourceDetailModel, BaseViewHolder>(R.layout.item_source_list, sourceDetails) {
            @Override
            protected void convert(BaseViewHolder helper, final SourceDetailModel item) {
                helper.setVisible(R.id.txt_source_type, true);
                switch (item.getSourceType()) {
                    case Constant.SOURCE_TYPE_4:
                        helper.setText(R.id.txt_source_type, "A");
                        break;
                    case Constant.SOURCE_TYPE_1:
                        helper.setText(R.id.txt_source_type, "B");
                        break;
                    case Constant.SOURCE_TYPE_2:
                        helper.setText(R.id.txt_source_type, "C");
                        break;
                    case Constant.SOURCE_TYPE_3:
                        helper.setText(R.id.txt_source_type, "D");
                        break;
                    case Constant.SOURCE_TYPE_5:
                        helper.setText(R.id.txt_source_type, "E");
                        break;
                    default:
                        helper.setVisible(R.id.txt_source_type, false);
                        break;
                }
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
                    ImageManager.getInstance().loadImage(LocalSearchActivity.this, (ImageView) helper.getView(R.id.img_picture), images.get(0));
                }
                // 标题
                if (!TextUtils.isEmpty(item.getTitle())) {
                    String title = item.getTitle();
                    if (!TextUtils.isEmpty(keyword) && title.contains(keyword)) {
                        SpannableStringBuilder builder = new SpannableStringBuilder(title);
                        ForegroundColorSpan colorSpan = new ForegroundColorSpan(getCustomColor(R.styleable.BaseColor_com_assist_A));
                        int index = title.indexOf(keyword);
                        builder.setSpan(colorSpan, index, index + keyword.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        helper.setText(R.id.txt_title, builder);
                    } else {
                        helper.setText(R.id.txt_title, title);
                    }
                } else if (!TextUtils.isEmpty(item.getName()) && StringUtil.isHasChinese(item.getName())) {
                    String title = item.getName();
                    if (!TextUtils.isEmpty(keyword) && title.contains(keyword)) {
                        SpannableStringBuilder builder = new SpannableStringBuilder(title);
                        ForegroundColorSpan colorSpan = new ForegroundColorSpan(getCustomColor(R.styleable.BaseColor_com_assist_A));
                        int index = title.indexOf(keyword);
                        builder.setSpan(colorSpan, index, index + keyword.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        helper.setText(R.id.txt_title, builder);
                    } else {
                        helper.setText(R.id.txt_title, title);
                    }
                } else if (!TextUtils.isEmpty(item.getTranslateName())) {
                    String title = item.getTranslateName();
                    if (!TextUtils.isEmpty(keyword) && title.contains(keyword)) {
                        SpannableStringBuilder builder = new SpannableStringBuilder(title);
                        ForegroundColorSpan colorSpan = new ForegroundColorSpan(getCustomColor(R.styleable.BaseColor_com_assist_A));
                        int index = title.indexOf(keyword);
                        builder.setSpan(colorSpan, index, index + keyword.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        helper.setText(R.id.txt_title, builder);
                    } else {
                        helper.setText(R.id.txt_title, title);
                    }
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
                Intent intent = new Intent(LocalSearchActivity.this, SourceDetailActivity.class);
                intent.putExtra("url", sourceAdapter.getData().get(position).getUrl());
                intent.putExtra("sourceType", sourceAdapter.getData().get(position).getSourceType());
                intent.putExtra("isRefreshDetail", true);
                startActivity(intent);
            }
        });
        // 动画
        sourceAdapter.openLoadAnimation();
        recyclerviewResult.setAdapter(sourceAdapter);
    }

    private void closeKeyboard() {
        searchview.clearFocus();
        View view = getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        keyword = query;
        doSearch();
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (!TextUtils.isEmpty(newText) && newText.trim().length() > 0) {
            if (keyword == null || !keyword.trim().equals(newText.trim())) {
                keyword = newText;
            }
        }
        return false;
    }

    public void doSearch() {
        if (!TextUtils.isEmpty(keyword) && keyword.trim().length() > 0) {
            keyword = keyword.trim();
            sourceAdapter.setNewData(null);
            closeKeyboard();
            new HttpManager<SourceListModel>(LocalSearchActivity.this, HttpHelper.getInstance().getLocalSearch(keyword), true, true) {

                @Override
                public void onSuccess(BaseResponse<SourceListModel> response) {
                    SourceListModel sourceListModel = response.getResult();
                    sourceAdapter.setNewData(sourceListModel.getList());
                    if (sourceListModel == null || sourceListModel.getList() == null || sourceListModel.getList().size() == 0)
                        ToastUtil.showMessage("暂无数据");
                    closeKeyboard();
                }

                @Override
                public void onFailure(BaseResponse<SourceListModel> response) {
                    ToastUtil.showMessage("搜索失败");
                }
            }.doRequest();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                LocalSearchActivity.this.finish();
                break;
        }
    }
}
