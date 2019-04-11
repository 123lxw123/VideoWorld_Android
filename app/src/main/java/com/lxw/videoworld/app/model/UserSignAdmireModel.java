package com.lxw.videoworld.app.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Zion on 2017/8/6.
 */

public class UserSignAdmireModel implements Serializable {

    private String uid;
    private int restDay;
    private int signDay;
    private Date expirationDate;
    private Date signDate;
    private int sumSignDay;
    private int sumAdmireDay;
    private long createTime;
    private long updateTime;
    private boolean isSign = false;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getRestDay() {
        return restDay;
    }

    public void setRestDay(int restDay) {
        this.restDay = restDay;
    }

    public int getSignDay() {
        return signDay;
    }

    public void setSignDay(int signDay) {
        this.signDay = signDay;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Date getSignDate() {
        return signDate;
    }

    public void setSignDate(Date signDate) {
        this.signDate = signDate;
    }

    public int getSumSignDay() {
        return sumSignDay;
    }

    public void setSumSignDay(int sumSignDay) {
        this.sumSignDay = sumSignDay;
    }

    public int getSumAdmireDay() {
        return sumAdmireDay;
    }

    public void setSumAdmireDay(int sumAdmireDay) {
        this.sumAdmireDay = sumAdmireDay;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public boolean isSign() {
        return isSign;
    }

    public void setSign(boolean sign) {
        isSign = sign;
    }
}