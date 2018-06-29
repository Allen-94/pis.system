package com.yuyisz.pis.ui.vo;

import com.yuyisz.pis.ui.module.security.User;

public class UserForm {
	private User user;
	private Integer seletedGroup;
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Integer getSeletedGroup() {
		return seletedGroup;
	}
	public void setSeletedGroup(Integer seletedGroup) {
		this.seletedGroup = seletedGroup;
	}
}
