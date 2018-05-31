package com.lxw.videoworld.framework.base;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.StyleableRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.lxw.videoworld.R;
import com.lxw.videoworld.app.config.Constant;
import com.lxw.videoworld.framework.activitystack.ActivityStack;
import com.lxw.videoworld.framework.log.LoggerHelper;

import butterknife.Unbinder;
import cn.jpush.android.api.JPushInterface;

/**
 * Created by lxw9047 on 2016/10/12.
 */

public abstract class BaseActivity extends AppCompatActivity {

    /** 是否旋转屏幕 **/
    private boolean isAllowScreenRoate = Constant.isAllowScreenRoate;
    /** activity 加载中进度条 **/
    private MaterialDialog progressBar;
    /** activity标签 **/
    private String activityTag = "";

    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //添加activity到activity栈
        ActivityStack.getInstance().addActivity(this);

        //是否旋转屏幕
        if (isAllowScreenRoate) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        // 皮肤
        switch (Constant.THEME_TYPE){
            case Constant.THEME_TYPE_1:
                setTheme(R.style.GoldAppTheme);
                break;
            case Constant.THEME_TYPE_2:
                setTheme(R.style.RedAppTheme);
                break;
            case Constant.THEME_TYPE_3:
                setTheme(R.style.BlueAppTheme);
                break;
        }

        // 修改 状态栏、导航栏的颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //设置状态栏颜色
            window.setStatusBarColor(getCustomColor(R.styleable.BaseColor_com_main_A));
            //设置导航栏颜色
            window.setNavigationBarColor(getCustomColor(R.styleable.BaseColor_com_main_A));
            ViewGroup contentView = ((ViewGroup) findViewById(android.R.id.content));
            View childAt = contentView.getChildAt(0);
            if (childAt != null) {
                childAt.setFitsSystemWindows(true);
            }
//            contentView.setPadding(0, getStatusBarHeight(this), 0, 0);
        }

        progressBar = new MaterialDialog.Builder(this)
                .title("Loading")
                .cancelable(true)
                .progress(true, 70)
                .titleColor(getCustomColor(R.styleable.BaseColor_com_font_A))
                .backgroundColor(getResources().getColor(R.color.color_FFFFFF))
                .widgetColor(getCustomColor(R.styleable.BaseColor_com_assist_A))
                .build();
    }

    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        //activity跳转动画
//        overridePendingTransition(R.anim.in_bottom_to_top,R.anim.translate_do_nothing);
//        overridePendingTransition(R.anim.in_left_to_right, R.anim.out_left_to_right);
    }

    @Override
    public void finish() {
        super.finish();
        //移除activity
        ActivityStack.getInstance().finishActivity(this);
        //activity移除动画
//        overridePendingTransition(R.anim.translate_do_nothing,R.anim.out_top_to_bottom);
//        overridePendingTransition(R.anim.in_right_to_left, R.anim.out_right_to_left);
    }

    //为activity添加一个标签
    public void initActivityTag(String activityTag){
        this.activityTag = activityTag;
        LoggerHelper.info("ActivityTag", activityTag);
    }

    //显示加载中进度条
    public void showProgressBar(){
        if(progressBar == null){
            progressBar = new MaterialDialog.Builder(this)
                    .title("Loading")
                    .cancelable(true)
                    .progress(true, 70)
                    .titleColor(getCustomColor(R.styleable.BaseColor_com_font_A))
                    .backgroundColor(getResources().getColor(R.color.color_FFFFFF))
                    .widgetColor(getCustomColor(R.styleable.BaseColor_com_assist_A))
                    .build();
        }
        if(!progressBar.isShowing()){
            progressBar.show();
        }
    }

    //隐藏加载中进度条
    public void hideProgressBar(){
        if(progressBar != null){
            progressBar.dismiss();
        }
        progressBar = null;
    }

    //toast提示信息
    public void showMessage(String message){
        showMessage(message, Toast.LENGTH_SHORT);
    }
    public void showMessage(String message, int showTime){
        Toast.makeText(BaseActivity.this, message, showTime).show();
    }

    public int getCustomColor(@StyleableRes int index){
        return getCustomColor(index, 0xFFFFFF);
    }

    /**
     * 获取主题某颜色的值
     * @param index 如 R.styleable.BaseColor_com_wm_bg
     * @param defValue 默认颜色值
     * @return
     */
    public int getCustomColor(@StyleableRes int index, int defValue){
        TypedArray a = obtainStyledAttributes(null, R.styleable.BaseColor, 0, 0);
        int color = a.getColor(index, defValue);
        a.recycle();
        return color;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public MaterialDialog getProgressBar() {
        return progressBar;
    }

    public void setProgressBar(MaterialDialog progressBar) {
        this.progressBar = progressBar;
    }
}
