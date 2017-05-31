package com.lxw.videoworld.app.ui;

import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.lxw.videoworld.R;
import com.lxw.videoworld.framework.widget.DownloadDialog;
import com.lxw.videoworld.framework.widget.LoadingDialog;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.R.attr.width;
import static com.lxw.videoworld.R.attr.progressSleep;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.text)
    TextView text;
    int progress;

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
}
