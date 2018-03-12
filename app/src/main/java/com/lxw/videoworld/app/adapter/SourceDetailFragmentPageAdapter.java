package com.lxw.videoworld.app.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.lxw.videoworld.app.model.SourceDetailModel;
import com.lxw.videoworld.app.ui.SourceDetailFragment;

import java.util.List;

/**
 * Created by Zion on 2017/6/8.
 */

public class SourceDetailFragmentPageAdapter extends FragmentPagerAdapter {
    private List<SourceDetailModel> mList;

    /**
     * @param fm
     * @param list
     */
    public SourceDetailFragmentPageAdapter(FragmentManager fm, List<SourceDetailModel> list) {
        super(fm);
        mList = list;
    }

    @Override
    public Fragment getItem(int position) {
        SourceDetailFragment fragment = new SourceDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("sourceDetailModel", mList.get(position));
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        return mList.size();
    }
}