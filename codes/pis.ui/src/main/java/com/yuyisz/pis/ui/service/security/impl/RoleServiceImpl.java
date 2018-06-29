package com.yuyisz.pis.ui.service.security.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.yuyisz.pis.ui.dao.security.RoleRepository;
import com.yuyisz.pis.ui.module.security.Roles;
import com.yuyisz.pis.ui.module.security.User;
import com.yuyisz.pis.ui.module.security.UserGroup;
import com.yuyisz.pis.ui.service.security.RoleService;
@Scope("prototype")
@Service
public class RoleServiceImpl implements RoleService {
	@Autowired
	private RoleRepository roleRepository;

	@Override
	public Page<Roles> findAllRolesPage(Integer page, Integer size, String[] prop, Roles role) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Roles saveRoles(Roles roles) {
		Roles result = roleRepository.save(roles);
		return result;
	}

	@Override
	public Roles findOneRoles(Integer roleId) {
		return roleRepository.findOne(roleId);
	}

	public List<Roles> findAllRoles() {
		return roleRepository.findAll();
	}

	@Override
	public void deleteRoles(Integer roleId) {
		roleRepository.delete(roleId);
	}

	@Override
	public List<Roles> findbyFilter(String nameFilter, String descriptionFilter) {
		return roleRepository.findbyFilter(nameFilter, descriptionFilter);
	}

	@Override
	public List<Roles> findRolesOfGroup(Integer groupId) {
		return roleRepository.findRolesOfGroup(groupId);
	}

	@Override
	public Roles findByRoleName(String roleName) {
		return roleRepository.findByRoleName(roleName);
	}

}
