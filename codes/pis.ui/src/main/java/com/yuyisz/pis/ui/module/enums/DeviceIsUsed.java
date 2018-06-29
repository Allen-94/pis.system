package com.yuyisz.pis.ui.module.enums;

public enum DeviceIsUsed {
	used(0,"是"),
	notUsed(1,"否")
	;
	private int uesd_enum;
	private String state;
	private DeviceIsUsed(int uesd_enum,String state) {
		this.uesd_enum=uesd_enum;
		this.state = state;
	}
	public int getUesd_enum() {
		return uesd_enum;
	}
	public void setUesd_enum(int uesd_enum) {
		this.uesd_enum = uesd_enum;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}

}
