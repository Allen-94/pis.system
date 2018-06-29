package com.yuyisz.pis.ui.module.subsystem;

public enum ResultCode {
	SUCCESS(200, "请求成功");
	private Integer code;
	private String msg;

	private ResultCode(Integer code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public Integer getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}
}
