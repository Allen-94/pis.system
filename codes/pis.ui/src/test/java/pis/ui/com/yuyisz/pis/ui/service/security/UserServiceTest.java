package pis.ui.com.yuyisz.pis.ui.service.security;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.JavaType;
import com.yuyisz.pis.ui.module.security.Command;
import com.yuyisz.pis.ui.module.security.User;
import com.yuyisz.pis.ui.module.security.UserGroup;
import com.yuyisz.pis.ui.service.security.UserGroupService;
import com.yuyisz.pis.ui.service.security.UserService;
import com.yuyisz.pis.ui.util.MD5Util;

import pis.ui.com.yuyisz.pis.ui.service.TestBase;

public class UserServiceTest extends TestBase {

	@Autowired
	private UserService userService;
	@Autowired
	private UserGroupService userGroupService;
	private List<User> users = new ArrayList<User>();
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	/*@Before*/
	public void setUp() throws Exception {
		
		/*User user = new User();
		user.setAccount("张三");
		user.setEmail("1232@qq.com");
		user.setGender("男");
		user.setPassword("123456");
		user.setPhone("123456789");
		user.setRealname("张三");
		user.setCreateTime(new Date());
		users.add(user);
		mapper.writeValue(new File("D:\\work\\resp\\pis100\\trunk\\pis.ui\\src\\test\\java\\users.json"), users);*/
		/*JavaType javaType = mapper.getTypeFactory().constructParametricType(  
	                List.class, User.class);  
		 users = mapper.readValue(new File("C:\\Users\\郭芝辰\\Documents\\workspace-sts-3.9.1.RELEASE\\pis.ui\\src\\test\\java\\users.json"), javaType);
		 UserGroup group = userGroupService.findOneUserGroup(91);
		 for (User user : users) {
			 user.setUserGroup(group);
			 userService.saveUser(user);
		 }*/
	}

	/**
	 * 测试用户保存
	 */
	@Test
	//@Transactional
	//@Rollback(true)
	public void testSaveUser() {
		/*User user = new User();
		user.setAccount("mike");
		user.setEmail("1232@qq.com");
		user.setGender("男");
		user.setPassword(MD5Util.convertMD5("123456"));
		user.setPhone("123456789");
		user.setRealname("张三");
		user.setCreateTime(new Date());
		user.setUserGroup(userGroupService.findOneUserGroup(2));
		User result = userService.saveUser(user);
		assertTrue(result.getAccount().equals(user.getAccount()));*/
		
		User user = userService.findUserByAccount("root");
		System.out.println("ps "+user.getPassword()+" nos "+MD5Util.convertMD5(user.getPassword()));
	}
	/**
	 * 测试根据Id查找用户
	 */
	/*@Test*/
	@Transactional
	@Rollback(true)
	public void testFindUserById() {
		User user = new User();
		user.setAccount("测试");
		user.setEmail("1232@qq.com");
		user.setGender("男");
		user.setPassword("123456");
		user.setPhone("123456789");
		user.setRealname("张三");
		User result = userService.saveUser(user);
		User uu = userService.findUserById(result.getUserId());
		assertTrue(uu.getUserId()==result.getUserId());
	}
	/**
	 * 测试分页查找所有用户
	 */
	@Test
	@Transactional
	@Rollback(true)
	public void testFindAllUserPage() {
		User user = new User();
		user.setGender("男");
		Page<User> result = userService.findAllUserByExample(0, 2,new String[] {"account"} , user);
		assertTrue(result.getContent().size()==2);
	}
	/**
	 * 测试给用户设置用户组
	 */
	@Test
	@Transactional
	@Rollback(true)
	public void testSetUserGroup() {
		UserGroup group = new UserGroup();
		group.setGroupName("superuser");
		group.setGroup_description("超级管理员");
		UserGroup result = userGroupService.save(group);
		
		User user = new User();
		user.setAccount("测试");
		user.setEmail("1232@qq.com");
		user.setGender("男");
		user.setPassword("123456");
		user.setPhone("123456789");
		user.setRealname("张三");
		User resultU = userService.saveUser(user);
		
		resultU.setUserGroup(result);
		User resultU2 = userService.saveUser(resultU);
		assertTrue(resultU2.getUserGroup().getGroupId()==result.getGroupId());
	}
	/**
	 * 测试给用户设置清除告警列表
	 */
	@Test
	@Transactional
	@Rollback(true)
	public void testAddCleanAlarm() {
		fail("Not yet implemented");
	}
	/**
	 * 测试给用户设置确认告警列表
	 */
	@Test
	@Transactional
	@Rollback(true)
	public void testAddConfirmAlarm() {
		fail("Not yet implemented");
	}
}
