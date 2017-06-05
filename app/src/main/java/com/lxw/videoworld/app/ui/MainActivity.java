package com.lxw.videoworld.app.ui;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.lxw.videoworld.R;
import com.lxw.videoworld.framework.base.BaseActivity;
import com.lxw.videoworld.framework.widget.DownloadDialog;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {
    @BindView(R.id.text)
    TextView text;
    int progress;

    private boolean flag_exit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                LoadingDialog dialog = new LoadingDialog(MainActivity.this);
//                dialog.show();
                final DownloadDialog dialog = new DownloadDialog(MainActivity.this);
                dialog.show();
                new Thread() {
                    public void run() {
                        while (true) {
                            progress ++;
                            dialog.updateProgress(progress);
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }.start();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            exitByDoubleClick();
        }
        return false;
    }

    /**
     * 双击退出程序
     */
    private void exitByDoubleClick() {
        Timer tExit = null;
        if(!flag_exit){
            flag_exit = true;
            Toast.makeText(MainActivity.this,"再按一次退出程序",Toast.LENGTH_SHORT).show();
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    flag_exit=false;//取消退出
                }
            },2000);// 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务
        }else{
            finish();
            System.exit(0);
        }
    }
}
