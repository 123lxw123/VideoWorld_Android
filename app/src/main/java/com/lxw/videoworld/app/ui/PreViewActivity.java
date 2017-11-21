package com.lxw.videoworld.app.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.github.chrisbanes.photoview.PhotoView;
import com.lxw.videoworld.R;
import com.lxw.videoworld.framework.base.BaseActivity;
import com.lxw.videoworld.framework.image.ImageManager;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PreViewActivity extends BaseActivity {

    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.img_photo)
    PhotoView imgPhoto;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_view);
        ButterKnife.bind(this);
        url = getIntent().getStringExtra("url");
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if (!TextUtils.isEmpty(url)){
            ImageManager.getInstance().loadImage(this, imgPhoto, url);
        }

    }
}
