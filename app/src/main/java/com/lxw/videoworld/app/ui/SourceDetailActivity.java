package com.lxw.videoworld.app.ui;

import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.text.StaticLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lxw.videoworld.R;
import com.lxw.videoworld.app.model.SourceDetailModel;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SourceDetailActivity extends AppCompatActivity {

    @BindView(R.id.img_picture)
    ImageView imgPicture;
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
    private SourceDetailModel sourceDetailModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_source_detail);
        ButterKnife.bind(this);
        sourceDetailModel = (SourceDetailModel) getIntent().getSerializableExtra("sourceDetailModel");
        if (sourceDetailModel != null) {
            initViews();
        }
    }

    private void initViews() {
// 判断视频简介是否需要展开收起
        if(StringUtils.isNotEmpty(videoDetailModel.getIntro())){
            Layout layout = new StaticLayout(videoDetailModel.getIntro(), mDescTextView.getPaint(),
                    ScreenUtils.getScreenWidth(this) - mDescTextView.getPaddingLeft() - mDescTextView.getPaddingRight(),
                    Layout.Alignment.ALIGN_NORMAL, mDescTextView.getLineSpacingMultiplier(), mDescTextView.getLineSpacingExtra(), false);
            if (layout.getLineCount() > 3) {
                mExpandLinearLayout.setVisibility(View.VISIBLE);
            }
            mDescTextView.setText(videoDetailModel.getIntro());
            ll_desc.setVisibility(View.VISIBLE);
        }
    }
}
