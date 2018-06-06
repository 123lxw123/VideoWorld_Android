package com.xunlei.downloadlib.parameter;

public class InitParam {
    public String mAppKey;
    public String mAppVersion;
    public int mPermissionLevel;
    public String mStatCfgSavePath;
    public String mStatSavePath;

    public InitParam(String str, String str2, String str3, String str4, int i) {
        this.mAppKey = str;
        this.mAppVersion = str2;
        this.mStatSavePath = str3;
        this.mStatCfgSavePath = str4;
        this.mPermissionLevel = i;
    }

    public InitParam() {
    }

    public boolean checkMemberVar() {
        if (this.mAppKey == null || this.mAppVersion == null || this.mStatSavePath == null || this.mStatCfgSavePath == null) {
            return false;
        }
        return true;
    }
}
