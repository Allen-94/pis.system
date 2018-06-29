package com.yuyisz.pis.ui.service.security.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.yuyisz.pis.ui.dao.security.UserRepository;
import com.yuyisz.pis.ui.module.security.Command;
import com.yuyisz.pis.ui.module.security.Roles;
import com.yuyisz.pis.ui.module.security.User;
import com.yuyisz.pis.ui.module.security.UserGroup;
import com.yuyisz.pis.ui.service.security.UserService;
@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public User saveUser(User user) {
		user.setUpdateTime(new Date());
		User result = userRepository.save(user);
		return result;
	}

	@Override
	public User findUserById(int userId) {
		User result = userRepository.findOne(userId);
		return result;
	}

	@Override
	public Page<User> findAllUserByExample(Integer page, Integer size, String[] properties, User user) {
		Example<User> example = Example.of(user) ;
		Pageable pageable = new PageRequest(page, size, new Sort(properties));
		return userRepository.findAll(example, pageable);
	}

	@Override
	public UserGroup findUserGroupByUserId(Integer userId) {
		User result = userRepository.findOne(userId);
		return result.getUserGroup();
	}

	@Override
	public List<Roles> findRolesByUserId(Integer userId) {
		User result = userRepository.findOne(userId);
		UserGroup group = result.getUserGroup();
		if(group!=null) {
			return new ArrayList<Roles>(group.getRoles());
		}
		return null;
	}

	@Override
	public List<User> findAllUser() {
		return userRepository.findAll();
	}

	@Override
	public void deleteUser(Integer userId) {
		 userRepository.delete(userId);
	}

	@Override
	public List<User> findbyFilter(String accountFilter, String realnameFilter) {
		return userRepository.findbyFilter(accountFilter, realnameFilter);
	}

	@Override
	public User landingCheck(String username, String password) {
		return userRepository.findByAccountAndPassword(username,password);
	}

	@Override
	public User findUserByAccount(String account) {
		return userRepository.findByAccount(account);
	}

	@Override
	public User findByUserGroup(UserGroup supergroup) {
		return userRepository.findByUserGroup(supergroup.getGroupId());
	}

	@Override
	public List<User> findUserByIds(List<Integer> acceptors) {
		return userRepository.findAll(acceptors);
	}
}
