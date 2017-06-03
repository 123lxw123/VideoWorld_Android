package com.lxw.videoworld.framework.http;


import android.net.ParseException;

import com.google.gson.JsonParseException;
import com.lxw.videoworld.framework.config.Constant;
import com.lxw.videoworld.framework.log.LoggerHelper;
import com.lxw.videoworld.framework.util.StringUtil;
import com.lxw.videoworld.framework.util.ToastUtil;

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


/**
 * Created by lxw9047 on 2016/10/20.
 */

public class HttpManager {

    private static boolean flag_toast = true;

    public static void doRequest(final Flowable<BaseResponse> flowable, final HttpListener httpListener){
        Subscriber<BaseResponse> subscriber = new Subscriber<BaseResponse>(){
            @Override
            public void onError(Throwable error) {
                LoggerHelper.info("HttpManager-onError-->>",error.getMessage());
                String message = "";
                BaseResponse errorResponse = new BaseResponse();
                if (error instanceof HttpException || error instanceof ConnectException
                        || error instanceof UnknownHostException) {   //   连接错误
                    message = ExceptionReason.CONNECT_ERROR;
                    errorResponse.setCode(ExceptionReason.CONNECT_ERROR_CODE);
                } else if (error instanceof InterruptedIOException) {   //  连接超时
                    message = ExceptionReason.CONNECT_TIMEOUT;
                    errorResponse.setCode(ExceptionReason.CONNECT_TIMEOUT_CODE);
                } else if (error instanceof JsonParseException
                        || error instanceof JSONException
                        || error instanceof ParseException) {   //  解析错误
                    message = ExceptionReason.PARSE_ERROR;
                    errorResponse.setCode(ExceptionReason.PARSE_ERROR_CODE);
                } else {
                    message = ExceptionReason.UNKNOWN_ERROR;// 未知错误
                    errorResponse.setCode(ExceptionReason.UNKNOWN_ERROR_CODE);
                }
                if(StringUtil.isNotEmpty(message)){
                    errorResponse.setMessage(message);
                    if(flag_toast){
                        ToastUtil.showMessage(message);
                    }
                }
                httpListener.onFailure(errorResponse);
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
                if(response != null){
                    if(response.getCode() == Constant.CODE_SUCCESS){// 服务器返回正确结果
                        httpListener.onSuccess(response);
                    }else {// 服务器返回结果异常
                        if(flag_toast && StringUtil.isNotEmpty(response.getMessage())){
                            ToastUtil.showMessage(response.getMessage());
                        }
                        httpListener.onFailure(response);
                    }
                }else{// 服务器异常
                    BaseResponse errorResponse = new BaseResponse();
                    errorResponse.setCode(ExceptionReason.SERVICE_ERROR_CODE);
                    errorResponse.setMessage(ExceptionReason.SERVICE_ERROR);
                    httpListener.onFailure(response);
                }
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
    public interface ExceptionReason {
        /**
         * 解析数据失败
         */
        public static final String PARSE_ERROR = "数据解析失败";
        public static final int PARSE_ERROR_CODE = 1001;
        /**
         * 连接错误
         */
        public static final String CONNECT_ERROR = "网络连接失败";
        public static final int CONNECT_ERROR_CODE = 1002;
        /**
         * 连接超时
         */
        public static final String CONNECT_TIMEOUT = "网络连接超时";
        public static final int CONNECT_TIMEOUT_CODE = 1003;
        /**
         * 未知错误
         */
        public static final String UNKNOWN_ERROR = "未知错误";
        public static final int UNKNOWN_ERROR_CODE = 1004;
        /**
         * 未知错误
         */
        public static final String SERVICE_ERROR = "服务器异常";
        public static final int SERVICE_ERROR_CODE = 1005;
    }
}
