package com.yuyisz.pis.ui.vo;

import java.util.Map;

public class ContentPackageVO {

	private String contentName;
	private Map<String,String> content;
	
	public String getContentName() {
		return contentName;
	}
	public void setContentName(String contentName) {
		this.contentName = contentName;
	}
	public Map<String, String> getContent() {
		return content;
	}
	public void setContent(Map<String, String> content) {
		this.content = content;
	}
}
