package com.lxw.videoworld.app.ui;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.lxw.videoworld.R;
import com.lxw.videoworld.app.api.HttpHelper;
import com.lxw.videoworld.app.config.Constant;
import com.lxw.videoworld.app.model.BaseResponse;
import com.lxw.videoworld.framework.base.BaseActivity;
import com.lxw.videoworld.framework.http.HttpManager;
import com.lxw.videoworld.framework.image.ImageManager;
import com.lxw.videoworld.framework.util.FileUtil;
import com.lxw.videoworld.framework.util.ImageUtil;
import com.lxw.videoworld.framework.util.ToastUtil;
import com.lxw.videoworld.framework.widget.CustomDialog;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdmireActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.txt_tab1)
    TextView txtTab1;
    @BindView(R.id.txt_tab2)
    TextView txtTab2;
    @BindView(R.id.txt_tab3)
    TextView txtTab3;
    @BindView(R.id.txt_tab4)
    TextView txtTab4;
    @BindView(R.id.txt_step2)
    TextView txtStep2;
    @BindView(R.id.btn_save_image)
    Button btnSaveImage;
    @BindView(R.id.btn_admire)
    Button btnAdmire;
    @BindView(R.id.img_shotscreen)
    ImageView imgShotscreen;
    @BindView(R.id.img_add_image)
    ImageView imgAddImage;

    private int amount = 0;
    private String image = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admire);
        ButterKnife.bind(this);
        initViews();
    }

    private void initViews() {
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdmireActivity.this.finish();
            }
        });

        txtTab1.setOnClickListener(this);
        txtTab2.setOnClickListener(this);
        txtTab3.setOnClickListener(this);
        txtTab4.setOnClickListener(this);

        btnAdmire.setOnClickListener(this);
        btnSaveImage.setOnClickListener(this);
        imgAddImage.setOnClickListener(this);
    }

    public void addAdmire() {
        btnAdmire.setEnabled(false);
        new HttpManager<String>((BaseActivity) AdmireActivity.this, HttpHelper.getInstance().addAdmire(amount, image)) {

            @Override
            public void onSuccess(BaseResponse<String> response) {
                btnAdmire.setEnabled(true);
                CustomDialog customDialog = new CustomDialog(AdmireActivity.this, "上传成功",
                        "开发者会在 1~3 天内确认赞赏金额，并将赠送相应的畅玩特权天数到本账号，可在我的账号查看变动，如有问题请进群联系开发者。衷心您的支持！",
                        "知道了", "") {
                    @Override
                    public void ok() {
                        super.ok();
                        finish();
                    }

                    @Override
                    public void cancel() {
                        super.cancel();
                        finish();
                    }
                };
                customDialog.show();
            }

            @Override
            public void onFailure(BaseResponse<String> response) {
                btnAdmire.setEnabled(true);
            }
        }.doRequest();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txt_tab1:
                amount = 30;
                txtTab1.setSelected(true);
                txtTab2.setSelected(false);
                txtTab3.setSelected(false);
                txtTab4.setSelected(false);
                break;
            case R.id.txt_tab2:
                amount = 90;
                txtTab1.setSelected(false);
                txtTab2.setSelected(true);
                txtTab3.setSelected(false);
                txtTab4.setSelected(false);
                break;
            case R.id.txt_tab3:
                amount = 180;
                txtTab1.setSelected(false);
                txtTab2.setSelected(false);
                txtTab3.setSelected(true);
                txtTab4.setSelected(false);
                break;
            case R.id.txt_tab4:
                amount = 360;
                txtTab1.setSelected(false);
                txtTab2.setSelected(false);
                txtTab3.setSelected(false);
                txtTab4.setSelected(true);
                break;
            case R.id.btn_save_image:
                boolean saveWX = FileUtil.saveDrawable(AdmireActivity.this, R.drawable.img_wx_admire,
                        Constant.PATH_OFFLINE_DOWNLOAD, "wx_admire.jpeg");
                boolean saveALP = FileUtil.saveDrawable(AdmireActivity.this, R.drawable.img_alp_admire,
                        Constant.PATH_OFFLINE_DOWNLOAD, "alp_admire.jpeg");
                if (saveALP || saveWX) ToastUtil.showMessage("成功保存到相册");
                break;
            case R.id.img_add_image:
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 1);
                break;
            case R.id.btn_admire:
                if (amount == 0) {
                    ToastUtil.showMessage("请先选择赞赏金额");
                } else if (TextUtils.isEmpty(image)) {
                    ToastUtil.showMessage("请先选择赞赏截图");
                } else {
                    addAdmire();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //获取图片路径
        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {
            try {
                Uri selectedImage = data.getData();
                String[] filePathColumns = {MediaStore.Images.Media.DATA};
                Cursor c = getContentResolver().query(selectedImage, filePathColumns, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePathColumns[0]);
                String imagePath = c.getString(columnIndex);
                c.close();
                image = ImageUtil.getImageBase64(imagePath);
                ImageManager.getInstance().loadImage(AdmireActivity.this, imgShotscreen, imagePath);
                imgShotscreen.setVisibility(View.VISIBLE);
            } catch (Exception e) {
                e.printStackTrace();
                ToastUtil.showMessage("选择图片失败");
            }
        }
    }

}
