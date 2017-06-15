package com.lxw.videoworld.app.model;

import java.io.Serializable;

/**
 * Created by Zion on 2017/6/14.
 */
public class SearchResultModel implements Serializable{
    private String uid;
    private String url;
    private String ciliLink;
    private String thunderLink;
    private String amounts;
    private String title;
    private String date;
    private String size;
    private String hot;
    private String time;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCiliLink() {
        return ciliLink;
    }

    public void setCiliLink(String ciliLink) {
        this.ciliLink = ciliLink;
    }

    public String getThunderLink() {
        return thunderLink;
    }

    public void setThunderLink(String thunderLink) {
        this.thunderLink = thunderLink;
    }

    public String getAmounts() {
        return amounts;
    }

    public void setAmounts(String amounts) {
        this.amounts = amounts;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getHot() {
        return hot;
    }

    public void setHot(String hot) {
        this.hot = hot;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
