package com.lxw.videoworld.app.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gigamole.infinitecycleviewpager.HorizontalInfiniteCycleViewPager;
import com.lxw.videoworld.R;
import com.lxw.videoworld.app.adapter.QuickFragmentPageAdapter;
import com.lxw.videoworld.app.api.HttpHelper;
import com.lxw.videoworld.app.config.Constant;
import com.lxw.videoworld.app.model.SourceDetailModel;
import com.lxw.videoworld.app.model.SourceListModel;
import com.lxw.videoworld.framework.base.BaseActivity;
import com.lxw.videoworld.framework.http.BaseResponse;
import com.lxw.videoworld.framework.http.HttpManager;
import com.lxw.videoworld.framework.image.ImageManager;
import com.lxw.videoworld.framework.util.StringUtil;
import com.lxw.videoworld.framework.util.ValueUtil;
import com.lxw.videoworld.framework.widget.EmptyLoadMoreView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static butterknife.ButterKnife.bind;
import static com.lxw.videoworld.app.config.Constant.BANNER_LIMIT;

/**
 * 资源列表
 */
public class SourceTypeFragment extends Fragment {
    @BindView(R.id.recyclerview_source_type)
    RecyclerView recyclerviewSourceType;
    @BindView(R.id.refresh_source_type)
    SwipeRefreshLayout refreshSourceType;
    Unbinder unbinder;
    @BindView(R.id.ll_content)
    LinearLayout llContent;
    HorizontalInfiniteCycleViewPager viewpagerBanner;
    private View rootView;
    private QuickFragmentPageAdapter<SourceBannerFragment> bannerAdapter;
    private List<SourceBannerFragment> sourceBannerFragments = new ArrayList<>();
    private SourceListModel sourceListModel;
    private List<SourceDetailModel> sourceDetails = new ArrayList<>();
    private BaseQuickAdapter<SourceDetailModel, BaseViewHolder> sourceAdapter;
    private String sourceType;
    private String category;
    private String type;
    private boolean frag_refresh = true;
    private int page = 0;
    private int picWidth;
    private int picHeight;

    public SourceTypeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sourceType = getArguments().getString("sourceType");
        category = getArguments().getString("category");
        type = getArguments().getString("type");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null && !TextUtils.isEmpty(sourceType) && !TextUtils.isEmpty(category)) {
            rootView = inflater.inflate(R.layout.fragment_source_type, null);
            unbinder = ButterKnife.bind(this, rootView);
            // 计算列表 Item 图片展示宽高 比例 4:3
            WindowManager wm = this.getActivity().getWindowManager();
            picWidth = (wm.getDefaultDisplay().getWidth() - ValueUtil.dip2px(getActivity(), recyclerviewSourceType.getPaddingLeft() + recyclerviewSourceType.getPaddingRight()) -
                    ValueUtil.dip2px(getActivity(), (Constant.GRIDLAYOUTMANAGER_SPANCOUNT - 1) * 10)) / Constant.GRIDLAYOUTMANAGER_SPANCOUNT;
            if (!category.equals(Constant.CATEGORY_21)) {
                picHeight = picWidth * 4 / 3;
            } else {
                picHeight = picWidth * 3 / 4;
            }

            // 下拉刷新
            refreshSourceType.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    // TODO
                    page = 0;
                    frag_refresh = true;
                    sourceAdapter.setEnableLoadMore(true);
                    refreshSourceType.setRefreshing(false);
                    // 加载数据
                    getList(sourceType, category, type, "0", Constant.LIST_LIMIT + BANNER_LIMIT + "");

                }
            });
            // banner
            View bannerLayout = inflater.inflate(R.layout.layout_banner, null);
            viewpagerBanner = (HorizontalInfiniteCycleViewPager)bannerLayout.findViewById(R.id.viewpager_banner);
            viewpagerBanner.setScrollDuration(3000);
//            viewpagerBanner.setInterpolator(...);
            viewpagerBanner.setMediumScaled(true);
            viewpagerBanner.setMaxPageScale(0.8F);
            viewpagerBanner.setMinPageScale(0.5F);
            viewpagerBanner.setCenterPageScaleOffset(30.0F);
            viewpagerBanner.setMinPageScaleOffset(5.0F);
//            viewpagerBanner.setOnInfiniteCyclePageTransformListener(...);
            recyclerviewSourceType.setLayoutManager(new GridLayoutManager(SourceTypeFragment.this.getActivity(), Constant.GRIDLAYOUTMANAGER_SPANCOUNT));
