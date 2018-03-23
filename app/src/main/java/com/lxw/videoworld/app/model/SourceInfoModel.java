package com.lxw.videoworld.app.model;

import java.io.Serializable;

/**
 * Created by Zion on 2017/6/13.
 */

public class SourceInfoModel implements Serializable {

    public SourceInfoModel(){

    }

    public SourceInfoModel(String key, String value, boolean isSelected){
        this.key = key;
        this.value = value;
        this.isSelected = isSelected;
    }

    private String key;
    private String value;
    private boolean isSelected;

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

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
