package com.yuyisz.pis.ui.service.security;

import java.util.List;

import org.springframework.data.domain.Page;

import com.yuyisz.pis.ui.module.security.Roles;
import com.yuyisz.pis.ui.module.security.UserGroup;

public interface UserGroupService {

	UserGroup save(UserGroup userGroup);
	
	/**
	 * 按模板分页查找用户组
	 * @param page
	 * @param size
	 * @param prop
	 * @param group
	 * @return
	 */
	Page<UserGroup> findAllGroupPage(Integer page,Integer size,String[] prop,UserGroup group);
	/**
	 * 根据用户组ID查找所属权限
	 * @param groupId
	 * @return
	 */
	List<Roles> findRolesByGroupId(Integer groupId);
	/**
	 * 根据ID查找用户组
	 * @param groupId
	 * @return
	 */
	UserGroup findOneUserGroup(Integer groupId);

	List<UserGroup> findAllGroup();
	
	void deleteGroup(Integer groupId);

	/**
	 * 条件过滤
	 * @param nameFilter 名称
	 * @param descriptionFilter 描述
	 * @return
	 */
	List<UserGroup> findbyFilter(String nameFilter, String descriptionFilter);

	UserGroup findGroupOfUser(int userId);

	UserGroup findByGroupName(String string);
	
	
	
}
