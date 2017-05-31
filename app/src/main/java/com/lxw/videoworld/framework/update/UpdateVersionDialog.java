package com.lxw.videoworld.framework.update;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lxw.videoworld.R;
import com.lxw.videoworld.framework.activitystack.ActivityStack;
import com.lxw.videoworld.framework.util.ToastUtil;

import java.io.File;

public class UpdateVersionDialog extends AlertDialog {

	private Button update_version_dialog_cancel_btn;
	private Button update_version_dialog_ok_btn;
	private TextView update_version_dialog_head_tev;
	private EditText update_version_dialog_content_edt;
	private String mHeadStr;
	private String mContentStr;
	private String mDownUrl;
	private String mVersionCode = "";
	// 是否是强制更新的标志
	private boolean mForceClose = false;

	private NotificationManager updateNotificationManager = null;
	private Notification updateNotification = null;
	private NotificationCompat.Builder mBuilder = null;

	private File updateFile = null;
	private long currentSize = 0;
	private long totalSize = 0;
	private String title;
	private long lastPercentage = 0;

	private static final int DOWNLOAD_APK = 1000;
	private static final int DOWNLOAD_SUCCESS = 1001;
	private static final int DOWNLOAD_FAILURE = 1002;
	private static final int DOWNLOAD_PROGRESS = 1003;


	private Activity context;
	private AlertDialog cancelDialog;
	private boolean cancelFlag;
	private DownLoadProgressDialog downLoadProgressDialog;
	private static final int UPDATE_TOTALSIZE = 1004;
	private static final int UPDATE_PROGRESS = 1005;

	public UpdateVersionDialog(Activity context) {
		super(context);
		this.context=context;
	}

	public UpdateVersionDialog(Activity context, int theme) {
		super(context, theme);
		this.context=context;
	}

	public UpdateVersionDialog(Activity context, String head, String content,
                               String downUrl, boolean forceClose) {
		super(context);
		this.context = context;
		mHeadStr = head;
		mContentStr = content;
		mDownUrl = downUrl;
		mForceClose = forceClose;
	}

