package com.xunlei.downloadlib;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Base64;
import com.xunlei.downloadlib.android.XLLog;
import com.xunlei.downloadlib.android.XLUtil;
import com.xunlei.downloadlib.android.XLUtil.GUID_TYPE;
import com.xunlei.downloadlib.android.XLUtil.GuidInfo;
import com.xunlei.downloadlib.parameter.BtIndexSet;
import com.xunlei.downloadlib.parameter.BtSubTaskDetail;
import com.xunlei.downloadlib.parameter.BtTaskParam;
import com.xunlei.downloadlib.parameter.BtTaskStatus;
import com.xunlei.downloadlib.parameter.CIDTaskParam;
import com.xunlei.downloadlib.parameter.EmuleTaskParam;
import com.xunlei.downloadlib.parameter.GetDownloadHead;
import com.xunlei.downloadlib.parameter.GetDownloadLibVersion;
import com.xunlei.downloadlib.parameter.GetFileName;
import com.xunlei.downloadlib.parameter.GetTaskId;
import com.xunlei.downloadlib.parameter.InitParam;
import com.xunlei.downloadlib.parameter.MagnetTaskParam;
import com.xunlei.downloadlib.parameter.MaxDownloadSpeedParam;
import com.xunlei.downloadlib.parameter.P2spTaskParam;
import com.xunlei.downloadlib.parameter.PeerResourceParam;
import com.xunlei.downloadlib.parameter.ServerResourceParam;
import com.xunlei.downloadlib.parameter.ThunderUrlInfo;
import com.xunlei.downloadlib.parameter.TorrentInfo;
import com.xunlei.downloadlib.parameter.UrlQuickInfo;
import com.xunlei.downloadlib.parameter.XLConstant.XLManagerStatus;
import com.xunlei.downloadlib.parameter.XLProductInfo;
import com.xunlei.downloadlib.parameter.XLSessionInfo;
import com.xunlei.downloadlib.parameter.XLTaskInfo;
import com.xunlei.downloadlib.parameter.XLTaskInfoEx;
import com.xunlei.downloadlib.parameter.XLTaskLocalUrl;
import java.io.File;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class XLDownloadManager {
    private static final int GET_GUID_FIRST_TIME = 5000;
    private static final int GET_GUID_INTERVAL_TIME = 60000;
    private static final int QUERY_GUID_COUNT = 5;
    private static final String TAG = "XLDownloadManager";
    private static boolean mAllowExecution = true;
    public static XLManagerStatus mDownloadManagerState = XLManagerStatus.MANAGER_UNINIT;
    private static Map<String, Object> mErrcodeStringMap = null;
    private static XLDownloadManager mInstance = null;
    private static boolean mIsLoadErrcodeMsg = false;
    private static int mRunningRefCount = 0;
    private XLAppKeyChecker mAppkeyChecker;
    private Context mContext;
    private Timer mGetGuidTimer;
    private TimerTask mGetGuidTimerTask;
    private XLLoader mLoader;
    private int mQueryGuidCount;
    private NetworkChangeReceiver mReceiver;

    public static synchronized XLDownloadManager getInstance() {
        XLDownloadManager xLDownloadManager;
        synchronized (XLDownloadManager.class) {
            if (mInstance == null) {
                mInstance = new XLDownloadManager();
            }
            xLDownloadManager = mInstance;
        }
        return xLDownloadManager;
    }

    private XLDownloadManager() {
        this.mLoader = null;
        this.mContext = null;
        this.mReceiver = null;
        this.mAppkeyChecker = null;
        this.mQueryGuidCount = 0;
        this.mLoader = new XLLoader();
        XLLog.init(new File(Environment.getExternalStorageDirectory().getPath(), "xunlei_ds_log.ini").getPath());
    }

    public XLManagerStatus getManagerStatus() {
        return mDownloadManagerState;
    }

    private void doMonitorNetworkChange() {
        XLLog.i(TAG, "doMonitorNetworkChange()");
        if (this.mContext != null && this.mReceiver == null) {
            this.mReceiver = new NetworkChangeReceiver(this);
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
            XLLog.i(TAG, "register Receiver");
            this.mContext.registerReceiver(this.mReceiver, intentFilter);
        }
    }

    private void undoMonitorNetworkChange() {
        XLLog.i(TAG, "undoMonitorNetworkChange()");
        if (this.mContext != null && this.mReceiver != null) {
            try {
                this.mContext.unregisterReceiver(this.mReceiver);
                XLLog.i(TAG, "unregister Receiver");
            } catch (IllegalArgumentException e) {
                XLLog.e(TAG, "Receiver not registered");
            }
            this.mReceiver = null;
        }
    }

    private synchronized void increRefCount() {
        mRunningRefCount++;
    }

    private synchronized void decreRefCount() {
        mRunningRefCount--;
    }

    public synchronized int init(Context context, InitParam initParam) {
        return init(context, initParam, true);
    }

    public synchronized int init(Context context, InitParam initParam, boolean z) {
        int i = 9900;
        int i2 = 0;
        synchronized (this) {
            if (!mIsLoadErrcodeMsg) {
                loadErrcodeString(context);
                mIsLoadErrcodeMsg = true;
            }
            if (!(context == null || initParam == null || !initParam.checkMemberVar())) {
                this.mContext = context;
                mAllowExecution = z;
                if (mDownloadManagerState == XLManagerStatus.MANAGER_RUNNING) {
                    XLLog.i(TAG, "XLDownloadManager is already init");
                } else if (this.mLoader != null) {
                    this.mAppkeyChecker = new XLAppKeyChecker(context, initParam.mAppKey);
                    if (this.mAppkeyChecker.verify()) {
                        XLLog.i(TAG, "appKey check successful");
                        String soAppKey = this.mAppkeyChecker.getSoAppKey();
                        String peerid = getPeerid();
                        String guid = getGuid();
                        XLLog.i(TAG, "Peerid:" + new String(Base64.encode(peerid.getBytes(), 0)));
                        XLLog.i(TAG, "Guid:" + new String(Base64.encode(guid.getBytes(), 0)));
                        if (mAllowExecution) {
                            i2 = XLUtil.getNetworkTypeComplete(context);
                        }
                        i = this.mLoader.init(soAppKey, "com.xunlei.downloadprovider", initParam.mAppVersion, "", peerid, guid, initParam.mStatSavePath, initParam.mStatCfgSavePath, i2, initParam.mPermissionLevel);
                        if (i != 9000) {
                            mDownloadManagerState = XLManagerStatus.MANAGER_INIT_FAIL;
                        } else {
                            mDownloadManagerState = XLManagerStatus.MANAGER_RUNNING;
                            doMonitorNetworkChange();
                            setLocalProperty("PhoneModel", Build.MODEL);
                        }
                    } else {
                        XLLog.i(TAG, "appKey check failed");
                        i = 9901;
                    }
                }
            }
        }
        return i;
    }

    public synchronized int uninit() {
        int i = 9900;
        synchronized (this) {
            if (mRunningRefCount != 0) {
                XLLog.i(TAG, "some function of XLDownloadManager is running, uninit failed!");
            } else if (!(mDownloadManagerState == XLManagerStatus.MANAGER_UNINIT || this.mLoader == null)) {
                if (mDownloadManagerState == XLManagerStatus.MANAGER_RUNNING) {
                    undoMonitorNetworkChange();
                }
                stopGetGuidTimer();
                i = this.mLoader.unInit();
                mDownloadManagerState = XLManagerStatus.MANAGER_UNINIT;
                this.mContext = null;
            }
        }
        return i;
    }

    int notifyNetWorkType(int i, XLLoader xLLoader) {
        int i2 = 9900;
        if (mDownloadManagerState == XLManagerStatus.MANAGER_RUNNING && xLLoader != null) {
            try {
                i2 = xLLoader.notifyNetWorkType(i);
            } catch (Error e) {
                XLLog.e(TAG, "notifyNetWorkType failed," + e.getMessage());
            }
        }
        return i2;
    }

    public int createP2spTask(P2spTaskParam p2spTaskParam, GetTaskId getTaskId) {
        int i = 9900;
        if (!(p2spTaskParam == null || getTaskId == null || !p2spTaskParam.checkMemberVar())) {
            increRefCount();
            if (mDownloadManagerState == XLManagerStatus.MANAGER_RUNNING && this.mLoader != null) {
                i = this.mLoader.createP2spTask(p2spTaskParam.mUrl, p2spTaskParam.mRefUrl, p2spTaskParam.mCookie, p2spTaskParam.mUser, p2spTaskParam.mPass, p2spTaskParam.mFilePath, p2spTaskParam.mFileName, p2spTaskParam.mCreateMode, p2spTaskParam.mSeqId, getTaskId);
            }
            decreRefCount();
        }
        return i;
    }

    public int releaseTask(long j) {
        int i = 9900;
        increRefCount();
        if (mDownloadManagerState == XLManagerStatus.MANAGER_RUNNING && this.mLoader != null) {
            i = this.mLoader.releaseTask(j);
        }
        decreRefCount();
        return i;
    }

    int setTaskAppInfo(long j, String str, String str2, String str3) {
        if (mDownloadManagerState != XLManagerStatus.MANAGER_RUNNING || this.mLoader == null || str == null || str2 == null || str3 == null) {
            return 9900;
        }
        return this.mLoader.setTaskAppInfo(j, str, str2, str3);
    }

    public int setTaskAllowUseResource(long j, int i) {
        int i2 = 9900;
        increRefCount();
        if (mDownloadManagerState == XLManagerStatus.MANAGER_RUNNING && this.mLoader != null) {
            i2 = this.mLoader.setTaskAllowUseResource(j, i);
        }
        decreRefCount();
        return i2;
    }

    public int setTaskUid(long j, int i) {
        int i2 = 9900;
        increRefCount();
        if (mDownloadManagerState == XLManagerStatus.MANAGER_RUNNING && this.mLoader != null) {
            i2 = this.mLoader.setTaskUid(j, i);
        }
        decreRefCount();
        return i2;
    }

    public int startTask(long j, boolean z) {
        int i = 9900;
        increRefCount();
        if (mDownloadManagerState == XLManagerStatus.MANAGER_RUNNING && this.mLoader != null) {
            i = this.mLoader.startTask(j, z);
        }
        decreRefCount();
        return i;
    }

    public int startTask(long j) {
        return startTask(j, false);
    }

    int switchOriginToAllResDownload(long j) {
        if (mDownloadManagerState != XLManagerStatus.MANAGER_RUNNING || this.mLoader == null) {
            return 9900;
        }
        return this.mLoader.switchOriginToAllResDownload(j);
    }

    public int stopTask(long j) {
        int i = 9900;
        increRefCount();
        if (mDownloadManagerState == XLManagerStatus.MANAGER_RUNNING && this.mLoader != null) {
            i = this.mLoader.stopTask(j);
        }
        XLLog.i(TAG, "XLStopTask()----- ret=" + i);
        decreRefCount();
        return i;
    }

    public int stopTaskWithReason(long j, int i) {
        int i2 = 9900;
        increRefCount();
        if (mDownloadManagerState == XLManagerStatus.MANAGER_RUNNING && this.mLoader != null) {
            i2 = this.mLoader.stopTaskWithReason(j, i);
        }
        XLLog.i(TAG, "XLStopTask()----- ret=" + i2);
        decreRefCount();
        return i2;
    }

    public int getTaskInfo(long j, int i, XLTaskInfo xLTaskInfo) {
        int i2 = 9900;
        increRefCount();
        if (!(mDownloadManagerState != XLManagerStatus.MANAGER_RUNNING || this.mLoader == null || xLTaskInfo == null)) {
            i2 = this.mLoader.getTaskInfo(j, i, xLTaskInfo);
        }
        decreRefCount();
        return i2;
    }

    public int getTaskInfoEx(long j, XLTaskInfoEx xLTaskInfoEx) {
        int i = 9900;
        increRefCount();
        if (!(mDownloadManagerState != XLManagerStatus.MANAGER_RUNNING || this.mLoader == null || xLTaskInfoEx == null)) {
            i = this.mLoader.getTaskInfoEx(j, xLTaskInfoEx);
        }
        decreRefCount();
        return i;
    }

    public int getLocalUrl(String str, XLTaskLocalUrl xLTaskLocalUrl) {
        int i = 9900;
        increRefCount();
        if (!(mDownloadManagerState != XLManagerStatus.MANAGER_RUNNING || this.mLoader == null || xLTaskLocalUrl == null)) {
            i = this.mLoader.getLocalUrl(str, xLTaskLocalUrl);
        }
        decreRefCount();
        return i;
    }

    public int addServerResource(long j, ServerResourceParam serverResourceParam) {
        int i = 9900;
        if (serverResourceParam != null && serverResourceParam.checkMemberVar()) {
            increRefCount();
            if (mDownloadManagerState == XLManagerStatus.MANAGER_RUNNING && this.mLoader != null) {
                XLLog.i(TAG, "respara.mUrl=" + serverResourceParam.mUrl);
                i = this.mLoader.addServerResource(j, serverResourceParam.mUrl, serverResourceParam.mRefUrl, serverResourceParam.mCookie, serverResourceParam.mResType, serverResourceParam.mStrategy);
            }
            decreRefCount();
        }
        return i;
    }

    public int addPeerResource(long j, PeerResourceParam peerResourceParam) {
        int i = 9900;
        if (peerResourceParam == null || !peerResourceParam.checkMemberVar()) {
            return 9900;
        }
        increRefCount();
        if (mDownloadManagerState == XLManagerStatus.MANAGER_RUNNING && this.mLoader != null) {
            i = this.mLoader.addPeerResource(j, peerResourceParam.mPeerId, peerResourceParam.mUserId, peerResourceParam.mJmpKey, peerResourceParam.mVipCdnAuth, peerResourceParam.mInternalIp, peerResourceParam.mTcpPort, peerResourceParam.mUdpPort, peerResourceParam.mResLevel, peerResourceParam.mResPriority, peerResourceParam.mCapabilityFlag, peerResourceParam.mResType);
        }
        decreRefCount();
        return i;
    }

    public int removeServerResource(long j, int i) {
        int i2 = 9900;
        increRefCount();
        if (mDownloadManagerState == XLManagerStatus.MANAGER_RUNNING && this.mLoader != null) {
            i2 = this.mLoader.removeAddedServerResource(j, i);
        }
        decreRefCount();
        return i2;
    }

    int requeryTaskIndex(long j) {
        if (mDownloadManagerState != XLManagerStatus.MANAGER_RUNNING || this.mLoader == null) {
            return 9900;
        }
        return this.mLoader.requeryIndex(j);
    }

    public int setOriginUserAgent(long j, String str) {
        int i = 9900;
        increRefCount();
        if (!(mDownloadManagerState != XLManagerStatus.MANAGER_RUNNING || this.mLoader == null || str == null)) {
            i = this.mLoader.setOriginUserAgent(j, str);
        }
        decreRefCount();
        return i;
    }

    public int setUserId(String str) {
        if (mDownloadManagerState != XLManagerStatus.MANAGER_RUNNING || this.mLoader == null || str == null) {
            return 9900;
        }
        return this.mLoader.setUserId(str);
    }

    public int getDownloadHeader(long j, GetDownloadHead getDownloadHead) {
        int i = 9900;
        increRefCount();
        if (!(mDownloadManagerState != XLManagerStatus.MANAGER_RUNNING || this.mLoader == null || getDownloadHead == null)) {
            i = this.mLoader.getDownloadHeader(j, getDownloadHead);
        }
        decreRefCount();
        return i;
    }

    public int setFileName(long j, String str) {
        int i = 9900;
        increRefCount();
        if (!(mDownloadManagerState != XLManagerStatus.MANAGER_RUNNING || this.mLoader == null || str == null)) {
            i = this.mLoader.setFileName(j, str);
        }
        decreRefCount();
        return i;
    }

    int notifyNetWorkCarrier(int i) {
        if (mDownloadManagerState != XLManagerStatus.MANAGER_RUNNING || this.mLoader == null) {
            return 9900;
        }
        return this.mLoader.setNotifyNetWorkCarrier(i);
    }

    int notifyWifiBSSID(String str, XLLoader xLLoader) {
        int i = 9900;
        if (mDownloadManagerState == XLManagerStatus.MANAGER_RUNNING && xLLoader != null) {
            if (str == null || str.length() == 0 || str == "<unknown ssid>") {
                str = "";
            }
            try {
                i = xLLoader.setNotifyWifiBSSID(str);
            } catch (Error e) {
                XLLog.e(TAG, "setNotifyWifiBSSID failed," + e.getMessage());
            }
        }
        return i;
    }

    public int setDownloadTaskOrigin(long j, String str) {
        int i = 9900;
        increRefCount();
        if (!(mDownloadManagerState != XLManagerStatus.MANAGER_RUNNING || this.mLoader == null || str == null)) {
            i = this.mLoader.setDownloadTaskOrigin(j, str);
        }
        decreRefCount();
        return i;
    }

    int setMac(String str) {
        if (mDownloadManagerState != XLManagerStatus.MANAGER_RUNNING || this.mLoader == null || str == null) {
            return 9900;
        }
        return this.mLoader.setMac(str);
    }

    int setImei(String str) {
        if (mDownloadManagerState != XLManagerStatus.MANAGER_RUNNING || this.mLoader == null || str == null) {
            return 9900;
        }
        return this.mLoader.setImei(str);
    }

    private int setLocalProperty(String str, String str2) {
        if (mDownloadManagerState != XLManagerStatus.MANAGER_RUNNING || this.mLoader == null || str == null || str2 == null) {
            return 9900;
        }
        return this.mLoader.setLocalProperty(str, str2);
    }

    public int setOSVersion(String str) {
        int i = 9900;
        increRefCount();
        if (!(mDownloadManagerState != XLManagerStatus.MANAGER_RUNNING || this.mLoader == null || str == null)) {
            i = this.mLoader.setMiUiVersion(str);
        }
        decreRefCount();
        return i;
    }

    public int setHttpHeaderProperty(long j, String str, String str2) {
        int i = 9900;
        increRefCount();
        if (!(mDownloadManagerState != XLManagerStatus.MANAGER_RUNNING || this.mLoader == null || str == null || str2 == null)) {
            i = this.mLoader.setHttpHeaderProperty(j, str, str2);
        }
        decreRefCount();
        return i;
    }

    public int getDownloadLibVersion(GetDownloadLibVersion getDownloadLibVersion) {
        int i = 9900;
        increRefCount();
        if (!(mDownloadManagerState != XLManagerStatus.MANAGER_RUNNING || this.mLoader == null || getDownloadLibVersion == null)) {
            i = this.mLoader.getDownloadLibVersion(getDownloadLibVersion);
        }
        decreRefCount();
        return i;
    }

    public int getProductInfo(XLProductInfo xLProductInfo) {
        increRefCount();
        if (mDownloadManagerState != XLManagerStatus.MANAGER_RUNNING || this.mContext == null || xLProductInfo == null) {
            decreRefCount();
            return 9900;
        }
        xLProductInfo.mProductKey = this.mAppkeyChecker.getSoAppKey();
        xLProductInfo.mProductName = this.mContext.getPackageName();
        return 9000;
    }

    private String getPeerid() {
        if (!mAllowExecution) {
            return "000000000000000V";
        }
        String peerid = XLUtil.getPeerid(this.mContext);
        if (peerid == null) {
            return "000000000000000V";
        }
        return peerid;
    }

    private String getGuid() {
        if (!mAllowExecution) {
            return "00000000000000_000000000000";
        }
        GuidInfo guidInfo = new GuidInfo();
        guidInfo = XLUtil.generateGuid(this.mContext);
        if (guidInfo.mType != GUID_TYPE.ALL) {
            XLLog.i(TAG, "Start the GetGuidTimer");
            startGetGuidTimer();
        }
        return guidInfo.mGuid;
    }

    private void startGetGuidTimer() {
        this.mGetGuidTimer = new Timer();
        this.mGetGuidTimerTask = new MyTimerTask(this);
        this.mGetGuidTimer.schedule(this.mGetGuidTimerTask, 5000, 60000);
    }

    private void stopGetGuidTimer() {
        if (this.mGetGuidTimer instanceof Timer) {
            this.mGetGuidTimer.cancel();
            this.mGetGuidTimer.purge();
            this.mGetGuidTimer = null;
            XLLog.i(TAG, "stopGetGuidTimer");
        }
        if (this.mGetGuidTimerTask instanceof TimerTask) {
            this.mGetGuidTimerTask.cancel();
            this.mGetGuidTimerTask = null;
        }
    }

    public int enterPrefetchMode(long j) {
        int i = 9900;
        increRefCount();
        if (mDownloadManagerState == XLManagerStatus.MANAGER_RUNNING && this.mLoader != null) {
            i = this.mLoader.enterPrefetchMode(j);
        }
        decreRefCount();
        return i;
    }

    public int setTaskLxState(long j, int i, int i2) {
        int i3 = 9900;
        increRefCount();
        if (mDownloadManagerState == XLManagerStatus.MANAGER_RUNNING && this.mLoader != null) {
            i3 = this.mLoader.setTaskLxState(j, i, i2);
        }
        decreRefCount();
        return i3;
    }

    public int setTaskGsState(long j, int i, int i2) {
        int i3 = 9900;
        increRefCount();
        if (mDownloadManagerState == XLManagerStatus.MANAGER_RUNNING && this.mLoader != null) {
            i3 = this.mLoader.setTaskGsState(j, i, i2);
        }
        decreRefCount();
        return i3;
    }

    public int setReleaseLog(boolean z, String str, int i, int i2) {
        int i3 = 9900;
        increRefCount();
        if (mDownloadManagerState == XLManagerStatus.MANAGER_RUNNING && this.mLoader != null) {
            i3 = z ? this.mLoader.setReleaseLog(1, str, i, i2) : this.mLoader.setReleaseLog(0, null, 0, 0);
        }
        decreRefCount();
        return i3;
    }

    public int setReleaseLog(boolean z, String str) {
        return setReleaseLog(z, str, 0, 0);
    }

    public boolean isLogTurnOn() {
        boolean z = false;
        increRefCount();
        if (mDownloadManagerState == XLManagerStatus.MANAGER_RUNNING && this.mLoader != null) {
            z = this.mLoader.isLogTurnOn();
        }
        decreRefCount();
        return z;
    }

    public int setStatReportSwitch(boolean z) {
        int i = 9900;
        increRefCount();
        if (mDownloadManagerState == XLManagerStatus.MANAGER_RUNNING && this.mLoader != null) {
            i = this.mLoader.setStatReportSwitch(z);
        }
        decreRefCount();
        return i;
    }

    public int createBtMagnetTask(MagnetTaskParam magnetTaskParam, GetTaskId getTaskId) {
        int i = 9900;
        if (!(magnetTaskParam == null || getTaskId == null || !magnetTaskParam.checkMemberVar())) {
            increRefCount();
            if (mDownloadManagerState == XLManagerStatus.MANAGER_RUNNING && this.mLoader != null) {
                i = this.mLoader.createBtMagnetTask(magnetTaskParam.mUrl, magnetTaskParam.mFilePath, magnetTaskParam.mFileName, getTaskId);
            }
            decreRefCount();
        }
        return i;
    }

    public int createEmuleTask(EmuleTaskParam emuleTaskParam, GetTaskId getTaskId) {
        int i = 9900;
        if (!(emuleTaskParam == null || getTaskId == null || !emuleTaskParam.checkMemberVar())) {
            increRefCount();
            if (mDownloadManagerState == XLManagerStatus.MANAGER_RUNNING && this.mLoader != null) {
                i = this.mLoader.createEmuleTask(emuleTaskParam.mUrl, emuleTaskParam.mFilePath, emuleTaskParam.mFileName, emuleTaskParam.mCreateMode, emuleTaskParam.mSeqId, getTaskId);
            }
            decreRefCount();
        }
        return i;
    }

    public int createBtTask(BtTaskParam btTaskParam, GetTaskId getTaskId) {
        int i = 9900;
        if (!(btTaskParam == null || getTaskId == null || !btTaskParam.checkMemberVar())) {
            increRefCount();
            if (mDownloadManagerState == XLManagerStatus.MANAGER_RUNNING && this.mLoader != null) {
                i = this.mLoader.createBtTask(btTaskParam.mTorrentPath, btTaskParam.mFilePath, btTaskParam.mMaxConcurrent, btTaskParam.mCreateMode, btTaskParam.mSeqId, getTaskId);
            }
            decreRefCount();
        }
        return i;
    }

    public int getTorrentInfo(String str, TorrentInfo torrentInfo) {
        int i = 9900;
        increRefCount();
        if (!(this.mLoader == null || str == null || torrentInfo == null)) {
            i = this.mLoader.getTorrentInfo(str, torrentInfo);
        }
        decreRefCount();
        return i;
    }

    public int getBtSubTaskStatus(long j, BtTaskStatus btTaskStatus, int i, int i2) {
        int i3 = 9900;
        increRefCount();
        if (!(mDownloadManagerState != XLManagerStatus.MANAGER_RUNNING || this.mLoader == null || btTaskStatus == null)) {
            i3 = this.mLoader.getBtSubTaskStatus(j, btTaskStatus, i, i2);
        }
        decreRefCount();
        return i3;
    }

    public int getBtSubTaskInfo(long j, int i, BtSubTaskDetail btSubTaskDetail) {
        int i2 = 9900;
        increRefCount();
        if (!(mDownloadManagerState != XLManagerStatus.MANAGER_RUNNING || this.mLoader == null || btSubTaskDetail == null)) {
            i2 = this.mLoader.getBtSubTaskInfo(j, i, btSubTaskDetail);
        }
        decreRefCount();
        return i2;
    }

    public int selectBtSubTask(long j, BtIndexSet btIndexSet) {
        int i = 9900;
        increRefCount();
        if (!(mDownloadManagerState != XLManagerStatus.MANAGER_RUNNING || this.mLoader == null || btIndexSet == null)) {
            i = this.mLoader.selectBtSubTask(j, btIndexSet);
        }
        decreRefCount();
        return i;
    }

    public int deselectBtSubTask(long j, BtIndexSet btIndexSet) {
        int i = 9900;
        increRefCount();
        if (!(mDownloadManagerState != XLManagerStatus.MANAGER_RUNNING || this.mLoader == null || btIndexSet == null)) {
            i = this.mLoader.deselectBtSubTask(j, btIndexSet);
        }
        decreRefCount();
        return i;
    }

    public int btAddServerResource(long j, int i, ServerResourceParam serverResourceParam) {
        if (serverResourceParam == null) {
            XLLog.e(TAG, "btAddServerResource serverResPara is null, task=[" + j + ":" + i + "]");
            return 9112;
        }
        XLLog.d(TAG, "btAddServerResource beg, task=[" + j + ":" + i + "] mUrl=[" + serverResourceParam.mUrl + "] mRefUrl=[" + serverResourceParam.mRefUrl + "] mCookie=[" + serverResourceParam.mCookie + "] mResType=[" + serverResourceParam.mResType + "] mStrategy=[" + serverResourceParam.mStrategy + "]");
        if (serverResourceParam.checkMemberVar()) {
            try {
                increRefCount();
                if (this.mLoader == null) {
                    XLLog.e(TAG, "btAddServerResource mLoader is null, task=[" + j + ":" + i + "]");
                    return 9900;
                } else if (XLManagerStatus.MANAGER_RUNNING != mDownloadManagerState) {
                    XLLog.e(TAG, "btAddServerResource mDownloadManagerState is invaild, task=[" + j + ":" + i + "] mDownloadManagerState=[" + mDownloadManagerState + "]");
                    decreRefCount();
                    return 9900;
                } else {
                    int btAddServerResource = mLoader.btAddServerResource(j, i, serverResourceParam.mUrl, serverResourceParam.mRefUrl, serverResourceParam.mCookie, serverResourceParam.mResType, serverResourceParam.mStrategy);
                    if (9000 != btAddServerResource) {
                        XLLog.e(TAG, "btAddServerResource btAddServerResource failed, task=[" + j + ":" + i + "] errno=[" + btAddServerResource + "]");
                        decreRefCount();
                        return btAddServerResource;
                    }
                    String str = TAG;
                    String str__ = "btAddServerResource end success, task=[" + j + ":" + i + "]";
                    XLLog.d(str, str__);
                    decreRefCount();
                    return 9000;
                }
            } finally {
                decreRefCount();
            }
        } else {
            XLLog.e(TAG, "btAddServerResource checkMemberVar failed, task=[" + j + ":" + i + "] mUrl=[" + serverResourceParam.mUrl + "] mRefUrl=[" + serverResourceParam.mRefUrl + "] mCookie=[" + serverResourceParam.mCookie + "]");
            return 9112;
        }
    }

    public int btAddPeerResource(long j, int i, PeerResourceParam peerResourceParam) {
        if (peerResourceParam == null) {
            XLLog.e(TAG, "btAddPeerResource peerResPara is null, task=[" + j + ":" + i + "]");
            return 9112;
        }
        XLLog.d(TAG, "btAddPeerResource beg, task=[" + j + ":" + i + "] mPeerId=[" + peerResourceParam.mPeerId + "] mUserId=[" + peerResourceParam.mUserId + "] mJmpKey=[" + peerResourceParam.mJmpKey + "] mVipCdnAuth=[" + peerResourceParam.mVipCdnAuth + "] mInternalIp=[" + peerResourceParam.mInternalIp + "] mTcpPort=[" + peerResourceParam.mTcpPort + "] mUdpPort=[" + peerResourceParam.mUdpPort + "] mResLevel=[" + peerResourceParam.mResLevel + "] mResPriority=[" + peerResourceParam.mResPriority + "] mCapabilityFlag=[" + peerResourceParam.mCapabilityFlag + "] mResType=[" + peerResourceParam.mResType + "]");
        if (peerResourceParam.checkMemberVar()) {
            try {
                increRefCount();
                if (this.mLoader == null) {
                    XLLog.e(TAG, "btAddPeerResource mLoader is null, task=[" + j + ":" + i + "]");
                    return 9900;
                } else if (XLManagerStatus.MANAGER_RUNNING != mDownloadManagerState) {
                    XLLog.e(TAG, "btAddPeerResource mDownloadManagerState is invaild, task=[" + j + ":" + i + "] mDownloadManagerState=[" + mDownloadManagerState + "]");
                    decreRefCount();
                    return 9900;
                } else {
                    int btAddPeerResource = this.mLoader.btAddPeerResource(j, i, peerResourceParam.mPeerId, peerResourceParam.mUserId, peerResourceParam.mJmpKey, peerResourceParam.mVipCdnAuth, peerResourceParam.mInternalIp, peerResourceParam.mTcpPort, peerResourceParam.mUdpPort, peerResourceParam.mResLevel, peerResourceParam.mResPriority, peerResourceParam.mCapabilityFlag, peerResourceParam.mResType);
                    if (9000 != btAddPeerResource) {
                        XLLog.e(TAG, "btAddPeerResource btAddPeerResource failed, task=[" + j + ":" + i + "] errno=[" + btAddPeerResource + "]");
                        decreRefCount();
                        return btAddPeerResource;
                    }
                    XLLog.d(TAG, "btAddPeerResource end success, task=[" + j + ":" + i + "]");
                    decreRefCount();
                    return 9000;
                }
            } finally {
                decreRefCount();
            }
        } else {
            XLLog.e(TAG, "btAddPeerResource peerResPara checkMemberVar failed, task=[" + j + ":" + i + "]");
            return 9112;
        }
    }

    public int btRemoveAddedResource(long j, int i, int i2) {
        int i3 = 9900;
        increRefCount();
        if (mDownloadManagerState == XLManagerStatus.MANAGER_RUNNING && this.mLoader != null) {
            i3 = this.mLoader.btRemoveAddedResource(j, i, i2);
        }
        decreRefCount();
        return i3;
    }

    private void loadErrcodeString(Context context) {
        if (context == null) {
            XLLog.e(TAG, "loadErrcodeString, context invalid");
        } else {
            mErrcodeStringMap = XLUtil.parseJSONString("{  \"9000\": \"XL_NO_ERRNO                 \"  ,  \"9101\": \"XL_ALREADY_INIT             \"  ,  \"9102\": \"XL_SDK_NOT_INIT             \"  ,  \"9103\": \"XL_TASK_ALREADY_EXIST       \"  ,  \"9104\": \"XL_TASK_NOT_EXIST           \"  ,  \"9105\": \"XL_TASK_ALREADY_STOPPED     \"  ,  \"9106\": \"XL_TASK_ALREADY_RUNNING     \"  ,  \"9107\": \"XL_TASK_NOT_START           \"  ,  \"9108\": \"XL_TASK_STILL_RUNNING       \"  ,  \"9109\": \"XL_FILE_EXISTED             \"  ,  \"9110\": \"XL_DISK_FULL                \"  ,  \"9111\": \"XL_TOO_MUCH_TASK            \"  ,  \"9112\": \"XL_PARAM_ERROR              \"  ,  \"9113\": \"XL_SCHEMA_NOT_SUPPORT       \"  ,  \"9114\": \"XL_DYNAMIC_PARAM_FAIL       \"  ,  \"9115\": \"XL_CONTINUE_NO_NAME         \"  ,  \"9116\": \"XL_APPNAME_APPKEY_ERROR     \"  ,  \"9117\": \"XL_CREATE_THREAD_ERROR      \"  ,  \"9118\": \"XL_TASK_FINISH              \"  ,  \"9119\": \"XL_TASK_NOT_RUNNING         \"  ,  \"9120\": \"XL_TASK_NOT_IDLE            \"  ,  \"9121\": \"XL_TASK_TYPE_NOT_SUPPORT    \"  ,  \"9122\": \"XL_ADD_RESOURCE_ERROR       \"  ,  \"9123\": \"XL_TASK_LOADING_CFG         \"  ,  \"9301\": \"XL_NO_ENOUGH_BUFFER         \"  ,  \"9302\": \"XL_TORRENT_PARSE_ERROR      \"  ,  \"9303\": \"XL_INDEX_NOT_READY          \"  ,  \"9304\": \"XL_TORRENT_IMCOMPLETE       \"  ,  \"9900\": \"DOWNLOAD_MANAGER_ERROR      \"  ,  \"9901\": \"APPKEY_CHECKER_ERROR        \"  ,  \"111024\": \"COMMON_ERRCODE_BASE                \"  ,  \"111025\": \"TARGET_THREAD_STOPING              \"  ,  \"111026\": \"OUT_OF_MEMORY                      \"  ,  \"111031\": \"TASK_USE_TOO_MUCH_MEM              \"  ,  \"111032\": \"OUT_OF_FIXED_MEMORY                \"  ,  \"111033\": \"QUEUE_NO_ROOM                      \"  ,  \"111035\": \"MAP_UNINIT                         \"  ,  \"111036\": \"MAP_DUPLICATE_KEY                  \"  ,  \"111037\": \"MAP_KEY_NOT_FOUND                  \"  ,  \"111038\": \"INVALID_ITERATOR                   \"  ,  \"111039\": \"BUFFER_OVERFLOW                    \"  ,  \"111041\": \"INVALID_ARGUMENT                   \"  ,  \"111048\": \"INVALID_SOCKET_DESCRIPTOR          \"  ,  \"111050\": \"ERROR_INVALID_INADDR               \"  ,  \"111181\": \"REDIRECT_TOO_MUCH                  \"  ,  \"111057\": \"NOT_IMPLEMENT                      \"  ,  \"111074\": \"INVALID_TIMER_INDEX                \"  ,  \"111078\": \"DNS_INVALID_ADDR                   \"  ,  \"111083\": \"BAD_DIR_PATH                       \"  ,  \"111084\": \"FILE_CANNOT_TRUNCATE               \"  ,  \"111085\": \"INSUFFICIENT_DISK_SPACE            \"  ,  \"111086\": \"FILE_TOO_BIG                       \"  ,  \"111118\": \"DISPATCHER_ERRCODE_BASE            \"  ,  \"111119\": \"DATA_MGR_ERRCODE_BASE              \"  ,  \"111120\": \"ALLOC_INVALID_SIZE                 \"  ,  \"111121\": \"DATA_BUFFER_IS_FULL                \"  ,  \"111122\": \"BLOCK_NO_INVALID                   \"  ,  \"111123\": \"CHECK_DATA_BUFFER_NOT_ENOUG        \"  ,  \"111124\": \"BCID_CHECK_FAIL                    \"  ,  \"111125\": \"BCID_ONCE_CHECT_TOO_MUCH           \"  ,  \"111126\": \"READ_FILE_ERR                      \"  ,  \"111127\": \"WRITE_FILE_ERR                     \"  ,  \"111128\": \"OPEN_FILE_ERR                      \"  ,  \"111129\": \"FILE_PATH_TOO_LONG                 \"  ,  \"111130\": \"SD_INVALID_FILE_SIZE               \"  ,  \"111131\": \"FILE_CFG_MAGIC_ERROR               \"  ,  \"111132\": \"FILE_CFG_READ_ERROR                \"  ,  \"111133\": \"FILE_CFG_WRITE_ERROR               \"  ,  \"111134\": \"FILE_CFG_READ_HEADER_ERROR         \"  ,  \"111135\": \"FILE_CFG_RESOLVE_ERROR             \"  ,  \"111136\": \"TASK_FAILURE_NO_DATA_PIPE          \"  ,  \"111137\": \"NO_FILE_NAME                       \"  ,  \"111138\": \"CANNOT_GET_FILE_NAME               \"  ,  \"111139\": \"CREATE_FILE_FAIL                   \"  ,  \"111140\": \"OPEN_OLD_FILE_FAIL                 \"  ,  \"111141\": \"FILE_SIZE_NOT_BELIEVE              \"  ,  \"111142\": \"FILE_SIZE_TOO_SMALL                \"  ,  \"111143\": \"FILE_NOT_EXIST                     \"  ,  \"111144\": \"FILE_INVALID_PARA                  \"  ,  \"111145\": \"FILE_CREATING                      \"  ,  \"111146\": \"FIL_INFO_INVALID_DATA              \"  ,  \"111147\": \"FIL_INFO_RECVED_DATA               \"  ,  \"111159\": \"CONF_MGR_ERRCODE_BASE              \"  ,  \"111160\": \"SETTINGS_ERR_UNKNOWN               \"  ,  \"111161\": \"SETTINGS_ERR_INVALID_FILE_NAME     \"  ,  \"111162\": \"SETTINGS_ERR_CFG_FILE_NOT_EXIST    \"  ,  \"111163\": \"SETTINGS_ERR_INVALID_LINE          \"  ,  \"111164\": \"SETTINGS_ERR_INVALID_ITEM_NAME     \"  ,  \"111165\": \"SETTINGS_ERR_INVALID_ITEM_VALUE    \"  ,  \"111166\": \"SETTINGS_ERR_LIST_EMPTY            \"  ,  \"111167\": \"SETTINGS_ERR_ITEM_NOT_FOUND        \"  ,  \"111168\": \"NET_REACTOR_ERRCODE_BASE           \"  ,  \"111169\": \"NET_CONNECT_SSL_ERR                \"  ,  \"111170\": \"NET_BROKEN_PIPE                    \"  ,  \"111171\": \"NET_CONNECTION_REFUSED             \"  ,  \"111172\": \"NET_SSL_GET_FD_ERROR               \"  ,  \"111173\": \"NET_OP_CANCEL                      \"  ,  \"111174\": \"NET_UNKNOWN_ERROR                  \"  ,  \"111175\": \"NET_NORMAL_CLOSE                   \"  ,  \"111176\": \"TASK_FAIL_LONG_TIME_NO_RECV_DATA   \"  ,  \"111177\": \"TASK_FILE_SIZE_TOO_LARGE           \"  ,  \"111178\": \"TASK_RETRY_ALWAY_FAIL              \"  ,  \"111300\": \"ASYN_FILE_E_BASE                   \"  ,  \"111301\": \"ASYN_FILE_E_OP_NONE                \"  ,  \"111302\": \"ASYN_FILE_E_OP_BUSY                \"  ,  \"111303\": \"ASYN_FILE_E_FILE_NOT_OPEN          \"  ,  \"111304\": \"ASYN_FILE_E_FILE_REOPEN            \"  ,  \"111305\": \"ASYN_FILE_E_EMPTY_FILE             \"  ,  \"111306\": \"ASYN_FILE_E_FILE_SIZE_LESS         \"  ,  \"111307\": \"ASYN_FILE_E_TOO_MUCH_DATA          \"  ,  \"111308\": \"ASYN_FILE_E_FILE_CLOSING           \"  ,  \"112400\": \"ERR_PTL_PROTOCOL_NOT_SUPPORT       \"  ,  \"112500\": \"ERR_PTL_PEER_OFFLINE               \"  ,  \"112600\": \"ERR_PTL_GET_PEERSN_FAILED          \"  ,  \"11300\": \"P2P_PIPE_ERRCODE_BASE\t\t\t    \"  ,  \"11301\": \"ERR_P2P_VERSION_NOT_SUPPORT\t\t    \"  ,  \"11302\": \"ERR_P2P_WAITING_CLOSE\t\t\t    \"  ,  \"11303\": \"ERR_P2P_HANDSHAKE_RESP_FAIL\t\t    \"  ,  \"11304\": \"ERR_P2P_REQUEST_RESP_FAIL\t\t    \"  ,  \"11305\": \"ERR_P2P_UPLOAD_OVER_MAX\t\t\t    \"  ,  \"11306\": \"ERR_P2P_REMOTE_UNKNOWN_MY_CMD\t    \"  ,  \"11307\": \"ERR_P2P_NOT_SUPPORT_UDT\t\t\t    \"  ,  \"11308\": \"ERR_P2P_BROKER_CONNECT\t\t\t    \"  ,  \"11309\": \"ERR_P2P_INVALID_COMMAND\t\t\t    \"  ,  \"11310\": \"ERR_P2P_INVALID_PARAM\t\t\t    \"  ,  \"11311\": \"ERR_P2P_CONNECT_FAILED\t\t\t    \"  ,  \"11312\": \"ERR_P2P_CONNECT_UPLOAD_SLOW\t        \"  ,  \"11313\": \"ERR_P2P_ALLOC_MEM_ERR               \"  ,  \"11314\": \"ERR_P2P_SEND_HANDSHAKE              \"  ,  \"114001\": \"TASK_FAILURE_QUERY_EMULE_HUB_FAILED\"  ,  \"114101\": \"TASK_FAILURE_EMULE_NO_RECORD       \"  ,  \"114002\": \"TASK_FAILURE_SUBTASK_FAILED        \"  ,  \"114003\": \"TASK_FAILURE_CANNOT_START_SUBTASK  \"  ,  \"114004\": \"TASK_FAILURE_QUERY_BT_HUB_FAILED   \"  ,  \"114005\": \"TASK_FAILURE_PARSE_TORRENT_FAILED  \"  ,  \"114006\": \"TASK_FAILURE_GET_TORRENT_FAILED    \"  ,  \"114007\": \"TASK_FAILURE_SAVE_TORRENT_FAILED   \"  ,  \"115000\": \"RES_QUERY_E_BASE                   \"  ,  \"115100\": \"HTTP_HUB_CLIENT_E_BASE             \"  ,  \"116000\": \"IP6_ERRCODE_BASE                   \"  ,  \"116001\": \"ERR_INVALID_ADDRESS_FAMILY         \"  ,  \"116002\": \"IP6_INVALID_IN6ADDR                \"  ,  \"116003\": \"IP6_NOT_SUPPORT_SSL                \"  ,  \"117000\": \"PAUSE_TASK_WRITE_CFG_ERR           \"  ,  \"117001\": \"PAUSE_TASK_WRITE_DATA_TIMEOUT      \"   }");
        }
    }

    public String getErrorCodeMsg(int i) {
        String str = null;
        String num = Integer.toString(i);
        if (!(mErrcodeStringMap == null || num == null)) {
            Object obj = mErrcodeStringMap.get(num);
            if (obj != null) {
                str = obj.toString().trim();
            }
            XLLog.i(TAG, "errcode:" + i + ", errcodeMsg:" + str);
        }
        return str;
    }

    public int getUrlQuickInfo(long j, UrlQuickInfo urlQuickInfo) {
        int i = 9900;
        increRefCount();
        if (!(mDownloadManagerState != XLManagerStatus.MANAGER_RUNNING || this.mLoader == null || urlQuickInfo == null)) {
            i = this.mLoader.getUrlQuickInfo(j, urlQuickInfo);
        }
        decreRefCount();
        return i;
    }

    public int createCIDTask(CIDTaskParam cIDTaskParam, GetTaskId getTaskId) {
        int i = 9900;
        if (!(cIDTaskParam == null || getTaskId == null || !cIDTaskParam.checkMemberVar())) {
            increRefCount();
            if (mDownloadManagerState == XLManagerStatus.MANAGER_RUNNING && this.mLoader != null) {
                i = this.mLoader.createCIDTask(cIDTaskParam.mCid, cIDTaskParam.mGcid, cIDTaskParam.mBcid, cIDTaskParam.mFilePath, cIDTaskParam.mFileName, cIDTaskParam.mFileSize, cIDTaskParam.mCreateMode, cIDTaskParam.mSeqId, getTaskId);
            }
            decreRefCount();
        }
        return i;
    }

    public String parserThunderUrl(String str) {
        int i = 9900;
        ThunderUrlInfo thunderUrlInfo = new ThunderUrlInfo();
        if (!(this.mLoader == null || str == null)) {
            i = this.mLoader.parserThunderUrl(str, thunderUrlInfo);
        }
        if (9000 == i) {
            return thunderUrlInfo.mUrl;
        }
        return null;
    }

    public int getFileNameFromUrl(String str, GetFileName getFileName) {
        if (this.mLoader == null || str == null || getFileName == null) {
            return 9900;
        }
        return this.mLoader.getFileNameFromUrl(str, getFileName);
    }

    public int getNameFromUrl(String str, String str2) {
        if (this.mLoader == null || str == null || str2 == null) {
            return 9900;
        }
        return this.mLoader.getNameFromUrl(str, str2);
    }

    public int setSpeedLimit(long j, long j2) {
        XLLog.d(TAG, "debug: XLDownloadManager::setSpeedLimit beg, maxDownloadSpeed=[" + j + "] maxUploadSpeed=[" + j2 + "]");
        if (this.mLoader == null) {
            XLLog.e(TAG, "error: XLDownloadManager::setSpeedLimit mLoader is null, maxDownloadSpeed=[" + j + "] maxUploadSpeed=[" + j2 + "] ret=[9900]");
            return 9900;
        }
        int speedLimit = this.mLoader.setSpeedLimit(j, j2);
        XLLog.d(TAG, "debug: XLDownloadManager::setSpeedLimit end, maxDownloadSpeed=[" + j + "] maxUploadSpeed=[" + j2 + "] ret=[" + speedLimit + "]");
        return speedLimit;
    }

    public int setBtPriorSubTask(long j, int i) {
        XLLog.d(TAG, "XLDownloadManager::setBtPriorSubTask beg, taskId=[" + j + "] fileIndex=[" + i + "]");
        if (this.mLoader == null) {
            XLLog.e(TAG, "XLDownloadManager::setBtPriorSubTask mLoader is null, taskId=[" + j + "] fileIndex=[" + i + "]");
            return 9900;
        }
        int btPriorSubTask = this.mLoader.setBtPriorSubTask(j, i);
        if (9000 != btPriorSubTask) {
            XLLog.e(TAG, "XLDownloadManager::setBtPriorSubTask end, taskId=[" + j + "] fileIndex=[" + i + "] ret=[" + btPriorSubTask + "]");
            return btPriorSubTask;
        }
        XLLog.d(TAG, " XLDownloadManager::setBtPriorSubTask end, taskId=[" + j + "] fileIndex=[" + i + "]");
        return 9000;
    }

    public int getMaxDownloadSpeed(MaxDownloadSpeedParam maxDownloadSpeedParam) {
        if (this.mLoader == null) {
            XLLog.e(TAG, "XLDownloadManager::getMaxDownloadSpeed mLoader is null");
            return 9900;
        }
        int maxDownloadSpeed = this.mLoader.getMaxDownloadSpeed(maxDownloadSpeedParam);
        if (9000 != maxDownloadSpeed) {
            XLLog.e(TAG, "XLDownloadManager::getMaxDownloadSpeed end, ret=[" + maxDownloadSpeed + "]");
            return maxDownloadSpeed;
        }
        XLLog.d(TAG, "XLDownloadManager::getMaxDownloadSpeed end, speed=[" + maxDownloadSpeedParam.mSpeed + "] ret=[" + maxDownloadSpeed + "]");
        return maxDownloadSpeed;
    }

    public int statExternalInfo(long j, int i, String str, String str2) {
        XLLog.d(TAG, "XLDownloadManager::statExternalInfo beg, taskId=[" + j + "] fileIndex=[" + i + "] key=[" + str + "] value=[" + str2 + "]");
        if (this.mLoader == null) {
            XLLog.e(TAG, "XLDownloadManager::statExternalInfo mLoader is null, taskId=[" + j + "] fileIndex=[" + i + "]");
            return 9900;
        }
        int statExternalInfo = this.mLoader.statExternalInfo(j, i, str, str2);
        if (9000 != statExternalInfo) {
            XLLog.e(TAG, "XLDownloadManager::statExternalInfo end, taskId=[" + j + "] fileIndex=[" + i + "] ret=[" + statExternalInfo + "]");
            return statExternalInfo;
        }
        XLLog.d(TAG, "XLDownloadManager::statExternalInfo end, taskId=[" + j + "] fileIndex=[" + i + "] ret=[" + statExternalInfo + "]");
        return statExternalInfo;
    }

    public int statExternalInfo(long j, int i, String str, int i2) {
        return statExternalInfo(j, i, str, String.valueOf(i2));
    }

    public int clearTaskFile(String str) {
        XLLog.d(TAG, "XLDownloadManager::clearTaskFile filePath=[" + str + "]");
        if (TextUtils.isEmpty(str)) {
            return 9900;
        }
        if (this.mLoader == null) {
            XLLog.e(TAG, "XLDownloadManager::clearTaskFile mLoader is null");
            return 9900;
        }
        int clearTaskFile = this.mLoader.clearTaskFile(str);
        if (9000 == clearTaskFile) {
            return 9000;
        }
        XLLog.e(TAG, "XLDownloadManager::clearTaskFile end, ret=[" + clearTaskFile + "]");
        return clearTaskFile;
    }

    public int startDcdn(long j, int i, String str, String str2, String str3) {
        int i2 = 9900;
        increRefCount();
        if (mDownloadManagerState == XLManagerStatus.MANAGER_RUNNING && this.mLoader != null) {
            i2 = this.mLoader.startDcdn(j, i, str, str2, str3);
        }
        decreRefCount();
        XLLog.d(TAG, String.format("XLDownloadManager::startDcdn ret=[%d] taskId=[%d] subIndex=[%d] sessionId=[%s] productType=[%s] verifyInfo=[%s]", new Object[]{Integer.valueOf(i2), Long.valueOf(j), Integer.valueOf(i), str, str2, str3}));
        return i2;
    }

    public int stopDcdn(long j, int i) {
        int i2 = 9900;
        increRefCount();
        if (mDownloadManagerState == XLManagerStatus.MANAGER_RUNNING && this.mLoader != null) {
            i2 = this.mLoader.stopDcdn(j, i);
        }
        decreRefCount();
        XLLog.d(TAG, String.format("XLDownloadManager::stopDcdn ret=[%d] taskId=[%d] subIndex=[%d]", new Object[]{Integer.valueOf(i2), Long.valueOf(j), Integer.valueOf(i)}));
        return i2;
    }

    public int createShortVideoTask(String str, String str2, String str3, String str4, int i, int i2, int i3, GetTaskId getTaskId) {
        XLLog.d(TAG, "XLDownloadManager::createShortVideoTask beg, url=[" + str + "] path=[" + str2 + "] filename=[" + str3 + "] title=[" + str4 + "]");
        if (this.mLoader == null) {
            XLLog.e(TAG, "XLDownloadManager::createShortVideoTask mLoader is null");
            return 9900;
        }
        String str5;
        if (str4 == null) {
            str5 = "default Title";
        } else {
            str5 = str4;
        }
        int createShortVideoTask = this.mLoader.createShortVideoTask(str, str2, str3, str5, i, i2, i3, getTaskId);
        if (9000 != createShortVideoTask) {
            XLLog.e(TAG, "XLDownloadManager::createShortVideoTask end, ret=[" + createShortVideoTask + "]");
            return createShortVideoTask;
        }
        XLLog.d(TAG, "XLDownloadManager::createShortVideoTask end, taskId=[" + getTaskId.getTaskId() + "] ret=[" + createShortVideoTask + "]");
        return createShortVideoTask;
    }

    public int playShortVideoBegin(long j) {
        XLLog.d(TAG, "XLDownloadManager::playShortVideoBegin beg, taskId=[" + j + "]");
        if (this.mLoader == null) {
            XLLog.e(TAG, "XLDownloadManager::playShortVideoBegin mLoader is null");
            return 9900;
        }
        int playShortVideoBegin = this.mLoader.playShortVideoBegin(j);
        if (9000 != playShortVideoBegin) {
            XLLog.e(TAG, "XLDownloadManager::playShortVideoBegin end, ret=[" + playShortVideoBegin + "]");
            return playShortVideoBegin;
        }
        XLLog.d(TAG, "XLDownloadManager::playShortVideoBegin end, taskId=[" + j + "] ret=[" + playShortVideoBegin + "]");
        return playShortVideoBegin;
    }

    public int getSessionInfoByUrl(String str, XLSessionInfo xLSessionInfo) {
        if (this.mLoader == null) {
            XLLog.e(TAG, "XLDownloadManager::getSessionInfoByUrl mLoader is null");
            return 9900;
        }
        int sessionInfoByUrl = this.mLoader.getSessionInfoByUrl(str, xLSessionInfo);
        if (9000 == sessionInfoByUrl) {
            return sessionInfoByUrl;
        }
        XLLog.e(TAG, "XLDownloadManager::getSessionInfoByUrl end, ret=[" + sessionInfoByUrl + "]");
        return sessionInfoByUrl;
    }



    class NetworkChangeReceiver extends BroadcastReceiver {
        private static final String TAG = "TAG_DownloadReceiver";
        final /* synthetic */ XLDownloadManager this$0;

        public NetworkChangeReceiver(XLDownloadManager xLDownloadManager) {
            this.this$0 = xLDownloadManager;
        }

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action != null && action.equals("android.net.conn.CONNECTIVITY_CHANGE")) {
                new Thread(new NetworkChangeHandlerThread(this.this$0, context, mLoader, mAllowExecution)).start();
            }
        }
    }
    class NetworkChangeHandlerThread implements Runnable {
        private boolean m_allow_execution = true;
        private Context m_context = null;
        private XLLoader m_loader = null;
        final /* synthetic */ XLDownloadManager this$0;

        public NetworkChangeHandlerThread(XLDownloadManager xLDownloadManager, Context context, XLLoader xLLoader, boolean z) {
            this.this$0 = xLDownloadManager;
            this.m_context = context;
            this.m_loader = xLLoader;
            this.m_allow_execution = z;
        }

        public void run() {
            if (this.m_allow_execution) {
                int networkTypeComplete = XLUtil.getNetworkTypeComplete(this.m_context);
                XLLog.d("XLDownloadManager", "NetworkChangeHandlerThread nettype=" + networkTypeComplete);
                this.this$0.notifyNetWorkType(networkTypeComplete, this.m_loader);
                String bssid = XLUtil.getBSSID(this.m_context);
                XLLog.d("XLDownloadManager", "NetworkChangeHandlerThread bssid=" + bssid);
                this.this$0.notifyWifiBSSID(bssid, this.m_loader);
                XLUtil.NetWorkCarrier netWorkCarrier = XLUtil.getNetWorkCarrier(this.m_context);
                XLLog.d("XLDownloadManager", "NetworkChangeHandlerThread NetWorkCarrier=" + netWorkCarrier);
                this.this$0.notifyNetWorkCarrier(netWorkCarrier.ordinal());
            }
        }
    }
    class MyTimerTask extends TimerTask {
        final /* synthetic */ XLDownloadManager this$0;

        MyTimerTask(XLDownloadManager xLDownloadManager) {
            this.this$0 = xLDownloadManager;
        }

        public void run() {
            if (mQueryGuidCount < 5) {
//                XLDownloadManager.access$208(this.this$0);
                GuidInfo guidInfo = new GuidInfo();
                guidInfo = XLUtil.generateGuid(mContext);
                if (guidInfo.mType == GUID_TYPE.ALL) {
//                    XLDownloadManager.access$400(this.this$0);
                }
                if (guidInfo.mType != GUID_TYPE.DEFAULT) {
                    setLocalProperty("Guid", guidInfo.mGuid);
                    return;
                }
                return;
            }
//            XLDownloadManager.access$400(this.this$0);
        }
    }
}