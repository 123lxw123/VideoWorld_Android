package com.lxw.videoworld.framework.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;


/**
 * 适配了悬浮窗的view
 */

public class FloatPlayerView extends FrameLayout {

    private String url;
    private int seek;
    private FloatingVideo videoPlayer;

    public FloatPlayerView(Context context) {
        super(context);
        init();
    }

    public FloatPlayerView(Context context, String url, int seek) {
        super(context);
        this.url = url;
        this.seek = seek;
        init();
    }

    public FloatPlayerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FloatPlayerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

        videoPlayer = new FloatingVideo(getContext());

        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;

        addView(videoPlayer, layoutParams);

        //是否可以滑动调整
        videoPlayer.setIsTouchWiget(false);

        if (url != null && !url.isEmpty()) {
            videoPlayer.setSeekOnStart(seek);
            videoPlayer.setUp(url, false, "");
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
