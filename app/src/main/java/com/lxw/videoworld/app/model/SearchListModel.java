package com.lxw.videoworld.app.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Zion on 2017/6/16.
 */

public class SearchListModel implements Serializable {
    private List<SearchResultModel> list;

    public List<SearchResultModel> getList() {
        return list;
    }

    public void setList(List<SearchResultModel> list) {
        this.list = list;
    }
}
