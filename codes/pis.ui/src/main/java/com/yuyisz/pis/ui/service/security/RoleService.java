package com.yuyisz.pis.ui.service.security;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.Query;

import com.yuyisz.pis.ui.module.security.Roles;
import com.yuyisz.pis.ui.module.security.User;

public interface RoleService {
	
	/**
	 * 按模板分页查找权限
	 * @param page
	 * @param size
	 * @param prop
	 * @param role
	 * @return
	 */
	Page<Roles> findAllRolesPage(Integer page,Integer size , String[] prop , Roles role);
	/**
	 * 根据ID查找权限
	 * @param roleId
	 * @return
	 */
	Roles findOneRoles(Integer roleId);
	public List<Roles> findAllRoles();
	Roles saveRoles(Roles roles);
	void deleteRoles(Integer roleId);
	
	List<Roles> findbyFilter(String nameFilter, String descriptionFilter);
	List<Roles> findRolesOfGroup(Integer groupId);
	Roles findByRoleName(String string);
	
}
