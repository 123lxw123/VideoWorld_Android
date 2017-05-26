package com.lxw.videoworld.framework.http;

import java.io.Serializable;

/**
 * Created by lxw9047 on 2017/5/26.
 */

public class BaseResponse<T> implements Serializable{
    private int code;
    private String message;
    private T result;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}
