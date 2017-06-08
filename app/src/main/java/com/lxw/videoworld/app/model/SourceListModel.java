package com.lxw.videoworld.app.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Zion on 2017/6/8.
 */

public class SourceListModel implements Serializable {
    private List<SourceDetailModel> list;

    public List<SourceDetailModel> getList() {
        return list;
    }

    public void setList(List<SourceDetailModel> list) {
        this.list = list;
    }
}
