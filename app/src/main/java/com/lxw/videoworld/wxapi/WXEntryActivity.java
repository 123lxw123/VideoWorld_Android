package com.lxw.videoworld.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.lxw.videoworld.framework.weixin.WXShareAction;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;


public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
	WXShareAction wxShareAction = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		wxShareAction = new WXShareAction(this);
		wxShareAction.respFromWX(this);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		wxShareAction.respFromWX(this);
	}

	@Override
	public void onReq(BaseReq req) {
		// Toast.makeText(this, "onReq", Toast.LENGTH_SHORT).show();
		if (null != req) {
			switch (req.getType()) {
			case ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX:

				break;

			}
		}
	}

	@Override
	public void onResp(BaseResp resp) {
		String content = "";
		switch (resp.errCode) {
		case BaseResp.ErrCode.ERR_OK:
			content = "分享成功";
			break;
		case BaseResp.ErrCode.ERR_USER_CANCEL:
			content = "用户取消";
			break;
		case BaseResp.ErrCode.ERR_AUTH_DENIED:
			content = "发送被拒绝";
			break;
		default:
			content = "发送返回";
			break;
		}
		Toast.makeText(this, content, Toast.LENGTH_SHORT).show();
		this.finish();
	}
}
