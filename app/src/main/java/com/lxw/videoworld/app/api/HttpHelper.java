package com.lxw.videoworld.app.api;


import com.lxw.videoworld.app.config.Constant;
import com.lxw.videoworld.app.model.ConfigModel;
import com.lxw.videoworld.app.model.SourceListModel;
import com.lxw.videoworld.framework.http.BaseResponse;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by lxw9047 on 2016/10/20.
 */

public class HttpHelper {

    private static final int DEFAULT_TIMEOUT = 15;
    private Retrofit retrofit;
    public static HttpService httpService;
    public static HttpHelper httpHelper = new HttpHelper();

    private HttpHelper(){
        //手动创建一个OkHttpClient并设置超时时间
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        httpClientBuilder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        retrofit = new Retrofit.Builder()
                .client(httpClientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(Constant.BASE_URL)
                .build();
        httpService = retrofit.create(HttpService.class);
    }

    public static HttpHelper getInstance(){
        return httpHelper;
    }

    public Observable<BaseResponse<ConfigModel>> getConfig(String id){
        return httpService.getConfig(id);
    }

    public Observable<BaseResponse<SourceListModel>> getList(String category, String type, String start, String limit){
        return httpService.getList(Constant.SOURCE_TYPE, category, type, start, limit);
    }
}
