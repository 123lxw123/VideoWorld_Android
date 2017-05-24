package com.lxw.videoworld.framework.http;

import com.lxw.dailynews.framework.log.LoggerHelper;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by lxw9047 on 2016/10/20.
 */

public abstract class HttpManager<T> {
    private Observable<T> observable;
    private Subscriber<T> subscriber;
    private  HttpListener<T> httpListener;

    public HttpManager(HttpListener<T> httpListener){
        HttpManager.this.httpListener = httpListener;
        observable = createObservable();
        subscriber = new Subscriber<T>(){

            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable error) {
                LoggerHelper.info("HttpManager-onError-->>",error.getMessage());
                HttpManager.this.httpListener.onFailure(error);
            }

            @Override
            public void onNext(T response) {
                LoggerHelper.info("HttpManager-onNext-->>",response.toString());
                HttpManager.this.httpListener.onSuccess(response);
            }
        };
        doSubscribe();
    }

    public abstract Observable<T> createObservable();

    public void doSubscribe(){
        if(observable != null && subscriber != null){
            observable.subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(subscriber);
        }
    }

    public void unSubscribe(){
        if(subscriber != null && !subscriber.isUnsubscribed())
        subscriber.unsubscribe();
    }
}
