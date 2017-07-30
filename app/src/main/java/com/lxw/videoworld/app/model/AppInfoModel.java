package com.lxw.videoworld.app.model;

import android.graphics.drawable.Drawable;

import java.io.Serializable;

/**
 * Created by Zion on 2017/7/30.
 */

public class AppInfoModel implements Serializable{
    private Drawable image;
    private String appName;

    public AppInfoModel(Drawable image, String appName) {
        this.image = image;
        this.appName = appName;
    }
    public AppInfoModel() {

    }

    public Drawable getImage() {
        return image;
    }

    public void setImage(Drawable image) {
        this.image = image;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }
}
