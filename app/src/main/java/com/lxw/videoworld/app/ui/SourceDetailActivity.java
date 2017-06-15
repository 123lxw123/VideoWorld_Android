package com.lxw.videoworld.app.ui;

import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.lxw.videoworld.R;
import com.lxw.videoworld.app.config.Constant;
import com.lxw.videoworld.app.model.SourceDetailModel;
import com.lxw.videoworld.app.model.SourceInfoModel;
import com.lxw.videoworld.framework.base.BaseActivity;
import com.lxw.videoworld.framework.image.ImageManager;
import com.lxw.videoworld.framework.util.ValueUtil;
import com.lxw.videoworld.framework.weixin.WXShareDialog;
import com.lxw.videoworld.framework.widget.SourceLinkDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SourceDetailActivity extends BaseActivity {

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
    @BindView(R.id.edit_empty)
    EditText editEmpty;
    private boolean flag_expand = false;// 简介展开收起标识
    private int width;
    private int height;
    private int picHeight;
    private List<String> images;
    private SourceDetailModel sourceDetailModel;
    private List<SourceInfoModel> sourceInfoModels = new ArrayList<>();
    private BaseQuickAdapter<SourceInfoModel, BaseViewHolder> sourceInfoAdapter;
    private BaseQuickAdapter<String, BaseViewHolder> sourceLinkAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_source_detail);
        ButterKnife.bind(this);
        sourceDetailModel = (SourceDetailModel) getIntent().getSerializableExtra("sourceDetailModel");
        if (sourceDetailModel != null) {
            initDatas();
            initViews();
        }
    }

    private void initViews() {
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SourceDetailActivity.this.finish();
            }
        });
        images = ValueUtil.string2list(sourceDetailModel.getImages());

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
                WXShareDialog dlg = new WXShareDialog(SourceDetailActivity.this, sourceDetailModel.getUrl(), wechatTitle, wechatContent, imageUrl);
                dlg.show();
            }
        });
        // 图片
        if (images != null && images.size() > 0) {
            if (!TextUtils.isEmpty(sourceDetailModel.getCategory()) && sourceDetailModel.getCategory().equals(Constant.CATEGORY_21)) {
                picHeight = width * 3 / 4;
            } else {
                picHeight = width * 4 / 3;
            }
            imgPicture.getLayoutParams().height = picHeight;
            viewEmpty.getLayoutParams().height = picHeight;
            ImageManager.getInstance().loadImage(this, imgPicture, images.get(0));
        }
        if (!TextUtils.isEmpty(sourceDetailModel.getTitle())) {
            txtTitle.setText(sourceDetailModel.getTitle());
            txtTitle.setVisibility(View.VISIBLE);
        }

        if (sourceInfoModels.size() > 0) {
            recyclerviewInfo.setLayoutManager(new LinearLayoutManager(this));
            recyclerviewInfo.setNestedScrollingEnabled(false);
            sourceInfoAdapter = new BaseQuickAdapter<SourceInfoModel, BaseViewHolder>(R.layout.item_source_info, sourceInfoModels) {
                @Override
                protected void convert(BaseViewHolder helper, SourceInfoModel item) {
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

        if (images != null && images.size() > 1) {
            for (int i = 1; i < images.size(); i++) {
                ImageView imageView = new ImageView(this);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, picHeight);
//                imageView.getLayoutParams().width = width;
//                imageView.getLayoutParams().height = picHeight;
                imageView.setLayoutParams(params);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                ImageManager.getInstance().loadImage(this, imageView, images.get(i));
                llPicture.addView(imageView);
            }
        }
        List<String> links = ValueUtil.string2list(sourceDetailModel.getLinks());
        if (links != null && links.size() > 0) {
            recyclerviewLink.setNestedScrollingEnabled(false);
            recyclerviewLink.setLayoutManager(new LinearLayoutManager(this));
            sourceLinkAdapter = new BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_source_link, links) {
                @Override
                protected void convert(BaseViewHolder helper, String item) {
                    helper.setText(R.id.txt_link, item);
                }
            };
            sourceLinkAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {

                @Override
                public void onItemClick(final BaseQuickAdapter adapter, View view, final int position) {
                    // TODO
                    SourceLinkDialog dialog = new SourceLinkDialog(SourceDetailActivity.this, (String) adapter.getData().get(position));
                    dialog.show();

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
            }
        });

        // 顶部获取焦点
        editEmpty.requestFocus();
        if(picHeight > 0){
            scrollView.scrollBy(0, picHeight / 2);
        }
    }

    private void initDatas() {
        if (!TextUtils.isEmpty(sourceDetailModel.getDate())) {
            SourceInfoModel sourceInfoModel = new SourceInfoModel();
            try {
                String date = sourceDetailModel.getDate().substring(0, 4) + "-" + sourceDetailModel.getDate().substring(4, 6) + "-" + sourceDetailModel.getDate().substring(6, 8);
                sourceInfoModel.setKey(getString(R.string.txt_date));
                sourceInfoModel.setValue(date);
                sourceInfoModels.add(sourceInfoModel);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (!TextUtils.isEmpty(sourceDetailModel.getName())) {
            SourceInfoModel sourceInfoModel = new SourceInfoModel();
            sourceInfoModel.setKey(getString(R.string.txt_name));
            sourceInfoModel.setValue(sourceDetailModel.getName());
            sourceInfoModels.add(sourceInfoModel);
        }
        if (!TextUtils.isEmpty(sourceDetailModel.getTranslateName())) {
            SourceInfoModel sourceInfoModel = new SourceInfoModel();
            sourceInfoModel.setKey(getString(R.string.txt_translate_name));
            sourceInfoModel.setValue(sourceDetailModel.getTranslateName());
            sourceInfoModels.add(sourceInfoModel);
        }
        if (!TextUtils.isEmpty(sourceDetailModel.getYear())) {
            SourceInfoModel sourceInfoModel = new SourceInfoModel();
            sourceInfoModel.setKey(getString(R.string.txt_year));
            sourceInfoModel.setValue(sourceDetailModel.getYear());
            sourceInfoModels.add(sourceInfoModel);
        }
        if (!TextUtils.isEmpty(sourceDetailModel.getArea())) {
            SourceInfoModel sourceInfoModel = new SourceInfoModel();
            sourceInfoModel.setKey(getString(R.string.txt_area));
            sourceInfoModel.setValue(sourceDetailModel.getArea());
            sourceInfoModels.add(sourceInfoModel);
        }
        if (!TextUtils.isEmpty(sourceDetailModel.getStyle())) {
            SourceInfoModel sourceInfoModel = new SourceInfoModel();
            sourceInfoModel.setKey(getString(R.string.txt_style));
            sourceInfoModel.setValue(sourceDetailModel.getStyle());
            sourceInfoModels.add(sourceInfoModel);
        }
        if (!TextUtils.isEmpty(sourceDetailModel.getLanguage())) {
            SourceInfoModel sourceInfoModel = new SourceInfoModel();
            sourceInfoModel.setKey(getString(R.string.txt_language));
            sourceInfoModel.setValue(sourceDetailModel.getLanguage());
            sourceInfoModels.add(sourceInfoModel);
        }
        if (!TextUtils.isEmpty(sourceDetailModel.getSubtitles())) {
            SourceInfoModel sourceInfoModel = new SourceInfoModel();
            sourceInfoModel.setKey(getString(R.string.txt_subtitles));
            sourceInfoModel.setValue(sourceDetailModel.getSubtitles());
            sourceInfoModels.add(sourceInfoModel);
        }
        if (!TextUtils.isEmpty(sourceDetailModel.getReleaseDate())) {
            SourceInfoModel sourceInfoModel = new SourceInfoModel();
            sourceInfoModel.setKey(getString(R.string.txt_release_date));
            sourceInfoModel.setValue(sourceDetailModel.getReleaseDate());
            sourceInfoModels.add(sourceInfoModel);
        }
        if (!TextUtils.isEmpty(sourceDetailModel.getImdbScore())) {
            SourceInfoModel sourceInfoModel = new SourceInfoModel();
            sourceInfoModel.setKey(getString(R.string.txt_imdb_score));
            if (!TextUtils.isEmpty(sourceDetailModel.getImdbIntro())) {
                sourceInfoModel.setValue(sourceDetailModel.getImdbScore() + " (" + sourceDetailModel.getImdbIntro() + ")");
            } else {
                sourceInfoModel.setValue(sourceDetailModel.getImdbScore());
            }
            sourceInfoModels.add(sourceInfoModel);
        }
        if (!TextUtils.isEmpty(sourceDetailModel.getDoubanScore())) {
            SourceInfoModel sourceInfoModel = new SourceInfoModel();
            sourceInfoModel.setKey(getString(R.string.txt_douban_score));
            if (!TextUtils.isEmpty(sourceDetailModel.getDoubanIntro())) {
                sourceInfoModel.setValue(sourceDetailModel.getDoubanScore() + " (" + sourceDetailModel.getDoubanIntro() + ")");
            } else {
                sourceInfoModel.setValue(sourceDetailModel.getDoubanScore());
            }
            sourceInfoModels.add(sourceInfoModel);
        }
        if (!TextUtils.isEmpty(sourceDetailModel.getFileFormat())) {
            SourceInfoModel sourceInfoModel = new SourceInfoModel();
            sourceInfoModel.setKey(getString(R.string.txt_file_format));
            sourceInfoModel.setValue(sourceDetailModel.getFileFormat());
            sourceInfoModels.add(sourceInfoModel);
        }
        if (!TextUtils.isEmpty(sourceDetailModel.getFileSize())) {
            SourceInfoModel sourceInfoModel = new SourceInfoModel();
            sourceInfoModel.setKey(getString(R.string.txt_file_size));
            sourceInfoModel.setValue(sourceDetailModel.getFileSize());
            sourceInfoModels.add(sourceInfoModel);
        }
        if (!TextUtils.isEmpty(sourceDetailModel.getFileAmounts())) {
            SourceInfoModel sourceInfoModel = new SourceInfoModel();
            sourceInfoModel.setKey(getString(R.string.txt_file_amounts));
            sourceInfoModel.setValue(sourceDetailModel.getFileAmounts());
            sourceInfoModels.add(sourceInfoModel);
        }
        if (!TextUtils.isEmpty(sourceDetailModel.getFileLength())) {
            SourceInfoModel sourceInfoModel = new SourceInfoModel();
            sourceInfoModel.setKey(getString(R.string.txt_file_length));
            sourceInfoModel.setValue(sourceDetailModel.getFileLength());
            sourceInfoModels.add(sourceInfoModel);
        }
        if (!TextUtils.isEmpty(sourceDetailModel.getAuthor())) {
            SourceInfoModel sourceInfoModel = new SourceInfoModel();
            sourceInfoModel.setKey(getString(R.string.txt_author));
            sourceInfoModel.setValue(sourceDetailModel.getAuthor());
            sourceInfoModels.add(sourceInfoModel);
        }
        if (!TextUtils.isEmpty(sourceDetailModel.getDirector())) {
            SourceInfoModel sourceInfoModel = new SourceInfoModel();
            sourceInfoModel.setKey(getString(R.string.txt_director));
            sourceInfoModel.setValue(sourceDetailModel.getDirector());
            sourceInfoModels.add(sourceInfoModel);
        }
        if (!TextUtils.isEmpty(sourceDetailModel.getPerformer())) {
            SourceInfoModel sourceInfoModel = new SourceInfoModel();
            sourceInfoModel.setKey(getString(R.string.txt_performer));
            sourceInfoModel.setValue(sourceDetailModel.getPerformer());
            sourceInfoModels.add(sourceInfoModel);
        }
        if (!TextUtils.isEmpty(sourceDetailModel.getAwards())) {
            SourceInfoModel sourceInfoModel = new SourceInfoModel();
            sourceInfoModel.setKey(getString(R.string.txt_awards));
            sourceInfoModel.setValue(sourceDetailModel.getAwards());
            sourceInfoModels.add(sourceInfoModel);
        }
        if (!TextUtils.isEmpty(sourceDetailModel.getEpisodes())) {
            SourceInfoModel sourceInfoModel = new SourceInfoModel();
            sourceInfoModel.setKey(getString(R.string.txt_episodes));
            sourceInfoModel.setValue(sourceDetailModel.getEpisodes());
            sourceInfoModels.add(sourceInfoModel);
        }
        WindowManager wm = this.getWindowManager();
        width = wm.getDefaultDisplay().getWidth();
        height = wm.getDefaultDisplay().getHeight();
    }
}
