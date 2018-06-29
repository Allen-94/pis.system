package com.yuyisz.pis.ui.vo;

import java.util.List;

import com.yuyisz.pis.ui.module.security.Roles;
import com.yuyisz.pis.ui.module.security.UserGroup;

public class UserGroupForm {

	private UserGroup userGroup;
	private List<Roles> selectedRoles;
	public UserGroup getUserGroup() {
		return userGroup;
	}
	public void setUserGroup(UserGroup userGroup) {
		this.userGroup = userGroup;
	}
	public List<Roles> getSelectedRoles() {
		return selectedRoles;
	}
	public void setSelectedRoles(List<Roles> selectedRoles) {
		this.selectedRoles = selectedRoles;
	}
}
