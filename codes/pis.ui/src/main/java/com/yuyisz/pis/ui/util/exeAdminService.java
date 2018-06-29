package com.yuyisz.pis.ui.util;

import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yuyisz.pis.ui.module.security.Command;
import com.yuyisz.pis.ui.module.security.Roles;
import com.yuyisz.pis.ui.module.security.User;
import com.yuyisz.pis.ui.module.security.UserGroup;
import com.yuyisz.pis.ui.service.security.CmdService;
import com.yuyisz.pis.ui.service.security.RoleService;
import com.yuyisz.pis.ui.service.security.UserGroupService;
import com.yuyisz.pis.ui.service.security.UserService;
@Component
//初始化超级管理员
public class exeAdminService {
	@Autowired
	private UserService userSerivice;
	@Autowired
	private RoleService roleService;
	@Autowired
	private UserGroupService usergroupService;
	@Autowired
	private CmdService cmdService;

	//初始化超级管理员
	public void exe() {
		/*//获取所有权限
		List<Command> cmds = cmdService.finAll();*/
		
		// 角色'supermanager'不存在,初始化
		Roles supermanager = roleService.findByRoleName("supermanager");
		if(supermanager==null) {
			supermanager = new Roles();
			supermanager.setRoleName("supermanager");
			/*HashSet<Command> sets = new HashSet<Command>(cmds);*/
			/*supermanager.setCmds(sets);*/
			supermanager.setCreateTime(new Date());
			supermanager.setRole_description("超级管理员");
		}
		
		// 初始化超级管理员组,保证唯一
		UserGroup supergroup = usergroupService.findByGroupName("supergroup");
		if(supergroup==null) {
			//不存在
			supergroup = new UserGroup();
			supergroup.setGroupName("supergroup");
			HashSet<Roles> superroles = new HashSet<Roles>();
			superroles.add(supermanager);
			supergroup.setRoles(superroles);
			supergroup.setCreateTime(new Date());
		}
		
		//初始化超级管理员
		User superuser = userSerivice.findUserByAccount("admin");
		 if(superuser==null) {
			 //不存在则创建
			 superuser = new User();
			 superuser.setUserId(1);
			 superuser.setAccount("admin");//超级管理员 账号
			 superuser.setPassword(MD5Util.getMD5("admin"));//超级管理员 密码
			 superuser.setEmail("yuyi@123.com");
			 superuser.setRealname("admin");
			 superuser.setCreateTime(new Date());
			 superuser.setUserGroup(supergroup);
			 superuser.setGender("男");
			 superuser.setPhone("1234567890");
			 //执行初始化
			 userSerivice.saveUser(superuser);
			 System.out.println("-->>初始化超级管理员:admin,密码:admin");
		 }else {
			 System.out.println("-->>超级管理员已存在");
		 }
	}

}
