package com.lxw.videoworld.framework.base;

import android.app.Activity;
import android.widget.Toast;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;
import com.lxw.dailynews.framework.application.BaseApplication;
import com.lxw.dailynews.framework.util.NetUtil;

/**
 * Created by lxw9047 on 2016/10/21.
 */

public class BaseMvpPresenter<V extends MvpView> extends MvpBasePresenter<V> {

    //判断手机是否联网，联网失败toast提示
    public boolean checkNetword(){
        return NetUtil.note_Intent(BaseApplication.appContext);
    }

    //判断手机是否联网，
    public boolean isNetworkAvailable(){
        return NetUtil.isNetworkAvailable(BaseApplication.appContext);
    }

    public void showProgressBar(){
        ((BaseMvpActivity)getView()).showProgressBar();
    }

    public void hideProgressBar(){
        ((BaseMvpActivity)getView()).hideProgressBar();
    }

    public void showMessage(String message){
        showMessage(message, Toast.LENGTH_SHORT);
    }
    public void showMessage(String message, int showTime){
        boolean activityFlag = (getView()) instanceof BaseMvpActivity;
        boolean fragmentFlag = (getView()) instanceof BaseMvpFragment;
        if(activityFlag){
            ((BaseMvpActivity)getView()).showMessage(message, showTime);
        }else if(fragmentFlag){
            (((BaseMvpFragment)getView())).showMessage(message, showTime);
        }
    }
}
