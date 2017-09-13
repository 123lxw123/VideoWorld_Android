package com.lxw.videoworld.app.util;

import com.xunlei.downloadlib.parameter.XLTaskInfo;

import java.util.Comparator;

/**
 * Created by Zion on 2017/9/13.
 */

public class XLTaskInfoComparator implements Comparator<XLTaskInfo> {
    @Override
    public int compare(XLTaskInfo A, XLTaskInfo B) {
        if (A.mDownloadSpeed < B.mDownloadSpeed) return 1;
        else if (A.mDownloadSpeed > B.mDownloadSpeed) return -1;
        else {
            boolean isCompleteA = A.mTaskStatus == 0 || A.mFileSize == A.mDownloadSize;
            boolean isCompleteB = B.mTaskStatus == 0 || B.mFileSize == B.mDownloadSize;
        }
        return 0;
    }
}
