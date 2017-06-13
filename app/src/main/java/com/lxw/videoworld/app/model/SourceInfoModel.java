package com.lxw.videoworld.app.model;

import java.io.Serializable;

/**
 * Created by Zion on 2017/6/13.
 */

public class SourceInfoModel implements Serializable {
    private String key;
    private String value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
