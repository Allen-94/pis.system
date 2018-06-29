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
import com.yuyisz.pis.ui.module.security.User;
import com.yuyisz.pis.ui.module.security.UserGroup;
import com.yuyisz.pis.ui.service.security.CmdService;
import com.yuyisz.pis.ui.service.security.CmdsetService;
import com.yuyisz.pis.ui.service.security.UserGroupService;

import pis.ui.com.yuyisz.pis.ui.service.TestBase;

public class CmdSetServiceTest extends TestBase {
	@Autowired
	private CmdService cmdService;
	@Autowired
	private CmdsetService cmdsetService;
	private List<CommandSet> cmdSets = new ArrayList<CommandSet>();
	private Set<Command> cmds = new HashSet<Command>();

	@Before 
	public void setUp() throws Exception {
		/*
		 * UserGroup group = new UserGroup(); group.setGroupName("superuser");
		 * group.setDescription("超级管理员"); usergroups.add(group); mapper.writeValue(new
		 * File(
		 * "D:\\work\\resp\\pis100\\trunk\\pis.ui\\src\\test\\java\\usergroups.json"),
		 * usergroups);
		 */
		cmdSets = mapper.readValue(new File(
				"C:\\Users\\郭芝辰\\Documents\\workspace-sts-3.9.1.RELEASE\\pis.ui\\src\\test\\java\\commandsets.json"),
				mapper.getTypeFactory().constructParametricType(List.class, CommandSet.class));
		
		for (CommandSet cmdset : cmdSets) {
			cmdsetService.save(cmdset);
		}
	}

	@Test
	public void testSaveUserGroup() {
	/*	UserGroup group = new UserGroup();
		group.setGroupName("superuser");
		group.setGroup_description("超级管理员");
		UserGroup result = userGroupService.save(group);
		assertTrue(result.getGroupName().equals(group.getGroupName()));*/
	}

	

}
