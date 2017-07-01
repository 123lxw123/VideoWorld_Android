package com.lxw.videoworld.app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.IntentCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.lxw.videoworld.R;
import com.lxw.videoworld.app.adapter.QuickFragmentPageAdapter;
import com.lxw.videoworld.app.api.HttpHelper;
import com.lxw.videoworld.app.config.Constant;
import com.lxw.videoworld.app.model.ConfigModel;
import com.lxw.videoworld.framework.base.BaseActivity;
import com.lxw.videoworld.framework.http.BaseResponse;
import com.lxw.videoworld.framework.http.HttpManager;
import com.lxw.videoworld.framework.image.ImageManager;
import com.lxw.videoworld.framework.util.DownloadUtil;
import com.lxw.videoworld.framework.util.ManifestUtil;
import com.lxw.videoworld.framework.util.SharePreferencesUtil;
import com.lxw.videoworld.framework.util.ToastUtil;
import com.lxw.videoworld.framework.widget.CustomDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static com.lxw.videoworld.app.ui.SplashActivity.SPLASH_PICTURE_LINK;

public class MainActivity extends BaseActivity {

    @BindView(R.id.viewpager_main)
    ViewPager viewpagerMain;
    @BindView(R.id.navigationbar_main)
    BottomNavigationBar navigationbarMain;
    @BindView(R.id.toobar_main)
    Toolbar toobarMain;
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
        initViews();
        getConfig();
    }

    private void initViews() {

        tabs = new String[]{getString(R.string.txt_tab1), getString(R.string.txt_tab2), getString(R.string.txt_tab3), getString(R.string.txt_tab4), getString(R.string.txt_tab5)};
        toobarMain.inflateMenu(R.menu.toolbar_main);//设置右上角的填充菜单
        toobarMain.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    // 主题切换
                    case R.id.action_change_theme:
                        switch (Constant.THEME_TYPE){
                            case Constant.THEME_TYPE_1:
                                Constant.THEME_TYPE = Constant.THEME_TYPE_2;
                                SharePreferencesUtil.setStringSharePreferences(MainActivity.this, Constant.KEY_THEME_TYPE, Constant.THEME_TYPE_2);
                                break;
                            case Constant.THEME_TYPE_2:
                                Constant.THEME_TYPE = Constant.THEME_TYPE_3;
                                SharePreferencesUtil.setStringSharePreferences(MainActivity.this, Constant.KEY_THEME_TYPE, Constant.THEME_TYPE_3);
                                break;
                            case Constant.THEME_TYPE_3:
                                Constant.THEME_TYPE = Constant.THEME_TYPE_1;
                                SharePreferencesUtil.setStringSharePreferences(MainActivity.this, Constant.KEY_THEME_TYPE, Constant.THEME_TYPE_1);
                                break;
                        }
                        MainActivity.this.finish();
                        Intent intent = MainActivity.this.getIntent();
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        break;
                    case R.id.action_search:
                        Intent intent1 = new Intent(MainActivity.this, SearchActivity.class);
                        startActivity(intent1);
                        break;

                    case R.id.action_change_source:
                        switch (Constant.SOURCE_TYPE){
                            case Constant.SOURCE_TYPE_1:
                                Constant.SOURCE_TYPE = Constant.SOURCE_TYPE_2;
                                SharePreferencesUtil.setStringSharePreferences(MainActivity.this, Constant.KEY_SOURCE_TYPE, Constant.SOURCE_TYPE_2);
                                break;
                            case Constant.SOURCE_TYPE_2:
                                Constant.SOURCE_TYPE = Constant.SOURCE_TYPE_3;
                                SharePreferencesUtil.setStringSharePreferences(MainActivity.this, Constant.KEY_SOURCE_TYPE, Constant.SOURCE_TYPE_3);
                                break;
                            case Constant.SOURCE_TYPE_3:
                                Constant.SOURCE_TYPE = Constant.SOURCE_TYPE_1;
                                SharePreferencesUtil.setStringSharePreferences(MainActivity.this, Constant.KEY_SOURCE_TYPE, Constant.SOURCE_TYPE_1);
                                break;
                        }
                        ToastUtil.showMessage(getString(R.string.txt_change_source));
                        MainActivity.this.finish();
                        Intent intent2 = MainActivity.this.getIntent();
                        intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent2);
                        overridePendingTransition(0, 0);
                        break;
                }
                return true;
            }
        });

        navigationbarMain.setBackgroundColor(getCustomColor(R.styleable.BaseColor_com_main_A));
        navigationbarMain
                .setMode(BottomNavigationBar.MODE_FIXED);

        navigationbarMain.addItem(new BottomNavigationItem(R.drawable.ic_tab_unselect_1, getString(R.string.txt_tab1)))
                .addItem(new BottomNavigationItem(R.drawable.ic_tab_unselect_2, getString(R.string.txt_tab2)))
                .addItem(new BottomNavigationItem(R.drawable.ic_tab_unselect_3, getString(R.string.txt_tab3)))
                .addItem(new BottomNavigationItem(R.drawable.ic_tab_unselect_4, getString(R.string.txt_tab4)))
                .addItem(new BottomNavigationItem(R.drawable.ic_tab_unselect_5, getString(R.string.txt_tab5)))
                .initialise();

        navigationbarMain.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                viewpagerMain.setCurrentItem(position);
            }

            @Override
            public void onTabUnselected(int position) {
            }

            @Override
            public void onTabReselected(int position) {
            }
        });

        createFragment();
        pagerAdapter = new QuickFragmentPageAdapter(getSupportFragmentManager(), fragments, tabs);
        viewpagerMain.setAdapter(pagerAdapter);
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
            finish();
            System.exit(0);
        }
    }

    public void createFragment() {
        fragments.clear();
        switch (Constant.SOURCE_TYPE){
            case Constant.SOURCE_TYPE_1:
                createSourceCategoryFragment(Constant.TAB_1);
                createSourceTypeFragment(Constant.SOURCE_TYPE_1, Constant.CATEGORY_11, null);
                createSourceCategoryFragment(Constant.TAB_3);
                createSourceTypeFragment(Constant.SOURCE_TYPE_1, Constant.CATEGORY_12, null);
                createSourceCategoryFragment(Constant.TAB_5);
                break;
            case Constant.SOURCE_TYPE_2:
                createSourceTypeFragment(Constant.SOURCE_TYPE_2, Constant.CATEGORY_14, null);
                createSourceTypeFragment(Constant.SOURCE_TYPE_2, Constant.CATEGORY_15, null);
                createSourceCategoryFragment(Constant.TAB_3);
                createSourceTypeFragment(Constant.SOURCE_TYPE_2, Constant.CATEGORY_16, null);
                createSourceCategoryFragment(Constant.TAB_5);
                break;
            case Constant.SOURCE_TYPE_3:
                createSourceCategoryFragment(Constant.TAB_1);
                createSourceCategoryFragment(Constant.TAB_2);
                createSourceCategoryFragment(Constant.TAB_3);
                createSourceTypeFragment(Constant.SOURCE_TYPE_3, Constant.CATEGORY_20, null);
                createSourceCategoryFragment(Constant.TAB_5);
                break;
        }
    }

    public void createSourceCategoryFragment(String tab){
        SourceCategoryFragment fragment = new SourceCategoryFragment();
        Bundle bundle = new Bundle();
        bundle.putString("tab", tab);
        fragment.setArguments(bundle);
        fragments.add(fragment);
    }

    public void createSourceTypeFragment(String sourceType, String category, String type){
        SourceTypeFragment fragment = new SourceTypeFragment();
        Bundle bundle = new Bundle();
        bundle.putString("sourceType", sourceType);
        bundle.putString("category", category);
        bundle.putString("type", type);
        fragment.setArguments(bundle);
        fragments.add(fragment);
    }

    public void getConfig() {
        final String url = SharePreferencesUtil.getStringSharePreferences(this, SplashActivity.SPLASH_PICTURE_LINK, null);
        new HttpManager<ConfigModel>(MainActivity.this, HttpHelper.getInstance().getConfig("1"), false, false) {

            @Override
            public void onSuccess(BaseResponse<ConfigModel> response) {
                if(response.getResult() != null){
                    // 保存热搜关键词
                    if(!TextUtils.isEmpty(response.getResult().getKeyword())){
                        SharePreferencesUtil.setStringSharePreferences(MainActivity.this, Constant.KEY_SEARCH_HOTWORDS, response.getResult().getKeyword());
                    }

                    final String imageUrl = response.getResult().getImage();
                    if (!TextUtils.isEmpty(imageUrl)) {
                        if (!TextUtils.isEmpty(url) && !url.equals(imageUrl)) {
                            return;
                        } else {
                            SharePreferencesUtil.setStringSharePreferences(MainActivity.this, SPLASH_PICTURE_LINK, imageUrl);
                            // 缓存启动页图片
                            Observable.create(new ObservableOnSubscribe<Integer>() {
                                @Override
                                public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                                    ImageManager.getInstance().downloadImage(MainActivity.this, imageUrl, Constant.PATH_SPLASH_PICTURE, Constant.PATH_SPLASH_PICTURE_PNG, true);
                                }
                            }).subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Consumer<Integer>() {

                                        @Override
                                        public void accept(Integer i) {
                                        }
                                    });
                        }
                    }
                    // 更新升级
                    try{
                        final int lacalVersionCode = Integer.valueOf(ManifestUtil.getApkVersionCode(MainActivity.this));
                        final int versionCode = Integer.valueOf(response.getResult().getVersionCode());
                        final int forceVersionCode = Integer.valueOf(response.getResult().getForceVersionCode());
                        final String notice = response.getResult().getNotice();
                        final String link = response.getResult().getLink();
                        // 有更新
                        if(lacalVersionCode < versionCode && !TextUtils.isEmpty(link)){
                            // 强制更新,拦截返回虚拟键
                            if(lacalVersionCode < forceVersionCode){
                                flag_back = false;
                            }
                            CustomDialog customDialog = new CustomDialog(MainActivity.this, getString(R.string.update), notice){
                                @Override
                                public void ok() {
                                    super.ok();
                                    new DownloadUtil(MainActivity.this).downloadAPK(link, getString(R.string.update_file_name));
                                }

                                @Override
                                public void cancel() {
                                    super.cancel();
                                    // 强制更新
                                    if(lacalVersionCode < forceVersionCode){
                                        MainActivity.this.finish();
                                    }
                                }
                            };
                            // 屏蔽返回键
                            hideProgressBar();
                            customDialog.show();
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(BaseResponse<ConfigModel> response) {

            }
        }.doRequest();
    }

}
