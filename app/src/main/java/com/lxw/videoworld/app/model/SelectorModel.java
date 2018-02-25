package com.lxw.videoworld.app.model;

import java.io.Serializable;

/**
 * Created by Zion on 2018/2/25.
 */

public class SelectorModel implements Serializable {

    public SelectorModel(String title, String sourceType, String category, String type){
        this.title = title;
        this.category = category;
        this.sourceType = sourceType;
        this.type = type;
    }

    private String title;
    private String sourceType;
    private String category;
    private String type;
    private boolean isSelected;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
