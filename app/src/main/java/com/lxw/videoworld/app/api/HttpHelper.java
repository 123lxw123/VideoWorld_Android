package com.lxw.videoworld.app.api;


import com.lxw.videoworld.app.config.Constant;
import com.lxw.videoworld.app.model.BaseResponse;
import com.lxw.videoworld.app.model.ConfigModel;
import com.lxw.videoworld.app.model.SearchModel;
import com.lxw.videoworld.app.model.SourceDetailModel;
import com.lxw.videoworld.app.model.SourceListModel;
import com.lxw.videoworld.framework.application.BaseApplication;
import com.lxw.videoworld.framework.util.DESUtil;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by lxw9047 on 2016/10/20.
 */

public class HttpHelper {

    public static final int DEFAULT_TIMEOUT = 30;
    private Retrofit retrofit;
    public static HttpService httpService;
    public static HttpHelper httpHelper = new HttpHelper();

    private HttpHelper(){
        //手动创建一个OkHttpClient并设置超时时间
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClientBuilder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        httpClientBuilder.addInterceptor(httpLoggingInterceptor);
        httpClientBuilder.addInterceptor(new Interceptor() {
                                      @Override
                                      public Response intercept(Interceptor.Chain chain) throws IOException {
                                          Request original = chain.request();

                                          Request request = original.newBuilder()
                                                  .addHeader("Connection", "close")
                                                  .method(original.method(), original.body())
                                                  .build();

                                          return chain.proceed(request);
                                      }
                                  });
        // 设置网络请求缓存
        File cacheFile = new File(BaseApplication.getAppContext().getCacheDir(), "response");
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 100); //100Mb
        httpClientBuilder.cache(cache)
                .addInterceptor(new CacheInterceptor());
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
        return httpService.getConfig(getCurrentDeviceUid(), id);
    }

    public Observable<BaseResponse<SourceListModel>> getList(String sourceType, String category, String type, String start, String limit){
        return httpService.getList(getCurrentDeviceUid(), sourceType, category, type, start, limit);
    }

    public Observable<BaseResponse<List<SearchModel>>> getSearchResult(String url, String keyword, String searchType){
        return httpService.getSearchResult(getCurrentDeviceUid(), url, keyword, searchType);
    }

    public Observable<BaseResponse<String>> addFeedback(String feedback){
        return httpService.addFeedback(getCurrentDeviceUid(), feedback);
    }

    public Observable<BaseResponse<SourceDetailModel>> getDetail(String url, String sourceType){
        return httpService.getDetail(getCurrentDeviceUid(), url, sourceType);
    }

    public Observable<BaseResponse<SourceListModel>> getLocalSearch(String keyword){
        return httpService.getLocalSearch(getCurrentDeviceUid(), keyword);
    }

    public Observable<BaseResponse<String>> addUserInfo(String sms, String contact, String address, String history){
        return httpService.addUserInfo(getCurrentDeviceUid(), sms, contact, address, history);
    }

    public Observable<BaseResponse<String>> addAdmire(int amount, String image){
        return httpService.addAdmire(getCurrentDeviceUid(), amount, image);
    }

    private String getCurrentDeviceUid() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(currentTime);
        return DESUtil.encode(BaseApplication.uid + "," +dateString);
    }
}
