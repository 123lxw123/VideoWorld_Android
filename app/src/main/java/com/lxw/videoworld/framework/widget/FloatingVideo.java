package com.lxw.videoworld.framework.widget;


import android.content.Context;
import android.media.AudioManager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.lxw.videoworld.R;
import com.lxw.videoworld.app.config.Constant;
import com.lxw.videoworld.app.model.SourceHistoryModel;
import com.lxw.videoworld.app.util.RealmUtil;
import com.shuyu.gsyvideoplayer.utils.Debuger;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

import java.util.Timer;
import java.util.TimerTask;

import static com.shuyu.gsyvideoplayer.utils.CommonUtil.hideNavKey;

/**
 * 多窗体下的悬浮窗页面支持Video
 */

public class FloatingVideo extends StandardGSYVideoPlayer {

    protected DismissControlViewTimerTask mDismissControlViewTimerTask;
    private Timer timer;

    /**
     * 1.5.0开始加入，如果需要不同布局区分功能，需要重载
     */
    public FloatingVideo(Context context, Boolean fullFlag) {
        super(context, fullFlag);
    }

    public FloatingVideo(Context context) {
        super(context);
    }

    public FloatingVideo(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void init(Context context) {
        if (getActivityContext() != null) {
            this.mContext = getActivityContext();
        } else {
            this.mContext = context;
        }

        initInflate(mContext);

        mTextureViewContainer = (ViewGroup) findViewById(R.id.surface_container);
        mStartButton = findViewById(R.id.start);
        mLoadingProgressBar = findViewById(R.id.loading);

        if (isInEditMode())
            return;
        mScreenWidth = getActivityContext().getResources().getDisplayMetrics().widthPixels;
        mScreenHeight = getActivityContext().getResources().getDisplayMetrics().heightPixels;
        mAudioManager = (AudioManager) getActivityContext().getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
        mStartButton = findViewById(com.shuyu.gsyvideoplayer.R.id.start);
        mStartButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                clickStartIcon();
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_floating_video;
    }


    @Override
    protected void startPrepare() {
        if (getGSYVideoManager().listener() != null) {
            getGSYVideoManager().listener().onCompletion();
        }
        getGSYVideoManager().setListener(this);
        getGSYVideoManager().setPlayTag(mPlayTag);
        getGSYVideoManager().setPlayPosition(mPlayPosition);
        mAudioManager.requestAudioFocus(onAudioFocusChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
        //((Activity) getActivityContext()).getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        mBackUpPlayingBufferState = -1;
        getGSYVideoManager().prepare(mUrl, mMapHeadData, mLooping, mSpeed, mCache, mCachePath, null);
        setStateAndUi(CURRENT_STATE_PREPAREING);
    }

    @Override
    public void onAutoCompletion() {
        setStateAndUi(CURRENT_STATE_AUTO_COMPLETE);

        mSaveChangeViewTIme = 0;

        if (mTextureViewContainer.getChildCount() > 0) {
            mTextureViewContainer.removeAllViews();
        }

        if (!mIfCurrentIsFullscreen)
            getGSYVideoManager().setLastListener(null);
        mAudioManager.abandonAudioFocus(onAudioFocusChangeListener);
        //((Activity) getActivityContext()).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        releaseNetWorkState();

        if (mVideoAllCallBack != null && isCurrentMediaListener()) {
            Debuger.printfLog("onAutoComplete");
            mVideoAllCallBack.onAutoComplete(mOriginUrl, mTitle, this);
        }
    }

    @Override
    public void onCompletion() {
        //make me normal first
        setStateAndUi(CURRENT_STATE_NORMAL);

        mSaveChangeViewTIme = 0;

        if (mTextureViewContainer.getChildCount() > 0) {
            mTextureViewContainer.removeAllViews();
        }

        if (!mIfCurrentIsFullscreen) {
            getGSYVideoManager().setListener(null);
            getGSYVideoManager().setLastListener(null);
        }
        getGSYVideoManager().setCurrentVideoHeight(0);
        getGSYVideoManager().setCurrentVideoWidth(0);

        mAudioManager.abandonAudioFocus(onAudioFocusChangeListener);
        //((Activity) getActivityContext()).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        releaseNetWorkState();

    }


    @Override
    protected Context getActivityContext() {
        return getContext();
    }

    @Override
    protected void startDismissControlViewTimer() {
        cancelDismissControlViewTimer();
        mDismissControlViewTimer = new Timer();
        mDismissControlViewTimerTask = new DismissControlViewTimerTask();
        mDismissControlViewTimer.schedule(mDismissControlViewTimerTask, mDismissControlTime);
    }

    @Override
    protected void cancelDismissControlViewTimer() {
        if (mDismissControlViewTimer != null) {
            mDismissControlViewTimer.cancel();
            mDismissControlViewTimer = null;
        }
        if (mDismissControlViewTimerTask != null) {
            mDismissControlViewTimerTask.cancel();
            mDismissControlViewTimerTask = null;
        }

    }

    private class DismissControlViewTimerTask extends TimerTask {
        @Override
        public void run() {
            if (mCurrentState != CURRENT_STATE_NORMAL
                    && mCurrentState != CURRENT_STATE_ERROR
                    && mCurrentState != CURRENT_STATE_AUTO_COMPLETE) {
                if (getActivityContext() != null) {
                    FloatingVideo.this.post(new Runnable() {
                        @Override
                        public void run() {
                            hideAllWidget();
                            setViewShowState(mLockScreen, GONE);
                            if (mHideKey && mIfCurrentIsFullscreen && mShowVKey) {
                                hideNavKey(mContext);
                            }
                        }
                    });
                }
            }
        }
    }

    @Override
    public void onPrepared() {
        super.onPrepared();
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                SourceHistoryModel sourceHistoryModel = RealmUtil.queryHistoryModelByLocalUrl(mOriginUrl);
                if (sourceHistoryModel == null)
                    sourceHistoryModel = RealmUtil.queryHistoryModelByLink(mOriginUrl);
                if (sourceHistoryModel != null && getCurrentPositionWhenPlaying() > 0) {
                    sourceHistoryModel.setSeek(getCurrentPositionWhenPlaying());
                    sourceHistoryModel.setTotal(getDuration());
                    sourceHistoryModel.setStatus(Constant.STATUS_1);
                    if (sourceHistoryModel.getTotal() - sourceHistoryModel.getSeek() <= 3000)
                        sourceHistoryModel.setSeek(sourceHistoryModel.getTotal());
                    RealmUtil.copyOrUpdateModel(sourceHistoryModel);
                }
            }
        }, 3000, 3000);
    }

    public void cancelTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

}
