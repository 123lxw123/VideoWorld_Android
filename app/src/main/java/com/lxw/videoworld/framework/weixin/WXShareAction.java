package com.lxw.videoworld.framework.weixin;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import com.lxw.dailynews.R;
import com.lxw.dailynews.framework.application.BaseApplication;
import com.lxw.dailynews.framework.image.ImageManager;
import com.lxw.dailynews.framework.util.StringUtil;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.mm.sdk.openapi.WXImageObject;
import com.tencent.mm.sdk.openapi.WXMediaMessage;
import com.tencent.mm.sdk.openapi.WXTextObject;
import com.tencent.mm.sdk.openapi.WXWebpageObject;
import com.tencent.mm.sdk.platformtools.Util;

import java.io.File;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class WXShareAction {
    public static final int SHARETEXT = 0001;// 纯文本分享
    public static final int SHAREIMG = 0002;// 图片分享
    public static final int SHAREIMGURL = 0003;// 图文带链接的分享
    // IWXAPI 是第三方app和微信通信的openapi接口
    private IWXAPI api;
    // 已通过审核APP_ID
    public final String APP_ID = "wx16b17af3bfbcaf4d";// 正式签名

    private Context context;
    private final int THUMB_SIZE = 150;

    public WXShareAction(Context context) {
        this.context = context;
        // 通过WXAPIFactory工厂，获取IWXAPI的实例
        api = WXAPIFactory.createWXAPI(context, APP_ID, false);
        // 将该app注册到微信
        api.registerApp(APP_ID);
    }

    public void respFromWX(Activity activity) {
        api.handleIntent(activity.getIntent(), (IWXAPIEventHandler) activity);
    }

    public boolean isWXAppInstalled() {
        return api.isWXAppInstalled();
    }

    public boolean isWXAppSupportAPI() {
        return api.isWXAppSupportAPI();
    }

    public boolean isSupportShareWXFriendster() {
        return true;
    }

    public void sendToSession(final WXShareBaseBean bean) {
        if (!checkWx()) {
            return;
        }
        switch (bean.shareType) {
            case SHAREIMG:
                Observable.create(new Observable.OnSubscribe<Bitmap>() {
                    @Override
                    public void call(Subscriber<? super Bitmap> subscriber) {
                        Bitmap bmp = ImageManager.getInstance().downloadImage(BaseApplication.appContext, ((WXShareImgBean) bean).imageUrl);
                        subscriber.onNext(bmp);
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(new Action1<Bitmap>() {

                            @Override
                            public void call(Bitmap bmp) {
                                weixinShareImg(bean, bmp);
                            }
                        });
                break;
            case SHARETEXT:
                weixinShareText(bean);
                break;
            case SHAREIMGURL:
                Observable.create(new Observable.OnSubscribe<Bitmap>() {
                    @Override
                    public void call(Subscriber<? super Bitmap> subscriber) {
                        Bitmap bmp = ImageManager.getInstance().downloadImage(BaseApplication.appContext, ((WXShareImgUrlBean) bean).imageUrl);
                        subscriber.onNext(bmp);
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(new Action1<Bitmap>() {

                            @Override
                            public void call(Bitmap bmp) {
                                weixinShareImgUrl(bean, bmp);
                            }
                        });

                break;
            default:
                break;
        }
    }

    private boolean checkWx() {
        boolean flag = false;
        if (isWXAppInstalled()) {
            if (isWXAppSupportAPI()) {
                flag = true;
            } else {
                flag = false;
                Toast.makeText(context, "您的微信客户端版本较低,无法分享给好友哦！",
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            flag = false;
            Toast.makeText(context, "您尚未安装微信客户端，无法分享给好友哦！", Toast.LENGTH_SHORT)
                    .show();
        }
        return flag;
    }

    private void weixinShareText(WXShareBaseBean bean) {
        WXShareTextBean txtBean = (WXShareTextBean) bean;
        // 初始化一个WXTextObject对象
        WXTextObject textObj = new WXTextObject();
        textObj.text = txtBean.content;
        // 用WXTextObject对象初始化一个WXMediaMessage对象
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = textObj;
        // 发送文本类型的消息时，title字段不起作用
        // msg.title = "Will be ignored";
        msg.description = txtBean.content;

        // 构造一个Req
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("text"); // transaction字段用于唯一标识一个请求
        req.message = msg;
        req.scene = txtBean.isFriends ? SendMessageToWX.Req.WXSceneTimeline
                : SendMessageToWX.Req.WXSceneSession;
        // 调用api接口发送数据到微信
        api.sendReq(req);
    }

    private void weixinShareImg(WXShareBaseBean bean, Bitmap bmp) {
        WXShareImgBean ibean = (WXShareImgBean) bean;
        WXImageObject imgObj = new WXImageObject();
        imgObj.setImagePath(((WXShareImgBean) bean).imageUrl);
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = imgObj;
        if (bmp == null) {
            Toast.makeText(context, "分享的图片不存在", Toast.LENGTH_SHORT).show();
            // 图片不存在
            return;
        }
        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE,
                THUMB_SIZE, true);
        msg.thumbData = Util.bmpToByteArray(thumbBmp, true);
        int imageSize = msg.thumbData.length / 1024;
        if (imageSize > 32) {
            Toast.makeText(context, "您分享的图片过大", Toast.LENGTH_SHORT).show();
            return;
        }
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("img");
        req.message = msg;
        req.scene = ibean.isFriends ? SendMessageToWX.Req.WXSceneTimeline
                : SendMessageToWX.Req.WXSceneSession;
        api.sendReq(req);
    }

    private void weixinShareImgUrl(WXShareBaseBean bean, Bitmap bmp) {
        WXShareImgUrlBean ibean = (WXShareImgUrlBean) bean;
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = ibean.url;
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = ibean.title;
        msg.description = ibean.description;
        if(bmp == null){
            bmp = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_share_logo);
        }
        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE,
                THUMB_SIZE, true);
        msg.thumbData = Util.bmpToByteArray(thumbBmp, true); // 设置缩略图
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = msg;
        req.scene = ibean.isFriends ? SendMessageToWX.Req.WXSceneTimeline
                : SendMessageToWX.Req.WXSceneSession;
        api.sendReq(req);
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis())
                : type + System.currentTimeMillis();
    }
}
