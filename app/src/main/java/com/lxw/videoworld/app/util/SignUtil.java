package com.lxw.videoworld.app.util;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;

import com.lxw.videoworld.app.config.Constant;
import com.lxw.videoworld.app.model.UserSignAdmireModel;
import com.lxw.videoworld.app.ui.AdmireActivity;
import com.lxw.videoworld.framework.application.BaseApplication;
import com.lxw.videoworld.framework.util.DESUtil;
import com.lxw.videoworld.framework.util.GsonUtil;
import com.lxw.videoworld.framework.util.SharePreferencesUtil;
import com.lxw.videoworld.framework.util.ToastUtil;
import com.lxw.videoworld.framework.widget.CustomDialog;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class SignUtil {

    public static void handleSign(Activity activity, UserSignAdmireModel userSignAdmireModel) {
        if (activity == null || userSignAdmireModel == null) return;
        SharePreferencesUtil.setStringSharePreferences(activity, Constant.KEY_USER_SIGN_ADMIRE, GsonUtil.bean2json(userSignAdmireModel));
        if (userSignAdmireModel.isSign()) {
            showSignAdmire(activity, true);
        }
    }

    public static void showSignAdmire(final Activity activity, boolean isSign) {
        String json = SharePreferencesUtil.getStringSharePreferences(activity, Constant.KEY_USER_SIGN_ADMIRE, "");
        UserSignAdmireModel userSignAdmireModel = GsonUtil.json2Bean(json, UserSignAdmireModel.class);
        if (activity == null || userSignAdmireModel == null) return;
        CustomDialog customDialog;
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        if (isSign) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("已签到 ")
                    .append(userSignAdmireModel.getSignDay())
                    .append(" / 3 天，");
            if (userSignAdmireModel.getSignDay() == 3){
                stringBuilder.append("畅玩天数 + 1，");
            }
            stringBuilder.append("总签到天数为 ")
                    .append(userSignAdmireModel.getSumSignDay())
                    .append(" 天，");
            if (userSignAdmireModel.getSumAdmireDay() > 0) {
                stringBuilder.append("总获赠天数为 ")
                        .append(userSignAdmireModel.getSumAdmireDay())
                        .append(" 天，");
            }
            stringBuilder.append("剩余畅玩天数为 ")
                    .append(userSignAdmireModel.getRestDay())
                    .append(" 天，")
                    .append("畅玩特权将于 ")
                    .append(format.format(userSignAdmireModel.getExpirationDate()))
                    .append(" 到期")
                    .append("\n")
                    .append(Constant.USER_SIGN_ADMIRE_TIPS);

            customDialog = new CustomDialog(activity, "签到成功", stringBuilder.toString(), "去赞赏", "知道了") {
                @Override
                public void ok() {
                    super.ok();
                    Intent intent = new Intent(activity, AdmireActivity.class);
                    activity.startActivity(intent);
                }

                @Override
                public void cancel() {
                    super.cancel();
                }
            };
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("已签到 ")
                    .append(userSignAdmireModel.getSignDay())
                    .append(" / 3 天，")
                    .append("总签到天数为 ")
                    .append(userSignAdmireModel.getSumSignDay())
                    .append(" 天，");
            if (userSignAdmireModel.getSumAdmireDay() > 0) {
                stringBuilder.append("总获赠天数为 ")
                        .append(userSignAdmireModel.getSumAdmireDay())
                        .append(" 天，");
            }
            stringBuilder.append("剩余畅玩天数为 ")
                    .append(userSignAdmireModel.getRestDay())
                    .append(" 天，")
                    .append("畅玩特权将于 ")
                    .append(format.format(userSignAdmireModel.getExpirationDate()))
                    .append(" 到期，过期后资源链接将替换成 *****")
                    .append("\n")
                    .append(Constant.USER_SIGN_ADMIRE_TIPS);

            customDialog = new CustomDialog(activity, "我的账号", stringBuilder.toString(), "去赞赏", "复制账号") {
                @Override
                public void ok() {
                    super.ok();
                    Intent intent = new Intent(activity, AdmireActivity.class);
                    activity.startActivity(intent);
                }

                @Override
                public void cancel() {
                    super.cancel();
                    ClipboardManager clip = (ClipboardManager) activity.getSystemService
                            (Context.CLIPBOARD_SERVICE);
                    clip.setText(DESUtil.encode(BaseApplication.uid));
                    ToastUtil.showMessage("已复制账号");
                }
            };
        }
        customDialog.show();
    }
}
