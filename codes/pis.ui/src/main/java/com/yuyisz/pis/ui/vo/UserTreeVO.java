package com.yuyisz.pis.ui.vo;

import java.io.Serializable;

import com.yuyisz.pis.ui.module.security.DevResources;

public class UserTreeVO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private int id;
	private int pid;
	private int userId;
	private int groupId;
	private String name;
	private int type;	//节点类型 0：组，1：用户
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
	public int getType() {
		return type;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getGroupId() {
		return groupId;
	}
	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}
	public void setType(int type) {
		this.type = type;
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
