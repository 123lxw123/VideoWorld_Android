package com.lxw.videoworld.framework.base;

import android.widget.Toast;

import com.hannesdorfmann.mosby3.mvp.MvpFragment;
import com.hannesdorfmann.mosby3.mvp.MvpPresenter;
import com.hannesdorfmann.mosby3.mvp.MvpView;


/**
 * Created by Zion on 2016/11/20.
 * 懒加载fragment
 * ①isPrepared参数在系统调用onActivityCreated时设置为true,这时onCreateView方法已调用完毕
 * (一般我们在这方法里执行findviewbyid等方法),确保 initData()方法不会报空指针异常。
 * ②isVisible参数在fragment可见时通过系统回调setUserVisibileHint方法设置为true,不可见时为false，这是fragment实现懒加载的关键。
 * ③isFirst确保ViewPager来回切换时BaseFragment的initData方法不会被重复调用，
 * initData在该Fragment的整个生命周期只调用一次,第一次调用initData()方法后马上执行 isFirst = false。
 */

public abstract class BaseMvpFragment<V extends MvpView, P extends MvpPresenter<V>> extends MvpFragment<V, P> {

    //toast提示信息
    public void showMessage(String message){
        showMessage(message, Toast.LENGTH_SHORT);
    }
    public void showMessage(String message, int showTime){
        Toast.makeText(BaseMvpFragment.this.getActivity(), message, showTime).show();
    }

}
