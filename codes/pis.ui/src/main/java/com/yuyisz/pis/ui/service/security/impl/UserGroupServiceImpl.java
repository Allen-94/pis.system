package com.yuyisz.pis.ui.service.security.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.yuyisz.pis.ui.dao.security.UserGroupRepository;
import com.yuyisz.pis.ui.module.security.Roles;
import com.yuyisz.pis.ui.module.security.UserGroup;
import com.yuyisz.pis.ui.service.security.UserGroupService;
@Service
public class UserGroupServiceImpl implements UserGroupService {

	@Autowired
	private UserGroupRepository userGroupRepository;
	@Override
	public UserGroup save(UserGroup userGroup) {
		UserGroup result = userGroupRepository.save(userGroup);
		return result;
	}
	@Override
	public Page<UserGroup> findAllGroupPage(Integer page, Integer size, String[] properties, UserGroup group) {
		Pageable pageable = new PageRequest(page, size, new Sort(properties));
		return userGroupRepository.findAll(Example.of(group), pageable);
	}
	
	@Override
	public List<Roles> findRolesByGroupId(Integer groupId) {
		UserGroup result = userGroupRepository.findOne(groupId);
		if(result!=null) {
			return new ArrayList<Roles>(result.getRoles());
		}
		return null;
	}
	@Override
	public UserGroup findOneUserGroup(Integer groupId) {
		UserGroup result = null;
		if(groupId!=null) {
			result = userGroupRepository.findOne(groupId);
		}
		return result;
	}
	@Override
	public List<UserGroup> findAllGroup() {
		return userGroupRepository.findAll();
	}
	@Override
	public void deleteGroup(Integer groupId) {
		userGroupRepository.delete(groupId);
	}
	@Override
	public List<UserGroup> findbyFilter(String nameFilter, String descriptionFilter) {
		return userGroupRepository.findbyFilter(nameFilter, descriptionFilter);
	}
	@Override
	public UserGroup findGroupOfUser(int userId) {
		return userGroupRepository.findGroupOfUser(userId);
	}
	@Override
	public UserGroup findByGroupName(String string) {
		return userGroupRepository.findByGroupName(string);
	}

	
}
