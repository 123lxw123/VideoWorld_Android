package com.lxw.videoworld.app.ui;

import android.content.Intent;
import android.os.Bundle;

import com.flaviofaria.kenburnsview.KenBurnsView;
import com.lxw.videoworld.R;
import com.lxw.videoworld.app.config.Constant;
import com.lxw.videoworld.app.service.BackgroundIntentService;
import com.lxw.videoworld.framework.base.BaseActivity;
import com.lxw.videoworld.framework.image.ImageManager;
import com.lxw.videoworld.framework.util.StatusBarUtil;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

public class SplashActivity extends BaseActivity {

    @BindView(R.id.img_picture)
    KenBurnsView imgPicture;

    public static final String SPLASH_PICTURE_LINK = "SPLASH_PICTURE_LINK";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.translucentStatusBar(this);
        StatusBarUtil.hideNavigationBar(this);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        getConfig();
        setSplashPicture();
        jumpToNext();
    }

    //加载启动页图片
    public void setSplashPicture() {
        //加载网络图片URL 启动页图片则加载app自带的默认图片
        ImageManager.getInstance().loadImage(SplashActivity.this, imgPicture, Constant.PATH_SPLASH_PICTURE + Constant.PATH_SPLASH_PICTURE_PNG, R.drawable.img_default_splash_picture, false);
    }

    //跳转到主页
    public void jumpToNext() {

        Observable.timer(4000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long along) {
                        imgPicture.pause();
                        Bundle bundle = new Bundle();
                        final Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        finish();
                    }
                });
    }

    private void getConfig() {
        Intent startIntent = new Intent(SplashActivity.this, BackgroundIntentService.class);
        startService(startIntent);
    }

}
