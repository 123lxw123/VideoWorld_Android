package com.lxw.videoworld.app.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Zion on 2017/6/14.
 */
public class SearchListModel implements Serializable {

    private List<SearchModel> list;

    public List<SearchModel> getList() {
        return list;
    }

    public void setList(List<SearchModel> list) {
        this.list = list;
    }
}
