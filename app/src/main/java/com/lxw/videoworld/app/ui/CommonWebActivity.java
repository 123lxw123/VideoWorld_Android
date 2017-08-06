package com.lxw.videoworld.app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.lxw.videoworld.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CommonWebActivity extends AppCompatActivity {

    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.txt_feedback)
    TextView txtFeedback;
    @BindView(R.id.webview)
    WebView webview;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_web);
        ButterKnife.bind(this);
        url = getIntent().getStringExtra("url");
        initViews();
        if(!TextUtils.isEmpty(url)){
            webview.loadUrl(url);
        }
    }

    private void initViews() {
        txtFeedback.setText("详情");
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonWebActivity.this.finish();
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        url = getIntent().getStringExtra("url");
        if(!TextUtils.isEmpty(url)){
            webview.loadUrl(url);
        }
    }
}
