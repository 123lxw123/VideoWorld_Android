package com.lxw.videoworld.app.ui;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.lxw.videoworld.R;
import com.lxw.videoworld.app.api.HttpHelper;
import com.lxw.videoworld.app.config.Constant;
import com.lxw.videoworld.app.model.BaseResponse;
import com.lxw.videoworld.app.model.ConfigModel;
import com.lxw.videoworld.app.service.DownloadManager;
import com.lxw.videoworld.framework.http.HttpManager;
import com.lxw.videoworld.framework.util.DownloadUtil;
import com.lxw.videoworld.framework.util.FileUtil;
import com.lxw.videoworld.framework.util.ManifestUtil;
import com.lxw.videoworld.framework.util.SharePreferencesUtil;
import com.lxw.videoworld.framework.util.ToastUtil;
import com.lxw.videoworld.framework.util.ValueUtil;
import com.lxw.videoworld.framework.widget.CustomDialog;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.lxw.videoworld.app.config.Constant.PATH_OFFLINE_DOWNLOAD;
import static com.lxw.videoworld.app.config.Constant.THEME_TYPE;
import static com.lxw.videoworld.app.config.Constant.THEME_TYPE_1;
import static com.lxw.videoworld.app.config.Constant.THEME_TYPE_2;
import static com.lxw.videoworld.app.config.Constant.THEME_TYPE_3;
import static com.lxw.videoworld.app.config.Constant.configModel;

public class SettingsFragment extends Fragment {

