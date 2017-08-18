package com.xunlei.downloadlib.android;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Build.VERSION;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Base64;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.NetworkInterface;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.json.JSONException;
import org.json.JSONObject;

public class XLUtil {
    private static final int IMEI_LEN = 15;
    private static final String TAG = "XLUtil";
    private static final ConfigFile sConfigFile = new ConfigFile();

    private static class ConfigFile {
        private static final String IDENTIFY_FILE_NAME = "Identify.txt";
        private Map<String, String> configMap;
        private ReadWriteLock lock;

        private ConfigFile() {
            this.configMap = new HashMap();
            this.lock = new ReentrantReadWriteLock();
        }

        String get(Context context, String str, String str2) {
            this.lock.readLock().lock();
            String str3 = (String) this.configMap.get(str);
            if (str3 == null) {
                loadFile(context);
                str3 = (String) this.configMap.get(str);
            }
            this.lock.readLock().unlock();
            return str3 != null ? str3 : str2;
        }

        void set(Context context, String str, String str2) {
            this.lock.writeLock().lock();
            this.configMap.put(str, str2);
            saveFile(context);
            this.lock.writeLock().unlock();
            XLLog.i(XLUtil.TAG, "ConfigFile set key=" + str + " value=" + str2);
        }

        private void loadFile(Context context) {
            XLLog.i(XLUtil.TAG, "loadAndParseFile start");
            if (context == null || IDENTIFY_FILE_NAME == null) {
                XLLog.e(XLUtil.TAG, "loadAndParseFile end, parameter invalid, fileName:Identify.txt");
                return;
            }
            String readFromFile = readFromFile(context, IDENTIFY_FILE_NAME);
            if (readFromFile == null) {
                XLLog.i(XLUtil.TAG, "loadAndParseFile end, fileContext is empty");
                return;
            }
            this.configMap.clear();
            String[] split = readFromFile.split("\n");
            for (String split2 : split) {
                String[] split3 = split2.split("=");
                if (split3.length == 2) {
                    this.configMap.put(split3[0], split3[1]);
                    XLLog.i(XLUtil.TAG, "ConfigFile loadFile key=" + split3[0] + " value=" + split3[1]);
                }
            }
            XLLog.i(XLUtil.TAG, "loadAndParseFile end");
        }

        private void saveFile(Context context) {
            StringBuilder stringBuilder = new StringBuilder();
            for (Entry entry : this.configMap.entrySet()) {
                stringBuilder.append(((String) entry.getKey()) + "=" + ((String) entry.getValue()) + "\n");
            }
            writeToFile(context, stringBuilder.toString(), IDENTIFY_FILE_NAME);
        }

