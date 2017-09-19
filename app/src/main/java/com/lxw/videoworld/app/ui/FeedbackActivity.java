package com.lxw.videoworld.app.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.lxw.videoworld.R;
import com.lxw.videoworld.app.api.HttpHelper;
import com.lxw.videoworld.framework.base.BaseActivity;
import com.lxw.videoworld.app.model.BaseResponse;
import com.lxw.videoworld.framework.http.HttpManager;
import com.lxw.videoworld.framework.util.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FeedbackActivity extends BaseActivity {

    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.txt_feedback)
    TextView txtFeedback;
    @BindView(R.id.edit_feedback)
    EditText editFeedback;
    @BindView(R.id.btn_feedback)
    Button btnFeedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);
        ButterKnife.bind(this);
        initViews();
    }

    private void initViews() {
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FeedbackActivity.this.finish();
            }
        });

        editFeedback.requestFocus();

        btnFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = editFeedback.getText().toString();
                if (!TextUtils.isEmpty(content) && !TextUtils.isEmpty(content.trim())) {
                    content = content.trim();
                        new HttpManager<String>((BaseActivity) FeedbackActivity.this, HttpHelper.getInstance().addFeedback(content)) {

                        @Override
                        public void onSuccess(BaseResponse<String> response) {
                            ToastUtil.showMessage(FeedbackActivity.this, getString(R.string.txt_feedback_success_tips));
                        }

                        @Override
                        public void onFailure(BaseResponse<String> response) {

                        }
                    }.doRequest();
                } else {
                    ToastUtil.showMessage(FeedbackActivity.this, getString(R.string.txt_no_feedback_tips));
                }
            }
        });
    }
}