	public UpdateVersionDialog(Activity context, String head, String content,
                               String downUrl, boolean forceClose, String versionCode) {
		super(context);
		this.context=context;
		mHeadStr = head;
		mContentStr = content;
		mDownUrl = downUrl;
		mForceClose = forceClose;
		mVersionCode = versionCode;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_update_version);
		initUI();
		initNotifycation();
	}

	private void initUI() {
		update_version_dialog_cancel_btn = (Button) this
				.findViewById(R.id.update_version_dialog_cancel_btn);

		update_version_dialog_ok_btn = (Button) this
				.findViewById(R.id.update_version_dialog_ok_btn);
		update_version_dialog_ok_btn
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// 下载更新 mDownUrl
						mHandler.sendMessage(mHandler
								.obtainMessage(DOWNLOAD_APK));
					}
				});
		update_version_dialog_head_tev = (TextView) this
				.findViewById(R.id.update_version_dialog_head_tev);
		update_version_dialog_head_tev.setText(mHeadStr);
		update_version_dialog_content_edt = (EditText) this
				.findViewById(R.id.update_version_dialog_content_edt);
		update_version_dialog_content_edt.setText(mContentStr);

		title = this.getContext().getResources().getString(R.string.app_name);

		if (mForceClose) {
			// 若是强制更新
			update_version_dialog_cancel_btn.setText("退出");
			this.setCancelable(false);
			this.setCanceledOnTouchOutside(false);
			update_version_dialog_cancel_btn
					.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							UpdateVersionDialog.this.dismiss();
							ActivityStack.create().AppExit(
									UpdateVersionDialog.this.getContext());
						}
					});
		} else {
			update_version_dialog_cancel_btn.setText("不再提醒");
			this.setCancelable(true);
			this.setCanceledOnTouchOutside(true);
			update_version_dialog_cancel_btn
					.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							SharedPreferencesUtil.setUnForceUpdateVersionCode(
									getContext(), mVersionCode);
							UpdateVersionDialog.this.dismiss();
						}
					});
		}
		this.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface dialog) {
				// TODO Auto-generated method stub
				// if (!TextUtils.isEmpty(mVersionCode)) {
				// SharedPreferencesUtil.setUnForceUpdateVersionCode(
				// getContext(), mVersionCode);
				// }
				Assembly.supportCheckUpdateInMain = false;
			}
		});
	}

	private void initNotifycation() {
		updateNotificationManager = (NotificationManager) this.getContext()
				.getSystemService(Context.NOTIFICATION_SERVICE);
		mBuilder = new NotificationCompat.Builder(this.getContext());
		mBuilder.setContentTitle(title)// 设置通知栏标题
				.setContentText("0%") // 设置通知栏显示内容
				// .setContentIntent(getDefalutIntent(Notification.FLAG_AUTO_CANCEL))
				// //设置通知栏点击意图
				// .setNumber(1) //设置通知集合的数量
				.setTicker("应用更新下载") // 通知首次出现在通知栏，带上升动画效果的
				.setWhen(System.currentTimeMillis())// 通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
				.setPriority(Notification.PRIORITY_DEFAULT) // 设置该通知优先级
				// .setAutoCancel(true)//设置这个标志当用户单击面板就可以让通知将自动取消
				.setOngoing(false)// ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
				// .setDefaults(Notification.DEFAULT_VIBRATE)//向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合
				// Notification.DEFAULT_ALL Notification.DEFAULT_SOUND 添加声音 //
				// requires VIBRATE permission
				.setSmallIcon(R.drawable.icon_notification);// 设置通知小ICON

		updateNotification = mBuilder.build();
		updateNotification.flags = Notification.FLAG_AUTO_CANCEL;
	}

	public void cancelDownload(){
		cancelDialog= new CustomConfirmDialog(context, "温馨提示",
				"确定取消下载更新？", "确定", "取消",
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// 直接取消下载更新
						cancelFlag=true;
						downLoadProgressDialog.dismiss();
						cancelDialog.dismiss();
						}
				}, new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// 取消
						cancelDialog.dismiss();
						}
		});
	cancelDialog.show();
	}

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {
			case DOWNLOAD_APK:
				// 下载apk文件
				updateFile = FileSystemUtil.createFilePath(
						UpdateVersionDialog.this.getOwnerActivity(), "update",
						FileSystemUtil.genRandomFileName("apk"));
				updateNotificationManager.notify(0, updateNotification);
				updateRunnable();
				//隐藏是否更新对话框，弹出下载进度对话框
				 UpdateVersionDialog.this.dismiss();
				downLoadProgressDialog=new DownLoadProgressDialog(UpdateVersionDialog.this.getContext(),UpdateVersionDialog.this);
				downLoadProgressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
				downLoadProgressDialog.show();

				// 切换UI到下载中界面
				setContentView(R.layout.updateversiondialog_downloading);
				UpdateVersionDialog.this.setCancelable(false);
				UpdateVersionDialog.this.setCanceledOnTouchOutside(false);

				break;
			case DOWNLOAD_PROGRESS:
				long currentPercentage = (100 * currentSize) / totalSize;
				if (currentPercentage - lastPercentage > 5) {
					// 下载的进度大于5%才更新
					lastPercentage = currentPercentage;
					if (currentPercentage >= 100) {
						mBuilder.setContentText("99%");
					} else {
						mBuilder.setContentText(lastPercentage + "%");
					}
					updateNotification = mBuilder.build();
					updateNotificationManager.notify(0, updateNotification);
				}
				break;

				case UPDATE_TOTALSIZE:
					cancelFlag=false;
					 downLoadProgressDialog.setTotalSize(totalSize/(double)(1000*1000));

					break;

				case UPDATE_PROGRESS:
					downLoadProgressDialog.updateProgress(currentSize/(double)(1000*1000));

					break;

			case DOWNLOAD_SUCCESS:
				downLoadProgressDialog.dismiss();
				Uri uri = Uri.fromFile(updateFile);
				Intent installIntent = new Intent(Intent.ACTION_VIEW);
				installIntent.setDataAndType(uri,
						"application/vnd.android.package-archive");
				UpdateVersionDialog.this.getContext().startActivity(
						installIntent);
				updateNotificationManager.cancel(0);
				if (mForceClose) {
					// 若是强制更新
					setContentView(R.layout.updateversiondialog);
					initUI();
					initNotifycation();
				} else {
					UpdateVersionDialog.this.dismiss();
				}
				break;
			case DOWNLOAD_FAILURE:
				downLoadProgressDialog.dismiss();
				updateNotificationManager.cancel(0);
				ToastUtil.showMessage(UpdateVersionDialog.this.getContext(),
						"下载更新文件失败", Toast.LENGTH_LONG);
				// 下载失败 重新设置UI
				setContentView(R.layout.updateversiondialog);
				initUI();
				initNotifycation();
				break;
			default:
				break;
			}
		}

	};

	private void updateRunnable() {
		HttpConnecter.download(updateFile, mDownUrl, 2 * 60 * 1000, 5, new DownloadStatusListener() {
			@Override
			public void onDownloadComplete(int id) {
				mHandler.sendMessage(mHandler.obtainMessage(DOWNLOAD_SUCCESS));
			}

			@Override
			public void onDownloadFailed(int id, int errorCode, String errorMessage) {
				mHandler.sendMessage(mHandler.obtainMessage(DOWNLOAD_FAILURE));
			}

			@Override
			public void onProgress(int id, long totalBytes, long downloadedBytes, int progress) {
				currentSize = downloadedBytes;
				totalSize = totalBytes;
				mHandler.sendMessage(mHandler
						.obtainMessage(UPDATE_TOTALSIZE));
				mHandler.sendMessage(mHandler
						.obtainMessage(DOWNLOAD_PROGRESS));
				mHandler.sendMessage(mHandler
						.obtainMessage(UPDATE_PROGRESS));
			}
		});
	}
}
