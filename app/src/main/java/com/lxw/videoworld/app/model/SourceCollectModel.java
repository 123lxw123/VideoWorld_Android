package com.lxw.videoworld.app.model;

import com.lxw.videoworld.framework.application.BaseApplication;

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
    private String uid = BaseApplication.uid;
    private SourceDetailModel sourceDetailModel;
    private String status;// 0 取消收藏，1 已收藏
    private long time;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public SourceDetailModel getSourceDetailModel() {
        return sourceDetailModel;
    }

    public void setSourceDetailModel(SourceDetailModel sourceDetailModel) {
        this.sourceDetailModel = sourceDetailModel;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
