package com.lxw.videoworld.app.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.lxw.videoworld.R;
import com.lxw.videoworld.app.config.Constant;
import com.lxw.videoworld.app.model.SourceDetailModel;
import com.lxw.videoworld.framework.image.ImageManager;
import com.lxw.videoworld.framework.util.StringUtil;
import com.lxw.videoworld.framework.util.ValueUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by lxw9047 on 2017/6/12.
 */

public class SourceBannerFragment extends Fragment {

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
    private View rootView;
    private String imgUrl;
    private SourceDetailModel item;
    private int picWidth;
    private int picHeight;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        item = (SourceDetailModel)getArguments().getSerializable("item");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null && item != null) {
            rootView = inflater.inflate(R.layout.item_source_banner, null);
            unbinder = ButterKnife.bind(this, rootView);
            WindowManager wm = this.getActivity().getWindowManager();
            picWidth = wm.getDefaultDisplay().getWidth() * 2 / 3 ;
            picHeight = wm.getDefaultDisplay().getHeight() / 2 ;
            if (!TextUtils.isEmpty(item.getCategory()) && item.getCategory().equals(Constant.CATEGORY_21)) {
                picHeight = picWidth * 3 / 4;
            } else {
                picWidth = picHeight * 3 / 4;
            }
            FrameLayout flPicture = ((FrameLayout) rootView.findViewById(R.id.fl_picture));
            flPicture.getLayoutParams().width = picWidth;
            flPicture.getLayoutParams().height = picHeight;
            // 图片
            List<String> images = ValueUtil.string2list(item.getImages());
            if (images != null && images.size() > 0) {
                ImageManager.getInstance().loadImage(SourceBannerFragment.this.getActivity(), imgPicture, images.get(0));
            }
            // 标题
            txtTitle.setTextSize(18);
            if(!TextUtils.isEmpty(item.getTitle())){
                txtTitle.setText(item.getTitle());
            }else if (!TextUtils.isEmpty(item.getName()) && StringUtil.isHasChinese(item.getName())) {
                txtTitle.setText(item.getName());
            } else if (!TextUtils.isEmpty(item.getTranslateName())) {
                txtTitle.setText(item.getTranslateName());
            }
            // 评分
            if (!TextUtils.isEmpty(item.getImdbScore())) {
                txtImdb.setText(item.getImdbScore());
                txtImdb.setVisibility(View.VISIBLE);
            } else {
                txtImdb.setVisibility(View.GONE);
            }
            if (!TextUtils.isEmpty(item.getDoubanScore())) {
                txtDouban.setText(item.getDoubanScore());
                txtDouban.setVisibility(View.VISIBLE);
            } else {
                txtDouban.setVisibility(View.GONE);
            }
        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        return rootView;
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
