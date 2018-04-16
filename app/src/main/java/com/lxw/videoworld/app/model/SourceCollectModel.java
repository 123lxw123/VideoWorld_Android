package com.lxw.videoworld.app.model;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 *
 * Created by Zion on 2018/4/16.
 */

public class SourceCollectModel extends RealmObject implements Serializable {
    @PrimaryKey
    private String url;
    private String collectStatus;
    private long time;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCollectStatus() {
        return collectStatus;
    }

    public void setCollectStatus(String collectStatus) {
        this.collectStatus = collectStatus;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
