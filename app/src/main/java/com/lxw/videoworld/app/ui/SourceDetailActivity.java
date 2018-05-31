package com.lxw.videoworld.app.ui;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;

import com.lxw.videoworld.R;
import com.lxw.videoworld.app.adapter.QuickFragmentPageAdapter;
import com.lxw.videoworld.app.adapter.SourceDetailFragmentPageAdapter;
import com.lxw.videoworld.app.config.Constant;
import com.lxw.videoworld.app.model.SourceDetailModel;
import com.lxw.videoworld.framework.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SourceDetailActivity extends BaseActivity {

    @BindView(R.id.viewpager_detail)
    ViewPager viewpagerDetail;

    private String url;
    private String sourceType;
    private boolean isRefreshDetail;
    private SourceDetailModel sourceDetailModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_source_detail);
        ButterKnife.bind(this);
        url = getIntent().getStringExtra("url");
        sourceType = getIntent().getStringExtra("sourceType");
        isRefreshDetail = getIntent().getBooleanExtra("isRefreshDetail", false);
        if (!isRefreshDetail && !TextUtils.isEmpty(url) && !TextUtils.isEmpty(sourceType)) {
            SourceDetailFragment fragment = new SourceDetailFragment();
            Bundle bundle = new Bundle();
            bundle.putString("url", url);
            bundle.putString("sourceType", sourceType);
            bundle.putBoolean("isRefreshDetail", isRefreshDetail);
            fragment.setArguments(bundle);
            List<SourceDetailFragment> fragments = new ArrayList<>();
            fragments.add(fragment);
            String[] titles = new String[]{""};
            viewpagerDetail.setAdapter(new QuickFragmentPageAdapter<SourceDetailFragment>(getSupportFragmentManager(), fragments, titles));
        } else {
            sourceDetailModel = (SourceDetailModel) getIntent().getSerializableExtra("sourceDetailModel");
            List<SourceDetailModel> detailModels = new ArrayList<>();
            if (Constant.detailModels!= null) {
                for(int i = 0; i < Constant.detailModels.size(); i++) {
                    detailModels.add(Constant.detailModels.get(i));
                }
            }
            viewpagerDetail.setAdapter(new SourceDetailFragmentPageAdapter(getSupportFragmentManager(), detailModels, isRefreshDetail));
            int index = 0;
            for (int i = 0; i < Constant.detailModels.size(); i++) {
                if (!TextUtils.isEmpty(url)) {
                    if (url.equals(Constant.detailModels.get(i).getUrl()))
                        index = i;
                } else {
                    if (sourceDetailModel.getUrl().equals(Constant.detailModels.get(i).getUrl()))
                        index = i;
                }
            }
            viewpagerDetail.setCurrentItem(index, false);
        }
    }
}
