package com.lxw.videoworld.app.ui;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lxw.videoworld.R;
import com.lxw.videoworld.app.api.HttpHelper;
import com.lxw.videoworld.app.config.Constant;
import com.lxw.videoworld.app.model.BaseResponse;
import com.lxw.videoworld.app.model.ConfigModel;
import com.lxw.videoworld.app.model.KeyValueModel;
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
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.app.Activity.RESULT_OK;
import static com.lxw.videoworld.app.config.Constant.PATH_OFFLINE_DOWNLOAD;
import static com.lxw.videoworld.app.config.Constant.THEME_TYPE;
import static com.lxw.videoworld.app.config.Constant.THEME_TYPE_1;
import static com.lxw.videoworld.app.config.Constant.THEME_TYPE_2;
import static com.lxw.videoworld.app.config.Constant.THEME_TYPE_3;
import static com.lxw.videoworld.app.config.Constant.configModel;

public class SettingsFragment extends Fragment {

    @BindView(R.id.recyclerview_settings)
    RecyclerView recyclerviewSettings;
    private BaseQuickAdapter<KeyValueModel, BaseViewHolder> adapter;
    private List<KeyValueModel> settings = new ArrayList<>();
    Unbinder unbinder;
    private View rootView;
    private boolean flag_back = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_settings, container, false);
            ButterKnife.bind(this, rootView);
            settings.clear();
            settings.add(new KeyValueModel("下载中心", ""));
            settings.add(new KeyValueModel("收藏记录", ""));
            settings.add(new KeyValueModel("播放记录", ""));
            settings.add(new KeyValueModel("主题切换", ""));
            settings.add(new KeyValueModel("本地播放", ""));
            settings.add(new KeyValueModel("缓存清理", ValueUtil.formatFileSize(FileUtil.getFileSize(new File(PATH_OFFLINE_DOWNLOAD)))));
            settings.add(new KeyValueModel("版本更新", "V " + ManifestUtil.getApkVersionName(SettingsFragment.this.getContext())));
            settings.add(new KeyValueModel("和谐Q群", "126257036"));
            settings.add(new KeyValueModel("移动网络暂停下载", ""));
            settings.add(new KeyValueModel("赞赏", ""));
            settings.add(new KeyValueModel("反馈", ""));
            adapter = new BaseQuickAdapter<KeyValueModel, BaseViewHolder>(R.layout.item_setting, settings) {
                @Override
                protected void convert(BaseViewHolder helper, KeyValueModel item) {
                    helper.setText(R.id.txt_key, item.getKey())
                            .setText(R.id.txt_value, item.getValue());
                    helper.setVisible(R.id.img_go_to, !item.getKey().equals("移动网络暂停下载"))
                            .setVisible(R.id.switch_allow_4G, item.getKey().equals("移动网络暂停下载"));
                    helper.getView(R.id.ll_content).setClickable(!item.getKey().equals("移动网络暂停下载"));
                    if (item.getKey().equals("移动网络暂停下载")) {
                        boolean isAllow4G = SharePreferencesUtil.getBooleanSharePreferences(SettingsFragment.this.getContext(), Constant.KEY_IS_ALLOW_4G, false);
                        ((Switch) helper.getView(R.id.switch_allow_4G)).setChecked(!isAllow4G);
                        ((Switch) helper.getView(R.id.switch_allow_4G)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                SharePreferencesUtil.setBooleanSharePreferences(SettingsFragment.this.getContext(), Constant.KEY_IS_ALLOW_4G, !isChecked);
                            }
                        });
                    }
                }
            };
            adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(final BaseQuickAdapter adapter, View view, final int position) {
                    switch (((KeyValueModel) adapter.getData().get(position)).getKey()) {
                        case "下载中心":
                            Intent intent1 = new Intent(SettingsFragment.this.getContext(), DownloadManagerActivity.class);
                            startActivity(intent1);
                            break;
                        case "收藏记录":
                            Intent intent5 = new Intent(SettingsFragment.this.getContext(), CollectActivity.class);
                            startActivity(intent5);
                            break;
                        case "播放记录":
                            Intent intent6 = new Intent(SettingsFragment.this.getContext(), HistoryActivity.class);
                            startActivity(intent6);
                            break;
                        case "主题切换":
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
                            final int index = checked;
                            AlertDialog dialog = new AlertDialog.Builder(SettingsFragment.this.getContext())
                                    .setSingleChoiceItems(new String[]{"至尊黑", "魂动红", "魅惑蓝"}, index, new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (which == index) {
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
                            break;
                        case "本地播放":
                            FileUtil.updateVideoToSystem(getContext(), Constant.PATH_OFFLINE_DOWNLOAD);
                            Intent intent2 = new Intent();
                            intent2.setAction(Intent.ACTION_PICK);
                            intent2.setDataAndType(Uri.fromFile(new File(Constant.PATH_OFFLINE_DOWNLOAD)), "video/*");
                            startActivityForResult(intent2, 1);
                            break;
                        case "缓存清理":
                            CustomDialog customDialog = new CustomDialog(SettingsFragment.this.getActivity(), "清理缓存", "清理缓存将会删除所有下载记录以及下载路径中的所有文件，确定要清理缓存？", "立即清理", "取消") {
                                @Override
                                public void ok() {
                                    super.ok();
                                    File file = new File(PATH_OFFLINE_DOWNLOAD);
                                    String cacheSize = ValueUtil.formatFileSize(FileUtil.getFileSize(file));
                                    FileUtil.deleteFile(file);
                                    DownloadManager.removeAllXLTaskInfo();
                                    List<KeyValueModel> datas = adapter.getData();
                                    for (int i = 0; i < datas.size(); i++) {
                                        if (datas.get(i).getKey().equals("缓存清理")) {
                                            datas.get(i).setValue(ValueUtil.formatFileSize(FileUtil.getFileSize(file)));
                                            adapter.notifyDataSetChanged();
                                        }
                                    }
                                    ToastUtil.showMessage("已清理缓存：" + cacheSize);
                                }

                                @Override
                                public void cancel() {
                                    super.cancel();
                                }
                            };
                            customDialog.show();
                            break;
                        case "版本更新":
                            getConfig(true);
                            break;
                        case "和谐Q群":
                            ClipboardManager clip = (ClipboardManager) SettingsFragment.this.getContext().getSystemService
                                    (Context.CLIPBOARD_SERVICE);
                            clip.setText("126257036");
                            ToastUtil.showMessage("已复制群号");
                            break;
                        case "移动网络暂停下载":
                            break;
                        case "赞赏":
                            Intent intent3 = new Intent(SettingsFragment.this.getContext(), PreViewActivity.class);
                            intent3.putExtra("isAdmire", true);
                            startActivity(intent3);
                            break;
                        case "反馈":
                            Intent intent4 = new Intent(getContext(), FeedbackActivity.class);
                            startActivity(intent4);
                            break;
                    }
                }
            });
            recyclerviewSettings.setLayoutManager(new LinearLayoutManager(SettingsFragment.this.getContext()));
            recyclerviewSettings.setAdapter(adapter);
        }
        if (rootView != null) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }
        }
        getConfig(false);
        unbinder = ButterKnife.bind(this, rootView);
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

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (adapter != null && !adapter.getData().isEmpty()) {
                List<KeyValueModel> datas = adapter.getData();
                for (int i = 0; i < datas.size(); i++) {
                    if (datas.get(i).getKey().equals("缓存清理")) {
                        datas.get(i).setValue(ValueUtil.formatFileSize(FileUtil.getFileSize(new File(PATH_OFFLINE_DOWNLOAD))));
                        adapter.notifyDataSetChanged();
                    }
                }
            }

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            //
            if (resultCode == RESULT_OK) {
                try {
                    Uri uri = data.getData();
                    String path = FileUtil.getFilePath(SettingsFragment.this.getContext(), uri);
                    String url = DownloadManager.getLoclUrl(path);
                    Intent intent = new Intent(SettingsFragment.this.getContext(), PlayVideoActivity.class);
                    intent.putExtra("url", url);
                    SettingsFragment.this.getActivity().startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
