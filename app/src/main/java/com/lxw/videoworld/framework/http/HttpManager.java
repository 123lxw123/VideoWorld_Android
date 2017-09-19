package com.lxw.videoworld.framework.http;


import android.content.Context;
import android.net.ParseException;

import com.google.gson.JsonParseException;
import com.lxw.videoworld.app.config.Constant;
import com.lxw.videoworld.app.model.BaseResponse;
import com.lxw.videoworld.framework.base.BaseActivity;
import com.lxw.videoworld.framework.log.LoggerHelper;
import com.lxw.videoworld.framework.util.StringUtil;
import com.lxw.videoworld.framework.util.ToastUtil;

import org.json.JSONException;

import java.io.InterruptedIOException;
import java.net.ConnectException;
import java.net.UnknownHostException;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;


/**
 * Created by lxw9047 on 2016/10/20.
 */

public abstract class HttpManager<T> {
    private Context context;
    private Observable<BaseResponse<T>> observable;
    private boolean flag_dialog;
    private boolean flag_toast;
    private Disposable disposable;

    public HttpManager(Context context, Observable<BaseResponse<T>> observable){
        this(context, observable, true);
    }

    public HttpManager(Context context, Observable<BaseResponse<T>> observable, boolean flag_dialog){
        this(context, observable, flag_dialog, true);
    }

    public HttpManager(Context context, Observable<BaseResponse<T>> observable, boolean flag_dialog, boolean flag_toast){
        this.context = context;
        this.observable = observable;
        this.flag_dialog = flag_dialog;
        this.flag_toast = flag_toast;
    }

    public void doRequest(){
        Observer<BaseResponse<T>> observer = new Observer<BaseResponse<T>>(){
            @Override
            public void onError(Throwable error) {
                if(flag_dialog){
                    ((BaseActivity)context).hideProgressBar();
                }
                LoggerHelper.info("HttpManager-onError-->>",error.getMessage());
                String message = "";
                BaseResponse<T> errorResponse = new BaseResponse<T>();
                if (error instanceof HttpException || error instanceof ConnectException
                        || error instanceof UnknownHostException) {   //   连接错误
                    message = Constant.CONNECT_ERROR;
                    errorResponse.setCode(Constant.CONNECT_ERROR_CODE);
                } else if (error instanceof InterruptedIOException) {   //  连接超时
                    message = Constant.CONNECT_TIMEOUT;
                    errorResponse.setCode(Constant.CONNECT_TIMEOUT_CODE);
                } else if (error instanceof JsonParseException
                        || error instanceof JSONException
                        || error instanceof ParseException) {   //  解析错误
                    message = Constant.PARSE_ERROR;
                    errorResponse.setCode(Constant.PARSE_ERROR_CODE);
                } else {
                    message = Constant.UNKNOWN_ERROR;// 未知错误
                    errorResponse.setCode(Constant.UNKNOWN_ERROR_CODE);
                }
                if(StringUtil.isNotEmpty(message)){
                    errorResponse.setMessage(message);
                    if(flag_toast){
                        ToastUtil.showMessage(message);
                    }
                }
                onFailure(errorResponse);
            }

            @Override
            public void onComplete() {
                if(flag_dialog){
                    ((BaseActivity)context).hideProgressBar();
                }
            }

            @Override
            public void onSubscribe(Disposable d) {
                disposable = d;
                if(flag_dialog){
                    ((BaseActivity)context).getProgressBar().setHttpManager(HttpManager.this);
                    ((BaseActivity)context).showProgressBar();
                }
            }

            @Override
            public void onNext(BaseResponse<T> response) {
                LoggerHelper.info("HttpManager-onNext-->>",response.toString());
                if(flag_dialog){
                    ((BaseActivity)context).hideProgressBar();
                }
                if(response != null){
                    if(response.getCode() == Constant.CODE_SUCCESS){// 服务器返回正确结果
                        onSuccess(response);
                    }else {// 服务器返回结果异常
                        if(flag_toast && StringUtil.isNotEmpty(response.getMessage())){
                            ToastUtil.showMessage(response.getMessage());
                        }
                        onFailure(response);
                    }
                }else{// 服务器异常
                    BaseResponse<T> errorResponse = new BaseResponse<T>();
                    errorResponse.setCode(Constant.SERVICE_ERROR_CODE);
                    errorResponse.setMessage(Constant.SERVICE_ERROR);
                    onFailure(response);
                }
            }
        };

        if(observable != null && observer != null){
            observable.subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(observer);
        }
    }

    public void cancel(){
        if(disposable != null){
            disposable.dispose();
            ((BaseActivity)context).hideProgressBar();
        }
    }

    public abstract void onSuccess(BaseResponse<T> response);
    public abstract void onFailure(BaseResponse<T> response);

}