//            recyclerviewSourceType.addItemDecoration(new DividerGridItemDecoration(getActivity()));
            // 列表适配器
            sourceAdapter = new BaseQuickAdapter<SourceDetailModel, BaseViewHolder>(R.layout.item_source_list, sourceDetails) {
                @Override
                protected void convert(BaseViewHolder helper, final SourceDetailModel item) {
                    // 设置列表 Item 图片、标题展示宽高
                    FrameLayout flPicture = ((FrameLayout) helper.getView(R.id.fl_picture));
                    flPicture.getLayoutParams().width = picWidth;
                    flPicture.getLayoutParams().height = picHeight;
                    TextView txtTitle = ((TextView) helper.getView(R.id.txt_title));
                    txtTitle.getLayoutParams().width = picWidth;
                    // 图片
                    List<String> images = ValueUtil.string2list(item.getImages());
                    if (images != null && images.size() > 0) {
                        ImageManager.getInstance().loadImage(SourceTypeFragment.this.getActivity(), (ImageView) helper.getView(R.id.img_picture), images.get(0));
                    }
                    // 标题
                    if(!TextUtils.isEmpty(item.getTitle())){
                        helper.setText(R.id.txt_title, item.getTitle());
                    }else if (!TextUtils.isEmpty(item.getName()) && StringUtil.isHasChinese(item.getName())) {
                        helper.setText(R.id.txt_title, item.getName());
                    } else if (!TextUtils.isEmpty(item.getTranslateName())) {
                        helper.setText(R.id.txt_title, item.getTranslateName());
                    }
                    // 评分
                    if (!TextUtils.isEmpty(item.getImdbScore())) {
                        helper.setText(R.id.txt_imdb, item.getImdbScore());
                        helper.setVisible(R.id.txt_imdb, true);
                    } else {
                        helper.setVisible(R.id.txt_imdb, false);
                    }
                    if (!TextUtils.isEmpty(item.getDoubanScore())) {
                        helper.setText(R.id.txt_douban, item.getDoubanScore());
                        helper.setVisible(R.id.txt_douban, true);
                    } else {
                        helper.setVisible(R.id.txt_douban, false);
                    }

                }
            };
            // item 点击事件
            sourceAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    // TODO

                }
            });
            // 加载更多
            sourceAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
                @Override
                public void onLoadMoreRequested() {
                    recyclerviewSourceType.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // TODO
                            frag_refresh = false;
                            // 加载数据
                            getList(sourceType, category, type, Constant.LIST_LIMIT * page + BANNER_LIMIT + "", Constant.LIST_LIMIT + "");
                        }

                    }, 500);
                }
            }, recyclerviewSourceType);
            sourceAdapter.setLoadMoreView(new EmptyLoadMoreView());
            // 动画
            sourceAdapter.openLoadAnimation();
            recyclerviewSourceType.setAdapter(sourceAdapter);
            // 加载数据
            getList(sourceType, category, type, "0", Constant.LIST_LIMIT + BANNER_LIMIT + "");
        }
        if (rootView != null) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }
            unbinder = bind(this, rootView);
        }

        return rootView;
    }

    public void getList(String sourceType, String category, String type, String start, String limit) {
        new HttpManager<SourceListModel>((BaseActivity) SourceTypeFragment.this.getActivity(), HttpHelper.getInstance().getList(sourceType, category, type, start, limit)) {

            @Override
            public void onSuccess(BaseResponse<SourceListModel> response) {
                sourceListModel = response.getResult();

//                SourceTypeFragment.this.getActivity().runOnUiThread(new Runnable() {
//                    public void run() {
                        if (sourceListModel != null && sourceListModel.getList() != null) {
                            page++;
                            List<SourceDetailModel> sources = sourceListModel.getList();
                            if (frag_refresh) {
                                if(sources.size() >= Constant.BANNER_LIMIT){
                                    sourceBannerFragments.clear();
                                    sourceDetails.clear();
                                    // banner 数据
                                    for(int i = 0; i < Constant.BANNER_LIMIT; i++){
                                        SourceBannerFragment fragment = new SourceBannerFragment();
                                        Bundle bundle = new Bundle();
                                        bundle.putSerializable("item", (Serializable)sources.get(i));
                                        fragment.setArguments(bundle);
                                        sourceBannerFragments.add(fragment);
                                    }
                                    // banner 初始化
                                    if(sourceBannerFragments.size() > 0){
                                        bannerAdapter = new QuickFragmentPageAdapter(SourceTypeFragment.this.getChildFragmentManager(), sourceBannerFragments, new String[sourceBannerFragments.size()]);
                                        viewpagerBanner.setAdapter(bannerAdapter);
                                        viewpagerBanner.startAutoScroll(true);
                                        if(sourceAdapter.getHeaderLayoutCount() == 0){
                                            ViewGroup parent = (ViewGroup) viewpagerBanner.getParent();
                                            if (parent != null) {
                                                parent.removeView(viewpagerBanner);
                                            }
                                            sourceAdapter.addHeaderView(viewpagerBanner);
                                        }
                                    }
                                }
                                // 列表数据
                                for(int j = Constant.BANNER_LIMIT; j < sources.size(); j++){
                                    sourceDetails.add(sources.get(j));
                                }
                                sourceAdapter.setNewData(sourceDetails);
                                llContent.setVisibility(View.VISIBLE);
                            } else {
                                sourceDetails.addAll(sources);
                                sourceAdapter.addData(sources);
                                sourceAdapter.loadMoreComplete();
                            }
                        } else {
                            sourceAdapter.loadMoreFail();
                        }
//                    }

//                });
            }

            @Override
            public void onFailure(BaseResponse<SourceListModel> response) {
                SourceTypeFragment.this.getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        sourceAdapter.loadMoreFail();
                    }

                });
            }
        }.doRequest();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(viewpagerBanner != null){
            viewpagerBanner.startAutoScroll(true);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(viewpagerBanner != null){
            viewpagerBanner.stopAutoScroll();
        }
    }

    @Override
    public void onDestroyView() {
        if (unbinder != null) {
            try {
                unbinder.unbind();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }
        super.onDestroyView();
    }
}
