package com.lxw.videoworld.app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lxw.videoworld.R;
import com.lxw.videoworld.app.config.Constant;
import com.lxw.videoworld.app.model.SourceDetailModel;
import com.lxw.videoworld.framework.base.BaseFragment;
import com.lxw.videoworld.framework.image.ImageManager;
import com.lxw.videoworld.framework.util.StringUtil;
import com.lxw.videoworld.framework.util.ValueUtil;

import java.io.Serializable;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by lxw9047 on 2017/6/12.
 */

public class SourceBannerFragment extends BaseFragment {

    //    @BindView(R.id.img_banner)
//    ImageView imgBanner;
    Unbinder unbinder;
    @BindView(R.id.img_picture)
    ImageView imgPicture;
    @BindView(R.id.txt_imdb)
    TextView txtImdb;
    @BindView(R.id.txt_douban)
    TextView txtDouban;
    @BindView(R.id.fl_picture)
    FrameLayout flPicture;
    @BindView(R.id.txt_title)
    TextView txtTitle;
    @BindView(R.id.ll_imdb)
    LinearLayout llImdb;
    @BindView(R.id.ll_douban)
    LinearLayout llDouban;
    @BindView(R.id.ll_score)
    LinearLayout llScore;
    @BindView(R.id.ll_banner)
    LinearLayout llBanner;
    private View rootView;
    private String imgUrl;
    private SourceDetailModel item;
    private int picWidth;
    private int picHeight;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        item = (SourceDetailModel) getArguments().getSerializable("item");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null && item != null) {
            rootView = inflater.inflate(R.layout.item_source_banner, null);
            unbinder = ButterKnife.bind(this, rootView);
            WindowManager wm = this.getActivity().getWindowManager();
            picWidth = wm.getDefaultDisplay().getWidth() * 2 / 3;
            picHeight = wm.getDefaultDisplay().getHeight() / 2;
            if (!TextUtils.isEmpty(item.getCategory()) && (item.getCategory().equals(Constant.CATEGORY_21) || item.getCategory().equals(Constant.CATEGORY_19))) {
                picHeight = wm.getDefaultDisplay().getWidth() * 1 / 2;
            } else {
                picWidth = picHeight * 3 / 4;
            }
            FrameLayout flPicture = ((FrameLayout) rootView.findViewById(R.id.fl_picture));
            flPicture.getLayoutParams().width = picWidth;
            flPicture.getLayoutParams().height = picHeight;
            refreshUI(item);
        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        return rootView;
    }

    public void refreshUI(final SourceDetailModel item){
        this.item = item;
        if (rootView != null){
            // 图片
            List<String> images = ValueUtil.string2list(item.getImages());
            if (images != null && images.size() > 0) {
                ImageManager.getInstance().loadImage(SourceBannerFragment.this.getActivity(), imgPicture, images.get(0));
            }
            // 标题
            txtTitle.setTextSize(18);
            if (!TextUtils.isEmpty(item.getTitle())) {
                txtTitle.setText(item.getTitle());
            } else if (!TextUtils.isEmpty(item.getName()) && StringUtil.isHasChinese(item.getName())) {
                txtTitle.setText(item.getName());
            } else if (!TextUtils.isEmpty(item.getTranslateName())) {
                txtTitle.setText(item.getTranslateName());
            }
            // 评分
            if (!TextUtils.isEmpty(item.getImdbScore()) && item.getImdbScore().length() ==3) {
                txtImdb.setText(item.getImdbScore());
                llScore.setVisibility(View.VISIBLE);
                llImdb.setVisibility(View.VISIBLE);
            } else {
                llScore.setVisibility(View.GONE);
                llImdb.setVisibility(View.GONE);
            }
            if (!TextUtils.isEmpty(item.getDoubanScore()) && item.getDoubanScore().length() ==3) {

                txtDouban.setText(item.getDoubanScore());

                llScore.setVisibility(View.VISIBLE);
                llDouban.setVisibility(View.VISIBLE);
            } else {
                llScore.setVisibility(View.GONE);
                llDouban.setVisibility(View.GONE);
            }
            LinearLayout ll_banner = ((LinearLayout) rootView.findViewById(R.id.ll_banner));
            ll_banner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(SourceBannerFragment.this.getActivity(), SourceDetailActivity.class);
                    intent.putExtra("sourceDetailModel", (Serializable) item);
                    startActivity(intent);
                }
            });
        }
    }

}
