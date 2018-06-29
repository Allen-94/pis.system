package com.yuyisz.pis.ui.vo;

import java.io.Serializable;

import com.yuyisz.pis.ui.module.security.DevResources;

public class ResourceTreeVO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private int id;
	private int pid;
	private String name;
	private DevResources value;
	private boolean checked;
	private boolean open=false;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getPid() {
		return pid;
	}
	public void setPid(int pid) {
		this.pid = pid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public DevResources getValue() {
		return value;
	}
	public void setValue(DevResources value) {
		this.value = value;
	}
	public boolean isChecked() {
		return checked;
	}
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	public boolean isOpen() {
		return open;
	}
	public void setOpen(boolean open) {
		this.open = open;
	}
}
