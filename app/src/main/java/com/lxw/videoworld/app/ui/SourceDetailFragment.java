package com.lxw.videoworld.app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.lxw.videoworld.R;
import com.lxw.videoworld.app.api.HttpHelper;
import com.lxw.videoworld.app.config.Constant;
import com.lxw.videoworld.app.model.BaseResponse;
import com.lxw.videoworld.app.model.KeyValueModel;
import com.lxw.videoworld.app.model.SourceCollectModel;
import com.lxw.videoworld.app.model.SourceDetailModel;
import com.lxw.videoworld.app.service.DownloadManager;
import com.lxw.videoworld.app.util.RealmUtil;
import com.lxw.videoworld.app.widget.SourceLinkDialog;
import com.lxw.videoworld.framework.base.BaseFragment;
import com.lxw.videoworld.framework.http.HttpManager;
import com.lxw.videoworld.framework.image.ImageManager;
import com.lxw.videoworld.framework.util.ValueUtil;
import com.lxw.videoworld.framework.weixin.WXShareDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class SourceDetailFragment extends BaseFragment {

    @BindView(R.id.img_picture)
    KenBurnsView imgPicture;
    @BindView(R.id.recyclerview_info)
    RecyclerView recyclerviewInfo;
    @BindView(R.id.txt_desc)
    TextView txtDesc;
    @BindView(R.id.img_expand)
    ImageView imgExpand;
    @BindView(R.id.ll_expand)
    LinearLayout llExpand;
    @BindView(R.id.ll_desc)
    LinearLayout llDesc;
    @BindView(R.id.ll_picture)
    LinearLayout llPicture;
    @BindView(R.id.recyclerview_link)
    RecyclerView recyclerviewLink;
    @BindView(R.id.scrollView)
    NestedScrollView scrollView;
    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.txt_title)
    TextView txtTitle;
    @BindView(R.id.txt_detail)
    TextView txtDetail;
    @BindView(R.id.button_share)
    Button buttonShare;
    @BindView(R.id.fl_content)
    LinearLayout flContent;
    @BindView(R.id.view_empty)
    View viewEmpty;
    @BindView(R.id.img_expand_close)
    ImageView imgExpandClose;
    @BindView(R.id.img_play_video)
    ImageView imgPlayVideo;
    @BindView(R.id.button_collect)
    Button buttonCollect;
    Unbinder unbinder;
    private View rootView;
    private boolean flag_expand = false;// 简介展开收起标识
    private int width;
    private int height;
    private int picHeight;
    private List<String> images;
    private SourceDetailModel sourceDetailModel;
    private List<KeyValueModel> keyValueModels = new ArrayList<>();
    private BaseQuickAdapter<KeyValueModel, BaseViewHolder> sourceInfoAdapter;
    private BaseQuickAdapter<String, BaseViewHolder> sourceLinkAdapter;

    private String url;
    private String sourceType;
    private boolean isRefreshDetail;
    private boolean isCollected = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_source_detail, container, false);
            ButterKnife.bind(this, rootView);
            url = getArguments().getString("url");
            sourceType = getArguments().getString("sourceType");
            isRefreshDetail = getArguments().getBoolean("isRefreshDetail");
            imgBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SourceDetailFragment.this.getActivity().finish();
                }
            });
            if (isRefreshDetail || !TextUtils.isEmpty(url) && !TextUtils.isEmpty(sourceType)) {
                getSourceDetail();
            } else {
                sourceDetailModel = (SourceDetailModel) getArguments().getSerializable("sourceDetailModel");
                if (sourceDetailModel != null) {
                    initDatas();
                    initViews();
                }
            }
        }
        if (rootView != null) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }
        }
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    private void getSourceDetail() {
        new HttpManager<SourceDetailModel>(SourceDetailFragment.this.getActivity(), HttpHelper.getInstance().getDetail(url, sourceType)) {

            @Override
            public void onSuccess(BaseResponse<SourceDetailModel> response) {
                sourceDetailModel = response.getResult();
                if (sourceDetailModel != null) {
                    initDatas();
                    initViews();
                    RealmUtil.copyOrUpdateDetailModel(sourceDetailModel);
                }
            }

            @Override
            public void onFailure(BaseResponse<SourceDetailModel> response) {
                SourceDetailModel sourceDetailModel = RealmUtil.queryDetailModelByUrl(url);
                if (sourceDetailModel != null) {
                    initDatas();
                    initViews();
                }
            }
        }.doRequest();
    }

    private void initViews() {
        // 收藏
        final SourceCollectModel resultSourceCollectModel = RealmUtil.queryCollectModelByUrl(sourceDetailModel.getUrl());
        if (resultSourceCollectModel != null) {
            if (resultSourceCollectModel.getStatus().equals(Constant.STATUS_0)) {
                isCollected = false;
                buttonCollect.setBackgroundResource(R.drawable.svg_collect);
            } else {
                isCollected = true;
                buttonCollect.setBackgroundResource(R.drawable.svg_collected);
            }
        }
        buttonCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isCollected = !isCollected;
                SourceCollectModel sourceCollectModel = new SourceCollectModel();
                sourceCollectModel.setUrl(sourceDetailModel.getUrl());
                sourceDetailModel.setSourceType(Constant.SOURCE_TYPE);
                sourceCollectModel.setSourceDetailModel(sourceDetailModel);
                if (isCollected) {
                    sourceCollectModel.setStatus(Constant.STATUS_1);
                    buttonCollect.setBackgroundResource(R.drawable.svg_collected);
                } else {
                    sourceCollectModel.setStatus(Constant.STATUS_0);
                    buttonCollect.setBackgroundResource(R.drawable.svg_collect);
                }
                RealmUtil.copyOrUpdateCollectModel(sourceCollectModel);
            }
        });

        images = ValueUtil.string2list(sourceDetailModel.getImages());
        final List<String> tempList = ValueUtil.string2list(sourceDetailModel.getLinks());
        final ArrayList<String> links = new ArrayList<>();
        if (tempList != null) {
            for (int i = 0; i < tempList.size(); i++) {
                if (!TextUtils.isEmpty(tempList.get(i)) && !TextUtils.isEmpty(tempList.get(i).trim()))
                    if (Constant.SOURCE_TYPE.equals(Constant.SOURCE_TYPE_4)) {
                        if (tempList.get(i).contains("m3u8")) links.add(tempList.get(i).trim());
                    } else links.add(tempList.get(i).trim());
            }
        }
        buttonShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String wechatTitle = "", wechatContent = "", imageUrl = "";
                if (!TextUtils.isEmpty(sourceDetailModel.getTitle())) {
                    wechatTitle = sourceDetailModel.getTitle();
                }
                if (!TextUtils.isEmpty(sourceDetailModel.getIntro())) {
                    wechatContent = sourceDetailModel.getIntro();
                    if (wechatContent.length() > 50) {
                        wechatContent = wechatContent.substring(0, 50) + "...";
                    }
                }
                if (images != null && images.size() > 0) {
                    imageUrl = images.get(0);
                }
                WXShareDialog dlg = new WXShareDialog(SourceDetailFragment.this.getActivity(), sourceDetailModel.getUrl(), wechatTitle, wechatContent, imageUrl);
                dlg.show();
            }
        });
        // 图片
        if (images != null && images.size() > 0) {
            if (!TextUtils.isEmpty(sourceDetailModel.getCategory()) && (sourceDetailModel.getCategory().equals(Constant.CATEGORY_21) || sourceDetailModel.getCategory().equals(Constant.CATEGORY_19))) {
                picHeight = width * 3 / 4;
            } else {
                picHeight = width * 4 / 3;
            }
            imgPicture.getLayoutParams().height = picHeight;
            viewEmpty.getLayoutParams().height = picHeight;
            if (!sourceDetailModel.getCategory().equals(Constant.CATEGORY_21) && links.size() > 0) {
                imgPlayVideo.setVisibility(View.VISIBLE);
                imgPlayVideo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (Constant.SOURCE_TYPE.equals(Constant.SOURCE_TYPE_4)) {
                            Intent intent = new Intent(SourceDetailFragment.this.getActivity(), PlayVideoActivity.class);
                            intent.putExtra("url", links.get(0));
                            intent.putStringArrayListExtra("urlList", links);
                            startActivity(intent);
                        } else {
                            DownloadManager.addNormalTask(SourceDetailFragment.this.getActivity(), links.get(0), true, false, links);
                        }
                    }
                });
            } else imgPlayVideo.setVisibility(View.GONE);
            ImageManager.getInstance().loadImage(SourceDetailFragment.this.getContext(), imgPicture, images.get(0));
        }
        if (!TextUtils.isEmpty(sourceDetailModel.getTitle())) {
            txtTitle.setText(sourceDetailModel.getTitle());
            txtTitle.setVisibility(View.VISIBLE);
        }

        if (keyValueModels.size() > 0) {
            recyclerviewInfo.setLayoutManager(new LinearLayoutManager(SourceDetailFragment.this.getContext()));
            recyclerviewInfo.setNestedScrollingEnabled(false);
            sourceInfoAdapter = new BaseQuickAdapter<KeyValueModel, BaseViewHolder>(R.layout.item_source_info, keyValueModels) {
                @Override
                protected void convert(BaseViewHolder helper, KeyValueModel item) {
                    helper.setText(R.id.txt_key, item.getKey());
                    helper.setText(R.id.txt_value, item.getValue());
                }
            };
            recyclerviewInfo.setAdapter(sourceInfoAdapter);
            recyclerviewInfo.setVisibility(View.VISIBLE);
        }

        // 简介展开收起点击事件
        llExpand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 收起展开图片旋转
                if (flag_expand) {// 收起
                    imgExpand.setVisibility(View.VISIBLE);
                    imgExpandClose.setVisibility(View.GONE);
                    txtDesc.setMaxLines(3);
                } else {// 展开
                    imgExpand.setVisibility(View.GONE);
                    imgExpandClose.setVisibility(View.VISIBLE);
                    txtDesc.setMaxLines(Integer.MAX_VALUE);
                }
                txtDesc.setText(sourceDetailModel.getIntro());
                flag_expand = !flag_expand;
            }
        });
        // 判断简介是否需要展开收起
        if (!TextUtils.isEmpty(sourceDetailModel.getIntro())) {
            Layout layout = new StaticLayout(sourceDetailModel.getIntro(), txtDesc.getPaint(),
                    width - txtDesc.getPaddingLeft() - txtDesc.getPaddingRight(),
                    Layout.Alignment.ALIGN_NORMAL, txtDesc.getLineSpacingMultiplier(), txtDesc.getLineSpacingExtra(), false);
            if (layout.getLineCount() > 3) {
                llExpand.setVisibility(View.VISIBLE);
            }
            txtDesc.setText(sourceDetailModel.getIntro());
            llDesc.setVisibility(View.VISIBLE);
        }

        if (images != null && images.size() > 1 && images.get(1).contains("http")) {
//            for (int i = 1; i < images.size(); i++) {
            ImageView imageView = new ImageView(SourceDetailFragment.this.getContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, picHeight);
//                imageView.getLayoutParams().width = width;
//                imageView.getLayoutParams().height = picHeight;
            imageView.setLayoutParams(params);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            llPicture.addView(imageView);
            ImageManager.getInstance().loadImage(SourceDetailFragment.this.getContext(), imageView, images.get(1));
//            }
            llPicture.setVisibility(View.VISIBLE);
        }
        if (links.size() > 0) {
            recyclerviewLink.setNestedScrollingEnabled(false);
            recyclerviewLink.setLayoutManager(new LinearLayoutManager(SourceDetailFragment.this.getContext()));
            sourceLinkAdapter = new BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_source_link, links) {
                @Override
                protected void convert(BaseViewHolder helper, String item) {
                    if (links.size() > 3) {
                        helper.setText(R.id.txt_link, "( " + (helper.getAdapterPosition() + 1) + " ) " + item);
                    } else helper.setText(R.id.txt_link, item);
                }
            };
            sourceLinkAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {

                @Override
                public void onItemClick(final BaseQuickAdapter adapter, View view, final int position) {
                    // TODO
                    if (Constant.SOURCE_TYPE.equals(Constant.SOURCE_TYPE_4)) {
                        Intent intent = new Intent(SourceDetailFragment.this.getActivity(), PlayVideoActivity.class);
                        intent.putExtra("url", (String) adapter.getData().get(position));
                        intent.putStringArrayListExtra("urlList", links);
                        startActivity(intent);
                    } else if (sourceDetailModel.getCategory().equals(Constant.CATEGORY_21)) {
                        SourceLinkDialog dialog = new SourceLinkDialog(SourceDetailFragment.this.getActivity(), (String) adapter.getData().get(position), false);
                        dialog.show();
                    } else {
                        SourceLinkDialog dialog = new SourceLinkDialog(SourceDetailFragment.this.getActivity(), (String) adapter.getData().get(position), true, links);
                        dialog.show();
                    }
                }
            });
            recyclerviewLink.setAdapter(sourceLinkAdapter);
            recyclerviewLink.setVisibility(View.VISIBLE);
        }
        // 图片滑动效果
        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                imgPicture.scrollBy(0, (scrollY - oldScrollY) / 2);
                Log.d("scrollView", imgPicture.getHeight() + "====" + scrollY);
            }
        });

        if (picHeight > 0) {
            scrollView.scrollBy(0, picHeight / 2);
        }
    }

    private void initDatas() {
        if (!TextUtils.isEmpty(sourceDetailModel.getDate())) {
            KeyValueModel keyValueModel = new KeyValueModel();
            try {
                String date = sourceDetailModel.getDate().substring(0, 4) + "-" + sourceDetailModel.getDate().substring(4, 6) + "-" + sourceDetailModel.getDate().substring(6, 8);
                keyValueModel.setKey(getString(R.string.txt_date));
                keyValueModel.setValue(date);
                keyValueModels.add(keyValueModel);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (!TextUtils.isEmpty(sourceDetailModel.getName())) {
            KeyValueModel keyValueModel = new KeyValueModel();
            keyValueModel.setKey(getString(R.string.txt_name));
            keyValueModel.setValue(sourceDetailModel.getName());
            keyValueModels.add(keyValueModel);
        }
        if (!TextUtils.isEmpty(sourceDetailModel.getTranslateName())) {
            KeyValueModel keyValueModel = new KeyValueModel();
            keyValueModel.setKey(getString(R.string.txt_translate_name));
            keyValueModel.setValue(sourceDetailModel.getTranslateName());
            keyValueModels.add(keyValueModel);
        }
        if (!TextUtils.isEmpty(sourceDetailModel.getYear())) {
            KeyValueModel keyValueModel = new KeyValueModel();
            keyValueModel.setKey(getString(R.string.txt_year));
            keyValueModel.setValue(sourceDetailModel.getYear());
            keyValueModels.add(keyValueModel);
        }
        if (!TextUtils.isEmpty(sourceDetailModel.getArea())) {
            KeyValueModel keyValueModel = new KeyValueModel();
            keyValueModel.setKey(getString(R.string.txt_area));
            keyValueModel.setValue(sourceDetailModel.getArea());
            keyValueModels.add(keyValueModel);
        }
        if (!TextUtils.isEmpty(sourceDetailModel.getStyle())) {
            KeyValueModel keyValueModel = new KeyValueModel();
            keyValueModel.setKey(getString(R.string.txt_style));
            keyValueModel.setValue(sourceDetailModel.getStyle());
            keyValueModels.add(keyValueModel);
        }
        if (!TextUtils.isEmpty(sourceDetailModel.getLanguage())) {
            KeyValueModel keyValueModel = new KeyValueModel();
            keyValueModel.setKey(getString(R.string.txt_language));
            keyValueModel.setValue(sourceDetailModel.getLanguage());
            keyValueModels.add(keyValueModel);
        }
        if (!TextUtils.isEmpty(sourceDetailModel.getSubtitles())) {
            KeyValueModel keyValueModel = new KeyValueModel();
            keyValueModel.setKey(getString(R.string.txt_subtitles));
            keyValueModel.setValue(sourceDetailModel.getSubtitles());
            keyValueModels.add(keyValueModel);
        }
        if (!TextUtils.isEmpty(sourceDetailModel.getReleaseDate())) {
            KeyValueModel keyValueModel = new KeyValueModel();
            keyValueModel.setKey(getString(R.string.txt_release_date));
            keyValueModel.setValue(sourceDetailModel.getReleaseDate());
            keyValueModels.add(keyValueModel);
        }
        if (!TextUtils.isEmpty(sourceDetailModel.getImdbScore())) {
            KeyValueModel keyValueModel = new KeyValueModel();
            keyValueModel.setKey(getString(R.string.txt_imdb_score));
            if (!TextUtils.isEmpty(sourceDetailModel.getImdbIntro())) {
                keyValueModel.setValue(sourceDetailModel.getImdbScore() + " (" + sourceDetailModel.getImdbIntro() + ")");
            } else {
                keyValueModel.setValue(sourceDetailModel.getImdbScore());
            }
            keyValueModels.add(keyValueModel);
        }
        if (!TextUtils.isEmpty(sourceDetailModel.getDoubanScore())) {
            KeyValueModel keyValueModel = new KeyValueModel();
            keyValueModel.setKey(getString(R.string.txt_douban_score));
            if (!TextUtils.isEmpty(sourceDetailModel.getDoubanIntro())) {
                keyValueModel.setValue(sourceDetailModel.getDoubanScore() + " (" + sourceDetailModel.getDoubanIntro() + ")");
            } else {
                keyValueModel.setValue(sourceDetailModel.getDoubanScore());
            }
            keyValueModels.add(keyValueModel);
        }
        if (!TextUtils.isEmpty(sourceDetailModel.getFileFormat())) {
            KeyValueModel keyValueModel = new KeyValueModel();
            keyValueModel.setKey(getString(R.string.txt_file_format));
            keyValueModel.setValue(sourceDetailModel.getFileFormat());
            keyValueModels.add(keyValueModel);
        }
        if (!TextUtils.isEmpty(sourceDetailModel.getFileSize())) {
            KeyValueModel keyValueModel = new KeyValueModel();
            keyValueModel.setKey(getString(R.string.txt_file_size));
            keyValueModel.setValue(sourceDetailModel.getFileSize());
            keyValueModels.add(keyValueModel);
        }
        if (!TextUtils.isEmpty(sourceDetailModel.getFileAmounts())) {
            KeyValueModel keyValueModel = new KeyValueModel();
            keyValueModel.setKey(getString(R.string.txt_file_amounts));
            keyValueModel.setValue(sourceDetailModel.getFileAmounts());
            keyValueModels.add(keyValueModel);
        }
        if (!TextUtils.isEmpty(sourceDetailModel.getFileLength())) {
            KeyValueModel keyValueModel = new KeyValueModel();
            keyValueModel.setKey(getString(R.string.txt_file_length));
            keyValueModel.setValue(sourceDetailModel.getFileLength());
            keyValueModels.add(keyValueModel);
        }
        if (!TextUtils.isEmpty(sourceDetailModel.getAuthor())) {
            KeyValueModel keyValueModel = new KeyValueModel();
            keyValueModel.setKey(getString(R.string.txt_author));
            keyValueModel.setValue(sourceDetailModel.getAuthor());
            keyValueModels.add(keyValueModel);
        }
        if (!TextUtils.isEmpty(sourceDetailModel.getDirector())) {
            KeyValueModel keyValueModel = new KeyValueModel();
            keyValueModel.setKey(getString(R.string.txt_director));
            keyValueModel.setValue(sourceDetailModel.getDirector());
            keyValueModels.add(keyValueModel);
        }
        if (!TextUtils.isEmpty(sourceDetailModel.getPerformer())) {
            KeyValueModel keyValueModel = new KeyValueModel();
            keyValueModel.setKey(getString(R.string.txt_performer));
            keyValueModel.setValue(sourceDetailModel.getPerformer());
            keyValueModels.add(keyValueModel);
        }
        if (!TextUtils.isEmpty(sourceDetailModel.getAwards())) {
            KeyValueModel keyValueModel = new KeyValueModel();
            keyValueModel.setKey(getString(R.string.txt_awards));
            keyValueModel.setValue(sourceDetailModel.getAwards());
            keyValueModels.add(keyValueModel);
        }
        if (!TextUtils.isEmpty(sourceDetailModel.getEpisodes())) {
            KeyValueModel keyValueModel = new KeyValueModel();
            keyValueModel.setKey(getString(R.string.txt_episodes));
            keyValueModel.setValue(sourceDetailModel.getEpisodes());
            keyValueModels.add(keyValueModel);
        }
        WindowManager wm = this.getActivity().getWindowManager();
        width = wm.getDefaultDisplay().getWidth();
        height = wm.getDefaultDisplay().getHeight();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
