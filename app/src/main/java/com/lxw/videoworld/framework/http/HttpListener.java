package com.lxw.videoworld.framework.http;

/**
 * Created by lxw9047 on 2016/10/21.
 * 访问网络回调接口
 */

public interface HttpListener<T> {
    void onSuccess(T response);
    void onFailure(Throwable error);
}
