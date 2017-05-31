package com.lxw.videoworld.framework.weixin;

public class WXShareImgBean extends WXShareBaseBean {
	public String imageUrl = "";

	public WXShareImgBean() {
		shareType = WXShareAction.SHAREIMG;
	}
}
