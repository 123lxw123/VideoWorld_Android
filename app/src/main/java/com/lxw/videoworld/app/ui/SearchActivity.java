package com.lxw.videoworld.app.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lxw.videoworld.R;
import com.lxw.videoworld.app.config.Constant;
import com.lxw.videoworld.app.model.SearchModel;
import com.lxw.videoworld.app.service.SearchSpider;
import com.lxw.videoworld.app.widget.SourceLinkDialog;
import com.lxw.videoworld.framework.base.BaseActivity;
import com.lxw.videoworld.framework.util.SharePreferencesUtil;
import com.lxw.videoworld.framework.util.ToastUtil;
import com.lxw.videoworld.framework.util.ValueUtil;
import com.lxw.videoworld.framework.widget.EmptyLoadMoreView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SearchActivity extends BaseActivity implements SearchView.OnQueryTextListener, View.OnClickListener {


    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.searchview)
    SearchView searchview;
    @BindView(R.id.img_change_source)
    ImageView imgChangeSource;
    @BindView(R.id.recyclerview_keyword)
    RecyclerView recyclerviewKeyword;
    @BindView(R.id.recyclerview_result)
    RecyclerView recyclerviewResult;

    @BindView(R.id.txt_tab1)
    TextView txtTab1;
    @BindView(R.id.txt_tab2)
    TextView txtTab2;
    @BindView(R.id.txt_tab3)
    TextView txtTab3;
    @BindView(R.id.ll_head)
    LinearLayout llHead;
    private List<String> hotwords = new ArrayList<>();
    private BaseQuickAdapter<String, BaseViewHolder> hotwordAdapter;
    private List<SearchModel> searchModels = new ArrayList<>();
    private BaseQuickAdapter<SearchModel, BaseViewHolder> searchAdapter;
    private String keyword;
    private int page = 1;
    private boolean flag_loadmore = false;
    private String searchType = Constant.STATUS_0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        initViews();
    }

    private void initViews() {
        imgBack.setOnClickListener(this);
        imgChangeSource.setOnClickListener(this);
        searchview.setOnQueryTextListener(this);//为搜索框设置监听事件
        // 搜索结果排序
        txtTab1.setOnClickListener(this);
        txtTab2.setOnClickListener(this);
        txtTab3.setOnClickListener(this);
        txtTab1.setText(getString(R.string.txt_search_tab1));
        txtTab2.setText(getString(R.string.txt_search_tab2));
        if (Constant.SEARCH_TYPE.equals(Constant.SEARCH_TYPE_1)) {
            txtTab3.setText(getString(R.string.txt_search_tab3));
        } else if (Constant.SEARCH_TYPE.equals(Constant.SEARCH_TYPE_2)) {
            txtTab3.setText(getString(R.string.txt_search_tab4));
        }
        changeTabColor();
        // 热搜关键词
        String str = SharePreferencesUtil.getStringSharePreferences(this, Constant.KEY_SEARCH_HOTWORDS, "");
        hotwords = ValueUtil.string2list(str);
        if (hotwords != null && hotwords.size() > 0) {
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
        recyclerviewResult.setLayoutManager(new LinearLayoutManager(this));
        searchAdapter = new BaseQuickAdapter<SearchModel, BaseViewHolder>(R.layout.item_search, searchModels) {
            @Override
            protected void convert(BaseViewHolder helper, final SearchModel item) {
                String title = item.getTitle();
                if (!TextUtils.isEmpty(title)) {
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
                helper.setText(R.id.txt_date, item.getDate());
                helper.setText(R.id.txt_hot, item.getHot());
                helper.setText(R.id.txt_size, item.getSize());
                if (Constant.SEARCH_TYPE.equals(Constant.SEARCH_TYPE_1)) {
                    helper.setVisible(R.id.ll_amounts, false);
                    helper.setVisible(R.id.ll_thunder_link, false);
                } else if (Constant.SEARCH_TYPE.equals(Constant.SEARCH_TYPE_2)) {
                    helper.setText(R.id.txt_amounts, item.getAmounts());
                    helper.setVisible(R.id.ll_amounts, true);
                    helper.setOnClickListener(R.id.ll_thunder_link, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // TODO
                            SourceLinkDialog dialog = new SourceLinkDialog(SearchActivity.this, (String) item.getThunderLink(), true);
                            dialog.show();
                        }
                    });
                    helper.setVisible(R.id.ll_thunder_link, true);
                }

                helper.setOnClickListener(R.id.ll_thunder_ciliLink, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO
                        SourceLinkDialog dialog = new SourceLinkDialog(SearchActivity.this, (String) item.getCiliLink(), true);
                        dialog.show();
                    }
                });
            }
        };
        // item 点击事件
        searchAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                // TODO
            }
        });
        // 加载更多
        // 当列表滑动到倒数第N个Item的时候(默认是1)回调onLoadMoreRequested方法
        searchAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                recyclerviewResult.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        flag_loadmore = true;
                        doSearch();
                    }

                }, 0);
            }
        }, recyclerviewResult);
        searchAdapter.setLoadMoreView(new EmptyLoadMoreView());
        // 动画
        searchAdapter.openLoadAnimation();
        recyclerviewResult.setAdapter(searchAdapter);
    }

    private void getSearchResult() {
        if (!TextUtils.isEmpty(keyword) && keyword.trim().length() > 0) {
            keyword = keyword.trim();
            closeKeyboard();
            showProgressBar();
            Observable.create(new ObservableOnSubscribe<List<SearchModel>>() {
                @Override
                public void subscribe(ObservableEmitter<List<SearchModel>> emitter) throws Exception {
                    emitter.onNext(SearchSpider.getZhongziSearchResult(getSearchUrl()));
                }
            }).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())  //回到主线程去处理请求结果
                    .subscribe(new Observer<List<SearchModel>>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                        }

                        @Override
                        public void onNext(List<SearchModel> list) {
                            hideProgressBar();
                            if (list != null && !list.isEmpty()) {
                                if (flag_loadmore) {
                                    searchModels.addAll(list);
                                    searchAdapter.addData(list);
                                    searchAdapter.loadMoreComplete();
                                } else {
                                    searchModels.clear();
                                    searchModels.addAll(list);
                                    searchAdapter.setNewData(list);
                                }
                                page++;
                            } else if (list == null) ToastUtil.showMessage("");
                            searchview.clearFocus();
                            flag_loadmore = false;
                        }

                        @Override
                        public void onError(Throwable e) {
                            hideProgressBar();
                        }

                        @Override
                        public void onComplete() {
                            hideProgressBar();
                        }
                    });

        }
    }

    // 获取搜索的 Url
    public String getSearchUrl() {
        String searchUrl = "";
        if (Constant.SEARCH_TYPE.equals(Constant.SEARCH_TYPE_1)) {
            switch (searchType) {
                case Constant.STATUS_0:
                    searchUrl = Constant.BASE_ZHONGZI_SEARCH_1.replace("keyword", keyword).replace("page", page + "");
                    break;
                case Constant.STATUS_1:
                    searchUrl = Constant.BASE_ZHONGZI_SEARCH_2.replace("keyword", keyword).replace("page", page + "");
                    break;
                case Constant.STATUS_2:
                    searchUrl = Constant.BASE_ZHONGZI_SEARCH_3.replace("keyword", keyword).replace("page", page + "");
                    break;
            }
        } else if (Constant.SEARCH_TYPE.equals(Constant.SEARCH_TYPE_2)) {
            switch (searchType) {
                case Constant.STATUS_0:
                    searchUrl = Constant.BASE_DIAOSI_SEARCH_1.replace("keyword", keyword).replace("page", page + "");
                    break;
                case Constant.STATUS_1:
                    searchUrl = Constant.BASE_DIAOSI_SEARCH_2.replace("keyword", keyword).replace("page", page + "");
                    break;
                case Constant.STATUS_2:
                    searchUrl = Constant.BASE_DIAOSI_SEARCH_3.replace("keyword", keyword).replace("page", page + "");
                    break;
            }
        }
        return searchUrl;
    }

    private void closeKeyboard() {
        View view = getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void changeTabColor() {
        switch (searchType) {
            case Constant.STATUS_0:
                txtTab1.setSelected(true);
                txtTab2.setSelected(false);
                txtTab3.setSelected(false);
                break;
            case Constant.STATUS_1:
                txtTab1.setSelected(false);
                txtTab2.setSelected(true);
                txtTab3.setSelected(false);
                break;
            case Constant.STATUS_2:
                txtTab1.setSelected(false);
                txtTab2.setSelected(false);
                txtTab3.setSelected(true);
                break;
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
        if (!flag_loadmore) {
            page = 1;
        }
        getSearchResult();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                SearchActivity.this.finish();
                break;
            case R.id.txt_tab1:
                searchType = Constant.STATUS_0;
                changeTabColor();
                doSearch();
                break;
            case R.id.txt_tab2:
                searchType = Constant.STATUS_1;
                changeTabColor();
                doSearch();
                break;
            case R.id.txt_tab3:
                searchType = Constant.STATUS_2;
                changeTabColor();
                doSearch();
                break;
            case R.id.img_change_source:
                // 切换搜索引擎
                ToastUtil.showMessage(getString(R.string.txt_change_search));
                if (Constant.SEARCH_TYPE.equals(Constant.SEARCH_TYPE_1)) {
                    Constant.SEARCH_TYPE = Constant.SEARCH_TYPE_2;
                    SharePreferencesUtil.setStringSharePreferences(SearchActivity.this, Constant.KEY_SEARCH_TYPE, Constant.SEARCH_TYPE_2);
                } else if (Constant.SEARCH_TYPE.equals(Constant.SEARCH_TYPE_2)) {
                    Constant.SEARCH_TYPE = Constant.SEARCH_TYPE_1;
                    SharePreferencesUtil.setStringSharePreferences(SearchActivity.this, Constant.KEY_SEARCH_TYPE, Constant.SEARCH_TYPE_1);
                }
                searchType = Constant.STATUS_0;
                changeTabColor();
                doSearch();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
