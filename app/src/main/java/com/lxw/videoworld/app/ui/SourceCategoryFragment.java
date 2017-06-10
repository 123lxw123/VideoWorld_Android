package com.lxw.videoworld.app.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lxw.videoworld.R;
import com.lxw.videoworld.app.config.Constant;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentStatePagerItemAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 资源列表
 */
public class SourceCategoryFragment extends Fragment {

    @BindView(R.id.tab_source_category)
    SmartTabLayout tabSourceCategory;
    @BindView(R.id.viewpager_source_category)
    ViewPager viewpagerSourceCategory;
    Unbinder unbinder;
    private View rootView;
    private String category;

    public SourceCategoryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        category = getArguments().getString("category");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null && !TextUtils.isEmpty(category)) {
            rootView = inflater.inflate(R.layout.fragment_source_category, null);
            ButterKnife.bind(this, rootView);
            unbinder = ButterKnife.bind(this, rootView);

            FragmentStatePagerItemAdapter adapter = new FragmentStatePagerItemAdapter(
                    getChildFragmentManager(), createFragmentPagerItems());
            viewpagerSourceCategory.setAdapter(adapter);
            tabSourceCategory.setViewPager(viewpagerSourceCategory);
        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public FragmentPagerItems createFragmentPagerItems() {
        FragmentPagerItems fragmentPagerItems = null;
        switch (Constant.SOURCE_TYPE){
            case Constant.SOURCE_TYPE_1:
                fragmentPagerItems = FragmentPagerItems.with(this.getActivity())
                        .add(getString(R.string.txt_type1), SourceTypeFragment.class)
                        .add("title", SourceTypeFragment.class)
                        .create();
                break;
        }
        return fragmentPagerItems;
    }
}
