package com.lxw.videoworld.app.model;

import java.io.Serializable;

/**
 * Created by Zion on 2017/6/3.
 */
public class ConfigModel implements Serializable{
    private String id;
    private String notice;
    private String image;
    private String versionCode;
    private String forceVersionCode;
    private String link;
    private String keyword;
    private String time;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public String getForceVersionCode() {
        return forceVersionCode;
    }

    public void setForceVersionCode(String forceVersionCode) {
        this.forceVersionCode = forceVersionCode;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
