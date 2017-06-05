package com.lxw.videoworld.app.api;


import com.lxw.videoworld.app.model.ConfigModel;
import com.lxw.videoworld.framework.http.BaseResponse;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by lxw9047 on 2016/10/20.
 */

public interface HttpService {
    @FormUrlEncoded
    @POST("config")
    Observable<BaseResponse<ConfigModel>> getConfig(@Field("id") String id);
}
