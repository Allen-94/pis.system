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
import com.yuyisz.pis.ui.module.security.GroupField;
import com.yuyisz.pis.ui.module.security.Roles;
import com.yuyisz.pis.ui.module.security.User;
import com.yuyisz.pis.ui.module.security.UserGroup;
import com.yuyisz.pis.ui.service.security.CmdService;
import com.yuyisz.pis.ui.service.security.CmdsetService;
import com.yuyisz.pis.ui.service.security.GroupFieldService;
import com.yuyisz.pis.ui.service.security.RoleService;
import com.yuyisz.pis.ui.service.security.UserGroupService;

import pis.ui.com.yuyisz.pis.ui.service.TestBase;

public class GroupFieldServiceTest extends TestBase {
	
	@Autowired
	private UserGroupService usergroupService;
	@Autowired
	private GroupFieldService groupfieldService;
	private List<GroupField> groupfields = new ArrayList<GroupField>();
	
	@SuppressWarnings("unchecked")
	@Before
	public void setUp() throws Exception {
		/*UserGroup group = new UserGroup();
		group.setGroupName("superuser");
		group.setDescription("超级管理员");
		usergroups.add(group);
		mapper.writeValue(new File("D:\\work\\resp\\pis100\\trunk\\pis.ui\\src\\test\\java\\usergroups.json"), usergroups);*/
		String ss = "";
		ss = "C:\\Users\\郭芝辰\\Documents\\workspace-sts-3.9.1.RELEASE\\pis.ui\\src\\test\\java\\groupfields.json";
		groupfields  = mapper.readValue(new File(ss),
				mapper.getTypeFactory().constructParametricType(List.class, GroupField.class));
		UserGroup supergroup = usergroupService.findOneUserGroup(39);
		for(GroupField groupfield:groupfields) {
			
			groupfield.setUserGroup(supergroup);
			groupfieldService.save(groupfield);
		}
		
		//测试cmds_roles表在什么情况下才生成
		//1.创建role对象时关联已有的cmds组对象,失败
		//2.创建cmd对象关联role对象
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
