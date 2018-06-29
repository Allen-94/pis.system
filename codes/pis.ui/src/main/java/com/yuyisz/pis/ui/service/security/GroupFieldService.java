package com.yuyisz.pis.ui.service.security;

import com.yuyisz.pis.ui.module.security.GroupField;

public interface GroupFieldService {
	GroupField save(GroupField groupfield);

	void deleteByGroup(int groupId);
}
