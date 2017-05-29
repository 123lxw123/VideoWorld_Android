package com.lxw.videoworld.framework.base;

import android.widget.Toast;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.hannesdorfmann.mosby3.mvp.MvpView;
import com.lxw.videoworld.framework.application.BaseApplication;
import com.lxw.videoworld.framework.util.NetUtil;


/**
 * Created by lxw9047 on 2016/10/21.
 */

public class BaseMvpPresenter<V extends MvpView> extends MvpBasePresenter<V> {

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
