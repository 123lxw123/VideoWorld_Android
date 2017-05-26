package com.lxw.videoworld.framework.http;


import android.net.ParseException;

import com.google.gson.JsonParseException;
import com.lxw.videoworld.framework.log.LoggerHelper;

import org.json.JSONException;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.io.InterruptedIOException;
import java.net.ConnectException;
import java.net.UnknownHostException;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;

import static com.lxw.videoworld.framework.http.HttpManager.ExceptionReason.CONNECT_ERROR;
import static com.lxw.videoworld.framework.http.HttpManager.ExceptionReason.CONNECT_TIMEOUT;
import static com.lxw.videoworld.framework.http.HttpManager.ExceptionReason.PARSE_ERROR;
import static com.lxw.videoworld.framework.http.HttpManager.ExceptionReason.UNKNOWN_ERROR;


/**
 * Created by lxw9047 on 2016/10/20.
 */

public class HttpManager {

    public static void doRequest(final Flowable<BaseResponse> flowable, final HttpListener httpListener){
        Subscriber<BaseResponse> subscriber = new Subscriber<BaseResponse>(){
            @Override
            public void onError(Throwable error) {
                LoggerHelper.info("HttpManager-onError-->>",error.getMessage());
                if (error instanceof HttpException) {     //   HTTP错误
                    onException(ExceptionReason.BAD_NETWORK);
                } else if (error instanceof ConnectException
                        || error instanceof UnknownHostException) {   //   连接错误
                    onException(CONNECT_ERROR);
                } else if (error instanceof InterruptedIOException) {   //  连接超时
                    onException(CONNECT_TIMEOUT);
                } else if (error instanceof JsonParseException
                        || error instanceof JSONException
                        || error instanceof ParseException) {   //  解析错误
                    onException(PARSE_ERROR);
                } else {
                    onException(UNKNOWN_ERROR);
                }
                httpListener.onFailure(error);
            }

            @Override
            public void onComplete() {

            }

            @Override
            public void onSubscribe(Subscription s) {

            }

            @Override
            public void onNext(BaseResponse response) {
                LoggerHelper.info("HttpManager-onNext-->>",response.toString());
                httpListener.onSuccess(response);
            }
        };

        if(flowable != null && subscriber != null){
            flowable.subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .onBackpressureLatest()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(subscriber);
        }
    }


    /**
     * 请求网络失败原因
     */
    public enum ExceptionReason {
        /**
         * 解析数据失败
         */
        PARSE_ERROR,
        /**
         * 网络问题
         */
        BAD_NETWORK,
        /**
         * 连接错误
         */
        CONNECT_ERROR,
        /**
         * 连接超时
         */
        CONNECT_TIMEOUT,
        /**
         * 未知错误
         */
        UNKNOWN_ERROR,
    }
}