        private void writeToFile(Context context, String str, String str2) {
            if (context == null || str == null || str2 == null) {
                XLLog.e(XLUtil.TAG, "writeToFile, Parameter invalid, fileName:" + str2);
                return;
            }
            try {
                OutputStream openFileOutput = context.openFileOutput(str2, 0);
                try {
                    openFileOutput.write(str.getBytes("utf-8"));
                    openFileOutput.close();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
            } catch (FileNotFoundException e3) {
                e3.printStackTrace();
            }
        }

        private String readFromFile(Context context, String str) {
            String str2 = null;
            if (context == null || str == null) {
                XLLog.e(XLUtil.TAG, "readFromFile, parameter invalid, fileName:" + str);
            } else {
                try {
                    InputStream openFileInput = context.openFileInput(str);
                    byte[] bArr = new byte[256];
                    try {
                        int read = openFileInput.read(bArr);
                        if (read > 0) {
                            str2 = new String(bArr, 0, read, "utf-8");
                        }
                        openFileInput.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (FileNotFoundException e2) {
                    XLLog.i(XLUtil.TAG, str + " File Not Found");
                }
            }
            return str2;
        }
    }

    public enum GUID_TYPE {
        DEFAULT,
        JUST_IMEI,
        JUST_MAC,
        ALL
    }

    public static class GuidInfo {
        public String mGuid = null;
        public GUID_TYPE mType = GUID_TYPE.DEFAULT;
    }

    public enum NetWorkCarrier {
        UNKNOWN,
        CMCC,
        CU,
        CT
    }

    public static long getCurrentUnixTime() {
        return System.currentTimeMillis() / 1000;
    }

    public static String getPeerid(Context context) {
        if (context == null) {
            return null;
        }
        String str = sConfigFile.get(context, "peerid", null);
        if (!TextUtils.isEmpty(str)) {
            return str;
        }
        String mac = getMAC(context);
        if (TextUtils.isEmpty(mac)) {
            mac = getIMEI(context);
            if (!TextUtils.isEmpty(mac)) {
                str = mac + "V";
            }
        } else {
            str = mac + "004V";
        }
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        sConfigFile.set(context, "peerid", str);
        return str;
    }

    public static String getMAC(Context context) {
        String str = null;
        if (context != null) {
            str = sConfigFile.get(context, "MAC", null);
            if (TextUtils.isEmpty(str)) {
                str = getWifiMacAddress();
                if (!TextUtils.isEmpty(str)) {
                    sConfigFile.set(context, "MAC", str);
                }
            }
        }
        return str;
    }

    @SuppressLint({"NewApi"})
    public static String getWifiMacAddress() {
        try {
            String str = "wlan0";
            for (NetworkInterface networkInterface : Collections.list(NetworkInterface.getNetworkInterfaces())) {
                if (networkInterface.getName().equalsIgnoreCase(str)) {
                    byte[] hardwareAddress = networkInterface.getHardwareAddress();
                    if (hardwareAddress == null) {
                        return null;
                    }
                    StringBuilder stringBuilder = new StringBuilder();
                    int length = hardwareAddress.length;
                    for (int i = 0; i < length; i++) {
                        stringBuilder.append(String.format("%02X", new Object[]{Byte.valueOf(hardwareAddress[i])}));
                    }
                    return stringBuilder.toString();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getIMEI(Context context) {
        Exception e;
        if (context == null) {
            return null;
        }
        String str = sConfigFile.get(context, "IMEI", null);
        if (!TextUtils.isEmpty(str)) {
            return str;
        }
        String deviceId;
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
        if (telephonyManager != null) {
            try {
                deviceId = telephonyManager.getDeviceId();
                if (deviceId != null) {
                    try {
                        if (deviceId.length() < 15) {
                            str = deviceId;
                            int length = 15 - deviceId.length();
                            while (true) {
                                int i = length - 1;
                                if (length <= 0) {
                                    break;
                                }
                                str = str + "M";
                                length = i;
                            }
                            deviceId = str;
                        }
                        sConfigFile.set(context, "IMEI", deviceId);
                    } catch (Exception e2) {
                        e = e2;
                        e.printStackTrace();
                        return deviceId;
                    }
                }
            } catch (Exception e3) {
                Exception exception = e3;
                deviceId = str;
                e = exception;
            }
        } else {
            deviceId = str;
        }
        return deviceId;
    }

    public static GuidInfo generateGuid(Context context) {
        GuidInfo guidInfo = new GuidInfo();
        GUID_TYPE guid_type = GUID_TYPE.DEFAULT;
        String imei = getIMEI(context);
        if (TextUtils.isEmpty(imei)) {
            imei = "000000000000000";
        } else {
            guid_type = GUID_TYPE.JUST_IMEI;
        }
        String mac = getMAC(context);
        if (TextUtils.isEmpty(mac)) {
            mac = "000000000000";
        } else if (guid_type == GUID_TYPE.JUST_IMEI) {
            guid_type = GUID_TYPE.ALL;
        } else {
            guid_type = GUID_TYPE.JUST_MAC;
        }
        guidInfo.mGuid = imei + "_" + mac;
        guidInfo.mType = guid_type;
        return guidInfo;
    }

    public static String getOSVersion(Context context) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SDKV = " + VERSION.RELEASE);
        stringBuilder.append("_MANUFACTURER = " + Build.MANUFACTURER);
        stringBuilder.append("_MODEL = " + Build.MODEL);
        stringBuilder.append("_PRODUCT = " + Build.PRODUCT);
        stringBuilder.append("_FINGERPRINT = " + Build.FINGERPRINT);
        stringBuilder.append("_CPU_ABI = " + Build.CPU_ABI);
        stringBuilder.append("_ID = " + Build.ID);
        return stringBuilder.toString();
    }

    public static String getAPNName(Context context) {
        if (context == null) {
            return null;
        }
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
        if (connectivityManager == null) {
            return null;
        }
        NetworkInfo networkInfo = connectivityManager.getNetworkInfo(0);
        if (networkInfo == null) {
            return null;
        }
        return networkInfo.getExtraInfo();
    }

    public static String getSSID(Context context) {
        if (context == null) {
            XLLog.e(TAG, "getSSID, context invalid");
            return null;
        }
        WifiManager wifiManager = (WifiManager) context.getSystemService("wifi");
        if (wifiManager != null) {
            WifiInfo connectionInfo = wifiManager.getConnectionInfo();
            if (connectionInfo != null) {
                return connectionInfo.getSSID();
            }
        }
        return null;
    }

    public static String getBSSID(Context context) {
        if (context == null) {
            XLLog.e(TAG, "getBSSID, context invalid");
            return null;
        }
        WifiManager wifiManager = (WifiManager) context.getSystemService("wifi");
        if (wifiManager != null) {
            try {
                WifiInfo connectionInfo = wifiManager.getConnectionInfo();
                if (connectionInfo != null) {
                    return connectionInfo.getBSSID();
                }
            } catch (Exception e) {
            }
        }
        return null;
    }

    public static int getNetworkTypeComplete(Context context) {
        if (context == null) {
            XLLog.e(TAG, "getNetworkTypeComplete, context invalid");
            return 0;
        }
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
        if (connectivityManager == null) {
            return 0;
        }
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo == null) {
            return 0;
        }
        int type = activeNetworkInfo.getType();
        if (type == 1) {
            return 9;
        }
        if (type != 0) {
            return 5;
        }
        int i;
        switch (activeNetworkInfo.getSubtype()) {
            case 1:
            case 2:
            case 4:
            case 7:
            case 11:
                return 2;
            case 3:
            case 5:
            case 6:
            case 8:
            case 9:
            case 10:
            case 12:
            case 14:
            case 15:
                return 3;
            case 13:
                i = 4;
                break;
            default:
                i = 0;
                break;
        }
        return i;
    }

    public static int getNetworkTypeSimple(Context context) {
        if (context == null) {
            XLLog.e(TAG, "getNetworkTypeSimple, context invalid");
            return 0;
        }
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
        if (connectivityManager == null) {
            return 1;
        }
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo == null) {
            return 1;
        }
        int type = activeNetworkInfo.getType();
        if (type == 1) {
            return 2;
        }
        if (type != 0) {
            return 1;
        }
        int i;
        switch (activeNetworkInfo.getSubtype()) {
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
                i = 3;
                break;
            default:
                i = 1;
                break;
        }
        return i;
    }

    public static String getMd5(String str) {
        if (str == null) {
            XLLog.e(TAG, "getMd5, key invalid");
            return null;
        }
        try {
            char[] cArr = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
            MessageDigest instance = MessageDigest.getInstance("MD5");
            byte[] bytes = str.getBytes();
            instance.update(bytes, 0, bytes.length);
            byte[] digest = instance.digest();
            StringBuilder stringBuilder = new StringBuilder(32);
            for (byte b : digest) {
                stringBuilder.append(cArr[(b >> 4) & 15]).append(cArr[(b >> 0) & 15]);
            }
            return stringBuilder.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return str;
        }
    }

    public static String generateAppKey(String str, short s, byte b) {
        if (str == null) {
            XLLog.e(TAG, "generateAppKey, appName invalid");
            return null;
        }
        int length = str.length();
        byte[] bArr = new byte[(((length + 1) + 2) + 1)];
        byte[] bytes = str.getBytes();
        for (int i = 0; i < bytes.length; i++) {
            bArr[i] = bytes[i];
        }
        bArr[length] = (byte) 0;
        bArr[length + 1] = (byte) (s & 255);
        bArr[length + 2] = (byte) ((s >> 8) & 255);
        bArr[length + 3] = b;
        return new String(Base64.encode(bArr, 0)).trim();
    }

    public static Map<String, Object> parseJSONString(String str) {
        if (str == null) {
            XLLog.e(TAG, "parseJSONString, JSONString invalid");
            return null;
        }
        Map<String, Object> hashMap = new HashMap();
        try {
            JSONObject jSONObject = new JSONObject(str);
            Iterator keys = jSONObject.keys();
            while (keys.hasNext()) {
                String str2 = (String) keys.next();
                hashMap.put(str2, jSONObject.getString(str2));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return hashMap;
    }

    public static NetWorkCarrier getNetWorkCarrier(Context context) {
        if (context != null) {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
            if (telephonyManager != null) {
                try {
                    String subscriberId = telephonyManager.getSubscriberId();
                    if (subscriberId.startsWith("46000") || subscriberId.startsWith("46002")) {
                        return NetWorkCarrier.CMCC;
                    }
                    if (subscriberId.startsWith("46001")) {
                        return NetWorkCarrier.CU;
                    }
                    if (subscriberId.startsWith("46003")) {
                        return NetWorkCarrier.CT;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return NetWorkCarrier.UNKNOWN;
    }
}
