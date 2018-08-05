package com.lxw.videoworld.framework.base;

import android.content.res.TypedArray;
import android.support.annotation.StyleableRes;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.lxw.videoworld.R;

/**
 * Created by Zion on 2016/11/20.
 */

public abstract class BaseFragment extends Fragment {

    //toast提示信息
    public void showMessage(String message){
        showMessage(message, Toast.LENGTH_SHORT);
    }
    public void showMessage(String message, int showTime){
        Toast.makeText(BaseFragment.this.getActivity(), message, showTime).show();
    }

    public int getCustomColor(@StyleableRes int index){
        return getCustomColor(index, 0xFFFFFF);
    }

    /**
     * 获取主题某颜色的值
     * @param index 如 R.styleable.BaseColor_com_wm_bg
     * @param defValue 默认颜色值
     * @return
     */
    public int getCustomColor(@StyleableRes int index, int defValue){
        TypedArray a = getActivity().obtainStyledAttributes(null, R.styleable.BaseColor, 0, 0);
        int color = a.getColor(index, defValue);
        a.recycle();
        return color;
    }

}
