package com.yuyisz.pis.ui.vo;

public class StationServerVo {
	private Integer station_server_id;
	private String name;
	private String model;
	private Integer id;
	private String ip;
	private String status;
	private String account;
	private String password;
	private String version;
	
	public Integer getStation_server_id() {
		return station_server_id;
	}
	public void setStation_server_id(Integer integer) {
		this.station_server_id = integer;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	@Override
	public String toString() {
		return "StationServerVo [name=" + name + ", model=" + model + ", id=" + id + ", ip=" + ip + ", status=" + status
				+ ", account=" + account + ", password=" + password + ", version=" + version + "]";
	}
	
}
