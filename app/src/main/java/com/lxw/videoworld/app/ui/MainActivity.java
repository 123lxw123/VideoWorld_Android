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
    private List<String> categorys = new ArrayList<>();
    private List<Fragment> fragments = new ArrayList<>();
    private String[] tabs = new String[]{getString(R.string.txt_tab1), getString(R.string.txt_tab2), getString(R.string.txt_tab3), getString(R.string.txt_tab4), getString(R.string.txt_tab5)};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initViews();
    }

    private void initViews() {
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
                switch (position) {
                    case 0:

                        break;
                }
            }

            @Override
            public void onTabUnselected(int position) {
            }

            @Override
            public void onTabReselected(int position) {
            }
        });

        categorys.clear();
        categorys.add(Constant.TAB_1);
        categorys.add(Constant.TAB_2);
        categorys.add(Constant.TAB_3);
        categorys.add(Constant.TAB_4);
        categorys.add(Constant.TAB_5);
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
        if (categorys != null && categorys.size() > 0) {
            fragments.clear();
            for (int i = 0; i < categorys.size(); i++) {
                SourceCategoryFragment fragment = new SourceCategoryFragment();
                Bundle bundle = new Bundle();
                bundle.putString("category", categorys.get(i));
                fragment.setArguments(bundle);
                fragments.add(fragment);
            }
        }
    }
}
