package com.lxw.videoworld.framework.weixin;

public class WXShareImgUrlBean extends WXShareBaseBean {
	public String description;
	public String url;
	public String imageUrl = "";
	public String title = "";

	public WXShareImgUrlBean() {
		shareType = WXShareAction.SHAREIMGURL;
	}
}
