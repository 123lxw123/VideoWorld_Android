package com.lxw.videoworld.app.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lxw.videoworld.R;
import com.lxw.videoworld.app.adapter.QuickFragmentPageAdapter;
import com.lxw.videoworld.app.api.HttpHelper;
import com.lxw.videoworld.app.config.Constant;
import com.lxw.videoworld.app.model.BaseResponse;
import com.lxw.videoworld.app.model.ConfigModel;
import com.lxw.videoworld.app.service.BackgroundIntentService;
import com.lxw.videoworld.app.service.DownloadManager;
import com.lxw.videoworld.app.service.NetBroadcastReceiver;
import com.lxw.videoworld.framework.application.BaseApplication;
import com.lxw.videoworld.framework.base.BaseActivity;
import com.lxw.videoworld.framework.http.HttpManager;
import com.lxw.videoworld.framework.log.LoggerHelper;
import com.lxw.videoworld.framework.util.DownloadUtil;
import com.lxw.videoworld.framework.util.ManifestUtil;
import com.lxw.videoworld.framework.util.SharePreferencesUtil;
import com.lxw.videoworld.framework.util.ToastUtil;
import com.lxw.videoworld.framework.widget.CustomDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import abc.abc.abc.AdManager;
import abc.abc.abc.nm.sp.SpotListener;
import abc.abc.abc.nm.sp.SpotManager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

import static com.lxw.videoworld.app.config.Constant.SOURCE_TYPE_1;
import static com.lxw.videoworld.app.config.Constant.SOURCE_TYPE_2;
import static com.lxw.videoworld.app.config.Constant.SOURCE_TYPE_3;
import static com.lxw.videoworld.app.config.Constant.SOURCE_TYPE_4;
import static com.lxw.videoworld.app.config.Constant.configModel;

public class MainActivity extends BaseActivity {

