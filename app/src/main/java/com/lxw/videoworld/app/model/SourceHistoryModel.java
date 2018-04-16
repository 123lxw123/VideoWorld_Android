package com.lxw.videoworld.app.model;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 *
 * Created by Zion on 2018/4/16.
 */

public class SourceHistoryModel extends RealmObject implements Serializable {
    @PrimaryKey
    private String url;
    @PrimaryKey
    private String link;
    private long seek;
    private String historyStatus;
    private long time;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public long getSeek() {
        return seek;
    }

    public void setSeek(long seek) {
        this.seek = seek;
    }

    public String getHistoryStatus() {
        return historyStatus;
    }

    public void setHistoryStatus(String historyStatus) {
        this.historyStatus = historyStatus;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
