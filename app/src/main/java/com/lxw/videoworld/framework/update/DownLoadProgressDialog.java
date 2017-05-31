package com.lxw.videoworld.framework.update;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lxw.videoworld.R;

import java.text.DecimalFormat;

/**
 * Created by lxw9047 on 2016/3/31.
 */
public class DownLoadProgressDialog extends AlertDialog {
    private double totalSize;
    private double currentSize;
    private int progress;
    private Context context;
    private UpdateVersionDialog updateVersionDialog;
    private UpdateAppDialog updateAppDialog;
    private ProgressBar downloadProgressBar;
    private TextView progressView;
    private TextView currentSize_totalSize;
    private DecimalFormat df =new DecimalFormat("#0.00");

    public DownLoadProgressDialog(Context context, UpdateVersionDialog updateVersionDialog) {
        super(context);
        this.setCanceledOnTouchOutside(false);
        this.context=context;
        this.updateVersionDialog=updateVersionDialog;
        this.setCanceledOnTouchOutside(false);
    }
    public DownLoadProgressDialog(Context context, UpdateAppDialog updateAppDialog) {
        super(context);
        this.setCanceledOnTouchOutside(false);
        this.context=context;
        this.updateAppDialog=updateAppDialog;
        this.setCanceledOnTouchOutside(false);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.download_progressdialog);
        downloadProgressBar= (ProgressBar) findViewById(R.id.downloadProgressBar);
        progressView= (TextView) findViewById(R.id.downloadProgress);
        currentSize_totalSize= (TextView) findViewById(R.id.currentSize_totalSize);
        downloadProgressBar.setMax(100);
        currentSize_totalSize.setText("");
    }
   public void updateProgress(double currentSize){
       this.currentSize=currentSize;
       progress= (int) Math.floor((this.currentSize*100/this.totalSize));
       if(progress>=100){
    	   progress=99;
       }
       downloadProgressBar.setProgress(progress);
//       Log.e("进度",  progress+"/");
       progressView.setText(progress+"%");
       if(this.currentSize>=this.totalSize){
    	   this.currentSize=this.totalSize-0.01;
       }
       currentSize_totalSize.setText(df.format(this.currentSize)+"/"+df.format(this.totalSize)+"M");
   }
   @Override
   public void onBackPressed() {
       //判断当前显示的是Version更新下载对话框还是APP更新下载对话框
				 if(updateAppDialog!=null){
                     updateAppDialog.cancelDownload();
	        	   }else if(updateVersionDialog!=null){
	        		   updateVersionDialog.cancelDownload();
	        	   }else{
	        		   super.onBackPressed();
	        	   }
   }
 
   
public double getTotalSize() {
	return totalSize;
}
public void setTotalSize(double totalSize) {
	this.totalSize=totalSize;
	currentSize_totalSize.setText("0/"+df.format(this.totalSize)+"M");
}
public double getCurrentSize() {
	return currentSize;
}
public void setCurrentSize(double currentSize) {
	this.currentSize = currentSize;
}
public int getProgress() {
	return progress;
}
public void setProgress(int progress) {
	this.progress = progress;
}
public void setContext(Context context) {
	this.context = context;
}
public ProgressBar getDownloadProgressBar() {
	return downloadProgressBar;
}
public void setDownloadProgressBar(ProgressBar downloadProgressBar) {
	this.downloadProgressBar = downloadProgressBar;
}
public TextView getProgressView() {
	return progressView;
}
public void setProgressView(TextView progressView) {
	this.progressView = progressView;
}
public TextView getCurrentSize_totalSize() {
	return currentSize_totalSize;
}
public void setCurrentSize_totalSize(TextView currentSize_totalSize) {
	this.currentSize_totalSize = currentSize_totalSize;
}

}