    @BindView(R.id.viewpager_main)
    ViewPager viewpagerMain;
    @BindView(R.id.navigationbar_main)
    BottomNavigationView navigationbarMain;
    @BindView(R.id.toobar_main)
    Toolbar toobarMain;
//    @BindView(R.id.txt_version)
//    TextView txtVersion;
//    @BindView(R.id.txt_change_theme)
//    TextView txtChangeThemem;
//    @BindView(R.id.txt_github)
//    TextView txtGitHub;
//    @BindView(R.id.txt_QQ)
//    TextView txtQQ;
//    @BindView(R.id.txt_feedback)
//    TextView txtFeedback;
//    @BindView(R.id.txt_about)
//    TextView txtAbout;
//    @BindView(R.id.txt_about_content)
    TextView txtAboutContent;
    @BindView(R.id.txt_notice)
    TextView txtNotice;
    @BindView(R.id.img_close)
    ImageView imgClose;
    @BindView(R.id.ll_notice)
    LinearLayout llNotice;
//    @BindView(R.id.drawerlayout)
//    DrawerLayout drawerlayout;
//    @BindView(R.id.switch_allow_4G)
//    Switch switchAllow4G;
//    @BindView(R.id.ll_change_theme)
//    LinearLayout llChangeTheme;
//    @BindView(R.id.txt_allow_4G)
//    TextView txtAllow4G;
//    @BindView(R.id.ll_version)
//    LinearLayout llVersion;
//    @BindView(R.id.ll_about_content)
//    LinearLayout llAboutContent;
//    @BindView(R.id.ll_QQ)
//    LinearLayout llQQ;
//    @BindView(R.id.txt_download_path)
//    TextView txtDownloadPath;
//    @BindView(R.id.txt_download_path_value)
//    TextView txtDownloadPathValue;
//    @BindView(R.id.txt_cache)
//    TextView txtCache;
//    @BindView(R.id.ll_clear_cache)
//    LinearLayout llClearCache;
//    @BindView(R.id.txt_local_play)
//    TextView txtLocalPlay;
//    @BindView(R.id.img_admire)
//    ImageView imgAdmire;
    private boolean flag_exit = false;
    private boolean flag_back = true;
    private QuickFragmentPageAdapter pagerAdapter;
    private List<Fragment> fragments = new ArrayList<>();
    private String[] tabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setUpAd();
        getConfig();
        createFolder();
        initViews();
        if (Constant.configModel != null) {
            setConfig(Constant.configModel, false);
        } else {
            getConfig(false);
        }
        setJpushAliasAndTags();

    }

    private void setUpAd() {
        if (BaseApplication.isFirstHomePage) BaseApplication.isFirstHomePage = false;
        else {
            AdManager.getInstance(this).init(BaseApplication.appId, BaseApplication.appSecret, true);
            SpotManager.getInstance(this).showSpot(this, new SpotListener() {
                @Override
                public void onShowSuccess() {

                }

                @Override
                public void onShowFailed(int i) {

                }

                @Override
                public void onSpotClosed() {

                }

                @Override
                public void onSpotClicked(boolean b) {

                }
            });
        }
    }

    private void setJpushAliasAndTags() {
        JPushInterface.setAliasAndTags(MainActivity.this.getApplicationContext(), BaseApplication
                .uid, null, mAliasCallback);
    }

    private void initViews() {

        tabs = new String[]{getString(R.string.txt_tab1), getString(R.string.txt_tab2), getString
                (R.string.txt_tab3), getString(R.string.txt_tab4), getString(R.string.txt_tab5)};
        toobarMain.inflateMenu(R.menu.toolbar_main);//设置右上角的填充菜单
        toobarMain.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    // 主题切换
                    case R.id.action_download_manager:
                        Intent intent = new Intent(MainActivity.this, DownloadManagerActivity.class);
                        startActivity(intent);
//                        if (drawerlayout.isDrawerOpen(GravityCompat.START)) {
//                            drawerlayout.closeDrawers();
//                        }
                        break;
                    case R.id.action_search:
                        Intent intent1 = new Intent(MainActivity.this, SearchActivity.class);
                        startActivity(intent1);
                        break;

                    case R.id.action_change_source:
                        int checked = -1;
                        switch (Constant.SOURCE_TYPE){
                            case SOURCE_TYPE_4:
                                checked = 0;
                                break;
                            case SOURCE_TYPE_1:
                                checked = 1;
                                break;
                            case SOURCE_TYPE_2:
                                checked = 2;
                                break;
                            case SOURCE_TYPE_3:
                                checked = 3;
                                break;
                        }
                        final int position = checked;
                        AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                                .setSingleChoiceItems(new String[]{"资源库A (在线播放)", "资源库B (边下边播)", "资源库C (边下边播)", "资源库D (边下边播)"},
                                        position, new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (which == position) {
                                            dialog.dismiss();
                                            return;
                                        }
                                        switch (which) {
                                            case 0:
                                                Constant.SOURCE_TYPE = SOURCE_TYPE_4;
                                                SharePreferencesUtil.setStringSharePreferences(MainActivity.this,
                                                        Constant.KEY_SOURCE_TYPE, SOURCE_TYPE_4);
                                                ToastUtil.showMessage(getString(R.string.txt_change_source_a));
                                                break;
                                            case 1:
                                                Constant.SOURCE_TYPE = Constant.SOURCE_TYPE_1;
                                                SharePreferencesUtil.setStringSharePreferences(MainActivity.this,
                                                        Constant.KEY_SOURCE_TYPE, Constant.SOURCE_TYPE_1);
                                                ToastUtil.showMessage(getString(R.string.txt_change_source_b));
                                                break;
                                            case 2:
                                                Constant.SOURCE_TYPE = SOURCE_TYPE_2;
                                                SharePreferencesUtil.setStringSharePreferences(MainActivity.this,
                                                        Constant.KEY_SOURCE_TYPE, SOURCE_TYPE_2);
                                                ToastUtil.showMessage(getString(R.string.txt_change_source_c));
                                                break;
                                            case 3:
                                                Constant.SOURCE_TYPE = SOURCE_TYPE_3;
                                                SharePreferencesUtil.setStringSharePreferences(MainActivity.this,
                                                        Constant.KEY_SOURCE_TYPE, SOURCE_TYPE_3);
                                                ToastUtil.showMessage(getString(R.string.txt_change_source_d));
                                                break;
                                        }
                                        MainActivity.this.finish();
                                        Intent intent2 = MainActivity.this.getIntent();
                                        intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent
                                                .FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent2);
                                        overridePendingTransition(0, 0);
                                        dialog.dismiss();
                                    }
                                }).create();
                        dialog.show();
                        break;
                }
                return true;
            }
        });

        navigationbarMain.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.bottom_nav_movie:
                        viewpagerMain.setCurrentItem(0);
                        break;
                    case R.id.bottom_nav_tv:
                        viewpagerMain.setCurrentItem(1);
                        break;
                    case R.id.bottom_nav_variety:
                        viewpagerMain.setCurrentItem(2);
                        break;
                    case R.id.bottom_nav_anime:
                        viewpagerMain.setCurrentItem(3);
                        break;
                    case R.id.bottom_nav_game:
                        viewpagerMain.setCurrentItem(4);
                        break;
                }
                return true;
            }
        });

        createFragment();
        pagerAdapter = new QuickFragmentPageAdapter(getSupportFragmentManager(), fragments, tabs);
        viewpagerMain.setAdapter(pagerAdapter);
