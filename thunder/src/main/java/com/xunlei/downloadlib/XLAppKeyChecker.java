package com.xunlei.downloadlib;

import android.content.Context;
import android.util.Base64;
import com.xunlei.downloadlib.android.XLLog;
import com.xunlei.downloadlib.android.XLUtil;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class XLAppKeyChecker {
    private static final byte APPTYPE_PRODUCT = (byte) 1;
    private static final String TAG = "XLAppKeyChecker";
    private short mAppId = (short) 0;
    private String mAppKey = "";
    private Context mContext = null;
    private String mPackageName = "";

    private class AppKeyEntity {
        private String MD5;
        private RawItemsEntity mItemsEntity;
        private String mRawItems;

        private AppKeyEntity() {
            this.mRawItems = "";
            this.MD5 = "";
            this.mItemsEntity = null;
        }

        public RawItemsEntity getmItemsEntity() {
            return this.mItemsEntity;
        }

        public void setmItemsEntity(RawItemsEntity rawItemsEntity) {
            this.mItemsEntity = rawItemsEntity;
        }

        public String getMD5() {
            return this.MD5;
        }

        public void setMD5(String str) {
            this.MD5 = str;
        }

        public String getmRawItems() {
            return this.mRawItems;
        }

        public void setmRawItems(String str) {
            this.mRawItems = str;
        }
    }

    public class KeyFormateException extends Exception {
        private static final long serialVersionUID = 13923744320L;

        public KeyFormateException(String str) {
            super(str);
        }
    }

    private class RawItemsEntity {
        private short mAppId;
        private Date mExpired;

        private RawItemsEntity() {
            this.mAppId = (short) 0;
            this.mExpired = null;
        }

        public short getAppId() {
            return this.mAppId;
        }

        public void setAppId(short s) {
            this.mAppId = s;
        }

        public Date getExpired() {
            return this.mExpired;
        }

        public void setExpired(Date date) {
            this.mExpired = date;
        }
    }

    public XLAppKeyChecker(Context context, String str) {
        this.mContext = context;
        this.mAppKey = str;
    }

    public boolean verify() {
        try {
            AppKeyEntity keyEntity = getKeyEntity();
            this.mPackageName = "com.xunlei.downloadprovider";
            if (!verifyAppKeyMD5(keyEntity, this.mPackageName)) {
                XLLog.i(TAG, "appkey MD5 invalid.");
                return false;
            } else if (!verifyAppKeyExpired(keyEntity)) {
                return true;
            } else {
                XLLog.i(TAG, "appkey expired.");
                return false;
            }
        } catch (KeyFormateException e) {
            return false;
        }
    }

    private boolean verifyAppKeyMD5(AppKeyEntity appKeyEntity, String str) {
        String str2 = appKeyEntity.getmRawItems() + ";" + str;
        XLLog.i(TAG, "totalContent:" + str2);
        str2 = XLUtil.getMd5(str2).toLowerCase().replace('b', '^').replace('9', 'b');
        XLLog.i(TAG, "keyEntity getMD5 MD5:" + appKeyEntity.getMD5());
        if (str2.compareTo(appKeyEntity.getMD5()) == 0) {
            return true;
        }
        return false;
    }

    private boolean verifyAppKeyExpired(AppKeyEntity appKeyEntity) {
        Date expired = appKeyEntity.getmItemsEntity().getExpired();
        if (expired == null) {
            return false;
        }
        return expired.before(Calendar.getInstance().getTime());
    }

    public String getSoAppKey() {
        return XLUtil.generateAppKey("com.xunlei.downloadprovider", this.mAppId, (byte) 1);
    }

    private AppKeyEntity getKeyEntity() throws KeyFormateException {
        String[] split = this.mAppKey.split("==");
        if (split.length != 2) {
            XLLog.i(TAG, "keyPair length invalid");
            throw new KeyFormateException("error");
        }
        AppKeyEntity appKeyEntity = new AppKeyEntity();
        appKeyEntity.setMD5(split[1]);
        try {
            String replace = split[0].replace('^', '=');
            String str = new String(Base64.decode(replace.substring(2, replace.length() - 2), 0), "UTF-8");
            appKeyEntity.setmRawItems(str);
            XLLog.i(TAG, "items:" + str);
            appKeyEntity.setmItemsEntity(getRawItemsEntity(str));
            return appKeyEntity;
        } catch (UnsupportedEncodingException e) {
            throw new KeyFormateException("error");
        }
    }

    private RawItemsEntity getRawItemsEntity(String str) throws KeyFormateException {
        String[] split = str.split(";");
        RawItemsEntity rawItemsEntity = new RawItemsEntity();
        if (split.length <= 0 || split.length > 2) {
            throw new KeyFormateException("raw item length invalid.");
        }
        try {
            this.mAppId = Short.parseShort(split[0]);
            rawItemsEntity.setAppId(this.mAppId);
            if (split.length == 2) {
                try {
                    rawItemsEntity.setExpired(new SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(split[1]));
                } catch (ParseException e) {
                    throw new KeyFormateException("expired field formate error.");
                }
            }
            return rawItemsEntity;
        } catch (NumberFormatException e2) {
            XLLog.i(TAG, "appId invalid");
            e2.printStackTrace();
            throw new KeyFormateException("app id format error.");
        }
    }
}