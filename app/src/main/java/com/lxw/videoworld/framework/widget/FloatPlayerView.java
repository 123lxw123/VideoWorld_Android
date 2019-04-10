package com.lxw.videoworld.framework.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.ArrayList;


/**
 * 适配了悬浮窗的view
 */

public class FloatPlayerView extends FrameLayout {

    private String url;
    private int seek;
    private ArrayList<String> urlList;

    public FloatPlayerView(Context context) {
        super(context);
        init();
    }

    public FloatPlayerView(Context context, String url, int seek, ArrayList<String> urlList) {
        super(context);
        this.url = url;
        this.seek = seek;
        this.urlList = urlList;
        init();
    }

    CustomListGSYVideoPlayer videoPlayer;

    public FloatPlayerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FloatPlayerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

        videoPlayer = new CustomListGSYVideoPlayer(getContext());


        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.gravity = Gravity.CENTER;

        addView(videoPlayer, layoutParams);

        //设置返回键
        videoPlayer.getBackButton().setVisibility(View.VISIBLE);

        //设置全屏按键功能,这是使用的是选择屏幕，而不是全屏
        videoPlayer.getFullscreenButton().setScaleType(ImageView.ScaleType.FIT_CENTER);

        //是否可以滑动调整
        videoPlayer.setIsTouchWiget(true);
        // 滑动快进的比例，默认1。数值越大，滑动的产生的seek越小
        videoPlayer.setSeekRatio(5);

        //全屏
        videoPlayer.setIfCurrentIsFullscreen(false);

        //是否可以滑动调整
        videoPlayer.setIsTouchWiget(false);

        if (url != null && url.isEmpty()) {
            videoPlayer.setSeekOnStart(seek);
            videoPlayer.setUpUrl(url, urlList);
            videoPlayer.startPlayLogic();
        }
    }


    public void onPause() {
        videoPlayer.getCurrentPlayer().onVideoPause();
    }

    public void onResume() {
        videoPlayer.getCurrentPlayer().onVideoResume();
    }

}
