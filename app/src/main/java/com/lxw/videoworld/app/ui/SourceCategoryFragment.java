package com.lxw.videoworld.app.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lxw.videoworld.R;
import com.lxw.videoworld.app.config.Constant;
import com.lxw.videoworld.framework.base.BaseFragment;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.Bundler;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 资源列表
 */
public class SourceCategoryFragment extends BaseFragment {

    @BindView(R.id.tab_source_category)
    SmartTabLayout tabSourceCategory;
    @BindView(R.id.viewpager_source_category)
    ViewPager viewpagerSourceCategory;
    Unbinder unbinder;
    private View rootView;
    private String tab;

    public SourceCategoryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tab = getArguments().getString("tab");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null && !TextUtils.isEmpty(tab)) {
            rootView = inflater.inflate(R.layout.fragment_source_category, null);
            unbinder = ButterKnife.bind(this, rootView);

            FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
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

    public FragmentPagerItems createFragmentPagerItems() {
        FragmentPagerItems fragmentPagerItems = null;
        switch (Constant.SOURCE_TYPE) {
            case Constant.SOURCE_TYPE_1:
                if (tab.equals(Constant.TAB_1)) {
                    fragmentPagerItems = FragmentPagerItems.with(this.getActivity())
                            .add(getString(R.string.txt_type0), SourceTypeFragment.class, new Bundler().putString("sourceType", Constant.SOURCE_TYPE_1).putString("category", Constant.TAB_1).putString("type", Constant.TYPE_0).get())
                            .add(getString(R.string.txt_type1), SourceTypeFragment.class, new Bundler().putString("sourceType", Constant.SOURCE_TYPE_1).putString("category", Constant.CATEGORY_1).get())
                            .add(getString(R.string.txt_type2), SourceTypeFragment.class, new Bundler().putString("sourceType", Constant.SOURCE_TYPE_1).putString("category", Constant.CATEGORY_2).get())
                            .add(getString(R.string.txt_type3), SourceTypeFragment.class, new Bundler().putString("sourceType", Constant.SOURCE_TYPE_1).putString("category", Constant.CATEGORY_3).get())
                            .add(getString(R.string.txt_type4), SourceTypeFragment.class, new Bundler().putString("sourceType", Constant.SOURCE_TYPE_1).putString("category", Constant.CATEGORY_4).get())
                            .add(getString(R.string.txt_type5), SourceTypeFragment.class, new Bundler().putString("sourceType", Constant.SOURCE_TYPE_1).putString("category", Constant.CATEGORY_5).get())
                            .add(getString(R.string.txt_type6), SourceTypeFragment.class, new Bundler().putString("sourceType", Constant.SOURCE_TYPE_1).putString("category", Constant.CATEGORY_6).get())
                            .add(getString(R.string.txt_type7), SourceTypeFragment.class, new Bundler().putString("sourceType", Constant.SOURCE_TYPE_1).putString("category", Constant.CATEGORY_7).get())
                            .add(getString(R.string.txt_type8), SourceTypeFragment.class, new Bundler().putString("sourceType", Constant.SOURCE_TYPE_1).putString("category", Constant.CATEGORY_8).get())
                            .add(getString(R.string.txt_type9), SourceTypeFragment.class, new Bundler().putString("sourceType", Constant.SOURCE_TYPE_1).putString("category", Constant.CATEGORY_9).get())
                            .add(getString(R.string.txt_type10), SourceTypeFragment.class, new Bundler().putString("sourceType", Constant.SOURCE_TYPE_1).putString("category", Constant.CATEGORY_10).get())
                            .create();
                } else if (tab.equals(Constant.TAB_3)) {
                    fragmentPagerItems = FragmentPagerItems.with(this.getActivity())
                            .add(getString(R.string.txt_type0), SourceTypeFragment.class, new Bundler().putString("sourceType", Constant.SOURCE_TYPE_3).putString("category", Constant.CATEGORY_19).get())
                            .add(getString(R.string.txt_type17), SourceTypeFragment.class, new Bundler().putString("sourceType", Constant.SOURCE_TYPE_3).putString("category", Constant.CATEGORY_19).putString("type", Constant.TYPE_6).get())
                            .add(getString(R.string.txt_type18), SourceTypeFragment.class, new Bundler().putString("sourceType", Constant.SOURCE_TYPE_3).putString("category", Constant.CATEGORY_19).putString("type", Constant.TYPE_7).get())
                            .add(getString(R.string.txt_type19), SourceTypeFragment.class, new Bundler().putString("sourceType", Constant.SOURCE_TYPE_3).putString("category", Constant.CATEGORY_19).putString("type", Constant.TYPE_8).get())
                            .create();
                } else if (tab.equals(Constant.TAB_5)) {
                    fragmentPagerItems = FragmentPagerItems.with(this.getActivity())
                            .add(getString(R.string.txt_type0), SourceTypeFragment.class, new Bundler().putString("sourceType", Constant.SOURCE_TYPE_3).putString("category", Constant.CATEGORY_21).get())
                            .add(getString(R.string.txt_type16), SourceTypeFragment.class, new Bundler().putString("sourceType", Constant.SOURCE_TYPE_3).putString("category", Constant.CATEGORY_21).putString("type", Constant.TYPE_11).get())
                            .add(getString(R.string.txt_type15), SourceTypeFragment.class, new Bundler().putString("sourceType", Constant.SOURCE_TYPE_3).putString("category", Constant.CATEGORY_21).putString("type", Constant.TYPE_10).get())
                            .add(getString(R.string.txt_type14), SourceTypeFragment.class, new Bundler().putString("sourceType", Constant.SOURCE_TYPE_3).putString("category", Constant.CATEGORY_21).putString("type", Constant.TYPE_9).get())
                            .create();
                }

                break;
            case Constant.SOURCE_TYPE_2:
                if (tab.equals(Constant.TAB_3)) {
                    fragmentPagerItems = FragmentPagerItems.with(this.getActivity())
                            .add(getString(R.string.txt_type0), SourceTypeFragment.class, new Bundler().putString("sourceType", Constant.SOURCE_TYPE_3).putString("category", Constant.CATEGORY_19).get())
                            .add(getString(R.string.txt_type17), SourceTypeFragment.class, new Bundler().putString("sourceType", Constant.SOURCE_TYPE_3).putString("category", Constant.CATEGORY_19).putString("type", Constant.TYPE_6).get())
                            .add(getString(R.string.txt_type18), SourceTypeFragment.class, new Bundler().putString("sourceType", Constant.SOURCE_TYPE_3).putString("category", Constant.CATEGORY_19).putString("type", Constant.TYPE_7).get())
                            .add(getString(R.string.txt_type19), SourceTypeFragment.class, new Bundler().putString("sourceType", Constant.SOURCE_TYPE_3).putString("category", Constant.CATEGORY_19).putString("type", Constant.TYPE_8).get())
                            .create();
                } else if (tab.equals(Constant.TAB_5)) {
                    fragmentPagerItems = FragmentPagerItems.with(this.getActivity())
                            .add(getString(R.string.txt_type0), SourceTypeFragment.class, new Bundler().putString("sourceType", Constant.SOURCE_TYPE_3).putString("category", Constant.CATEGORY_21).get())
                            .add(getString(R.string.txt_type16), SourceTypeFragment.class, new Bundler().putString("sourceType", Constant.SOURCE_TYPE_3).putString("category", Constant.CATEGORY_21).putString("type", Constant.TYPE_11).get())
                            .add(getString(R.string.txt_type15), SourceTypeFragment.class, new Bundler().putString("sourceType", Constant.SOURCE_TYPE_3).putString("category", Constant.CATEGORY_21).putString("type", Constant.TYPE_10).get())
                            .add(getString(R.string.txt_type14), SourceTypeFragment.class, new Bundler().putString("sourceType", Constant.SOURCE_TYPE_3).putString("category", Constant.CATEGORY_21).putString("type", Constant.TYPE_9).get())
                            .create();
                }

                break;
            case Constant.SOURCE_TYPE_3:
                if (tab.equals(Constant.TAB_1)) {
                    fragmentPagerItems = FragmentPagerItems.with(this.getActivity())
                            .add(getString(R.string.txt_type0), SourceTypeFragment.class, new Bundler().putString("sourceType", Constant.SOURCE_TYPE_3).putString("category", Constant.CATEGORY_17).get())
                            .add(getString(R.string.txt_type16), SourceTypeFragment.class, new Bundler().putString("sourceType", Constant.SOURCE_TYPE_3).putString("category", Constant.CATEGORY_17).putString("type", Constant.TYPE_2).get())
                            .add(getString(R.string.txt_type14), SourceTypeFragment.class, new Bundler().putString("sourceType", Constant.SOURCE_TYPE_3).putString("category", Constant.CATEGORY_17).putString("type", Constant.TYPE_1).get())
                            .create();
                } else if (tab.equals(Constant.TAB_2)) {
                    fragmentPagerItems = FragmentPagerItems.with(this.getActivity())
                            .add(getString(R.string.txt_type0), SourceTypeFragment.class, new Bundler().putString("sourceType", Constant.SOURCE_TYPE_3).putString("category", Constant.CATEGORY_18).get())
                            .add(getString(R.string.txt_type11), SourceTypeFragment.class, new Bundler().putString("sourceType", Constant.SOURCE_TYPE_3).putString("category", Constant.CATEGORY_18).putString("type", Constant.TYPE_3).get())
                            .add(getString(R.string.txt_type12), SourceTypeFragment.class, new Bundler().putString("sourceType", Constant.SOURCE_TYPE_3).putString("category", Constant.CATEGORY_18).putString("type", Constant.TYPE_5).get())
                            .add(getString(R.string.txt_type13), SourceTypeFragment.class, new Bundler().putString("sourceType", Constant.SOURCE_TYPE_3).putString("category", Constant.CATEGORY_18).putString("type", Constant.TYPE_4).get())
                            .create();
                } else if (tab.equals(Constant.TAB_3)) {
                    fragmentPagerItems = FragmentPagerItems.with(this.getActivity())
                            .add(getString(R.string.txt_type0), SourceTypeFragment.class, new Bundler().putString("sourceType", Constant.SOURCE_TYPE_3).putString("category", Constant.CATEGORY_19).get())
                            .add(getString(R.string.txt_type17), SourceTypeFragment.class, new Bundler().putString("sourceType", Constant.SOURCE_TYPE_3).putString("category", Constant.CATEGORY_19).putString("type", Constant.TYPE_6).get())
                            .add(getString(R.string.txt_type18), SourceTypeFragment.class, new Bundler().putString("sourceType", Constant.SOURCE_TYPE_3).putString("category", Constant.CATEGORY_19).putString("type", Constant.TYPE_7).get())
                            .add(getString(R.string.txt_type19), SourceTypeFragment.class, new Bundler().putString("sourceType", Constant.SOURCE_TYPE_3).putString("category", Constant.CATEGORY_19).putString("type", Constant.TYPE_8).get())
                            .create();
                } else if (tab.equals(Constant.TAB_5)) {
                    fragmentPagerItems = FragmentPagerItems.with(this.getActivity())
                            .add(getString(R.string.txt_type0), SourceTypeFragment.class, new Bundler().putString("sourceType", Constant.SOURCE_TYPE_3).putString("category", Constant.CATEGORY_21).get())
                            .add(getString(R.string.txt_type16), SourceTypeFragment.class, new Bundler().putString("sourceType", Constant.SOURCE_TYPE_3).putString("category", Constant.CATEGORY_21).putString("type", Constant.TYPE_11).get())
                            .add(getString(R.string.txt_type15), SourceTypeFragment.class, new Bundler().putString("sourceType", Constant.SOURCE_TYPE_3).putString("category", Constant.CATEGORY_21).putString("type", Constant.TYPE_10).get())
                            .add(getString(R.string.txt_type14), SourceTypeFragment.class, new Bundler().putString("sourceType", Constant.SOURCE_TYPE_3).putString("category", Constant.CATEGORY_21).putString("type", Constant.TYPE_9).get())
                            .create();
                }

                break;
            case Constant.SOURCE_TYPE_4:
                if (tab.equals(Constant.TAB_1)) {
                    fragmentPagerItems = FragmentPagerItems.with(this.getActivity())
                            .add(getString(R.string.txt_type0), SourceTypeFragment.class, new Bundler().putString("sourceType", Constant.SOURCE_TYPE_4).putString("category", Constant.CATEGORY_14).get())
                            .add(getString(R.string.txt_type1), SourceTypeFragment.class, new Bundler().putString("sourceType", Constant.SOURCE_TYPE_4).putString("category", Constant.CATEGORY_14).putString("type", Constant.TYPE_12).get())
                            .add(getString(R.string.txt_type2), SourceTypeFragment.class, new Bundler().putString("sourceType", Constant.SOURCE_TYPE_4).putString("category", Constant.CATEGORY_14).putString("type", Constant.TYPE_13).get())
                            .add(getString(R.string.txt_type5), SourceTypeFragment.class, new Bundler().putString("sourceType", Constant.SOURCE_TYPE_4).putString("category", Constant.CATEGORY_14).putString("type", Constant.TYPE_14).get())
                            .add(getString(R.string.txt_type3), SourceTypeFragment.class, new Bundler().putString("sourceType", Constant.SOURCE_TYPE_4).putString("category", Constant.CATEGORY_14).putString("type", Constant.TYPE_15).get())
                            .add(getString(R.string.txt_type4), SourceTypeFragment.class, new Bundler().putString("sourceType", Constant.SOURCE_TYPE_4).putString("category", Constant.CATEGORY_14).putString("type", Constant.TYPE_16).get())
                            .add(getString(R.string.txt_type8), SourceTypeFragment.class, new Bundler().putString("sourceType", Constant.SOURCE_TYPE_4).putString("category", Constant.CATEGORY_14).putString("type", Constant.TYPE_17).get())
                            .add(getString(R.string.txt_type9), SourceTypeFragment.class, new Bundler().putString("sourceType", Constant.SOURCE_TYPE_4).putString("category", Constant.CATEGORY_14).putString("type", Constant.TYPE_18).get())
                            .add(getString(R.string.txt_type20), SourceTypeFragment.class, new Bundler().putString("sourceType", Constant.SOURCE_TYPE_4).putString("category", Constant.CATEGORY_14).putString("type", Constant.TYPE_19).get())
                            .add(getString(R.string.txt_type21), SourceTypeFragment.class, new Bundler().putString("sourceType", Constant.SOURCE_TYPE_4).putString("category", Constant.CATEGORY_14).putString("type", Constant.TYPE_20).get())
                            .create();
                } else if (tab.equals(Constant.TAB_2)) {
                    fragmentPagerItems = FragmentPagerItems.with(this.getActivity())
                            .add(getString(R.string.txt_type0), SourceTypeFragment.class, new Bundler().putString("sourceType", Constant.SOURCE_TYPE_4).putString("category", Constant.CATEGORY_15).get())
                            .add(getString(R.string.txt_type22), SourceTypeFragment.class, new Bundler().putString("sourceType", Constant.SOURCE_TYPE_4).putString("category", Constant.CATEGORY_15).putString("type", Constant.TYPE_21).get())
                            .add(getString(R.string.txt_type23), SourceTypeFragment.class, new Bundler().putString("sourceType", Constant.SOURCE_TYPE_4).putString("category", Constant.CATEGORY_15).putString("type", Constant.TYPE_22).get())
                            .add(getString(R.string.txt_type24), SourceTypeFragment.class, new Bundler().putString("sourceType", Constant.SOURCE_TYPE_4).putString("category", Constant.CATEGORY_15).putString("type", Constant.TYPE_23).get())
                            .add(getString(R.string.txt_type25), SourceTypeFragment.class, new Bundler().putString("sourceType", Constant.SOURCE_TYPE_4).putString("category", Constant.CATEGORY_15).putString("type", Constant.TYPE_24).get())
                            .add(getString(R.string.txt_type26), SourceTypeFragment.class, new Bundler().putString("sourceType", Constant.SOURCE_TYPE_4).putString("category", Constant.CATEGORY_15).putString("type", Constant.TYPE_25).get())
                            .add(getString(R.string.txt_type27), SourceTypeFragment.class, new Bundler().putString("sourceType", Constant.SOURCE_TYPE_4).putString("category", Constant.CATEGORY_15).putString("type", Constant.TYPE_26).get())
                            .create();
                } else if (tab.equals(Constant.TAB_5)) {
                    fragmentPagerItems = FragmentPagerItems.with(this.getActivity())
                            .add(getString(R.string.txt_type0), SourceTypeFragment.class, new Bundler().putString("sourceType", Constant.SOURCE_TYPE_3).putString("category", Constant.CATEGORY_21).get())
                            .add(getString(R.string.txt_type16), SourceTypeFragment.class, new Bundler().putString("sourceType", Constant.SOURCE_TYPE_3).putString("category", Constant.CATEGORY_21).putString("type", Constant.TYPE_11).get())
                            .add(getString(R.string.txt_type15), SourceTypeFragment.class, new Bundler().putString("sourceType", Constant.SOURCE_TYPE_3).putString("category", Constant.CATEGORY_21).putString("type", Constant.TYPE_10).get())
                            .add(getString(R.string.txt_type14), SourceTypeFragment.class, new Bundler().putString("sourceType", Constant.SOURCE_TYPE_3).putString("category", Constant.CATEGORY_21).putString("type", Constant.TYPE_9).get())
                            .create();
                }

                break;
            case Constant.SOURCE_TYPE_5:
                if (tab.equals(Constant.TAB_1)) {
                    fragmentPagerItems = FragmentPagerItems.with(this.getActivity())
                            .add(getString(R.string.txt_type0), SourceTypeFragment.class, new Bundler().putString("sourceType", Constant.SOURCE_TYPE_5).putString("category", Constant.CATEGORY_14).get())
                            .add(getString(R.string.txt_type1), SourceTypeFragment.class, new Bundler().putString("sourceType", Constant.SOURCE_TYPE_5).putString("category", Constant.CATEGORY_14).putString("type", Constant.TYPE_12).get())
                            .add(getString(R.string.txt_type2), SourceTypeFragment.class, new Bundler().putString("sourceType", Constant.SOURCE_TYPE_5).putString("category", Constant.CATEGORY_14).putString("type", Constant.TYPE_13).get())
                            .add(getString(R.string.txt_type5), SourceTypeFragment.class, new Bundler().putString("sourceType", Constant.SOURCE_TYPE_5).putString("category", Constant.CATEGORY_14).putString("type", Constant.TYPE_14).get())
                            .add(getString(R.string.txt_type3), SourceTypeFragment.class, new Bundler().putString("sourceType", Constant.SOURCE_TYPE_5).putString("category", Constant.CATEGORY_14).putString("type", Constant.TYPE_15).get())
                            .add(getString(R.string.txt_type4), SourceTypeFragment.class, new Bundler().putString("sourceType", Constant.SOURCE_TYPE_5).putString("category", Constant.CATEGORY_14).putString("type", Constant.TYPE_16).get())
                            .add(getString(R.string.txt_type8), SourceTypeFragment.class, new Bundler().putString("sourceType", Constant.SOURCE_TYPE_5).putString("category", Constant.CATEGORY_14).putString("type", Constant.TYPE_17).get())
                            .add(getString(R.string.txt_type9), SourceTypeFragment.class, new Bundler().putString("sourceType", Constant.SOURCE_TYPE_5).putString("category", Constant.CATEGORY_14).putString("type", Constant.TYPE_18).get())
                            .create();
                } else if (tab.equals(Constant.TAB_2)) {
                    fragmentPagerItems = FragmentPagerItems.with(this.getActivity())
                            .add(getString(R.string.txt_type0), SourceTypeFragment.class, new Bundler().putString("sourceType", Constant.SOURCE_TYPE_5).putString("category", Constant.CATEGORY_15).get())
                            .add(getString(R.string.txt_type22), SourceTypeFragment.class, new Bundler().putString("sourceType", Constant.SOURCE_TYPE_5).putString("category", Constant.CATEGORY_15).putString("type", Constant.TYPE_21).get())
                            .add(getString(R.string.txt_type29), SourceTypeFragment.class, new Bundler().putString("sourceType", Constant.SOURCE_TYPE_5).putString("category", Constant.CATEGORY_15).putString("type", Constant.TYPE_22).get())
                            .add(getString(R.string.txt_type30), SourceTypeFragment.class, new Bundler().putString("sourceType", Constant.SOURCE_TYPE_5).putString("category", Constant.CATEGORY_15).putString("type", Constant.TYPE_23).get())
                            .add(getString(R.string.txt_type25), SourceTypeFragment.class, new Bundler().putString("sourceType", Constant.SOURCE_TYPE_5).putString("category", Constant.CATEGORY_15).putString("type", Constant.TYPE_24).get())
                            .add(getString(R.string.txt_type26), SourceTypeFragment.class, new Bundler().putString("sourceType", Constant.SOURCE_TYPE_5).putString("category", Constant.CATEGORY_15).putString("type", Constant.TYPE_25).get())
                            .add(getString(R.string.txt_type27), SourceTypeFragment.class, new Bundler().putString("sourceType", Constant.SOURCE_TYPE_5).putString("category", Constant.CATEGORY_15).putString("type", Constant.TYPE_26).get())
                            .create();
                }
                break;
        }
        return fragmentPagerItems;
    }
}
