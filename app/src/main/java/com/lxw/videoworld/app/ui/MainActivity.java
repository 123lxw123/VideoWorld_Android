package com.lxw.videoworld.app.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.lxw.videoworld.R;
import com.lxw.videoworld.app.adapter.QuickFragmentPageAdapter;
import com.lxw.videoworld.app.config.Constant;
import com.lxw.videoworld.framework.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {

    @BindView(R.id.viewpager_main)
    ViewPager viewpagerMain;
    @BindView(R.id.navigationbar_main)
    BottomNavigationBar navigationbarMain;
    @BindView(R.id.toobar_main)
    Toolbar toobarMain;
    private boolean flag_exit = false;
    private QuickFragmentPageAdapter pagerAdapter;
    private List<Fragment> fragments = new ArrayList<>();
    private String[] tabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initViews();
    }

    private void initViews() {

        tabs = new String[]{getString(R.string.txt_tab1), getString(R.string.txt_tab2), getString(R.string.txt_tab3), getString(R.string.txt_tab4), getString(R.string.txt_tab5)};
        toobarMain.inflateMenu(R.menu.toolbar_main);//设置右上角的填充菜单

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
        if (keyCode == KeyEvent.KEYCODE_BACK) {
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
                createSourceTypeFragment(Constant.SOURCE_TYPE_1, Constant.CATEGORY_13, null);
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
}
