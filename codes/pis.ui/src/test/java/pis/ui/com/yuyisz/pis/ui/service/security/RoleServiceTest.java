package pis.ui.com.yuyisz.pis.ui.service.security;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yuyisz.pis.ui.dao.security.RoleRepository;
import com.yuyisz.pis.ui.module.security.Command;
import com.yuyisz.pis.ui.module.security.Roles;
import com.yuyisz.pis.ui.module.security.UserGroup;
import com.yuyisz.pis.ui.service.security.CmdService;
import com.yuyisz.pis.ui.service.security.RoleService;
import pis.ui.com.yuyisz.pis.ui.service.TestBase;
public class RoleServiceTest extends TestBase {
	
	@Autowired
	private RoleService roleService;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private CmdService cmdService;
	private List<Roles> roles = new ArrayList<Roles>();
	private Set<UserGroup> usergroups = new HashSet<UserGroup>();
	private List<Command> list = new ArrayList<Command>();
	//@Before
	/*public void setUp() throws Exception {
		UserGroup group = new UserGroup();
		group.setGroupName("superuser");
		group.setDescription("超级管理员");
		usergroups.add(group);
		mapper.writeValue(new File("D:\\work\\resp\\pis100\\trunk\\pis.ui\\src\\test\\java\\usergroups.json"), usergroups);
		roles = mapper.readValue(new File("C:\\Users\\郭芝辰\\Documents\\workspace-sts-3.9.1.RELEASE\\pis.ui\\src\\test\\java\\roles.json"),
				mapper.getTypeFactory().constructParametricType(List.class, Roles.class));
		String ss = "";
		Set<Command> cmds = null;
		
		for(Roles role:roles) {
//			System.out.println("roleName:"+role.getRoleName());
			if(role.getRoleName().equals("超级管理员")) {
				list = cmdService.finAll();
				cmds = new HashSet<Command>(list);
			}else if(role.getRoleName().equals("设备管理员")) {
				cmds = cmdService.findByCmdsetid(183);
			}else if(role.getRoleName().equals("性能管理人员")) {
				cmds = cmdService.findByCmdsetid(185);
			}else if(role.getRoleName().equals("业务管理人员")) {
				cmds = cmdService.findByCmdsetid(182);
			}else if(role.getRoleName().equals("告警管理人员")) {
				cmds = cmdService.findByCmdsetid(184);
			}else if(role.getRoleName().equals("安全管理人员")) {
				cmds = cmdService.findByCmdsetid(186);
			}
			role.setCmds(cmds);
			role.setCreateTime(new Date());
			roleService.saveRoles(role);
		}
	}*/

	@Test
	public void testSaveRoles() {
	/*	Roles role = new Roles();
		role.setRoleName("superuser");
		role.setRole_description("超级管理员");
		role.setCmds(null);
		role.setCreateTime(new Date());
		Roles result = roleService.saveRoles(role);
		assertTrue(result.getRoleName().equals(role.getRoleName()));*/
	}
	
	/*@Test*/
	public void testfindGrouplist() {
//		List<UserGroup> findAllGroup = userGroupService.findAllGroup();
//		System.out.println(findAllGroup.size());
	}
	
	@Test
	public void testDel() {
		roleRepository.delete(205);
	}

}
