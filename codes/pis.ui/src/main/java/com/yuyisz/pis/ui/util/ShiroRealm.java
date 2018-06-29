package com.yuyisz.pis.ui.util;

import java.util.List;
import java.util.Set;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import com.yuyisz.pis.ui.module.security.Command;
import com.yuyisz.pis.ui.module.security.Roles;
import com.yuyisz.pis.ui.module.security.User;
import com.yuyisz.pis.ui.module.security.UserGroup;
import com.yuyisz.pis.ui.service.security.CmdService;
import com.yuyisz.pis.ui.service.security.RoleService;
import com.yuyisz.pis.ui.service.security.UserGroupService;
import com.yuyisz.pis.ui.service.security.UserService;
/**
 * 实现shiro的realm
 *
 */
public class ShiroRealm extends AuthorizingRealm{

	@Autowired
	private UserService userService;
	@Autowired
	private CmdService cmdService;
	@Autowired
	private UserGroupService groupService;
	@Autowired
	private RoleService roleService;
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		System.out.println("权限配置-->MyShiroRealm.doGetAuthorizationInfo()");
	    SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
	    User userInfo  = (User)principals.getPrimaryPrincipal();
	    if(userInfo.getAccount().equals("admin")) {//超级管理员
	    	userInfo.setUserGroup(groupService.findByGroupName("supergroup"));
	    }
	    
	    //添加权限和角色
	    	UserGroup userGroup = userInfo.getUserGroup();
	    	if((userGroup.getGroupName().equals("supergroup"))||(userGroup.getGroupName().equals("超级管理组"))) {
	    		//拥有所有权限和角色
	    		List<Roles> roles = roleService.findAllRoles();
	    		for (Roles role : roles) {
	    			authorizationInfo.addRole(role.getRoleName());
				}
		    	List<Command> permissions = cmdService.finAll();
		    	for (Command p : permissions) {
		    		authorizationInfo.addStringPermission(p.getCmdName());
				}
	    	}
	    	//普通人拥有的权限和角色
	    	List<Roles> roles = groupService.findRolesByGroupId(userGroup.getGroupId());
	    	for(Roles role:roles){
	    		authorizationInfo.addRole(role.getRoleName());
	    		for(Command p:role.getCmds()){
	    			authorizationInfo.addStringPermission(p.getCmdName());
	    		}
	    	}
	    
	    return authorizationInfo;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		 System.out.println("MyShiroRealm.doGetAuthenticationInfo()");
		    //获取用户的输入的账号.
		    String account = (String)token.getPrincipal();
		    System.out.println(token.getCredentials());
		    //通过username从数据库中查找 User对象，如果找到，没找到.
		    //实际项目中，这里可以根据实际情况做缓存，如果不做，Shiro自己也是有时间间隔机制，2分钟内不会重复执行该方法
		    User userInfo = userService.findUserByAccount(account);
		    System.out.println("----->>userInfo="+userInfo);
		    if(userInfo == null){
		        return null;
		    }
		    SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
		            userInfo, //用户名
		            userInfo.getPassword(), //密码
		            //ByteSource.Util.bytes(userInfo.getCredentialsSalt()),//salt=username+salt
		            getName()  //realm name
		    );
		    return authenticationInfo;
	}

}
