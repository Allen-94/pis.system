package com.yuyisz.pis.ui.vo;

public class PcListVo {
	private Integer id;
	private Integer pc_id;
	private String pc_name;
	private String pc_model;
	private String pc_IP;
	private String pc_status;
	private String pc_account;
	private String pc_password;
	private String isTimeSwitch;
	private String switchTime;
	private String isControllScreenSwitch;
	private String screenSwitchTime;
	private String version;
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getPc_id() {
		return pc_id;
	}
	public void setPc_id(Integer pc_id) {
		this.pc_id = pc_id;
	}
	public String getPc_name() {
		return pc_name;
	}
	public void setPc_name(String pc_name) {
		this.pc_name = pc_name;
	}
	public String getPc_model() {
		return pc_model;
	}
	public void setPc_model(String pc_model) {
		this.pc_model = pc_model;
	}
	public String getPc_IP() {
		return pc_IP;
	}
	public void setPc_IP(String pc_IP) {
		this.pc_IP = pc_IP;
	}
	public String getPc_status() {
		return pc_status;
	}
	public void setPc_status(String pc_status) {
		this.pc_status = pc_status;
	}
	public String getPc_account() {
		return pc_account;
	}
	public void setPc_account(String pc_account) {
		this.pc_account = pc_account;
	}
	public String getPc_password() {
		return pc_password;
	}
	public void setPc_password(String pc_password) {
		this.pc_password = pc_password;
	}
	public String getIsTimeSwitch() {
		return isTimeSwitch;
	}
	public void setIsTimeSwitch(String isTimeSwitch) {
		this.isTimeSwitch = isTimeSwitch;
	}
	public String getSwitchTime() {
		return switchTime;
	}
	public void setSwitchTime(String switchTime) {
		this.switchTime = switchTime;
	}
	public String getIsControllScreenSwitch() {
		return isControllScreenSwitch;
	}
	public void setIsControllScreenSwitch(String isControllScreenSwitch) {
		this.isControllScreenSwitch = isControllScreenSwitch;
	}
	public String getScreenSwitchTime() {
		return screenSwitchTime;
	}
	public void setScreenSwitchTime(String screenSwitchTime) {
		this.screenSwitchTime = screenSwitchTime;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	@Override
	public String toString() {
		return "PcListVo [pc_name=" + pc_name + ", pc_model=" + pc_model + ", pc_IP=" + pc_IP + ", pc_status="
				+ pc_status + ", pc_account=" + pc_account + ", pc_password=" + pc_password + ", isTimeSwitch="
				+ isTimeSwitch + ", switchTime=" + switchTime + ", isControllScreenSwitch=" + isControllScreenSwitch
				+ ", screenSwitchTime=" + screenSwitchTime + ", version=" + version + "]";
	}
	
}
