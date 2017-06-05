package com.lxw.videoworld.app.api;


import com.lxw.videoworld.app.model.ConfigModel;
import com.lxw.videoworld.framework.http.BaseResponse;

import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by lxw9047 on 2016/10/20.
 */

public interface HttpService {

    @GET("config/")
    Flowable<BaseResponse<ConfigModel>> getConfig(@Query("id") String id);
}
