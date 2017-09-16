package com.xunlei.downloadlib.parameter;

import android.os.Parcel;
import android.os.Parcelable;

public class XLTaskInfo implements Parcelable {
    public static final Creator<XLTaskInfo> CREATOR = new Creator<XLTaskInfo>() {
        public final XLTaskInfo createFromParcel(Parcel parcel) {
            return new XLTaskInfo(parcel);
        }

        public final XLTaskInfo[] newArray(int i) {
            return new XLTaskInfo[i];
        }
    };
    public int mAddedHighSourceState;
    public int mAdditionalResCount;
    public long mAdditionalResDCDNBytes;
    public long mAdditionalResDCDNSpeed;
    public long mAdditionalResPeerBytes;
    public long mAdditionalResPeerSpeed;
    public int mAdditionalResType;
    public long mAdditionalResVipRecvBytes;
    public long mAdditionalResVipSpeed;
    public String mCid;
    public long mDownloadSize;
    public long mDownloadSpeed;
    public int mErrorCode;
    public String mFileName;
    public long mFileSize;
    public String mGcid;
    public int mInfoLen;
    public long mOriginRecvBytes;
    public long mOriginSpeed;
    public long mP2PRecvBytes;
    public long mP2PSpeed;
    public long mP2SRecvBytes;
    public long mP2SSpeed;
    public int mQueryIndexStatus;
    public long mTaskId;
    public int mTaskStatus;
    public String sourceUrl;// 下载地址
    public String torrentPath;// 种子保存地址
    public int index = -1;// 种子里相应文件序号
    public long timestamp = -1;// 标记完成或者错误的时间戳

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(this.mTaskId);
        parcel.writeString(this.mFileName);
        parcel.writeInt(this.mInfoLen);
        parcel.writeInt(this.mTaskStatus);
        parcel.writeInt(this.mErrorCode);
        parcel.writeLong(this.mFileSize);
        parcel.writeLong(this.mDownloadSize);
        parcel.writeLong(this.mDownloadSpeed);
        parcel.writeInt(this.mQueryIndexStatus);
        parcel.writeString(this.mCid);
        parcel.writeString(this.mGcid);
        parcel.writeLong(this.mOriginSpeed);
        parcel.writeLong(this.mOriginRecvBytes);
        parcel.writeLong(this.mP2SSpeed);
        parcel.writeLong(this.mP2SRecvBytes);
        parcel.writeLong(this.mP2PSpeed);
        parcel.writeLong(this.mP2PRecvBytes);
        parcel.writeInt(this.mAdditionalResCount);
        parcel.writeInt(this.mAdditionalResType);
        parcel.writeLong(this.mAdditionalResVipSpeed);
        parcel.writeLong(this.mAdditionalResVipRecvBytes);
        parcel.writeLong(this.mAdditionalResPeerSpeed);
        parcel.writeLong(this.mAdditionalResPeerBytes);
        parcel.writeString(this.sourceUrl);
        parcel.writeString(this.torrentPath);
        parcel.writeInt(this.index);
        parcel.writeLong(this.timestamp);
    }

    public XLTaskInfo() {
    }

    public XLTaskInfo(Parcel parcel) {
        this.mTaskId = parcel.readLong();
        this.mFileName = parcel.readString();
        this.mInfoLen = parcel.readInt();
        this.mTaskStatus = parcel.readInt();
        this.mErrorCode = parcel.readInt();
        this.mFileSize = parcel.readLong();
        this.mDownloadSize = parcel.readLong();
        this.mDownloadSpeed = parcel.readLong();
        this.mQueryIndexStatus = parcel.readInt();
        this.mCid = parcel.readString();
        this.mGcid = parcel.readString();
        this.mOriginSpeed = parcel.readLong();
        this.mOriginRecvBytes = parcel.readLong();
        this.mP2SSpeed = parcel.readLong();
        this.mP2SRecvBytes = parcel.readLong();
        this.mP2PSpeed = parcel.readLong();
        this.mP2PRecvBytes = parcel.readLong();
        this.mAdditionalResCount = parcel.readInt();
        this.mAdditionalResType = parcel.readInt();
        this.mAdditionalResVipSpeed = parcel.readLong();
        this.mAdditionalResVipRecvBytes = parcel.readLong();
        this.mAdditionalResPeerSpeed = parcel.readLong();
        this.mAdditionalResPeerBytes = parcel.readLong();
        this.sourceUrl = parcel.readString();
        this.torrentPath = parcel.readString();
        this.index = parcel.readInt();
        this.timestamp = parcel.readLong();
    }

    @Override
    public String toString() {
        return "XLTaskInfo{" +
                "mAddedHighSourceState=" + mAddedHighSourceState +
                ", mAdditionalResCount=" + mAdditionalResCount +
                ", mAdditionalResDCDNBytes=" + mAdditionalResDCDNBytes +
                ", mAdditionalResDCDNSpeed=" + mAdditionalResDCDNSpeed +
                ", mAdditionalResPeerBytes=" + mAdditionalResPeerBytes +
                ", mAdditionalResPeerSpeed=" + mAdditionalResPeerSpeed +
                ", mAdditionalResType=" + mAdditionalResType +
                ", mAdditionalResVipRecvBytes=" + mAdditionalResVipRecvBytes +
                ", mAdditionalResVipSpeed=" + mAdditionalResVipSpeed +
                ", mCid='" + mCid + '\'' +
                ", mDownloadSize=" + mDownloadSize +
                ", mDownloadSpeed=" + mDownloadSpeed +
                ", mErrorCode=" + mErrorCode +
                ", mFileName='" + mFileName + '\'' +
                ", mFileSize=" + mFileSize +
                ", mGcid='" + mGcid + '\'' +
                ", mInfoLen=" + mInfoLen +
                ", mOriginRecvBytes=" + mOriginRecvBytes +
                ", mOriginSpeed=" + mOriginSpeed +
                ", mP2PRecvBytes=" + mP2PRecvBytes +
                ", mP2PSpeed=" + mP2PSpeed +
                ", mP2SRecvBytes=" + mP2SRecvBytes +
                ", mP2SSpeed=" + mP2SSpeed +
                ", mQueryIndexStatus=" + mQueryIndexStatus +
                ", mTaskId=" + mTaskId +
                ", mTaskStatus=" + mTaskStatus +
                ", sourceUrl='" + sourceUrl + '\'' +
                ", torrentPath='" + torrentPath + '\'' +
                ", index=" + index +
                ", timestamp=" + timestamp +
                '}';
    }
}
