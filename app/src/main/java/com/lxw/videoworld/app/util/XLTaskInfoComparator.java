package com.lxw.videoworld.app.util;

import com.xunlei.downloadlib.parameter.XLTaskInfo;

import java.util.Comparator;

/**
 * Created by Zion on 2017/9/13.
 */

public class XLTaskInfoComparator implements Comparator<XLTaskInfo> {
    @Override
    public int compare(XLTaskInfo A, XLTaskInfo B) {
        // 首先比较是否下载完成
        boolean isCompleteA = A.mTaskStatus == 2 || (A.mFileSize > 0 && A.mFileSize == A.mDownloadSize);
        boolean isCompleteB = B.mTaskStatus == 2 || (B.mFileSize > 0 && B.mFileSize == B.mDownloadSize);
        if (isCompleteA && !isCompleteB) return 1;
        else if (!isCompleteA && isCompleteB) return -1;
        else if (isCompleteA && isCompleteB) {
            if (A.timestamp > B.timestamp) return -1;
            else if (A.timestamp < B.timestamp) return 1;
            else return 0;
        } else {
            // 其次比较是否下载出错
            boolean isErrorA = A.mTaskStatus == 3;
            boolean isErrorB = B.mTaskStatus == 3;
            if (isErrorA && !isErrorB) return 1;
            else if (!isErrorA && isErrorB) return -1;
            else if (isErrorA && isErrorB) {
                if (A.timestamp > B.timestamp) return -1;
                else if (A.timestamp < B.timestamp) return 1;
                else return 0;
            } else {
                // 其次比较下载速度
                if (A.mDownloadSpeed < B.mDownloadSpeed) return 1;
                else if (A.mDownloadSpeed > B.mDownloadSpeed) return -1;
                else {// 最后比较已下载比例
                    if (A.mFileSize == 0 || B.mFileSize == 0) {
                        if (A.mFileSize == 0 && B.mFileSize == 0) return 0;
                        else if (A.mFileSize > 0) return -1;
                        else return 1;
                    } else {
                        float progressA = (float) A.mDownloadSize / A.mFileSize;
                        float progressB = (float) B.mDownloadSize / B.mFileSize;
                        if (progressA > progressB) return -1;
                        else if (progressA < progressB) return 1;
                        else return 0;
                    }
                }
            }
        }
    }
}
