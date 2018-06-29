package pis.ui.com.yuyisz.pis.ui.service.security;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.management.relation.Role;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import com.yuyisz.pis.ui.module.security.GroupField;
import com.yuyisz.pis.ui.module.security.Roles;
import com.yuyisz.pis.ui.module.security.User;
import com.yuyisz.pis.ui.module.security.UserGroup;
import com.yuyisz.pis.ui.service.security.GroupFieldService;
import com.yuyisz.pis.ui.service.security.RoleService;
import com.yuyisz.pis.ui.service.security.UserGroupService;
import com.yuyisz.pis.ui.service.security.UserService;

import pis.ui.com.yuyisz.pis.ui.service.TestBase;

public class UserGroupServiceTest extends TestBase {
	@Autowired
	private UserService userService;
	@Autowired
	private UserGroupService userGroupService;
	@Autowired
	private RoleService roleService;
	@Autowired
	private GroupFieldService groupFieldService;
	
	private List<UserGroup> usergroups = new ArrayList<UserGroup>();
	private Set<GroupField> groupfields = new HashSet<GroupField>();
	private Set<Roles> roles = new HashSet<Roles>();
	
	@Before
	public void setUp() throws Exception {
		/*UserGroup group = new UserGroup();
		group.setGroupName("superuser");
		group.setDescription("超级管理员");
		usergroups.add(group);*/
		/*mapper.writeValue(new File("D:\\work\\resp\\pis100\\trunk\\pis.ui\\src\\test\\java\\usergroups.json"), usergroups);*/
		usergroups = mapper.readValue(new File("C:\\Users\\郭芝辰\\Documents\\workspace-sts-3.9.1.RELEASE\\pis.ui\\src\\test\\java\\usergroups.json"),
				mapper.getTypeFactory().constructParametricType(List.class, UserGroup.class));
		Set<Roles> roles = null;
		for(UserGroup group:usergroups) {
			if(group.getGroupName().equals("超级管理组")) {
				List<Roles> findAllRoles = roleService.findAllRoles();
				roles = new HashSet<Roles>(findAllRoles);
			}else if(group.getGroupName().equals("播控管理组")) {
				Roles role = roleService.findOneRoles(190);
				roles = new HashSet<Roles>();
				roles.add(role);
			}else if(group.getGroupName().equals("设备管理组")) {
				Roles role = roleService.findOneRoles(188);
				roles = new HashSet<Roles>();
				roles.add(role);
			}else if(group.getGroupName().equals("设备监控组")) {
				roles = new HashSet<Roles>();
				Roles role = roleService.findOneRoles(189);
				Roles role2 = roleService.findOneRoles(191);
				roles.add(role);
				roles.add(role2);
			}else if(group.getGroupName().equals("安全管理组")) {
				roles = new HashSet<Roles>();
				Roles role = roleService.findOneRoles(192);
				roles.add(role);
			}
			group.setCreateTime(new Date());
			group.setRoles(roles);
			userGroupService.save(group);
		}
	}

	@Test
	public void testSaveUserGroup() {
		/*UserGroup group = new UserGroup();
		group.setGroupName("superuser");
		group.setRoles(new HashSet<Roles>());
		group.setGroup_description("超级管理员");*/
		/*group.setRoles(roles);*/
		/*UserGroup result = userGroupService.save(group);*/
		/*UserGroup group= userGroupService.findOneUserGroup(47);
		Roles roles = new Roles();
//		roles.setRoleId(1);
		roles.setRoleName("roleTesting");
		roles.setRole_description("测试角色");
		roles.setUserGroups(new HashSet<UserGroup>());
		roles.getUserGroups().add(group);
		roleService.saveRoles(roles);*/
//		assertTrue(result.getGroupName().equals(group.getGroupName()));
	}
	
	/*@Test*/
	public void testfindGrouplist() {
		List<UserGroup> findAllGroup = userGroupService.findAllGroup();
		System.out.println(findAllGroup.size());
	}

}