//        viewpagerMain.post(new Runnable() {
//            @Override
//            public void run() {
//                ((SourceTypeFragment) fragments.get(0)).setUpDrawerLayout();
//            }
//        });
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerlayout, toobarMain,
//                R.string.txt_copy_link, R.string.txt_copy_link);
//        drawerlayout.setDrawerListener(toggle);
//        toggle.syncState();
//
//        boolean isAllow4G = SharePreferencesUtil.getBooleanSharePreferences(MainActivity.this, Constant.KEY_IS_ALLOW_4G, false);
//        switchAllow4G.setChecked(!isAllow4G);
//        switchAllow4G.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                SharePreferencesUtil.setBooleanSharePreferences(MainActivity.this, Constant.KEY_IS_ALLOW_4G, !isChecked);
//            }
//        });
//
//        llChangeTheme.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int checked = -1;
//                switch (THEME_TYPE){
//                    case THEME_TYPE_1:
//                        checked = 0;
//                        break;
//                    case THEME_TYPE_2:
//                        checked = 1;
//                        break;
//                    case THEME_TYPE_3:
//                        checked = 2;
//                        break;
//                }
//                final int position = checked;
//                AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
//                        .setSingleChoiceItems(new String[]{"至尊黑", "魂动红", "魅惑蓝"}, position, new DialogInterface.OnClickListener() {
//
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                if (which == position) {
//                                    dialog.dismiss();
//                                    return;
//                                }
//                                switch (which) {
//                                    case 0:
//                                        THEME_TYPE = Constant.THEME_TYPE_1;
//                                        SharePreferencesUtil.setStringSharePreferences(MainActivity.this,
//                                                Constant.KEY_THEME_TYPE, Constant.THEME_TYPE_1);
//                                        break;
//                                    case 1:
//                                        THEME_TYPE = THEME_TYPE_2;
//                                        SharePreferencesUtil.setStringSharePreferences(MainActivity.this,
//                                                Constant.KEY_THEME_TYPE, THEME_TYPE_2);
//                                        break;
//                                    case 2:
//                                        THEME_TYPE = THEME_TYPE_3;
//                                        SharePreferencesUtil.setStringSharePreferences(MainActivity.this,
//                                                Constant.KEY_THEME_TYPE, THEME_TYPE_3);
//                                        break;
//                                }
//                                MainActivity.this.finish();
//                                Intent intent = MainActivity.this.getIntent();
//                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                startActivity(intent);
//                                overridePendingTransition(0, 0);
//                                dialog.dismiss();
//                            }
//                        }).create();
//                if (drawerlayout.isDrawerOpen(GravityCompat.START)) {
//                    drawerlayout.closeDrawers();
//                }
//                dialog.show();
//
//            }
//        });
//
//        // 复制群号
//        llQQ.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!TextUtils.isEmpty(txtQQ.getText())) {
//                    ClipboardManager clip = (ClipboardManager) MainActivity.this.getSystemService
//                            (Context.CLIPBOARD_SERVICE);
//                    clip.setText(txtQQ.getText());
//                    if (drawerlayout.isDrawerOpen(GravityCompat.START)) {
//                        drawerlayout.closeDrawers();
//                    }
//                    ToastUtil.showMessage("已复制群号");
//                }
//            }
//        });
//
//        llVersion.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getConfig(true);
//                if (drawerlayout.isDrawerOpen(GravityCompat.START)) {
//                    drawerlayout.closeDrawers();
//                }
//            }
//        });
//
//        llClearCache.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (drawerlayout.isDrawerOpen(GravityCompat.START)) {
//                    drawerlayout.closeDrawers();
//                }
//                CustomDialog customDialog = new CustomDialog(MainActivity.this, "清理缓存", "清理缓存将会删除所有下载记录以及下载路径中的所有文件，确定要清理缓存？", "立即清理", "取消") {
//                    @Override
//                    public void ok() {
//                        super.ok();
//                        File file = new File(PATH_OFFLINE_DOWNLOAD);
//                        String cacheSize = ValueUtil.formatFileSize(FileUtil.getFileSize(file));
//                        FileUtil.deleteFile(file);
//                        DownloadManager.removeAllXLTaskInfo();
//                        txtCache.setText(ValueUtil.formatFileSize(FileUtil.getFileSize(file)));
//                        ToastUtil.showMessage("已清理缓存：" + cacheSize);
//                    }
//
//                    @Override
//                    public void cancel() {
//                        super.cancel();
//                    }
//                };
//                customDialog.show();
//            }
//        });
//
//        txtDownloadPathValue.setText("../VideoWorld/download");
//        txtDownloadPath.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (txtDownloadPathValue.getVisibility() == View.GONE)
//                    txtDownloadPathValue.setVisibility(View.VISIBLE);
//                else txtDownloadPathValue.setVisibility(View.GONE);
//            }
//        });
//
//        imgAdmire.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this, PreViewActivity.class);
//                intent.putExtra("isAdmire", true);
//                startActivity(intent);
//            }
//        });

        // 监听网络变化
        NetBroadcastReceiver netBroadcastReceiver = new NetBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(netBroadcastReceiver, filter);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && flag_back) {
            exitByDoubleClick();
        }
        return false;
    }

    /**
     * 双击退出程序
     */
    private void exitByDoubleClick() {
        if (SpotManager.getInstance(this).isSpotShowing()) {
            SpotManager.getInstance(this).hideSpot();
            return;
        }
        Timer tExit = null;
        if (!flag_exit) {
            flag_exit = true;
            Toast.makeText(MainActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    flag_exit = false;//取消退出
                }
            }, 2000);// 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务
        } else {
            DownloadManager.stopAllTask();
            Observable.timer(1000, TimeUnit.MILLISECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Long>() {
                        @Override
                        public void onSubscribe(@NonNull Disposable disposable) {
                        }

                        @Override
                        public void onNext(@NonNull Long number) {
                            SpotManager.getInstance(MainActivity.this).onAppExit();
                            finish();
                            System.exit(0);
                        }

                        @Override
                        public void onError(@NonNull Throwable e) {
                        }

                        @Override
                        public void onComplete() {
                        }
                    });
        }
    }

    public void createFragment() {
        fragments.clear();
        createSourceTypeFragment(Constant.TAB_1);
        createSourceTypeFragment(Constant.TAB_2);
        createSourceTypeFragment(Constant.TAB_3);
        createSourceTypeFragment(Constant.TAB_4);
//        switch (Constant.SOURCE_TYPE) {
//            case Constant.SOURCE_TYPE_1:
//                createSourceTypeFragment(Constant.SOURCE_TYPE_1, Constant.TAB_1, Constant.TYPE_0);
//                createSourceTypeFragment(Constant.SOURCE_TYPE_1, Constant.CATEGORY_11, null);
//                createSourceTypeFragment(Constant.SOURCE_TYPE_3, Constant.CATEGORY_19, null);
//                createSourceTypeFragment(Constant.SOURCE_TYPE_1, Constant.CATEGORY_12, null);
//                break;
//            case SOURCE_TYPE_2:
//                createSourceTypeFragment(SOURCE_TYPE_2, Constant.CATEGORY_14, null);
//                createSourceTypeFragment(SOURCE_TYPE_2, Constant.CATEGORY_15, null);
//                createSourceTypeFragment(SOURCE_TYPE_3, Constant.CATEGORY_19, null);
//                createSourceTypeFragment(SOURCE_TYPE_2, Constant.CATEGORY_16, null);
//                break;
//            case SOURCE_TYPE_3:
//                createSourceTypeFragment(SOURCE_TYPE_3, Constant.CATEGORY_17, null);
//                createSourceTypeFragment(SOURCE_TYPE_3, Constant.CATEGORY_18, null);
//                createSourceTypeFragment(SOURCE_TYPE_3, Constant.CATEGORY_19, null);
//                createSourceTypeFragment(SOURCE_TYPE_3, Constant.CATEGORY_20, null);
//                break;
//            case SOURCE_TYPE_4:
//                createSourceTypeFragment(SOURCE_TYPE_4, Constant.CATEGORY_14, null);
//                createSourceTypeFragment(SOURCE_TYPE_4, Constant.CATEGORY_15, null);
//                createSourceTypeFragment(SOURCE_TYPE_4, Constant.CATEGORY_23, null);
//                createSourceTypeFragment(SOURCE_TYPE_4, Constant.CATEGORY_16, null);
//                break;
//        }
    }

    public void createSourceTypeFragment(String tab) {
        SourceTypeFragment fragment = new SourceTypeFragment();
        Bundle bundle = new Bundle();
        bundle.putString("tab", tab);
        fragment.setArguments(bundle);
        fragments.add(fragment);
    }

    public void getConfig(final boolean flag_dialog) {
        new HttpManager<ConfigModel>(MainActivity.this, HttpHelper.getInstance().getConfig("1"),
                flag_dialog, false) {

            @Override
            public void onSuccess(BaseResponse<ConfigModel> response) {
                if (response.getResult() != null) {
                    configModel = response.getResult();
                    setConfig(configModel, flag_dialog);
                }
            }

            @Override
            public void onFailure(BaseResponse<ConfigModel> response) {

            }
        }.doRequest();
    }

    public void setConfig(ConfigModel configModel, boolean flag_dialog) {

//        // 侧滑菜单
//        String versionName = ManifestUtil.getApkVersionName(MainActivity.this);
//        txtVersion.setText(versionName);
//
//        if (!TextUtils.isEmpty(configModel.getQQ1())) {
//            txtQQ.setText(configModel.getQQ1());
//        }
//        if (!TextUtils.isEmpty(configModel.getIntro())) {
//            txtAboutContent.setText(configModel.getIntro());
//        }
//
        // 公告
        if (!TextUtils.isEmpty(configModel.getNotice())) {
            String localNotice = SharePreferencesUtil.getStringSharePreferences(MainActivity
                    .this, Constant.KEY_NOTICE, "");
            if (TextUtils.isEmpty(localNotice) || !localNotice.equals(configModel.getNotice())) {
                SharePreferencesUtil.setStringSharePreferences(MainActivity.this, Constant
                        .KEY_NOTICE, configModel.getNotice());
                txtNotice.setText(configModel.getNotice());
                txtNotice.setSelected(true);
                llNotice.setVisibility(View.VISIBLE);
            }
        }
        // 更新升级
        try {
            final int lacalVersionCode = Integer.valueOf(ManifestUtil.getApkVersionCode
                    (MainActivity.this));
            final int versionCode = Integer.valueOf(configModel.getVersionCode());
            final int forceVersionCode = Integer.valueOf(configModel.getForceVersionCode());
            final String message = configModel.getMessage();
            final String link = configModel.getLink();
            // 有更新
            if (lacalVersionCode < versionCode && !TextUtils.isEmpty(link)) {
                // 强制更新,拦截返回虚拟键
                if (lacalVersionCode < forceVersionCode) {
                    flag_back = false;
                }
                CustomDialog customDialog = new CustomDialog(MainActivity.this, getString(R
                        .string.update), message) {
                    @Override
                    public void ok() {
                        super.ok();
                        flag_back = true;
                        new DownloadUtil(MainActivity.this).downloadAPK(link, getString(R.string
                                .update_file_name));
                    }

                    @Override
                    public void cancel() {
                        super.cancel();
                        // 强制更新
                        if (lacalVersionCode < forceVersionCode) {
                            MainActivity.this.finish();
                        }
                    }
                };
                // 屏蔽返回键
                hideProgressBar();
                customDialog.show();
            } else if (flag_dialog) {
                // 提示已经是最新版本
                ToastUtil.showMessage(MainActivity.this, getString(R.string.no_update_tips));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
//        File file = new File(PATH_OFFLINE_DOWNLOAD);
//        txtCache.setText(ValueUtil.formatFileSize(FileUtil.getFileSize(file)));
    }

//    @OnClick({R.id.txt_github, R.id.txt_feedback, R.id.txt_about, R.id.txt_local_play})
//    public void setTextViewOnClick(TextView tv) {
//        switch (tv.getId()) {
//            case R.id.txt_github:
//                Intent intent2 = new Intent(MainActivity.this, CommonWebActivity.class);
//                intent2.putExtra("url", "https://github.com/123lxw123");
//                startActivity(intent2);
//                if (drawerlayout.isDrawerOpen(GravityCompat.START)) {
//                    drawerlayout.closeDrawers();
//                }
//                break;
//            case R.id.txt_feedback:
//                // 反馈
//                if (drawerlayout.isDrawerOpen(GravityCompat.START)) {
//                    drawerlayout.closeDrawers();
//                }
//                Intent intent3 = new Intent(MainActivity.this, FeedbackActivity.class);
//                startActivity(intent3);
//                break;
//            case R.id.txt_about:
//                // 关于
//                if (llAboutContent.getVisibility() == View.GONE) {
//                    llAboutContent.setVisibility(View.VISIBLE);
//                } else {
//                    llAboutContent.setVisibility(View.GONE);
//                }
//                break;
//            case R.id.txt_local_play:
//                // 本地播放
//                Intent intent = new Intent();
//                intent.setType("video/*"); //选择视频 （mp4 3gp 是android支持的视频格式）
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(intent, 1);
//                break;
//        }
//
//    }


    @Override
    protected void onPause() {
        super.onPause();
        SpotManager.getInstance(this).onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        SpotManager.getInstance(this).onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SpotManager.getInstance(this).onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            //
            if (resultCode == RESULT_OK) {
                Uri uri = data.getData();
                Cursor cursor = getContentResolver().query(uri, null, null,
                        null, null);
                cursor.moveToFirst();
                String path = cursor.getString(1);
                String url = DownloadManager.getLoclUrl(path);
                Intent intent = new Intent(MainActivity.this, PlayVideoActivity.class);
                intent.putExtra("url", url);
                MainActivity.this.startActivity(intent);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public Toolbar getToobarMain() {
        return toobarMain;
    }

    @OnClick({R.id.img_close})
    public void setImageViewOnClick(ImageView imageView) {
        switch (imageView.getId()) {
            case R.id.img_close:
                llNotice.setVisibility(View.GONE);
                break;
        }
    }

    private final TagAliasCallback mAliasCallback = new TagAliasCallback() {
        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs;
            String TAG = "Jpush";
            switch (code) {
                case 0:
                    logs = "Set tag and alias success";
                    LoggerHelper.debug(TAG, logs);
                    break;
                case 6002:
                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                    LoggerHelper.debug(TAG, logs);
                    break;
                default:
                    logs = "Failed with errorCode = " + code;
                    LoggerHelper.debug(TAG, logs);
                    break;
            }
        }
    };

    private void createFolder() {
        File folder1 = new File(Constant.PATH_SPLASH_PICTURE);
        File folder2 = new File(Constant.PATH_OFFLINE_DOWNLOAD);
        if(!folder1.exists())
        { //如果该文件夹不存在，则进行创建
            folder1.mkdirs();//创建文件夹
        }
        if(!folder2.exists())
        { //如果该文件夹不存在，则进行创建
            folder2.mkdirs();//创建文件夹
        }
    }

    private void getConfig() {
        Intent startIntent = new Intent(MainActivity.this, BackgroundIntentService.class);
        startService(startIntent);
    }
}
