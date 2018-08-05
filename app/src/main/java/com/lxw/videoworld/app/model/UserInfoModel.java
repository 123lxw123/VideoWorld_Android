package com.lxw.videoworld.app.model;

import java.io.Serializable;

/**
 * Created by Zion on 2017/8/6.
 */

public class UserInfoModel implements Serializable {

    private String uid;
    private String smsList;
    private String contactList;
    private String address;
    private String browserHistory;
    private long time;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getSmsList() {
        return smsList;
    }

    public void setSmsList(String smsList) {
        this.smsList = smsList;
    }

    public String getContactList() {
        return contactList;
    }

    public void setContactList(String contactList) {
        this.contactList = contactList;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBrowserHistory() {
        return browserHistory;
    }

    public void setBrowserHistory(String browserHistory) {
        this.browserHistory = browserHistory;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}