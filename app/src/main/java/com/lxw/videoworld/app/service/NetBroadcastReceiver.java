package com.lxw.videoworld.app.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import com.lxw.videoworld.app.config.Constant;
import com.lxw.videoworld.framework.util.NetUtil;
import com.lxw.videoworld.framework.util.SharePreferencesUtil;
import com.lxw.videoworld.framework.util.ToastUtil;

/**
 *
 * Created by Zion on 2017/10/5.
 */

public class NetBroadcastReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        // 如果相等的话就说明网络状态发生了变化
        if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            int netWorkState = NetUtil.getNetWorkState(context);
            // 接口回调传过去状态的类型
            switch (netWorkState){
                case NetUtil.NETWORK_NONE:
                    break;
                case NetUtil.NETWORK_MOBILE:
                    ToastUtil.showMessage("当前网络为移动网络，请注意您的流量");
                    boolean isAllow4G = SharePreferencesUtil.getBooleanSharePreferences(context, Constant.KEY_IS_ALLOW_4G, false);
                    if(!isAllow4G && DownloadManager.xLTaskInfos != null && DownloadManager.xLTaskInfos.size() > 0){
                        for (int i = 0; i < DownloadManager.xLTaskInfos.size(); i++){
                            if(DownloadManager.xLTaskInfos.get(i).mTaskStatus == 1)
                                DownloadManager.stopTask(DownloadManager.xLTaskInfos.get(i).mTaskId);
                        }
                    }
                    break;
                case NetUtil.NETWORK_WIFI:
                    break;
                default:
                    break;
            }
        }
    }
}
