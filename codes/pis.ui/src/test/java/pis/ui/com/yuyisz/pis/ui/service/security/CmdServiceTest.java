package pis.ui.com.yuyisz.pis.ui.service.security;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yuyisz.pis.ui.module.security.Command;
import com.yuyisz.pis.ui.module.security.CommandSet;
import com.yuyisz.pis.ui.module.security.Roles;
import com.yuyisz.pis.ui.module.security.User;
import com.yuyisz.pis.ui.module.security.UserGroup;
import com.yuyisz.pis.ui.service.security.CmdService;
import com.yuyisz.pis.ui.service.security.CmdsetService;
import com.yuyisz.pis.ui.service.security.RoleService;
import com.yuyisz.pis.ui.service.security.UserGroupService;

import pis.ui.com.yuyisz.pis.ui.service.TestBase;

public class CmdServiceTest extends TestBase {
	
	@Autowired
	private CmdService cmdService;
	private List<Command> cmds = new ArrayList<Command>();
	@Autowired
	private CmdsetService cmdsetService;
	@Autowired
	private RoleService roleService;
	
	@SuppressWarnings("unchecked")
	/*@Before*/
	public void setUp() throws Exception {
		/*UserGroup group = new UserGroup();
		group.setGroupName("superuser");
		group.setDescription("超级管理员");
		usergroups.add(group);
		mapper.writeValue(new File("D:\\work\\resp\\pis100\\trunk\\pis.ui\\src\\test\\java\\usergroups.json"), usergroups);*/
		String ss = "";
		ss = "C:\\Users\\郭芝辰\\Documents\\workspace-sts-3.9.1.RELEASE\\pis.ui\\src\\test\\java\\cmd\\securityRequestment.json";
		cmds = mapper.readValue(new File(ss),
				mapper.getTypeFactory().constructParametricType(List.class, Command.class));
		CommandSet cmdset= cmdsetService.findOneCmdset(186);
		
		for(Command cmd:cmds) {
			cmd.setCmdSet(cmdset);
			cmdService.save(cmd);
		}
	}

	@Test
	public void testSaveUserGroup() {
	/*	UserGroup group = new UserGroup();
		group.setGroupName("superuser");
		group.setGroup_description("超级管理员");
		UserGroup result = userGroupService.save(group);
		assertTrue(result.getGroupName().equals(group.getGroupName()));*/
		/* cmdService.finAll();*/
		/*String url = cmd.getUrl();*/
	}
	/*@Test
	public void getUrl() {
		Command cmd = cmdService.findOneCmd(123);
		String url = cmd.getUrl();
		System.out.println(url);
	}
	@Test
	public void testgetPermission() {
		List<Command> findPermissions = cmdService.findPermissions(198);
		System.out.println(findPermissions.size());
	}
	*/
}