    private View rootView;
    @BindView(R.id.txt_version)
    TextView txtVersion;
    @BindView(R.id.txt_change_theme)
    TextView txtChangeThemem;
    @BindView(R.id.txt_github)
    TextView txtGitHub;
    @BindView(R.id.txt_QQ)
    TextView txtQQ;
    @BindView(R.id.txt_feedback)
    TextView txtFeedback;
    @BindView(R.id.txt_about)
    TextView txtAbout;
    @BindView(R.id.txt_about_content)
    TextView txtAboutContent;
    @BindView(R.id.switch_allow_4G)
    Switch switchAllow4G;
    @BindView(R.id.ll_change_theme)
    LinearLayout llChangeTheme;
    @BindView(R.id.txt_allow_4G)
    TextView txtAllow4G;
    @BindView(R.id.ll_version)
    LinearLayout llVersion;
    @BindView(R.id.ll_about_content)
    LinearLayout llAboutContent;
    @BindView(R.id.ll_QQ)
    LinearLayout llQQ;
    @BindView(R.id.txt_download_path)
    TextView txtDownloadPath;
    @BindView(R.id.txt_download_path_value)
    TextView txtDownloadPathValue;
    @BindView(R.id.txt_cache)
    TextView txtCache;
    @BindView(R.id.ll_clear_cache)
    LinearLayout llClearCache;
    @BindView(R.id.txt_local_play)
    TextView txtLocalPlay;
    @BindView(R.id.img_admire)
    ImageView imgAdmire;
    private boolean flag_back = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_settings, container, false);
            ButterKnife.bind(this, rootView);
            boolean isAllow4G = SharePreferencesUtil.getBooleanSharePreferences(SettingsFragment.this.getContext(), Constant.KEY_IS_ALLOW_4G, false);
            switchAllow4G.setChecked(!isAllow4G);
            switchAllow4G.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    SharePreferencesUtil.setBooleanSharePreferences(SettingsFragment.this.getContext(), Constant.KEY_IS_ALLOW_4G, !isChecked);
                }
            });

            llChangeTheme.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int checked = -1;
                    switch (THEME_TYPE) {
                        case THEME_TYPE_1:
                            checked = 0;
                            break;
                        case THEME_TYPE_2:
                            checked = 1;
                            break;
                        case THEME_TYPE_3:
                            checked = 2;
                            break;
                    }
                    final int position = checked;
                    AlertDialog dialog = new AlertDialog.Builder(SettingsFragment.this.getContext())
                            .setSingleChoiceItems(new String[]{"至尊黑", "魂动红", "魅惑蓝"}, position, new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (which == position) {
                                        dialog.dismiss();
                                        return;
                                    }
                                    switch (which) {
                                        case 0:
                                            THEME_TYPE = THEME_TYPE_1;
                                            SharePreferencesUtil.setStringSharePreferences(SettingsFragment.this.getContext(),
                                                    Constant.KEY_THEME_TYPE, THEME_TYPE_1);
                                            break;
                                        case 1:
                                            THEME_TYPE = THEME_TYPE_2;
                                            SharePreferencesUtil.setStringSharePreferences(SettingsFragment.this.getContext(),
                                                    Constant.KEY_THEME_TYPE, THEME_TYPE_2);
                                            break;
                                        case 2:
                                            THEME_TYPE = THEME_TYPE_3;
                                            SharePreferencesUtil.setStringSharePreferences(SettingsFragment.this.getContext(),
                                                    Constant.KEY_THEME_TYPE, THEME_TYPE_3);
                                            break;
                                    }
                                    SettingsFragment.this.getActivity().finish();
                                    Intent intent = SettingsFragment.this.getActivity().getIntent();
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    getActivity().overridePendingTransition(0, 0);
                                    dialog.dismiss();
                                }
                            }).create();
                    dialog.show();

                }
            });

            // 复制群号
            llQQ.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!TextUtils.isEmpty(txtQQ.getText())) {
                        ClipboardManager clip = (ClipboardManager) SettingsFragment.this.getContext().getSystemService
                                (Context.CLIPBOARD_SERVICE);
                        clip.setText(txtQQ.getText());
                        ToastUtil.showMessage("已复制群号");
                    }
                }
            });

            llVersion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getConfig(true);
                }
            });

            llClearCache.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CustomDialog customDialog = new CustomDialog(SettingsFragment.this.getActivity(), "清理缓存", "清理缓存将会删除所有下载记录以及下载路径中的所有文件，确定要清理缓存？", "立即清理", "取消") {
                        @Override
                        public void ok() {
                            super.ok();
                            File file = new File(PATH_OFFLINE_DOWNLOAD);
                            String cacheSize = ValueUtil.formatFileSize(FileUtil.getFileSize(file));
                            FileUtil.deleteFile(file);
                            DownloadManager.removeAllXLTaskInfo();
                            txtCache.setText(ValueUtil.formatFileSize(FileUtil.getFileSize(file)));
                            ToastUtil.showMessage("已清理缓存：" + cacheSize);
                        }

                        @Override
                        public void cancel() {
                            super.cancel();
                        }
                    };
                    customDialog.show();
                }
            });

            txtDownloadPathValue.setText("../VideoWorld/download");
            txtDownloadPath.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (txtDownloadPathValue.getVisibility() == View.GONE)
                        txtDownloadPathValue.setVisibility(View.VISIBLE);
                    else txtDownloadPathValue.setVisibility(View.GONE);
                }
            });

            imgAdmire.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(SettingsFragment.this.getContext(), PreViewActivity.class);
                    intent.putExtra("isAdmire", true);
                    startActivity(intent);
                }
            });
        }
        if (rootView != null) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }
        }
        getConfig(false);
        return rootView;
    }

    public void getConfig(final boolean flag_dialog) {
        new HttpManager<ConfigModel>(SettingsFragment.this.getContext(), HttpHelper.getInstance().getConfig("1"),
                flag_dialog, false) {

            @Override
            public void onSuccess(BaseResponse<ConfigModel> response) {
                if (response.getResult() != null) {
                    configModel = response.getResult();
                    setConfig(configModel, flag_dialog);
                }
            }

            @Override
            public void onFailure(BaseResponse<ConfigModel> response) {

            }
        }.doRequest();
    }

    public void setConfig(ConfigModel configModel, boolean flag_dialog) {

        String versionName = ManifestUtil.getApkVersionName(SettingsFragment.this.getContext());
        txtVersion.setText(versionName);

        if (!TextUtils.isEmpty(configModel.getQQ1())) {
            txtQQ.setText(configModel.getQQ1());
        }
        if (!TextUtils.isEmpty(configModel.getIntro())) {
            txtAboutContent.setText(configModel.getIntro());
        }

        // 更新升级
        try {
            final int lacalVersionCode = Integer.valueOf(ManifestUtil.getApkVersionCode
                    (SettingsFragment.this.getContext()));
            final int versionCode = Integer.valueOf(configModel.getVersionCode());
            final int forceVersionCode = Integer.valueOf(configModel.getForceVersionCode());
            final String message = configModel.getMessage();
            final String link = configModel.getLink();
            // 有更新
            if (lacalVersionCode < versionCode && !TextUtils.isEmpty(link)) {
                // 强制更新,拦截返回虚拟键
                if (lacalVersionCode < forceVersionCode) {
                    flag_back = false;
                }
                CustomDialog customDialog = new CustomDialog(SettingsFragment.this.getActivity(), getString(R
                        .string.update), message) {
                    @Override
                    public void ok() {
                        super.ok();
                        flag_back = true;
                        new DownloadUtil(SettingsFragment.this.getContext()).downloadAPK(link, getString(R.string
                                .update_file_name));
                    }

                    @Override
                    public void cancel() {
                        super.cancel();
                        // 强制更新
                        if (lacalVersionCode < forceVersionCode) {
                            SettingsFragment.this.getActivity().finish();
                        }
                    }
                };
                // 屏蔽返回键
                ((MainActivity) getActivity()).hideProgressBar();
                customDialog.show();
            } else if (flag_dialog) {
                // 提示已经是最新版本
                ToastUtil.showMessage(SettingsFragment.this.getActivity(), getString(R.string.no_update_tips));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @OnClick({R.id.txt_github, R.id.txt_feedback, R.id.txt_about, R.id.txt_local_play})
    public void setTextViewOnClick(TextView tv) {
        switch (tv.getId()) {
            case R.id.txt_github:
                Intent intent2 = new Intent(getContext(), CommonWebActivity.class);
                intent2.putExtra("url", "https://github.com/123lxw123");
                startActivity(intent2);
                break;
            case R.id.txt_feedback:
                // 反馈
                Intent intent3 = new Intent(getContext(), FeedbackActivity.class);
                startActivity(intent3);
                break;
            case R.id.txt_about:
                // 关于
                if (llAboutContent.getVisibility() == View.GONE) {
                    llAboutContent.setVisibility(View.VISIBLE);
                } else {
                    llAboutContent.setVisibility(View.GONE);
                }
                break;
            case R.id.txt_local_play:
                // 本地播放
                Intent intent = new Intent();
                intent.setType("video/*"); //选择视频 （mp4 3gp 是android支持的视频格式）
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 1);
                break;
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        File file = new File(PATH_OFFLINE_DOWNLOAD);
        txtCache.setText(ValueUtil.formatFileSize(FileUtil.getFileSize(file)));
    }

}
