package com.lxw.videoworld.app.model;

import java.io.Serializable;

/**
 * Created by Zion on 2017/6/3.
 */
public class ConfigModel implements Serializable{
    private String id;
    private String notice;
    private String image;
    private int versionCode;
    private int forceVersionCode;
    private String link;
    private String flag;

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

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public int getForceVersionCode() {
        return forceVersionCode;
    }

    public void setForceVersionCode(int forceVersionCode) {
        this.forceVersionCode = forceVersionCode;
    }

    @Override
    public String toString() {
        return "{" +
                "id='" + id + '\'' +
                ", notice='" + notice + '\'' +
                ", image='" + image + '\'' +
                ", versionCode=" + versionCode +
                ", forceVersionCode=" + forceVersionCode +
                ", link='" + link + '\'' +
                ", flag='" + flag + '\'' +
                '}';
    }
}
