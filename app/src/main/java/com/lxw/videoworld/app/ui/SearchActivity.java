package com.lxw.videoworld.app.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lxw.videoworld.R;
import com.lxw.videoworld.app.api.HttpHelper;
import com.lxw.videoworld.app.config.Constant;
import com.lxw.videoworld.app.model.SearchListModel;
import com.lxw.videoworld.app.model.SearchModel;
import com.lxw.videoworld.app.model.SearchResultModel;
import com.lxw.videoworld.framework.base.BaseActivity;
import com.lxw.videoworld.framework.http.BaseResponse;
import com.lxw.videoworld.framework.http.HttpManager;
import com.lxw.videoworld.framework.util.GsonUtil;
import com.lxw.videoworld.framework.util.SharePreferencesUtil;
import com.lxw.videoworld.framework.util.ToastUtil;
import com.lxw.videoworld.framework.util.ValueUtil;
import com.lxw.videoworld.framework.widget.EmptyLoadMoreView;
import com.lxw.videoworld.framework.widget.SourceLinkDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchActivity extends BaseActivity implements SearchView.OnQueryTextListener, View.OnClickListener {


    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.serachview)
    SearchView serachview;
    @BindView(R.id.img_change_source)
    ImageView imgChangeSource;
    @BindView(R.id.recyclerview_keyword)
    RecyclerView recyclerviewKeyword;
    @BindView(R.id.recyclerview_result)
    RecyclerView recyclerviewResult;

    private final int IMG_SEARCH = 1000;
    private final int IMG_SEARCH_RESULT = 1001;
    private final int IMG_SEARCH_TIMEOUT = 1002;// 搜索超时
    private final int INTERVAL = 500; //输入时间间隔为300毫秒
    @BindView(R.id.txt_tab1)
    TextView txtTab1;
    @BindView(R.id.txt_tab2)
    TextView txtTab2;
    @BindView(R.id.txt_tab3)
    TextView txtTab3;
    private List<String> hotwords = new ArrayList<>();
    private BaseQuickAdapter<String, BaseViewHolder> hotwordAdapter;
    private List<SearchModel> searchModels = new ArrayList<>();
    private BaseQuickAdapter<SearchModel, BaseViewHolder> searchAdapter;
    private String keyword;
    private Timer timer;
    private int page = 1;
    private boolean flag_loadmore = false;
    private String searchType = Constant.STATUS_0;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == IMG_SEARCH) {
                getSearch();
            }else if (msg.what == IMG_SEARCH_RESULT) {
                getSearchResult();
            }else if (msg.what == IMG_SEARCH_TIMEOUT) {
                ToastUtil.showMessage(SearchActivity.this, getString(R.string.txt_search_timeout));
            }
        }
    };

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
        serachview.setOnQueryTextListener(this);//为搜索框设置监听事件
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
        if(hotwords != null && hotwords.size() > 0){
            recyclerviewKeyword.setLayoutManager(new GridLayoutManager(this, 4));
            hotwordAdapter = new BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_hotword, hotwords) {
                @Override
                protected void convert(BaseViewHolder helper, String item) {
                    helper.setText(R.id.txt_hotword, item);
                }
            };
            recyclerviewKeyword.setAdapter(hotwordAdapter);
            recyclerviewKeyword.setVisibility(View.VISIBLE);
        }else{
            recyclerviewKeyword.setVisibility(View.GONE);
        }
        // 搜索结果
        recyclerviewResult.setLayoutManager(new LinearLayoutManager(this));
        searchAdapter = new BaseQuickAdapter<SearchModel, BaseViewHolder>(R.layout.item_search, searchModels) {
            @Override
            protected void convert(BaseViewHolder helper, final SearchModel item) {
                helper.setText(R.id.txt_date, item.getDate());
                helper.setText(R.id.txt_hot, item.getHot());
                helper.setText(R.id.txt_size, item.getSize());
                helper.setOnClickListener(R.id.ll_thunder_ciliLink, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO
                        SourceLinkDialog dialog = new SourceLinkDialog(SearchActivity.this, (String) item.getCiliLink());
                        dialog.show();
                    }
                });
                if(Constant.SEARCH_TYPE_1.equals(Constant.SEARCH_TYPE_1)){
                    helper.setVisible(R.id.ll_amounts, false);
                    helper.setVisible(R.id.ll_thunder_link, false);
                }else if(Constant.SEARCH_TYPE_1.equals(Constant.SEARCH_TYPE_2)){
                    helper.setText(R.id.txt_amounts, item.getAmounts());
                    helper.setVisible(R.id.ll_amounts, true);
                    helper.setOnClickListener(R.id.ll_thunder_link, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // TODO
                            SourceLinkDialog dialog = new SourceLinkDialog(SearchActivity.this, (String) item.getThunderLink());
                            dialog.show();
                        }
                    });
                    helper.setVisible(R.id.ll_thunder_link, true);
                }
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
        searchAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                recyclerviewResult.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        flag_loadmore = true;
                        page++;
                        doSearch();
                    }

                }, 500);
            }
        }, recyclerviewResult);
        searchAdapter.setLoadMoreView(new EmptyLoadMoreView());
        // 动画
        searchAdapter.openLoadAnimation();
        recyclerviewResult.setAdapter(searchAdapter);
    }

    private void getSearch() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (!TextUtils.isEmpty(keyword)) {
            new HttpManager<String>(SearchActivity.this, HttpHelper.getInstance().getSearch(getSearchUrl(), keyword, Constant.SEARCH_TYPE)) {

                @Override
                public void onSuccess(BaseResponse<String> response) {
                    if (timer == null) {
                        timer = new Timer();
                    }
                    timer.schedule(new TimerTask() {
                        private int count = 0;
                        @Override
                        public void run() {
                            if(count < HttpHelper.DEFAULT_TIMEOUT){
                                count++;
                                mHandler.sendEmptyMessage(IMG_SEARCH_RESULT);
                            }else{
                                timer.cancel();
                                timer = null;
                                mHandler.sendEmptyMessage(IMG_SEARCH_TIMEOUT);
                            }
                        }
                    }, 1000, 1000);
                }

                @Override
                public void onFailure(BaseResponse<String> response) {
                }
            }.doRequest();
        } else {
            ToastUtil.showMessage(SearchActivity.this, getString(R.string.txt_search_empty_tips));
        }
    }

    private void getSearchResult() {
        new HttpManager<SearchResultModel>(SearchActivity.this, HttpHelper.getInstance().getSearchResult(getSearchUrl())) {

            @Override
            public void onSuccess(BaseResponse<SearchResultModel> response) {
                if (timer != null) {
                    timer.cancel();
                    timer = null;
                }
                if (response.getResult() != null) {
                    String list = response.getResult().getList();
                    SearchListModel searchListModel = GsonUtil.json2Bean(list, SearchListModel.class);
                }
            }

            @Override
            public void onFailure(BaseResponse<SearchResultModel> response) {

            }
        }.doRequest();
    }

    // 获取搜索的 Url
    public String getSearchUrl() {
        String searchUrl = "";
        if (Constant.SOURCE_TYPE.equals(Constant.SEARCH_TYPE_1)) {
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
        } else if (Constant.SOURCE_TYPE.equals(Constant.SEARCH_TYPE_2)) {
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
        keyword = newText;
        doSearch();
        return false;
    }

    public void doSearch() {
        if(!flag_loadmore){
            page = 1;
        }
        flag_loadmore = false;

        if (mHandler.hasMessages(IMG_SEARCH)) {
            mHandler.removeMessages(IMG_SEARCH);
        }
        mHandler.sendEmptyMessageDelayed(IMG_SEARCH, INTERVAL);
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

                break;
        }
    }

    @Override
    protected void onDestroy() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        super.onDestroy();
    }
}
