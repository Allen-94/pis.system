package com.yuyisz.pis.ui.service.security;

import java.util.List;

import org.springframework.data.domain.Page;

import com.yuyisz.pis.ui.module.security.Command;
import com.yuyisz.pis.ui.module.security.Roles;
import com.yuyisz.pis.ui.module.security.User;
import com.yuyisz.pis.ui.module.security.UserGroup;

public interface UserService {
	/**
	 * 保存用户
	 * @param user
	 * @return 保存数，0表示保存失败
	 */
	public User saveUser(User user);
	/**
	 * 根据ID获取User
	 * @param userId
	 * @return
	 */
	public User findUserById(int userId);
	/**
	 * 
	 * @param page  查询的页码
	 * @param size	每页的个数
	 * @param properties 排序字段
	 * @param user  查找的模板
 	 * @return 
	 */
	public Page<User> findAllUserByExample(Integer page, Integer size,String[] properties ,final User user);
	
	public UserGroup findUserGroupByUserId(Integer userId);
	
	public List<Roles> findRolesByUserId(Integer userId);
	
	public List<User> findAllUser();
	
	public void deleteUser(Integer userId);
	/**
	 * 条件过滤
	 * @param accountFilter
	 * @param realnameFilter
	 * @return
	 */
	public List<User> findbyFilter(String accountFilter, String realnameFilter);
	/**
	 * 登陆验证
	 * @param username
	 * @param password
	 * @return
	 */
	public User landingCheck(String username, String password);
	/**
	 * 通过用户名找用户
	 * @param account 
	 * @return
	 */
	public User findUserByAccount(String account);
	/**
	 * 所属用户组
	 * @param supergroup
	 * @return
	 */
	public User findByUserGroup(UserGroup supergroup);
	public List<User> findUserByIds(List<Integer> acceptors);
	
	
}
