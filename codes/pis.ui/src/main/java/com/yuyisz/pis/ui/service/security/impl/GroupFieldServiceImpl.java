package com.yuyisz.pis.ui.service.security.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yuyisz.pis.ui.dao.security.GroupFieldRepository;
import com.yuyisz.pis.ui.module.security.GroupField;
import com.yuyisz.pis.ui.service.security.GroupFieldService;

@Service
public class GroupFieldServiceImpl implements GroupFieldService{
	@Autowired
	private GroupFieldRepository groupfieldRepository;
	
	@Override
	public GroupField save(GroupField groupfield) {
		return groupfieldRepository.save(groupfield);
	}

	@Override
	public void deleteByGroup(int groupId) {
		 groupfieldRepository.deleteByGroup(groupId);
	}

}
