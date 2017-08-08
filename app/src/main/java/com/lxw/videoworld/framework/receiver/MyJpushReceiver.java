package com.lxw.videoworld.framework.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.lxw.videoworld.app.ui.CommonWebActivity;
import com.lxw.videoworld.app.ui.SourceDetailActivity;
import com.lxw.videoworld.framework.application.BaseApplication;
import com.lxw.videoworld.framework.log.LoggerHelper;

import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class MyJpushReceiver extends BroadcastReceiver {
    private static final String TAG = "JPush";
    private static final String KEY_TAB_NUM = "user_tab";

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();

        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle
                    .getString(JPushInterface.EXTRA_REGISTRATION_ID);
            LoggerHelper.info(TAG, "[MyReceiver] 接收Registration Id : " + regId);
            // send the Registration Id to your server...

        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent
                .getAction())) {
            LoggerHelper.info(
                    TAG,
                    "[MyReceiver] 接收到推送下来的自定义消息: "
                            + bundle.getString(JPushInterface.EXTRA_MESSAGE));
            // 透传逻辑
            // String result = bundle.getString(JPushInterface.EXTRA_MESSAGE);
            // if ("backup".equals(result)) {
            // Intent mIntent = new Intent(InvestListFragment.ACTION_REFRESH);
            // context.sendBroadcast(mIntent);
            // }

            // processCustomMessage(context, bundle);

        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent
                .getAction())) {
            LoggerHelper.info(TAG, "[MyReceiver] 接收到推送下来的通知");
            int notifactionId = bundle
                    .getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);

        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent
                .getAction())) {
            LoggerHelper.info(TAG, "[MyReceiver] 用户点击打开了通知");
            String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
            if(!TextUtils.isEmpty(extras)){
                try{
                    JSONObject jsonObject = new JSONObject(extras);
                    String scheme = jsonObject.getString("url");
                    if(!TextUtils.isEmpty(scheme)){
                        String[] split = scheme.split(":.:");
                        if(split.length > 1){
                            String url = split[0];
                            String type = split[1];
                            if(type.equals("native")){
                                Intent intent1 = new Intent(BaseApplication.appContext, SourceDetailActivity.class);
                                intent1.putExtra("url", url);
                                intent1.putExtra("sourceType", split[2]);
                                intent1.addFlags(FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent1);
                            }else{
                                Intent intent1 = new Intent(BaseApplication.appContext, CommonWebActivity.class);
                                intent1.addFlags(FLAG_ACTIVITY_NEW_TASK);
                                intent1.putExtra("url", url);
                                context.startActivity(intent1);
                            }
                        }
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
            // TODO
//            Log.d(TAG, "[MyReceiver] 用户点击打开了通知=" + tabId + "user_"
//                    + Ifa.userInfo.uid);
//
//            LoggerHelper.serviceJumpToTargetActivity(context, tabId);
        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent
                .getAction())) {
            // Log.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " +
            // bundle.getString(JPushInterface.EXTRA_EXTRA));
            // 在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity，
            // 打开一个网页等..
        } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent
                .getAction())) {
            // boolean connected =
            // intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE,
            // false);
            // Log.w(TAG, "[MyReceiver]" + intent.getAction()
            // +" connected state change to "+connected);
        } else {
            LoggerHelper.info(TAG,
                    "[MyReceiver] Unhandled intent - " + intent.getAction());
        }

    }

    private static String printBundle(Bundle bundle) {
        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
        String faTab = "";
        if (!TextUtils.isEmpty(extras)) {
            try {
                JSONObject extraJson = new JSONObject(extras);
                if (null != extraJson && extraJson.length() > 0) {
                    faTab = extraJson.getString(KEY_TAB_NUM);
                    // msgIntent.putExtra(MainActivity.KEY_EXTRAS, extras);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return faTab;
    }
}