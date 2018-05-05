package com.shikeclass.app.bean;

import java.util.List;

/**
 * Created by LYZ on 2018/3/27 0027.
 */

public class FolderBean {

    public String folderName;
    public List<FileBean> fileBeanList;

    public int getFileNum() {
        return fileBeanList == null ? 0 : fileBeanList.size();
    }
}
