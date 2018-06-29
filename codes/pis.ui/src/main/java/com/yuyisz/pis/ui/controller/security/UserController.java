package com.yuyisz.pis.ui.controller.security;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.poifs.crypt.dsig.ExpiredCertificateSecurityException;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.mail.smtp.SMTPMessage;
import com.yuyisz.pis.ui.message.GeneralMsg;
import com.yuyisz.pis.ui.message.P2PMessageCommands;
import com.yuyisz.pis.ui.message.RabbitMqClient;
import com.yuyisz.pis.ui.message.Services;
import com.yuyisz.pis.ui.module.security.Roles;
import com.yuyisz.pis.ui.module.security.User;
import com.yuyisz.pis.ui.module.security.UserGroup;
import com.yuyisz.pis.ui.service.security.OperationLogService;
import com.yuyisz.pis.ui.service.security.UserGroupService;
import com.yuyisz.pis.ui.service.security.UserService;
import com.yuyisz.pis.ui.util.LogMessageUtil;
import com.yuyisz.pis.ui.util.MD5Util;
import com.yuyisz.pis.ui.vo.UserForm;
import com.yuyisz.pis.ui.vo.UserTreeVO;

@Controller
@RequestMapping("/security")
@EnableScheduling
public class UserController {
	@Autowired
	private UserService userService;
	@Autowired
	private UserGroupService userGroupService;
	@Autowired
	private OperationLogService logService;
	protected ObjectMapper mapper=new ObjectMapper();
	/*@RequiresPermissions("用户界面列表")
	@RequestMapping(value="/userlist")
	public String UserList() {
		return "security/userlist";
	}*/
	
	@RequestMapping("findUserTree")
	@ResponseBody
	public List<UserTreeVO> findUserTree(){
		List<User> users = userService.findAllUser();
		List<UserTreeVO> userTree = new ArrayList<UserTreeVO>();
		UserTreeVO vo = new UserTreeVO();
		vo.setId(10000);
		vo.setGroupId(10000);
		vo.setName("所有用户");
		vo.setOpen(true);
		vo.setPid(10000);
		vo.setType(0);
		userTree.add(vo);
		List<UserGroup> groups = new ArrayList<>();
		for(User user:users) {
			UserGroup group = user.getUserGroup();
			if(!groups.contains(group)) {
				groups.add(group);
				UserTreeVO vo1 = new UserTreeVO();
				vo1.setId(group.getGroupId()+10000);
				vo1.setGroupId(group.getGroupId());
				vo1.setName(group.getGroupName());
				vo1.setOpen(true);
				vo1.setPid(10000);
				vo1.setType(0);
				userTree.add(vo1);
			}
			UserTreeVO vo2 = new UserTreeVO();
			vo2.setId(user.getUserId());
			vo2.setGroupId(user.getUserGroup().getGroupId());
			vo2.setName(user.getRealname()+"/"+user.getAccount()+" Email: "+user.getEmail()+" Phone: "+user.getPhone());
			vo2.setOpen(false);
			vo2.setPid(user.getUserGroup().getGroupId()+10000);
			vo2.setType(1);
			vo2.setUserId(user.getUserId());
			userTree.add(vo2);
		}
		return userTree;
	}
	
	/*@RequiresPermissions("登录校验")*/
	@RequestMapping(value="/landingcheck")
	@ResponseBody
	public Boolean landingcheck(ModelMap modelMap,@RequestBody Map<String,Object> reqMap,HttpServletRequest request) throws MessagingException {
		String username = reqMap.get("username").toString();
		String password = reqMap.get("password").toString();
		User user = userService.findUserByAccount(username);//account唯一
		//提示文字
		String notify = "";
		//通过shiro安全框架测试是否授权登录成功
		Subject subject = SecurityUtils.getSubject();
		UsernamePasswordToken token = new UsernamePasswordToken(username, password);
		try {
			subject.login(token);
			assertEquals(true, subject.isAuthenticated());
//			subject.checkPermission("用户管理");
			//保存session
			request.getSession().setAttribute("cur_user", user); 
			notify = "登录成功!";
			//插入操作日志到数据库
			logService.save(LogMessageUtil.getOperationLog(request, null));
		}catch (IncorrectCredentialsException e) {
			notify="登录密码错误";
			System.out.println("登录密码错误,Password for account "+token.getPrincipal()+" was incorrent");
		}catch (ExcessiveAttemptsException e) {
			notify = "登录失败次数过多";
			System.out.println("登录失败次数过多");
		}catch (LockedAccountException e) {
			notify = "账号已被锁定";
			System.out.println("账号已被锁定.The account for uername "+token.getPrincipal()+"was locked");
		}catch (DisabledAccountException e) {
			notify = "账号已被禁用";
			System.out.println("账号已被禁用. The account for username "+token.getPrincipal()+"was disabled");
		}catch (ExpiredCertificateSecurityException e) {
			notify = "账号已过期";
			System.out.println("账号已过期 . The account for username "+token.getPrincipal()+"was expired");
		}catch (UnknownAccountException e) {
			notify = "账号不存在";
			System.out.println("账号不存在. There is no user with username of "+token.getPrincipal());
		}catch (UnauthenticatedException e) {
			System.out.println("您没有得到相应的授权!"+e.getMessage());
		}
		return notify.equals("登录成功!")?true:false;
	}
	
	@RequiresPermissions("查询用户列表")
	@RequestMapping(value="/findAllUsers")
	@ResponseBody
	public List<User> findUsers(){
		List<User> users = userService.findAllUser();
		LinkedList<User> newu = new LinkedList<User>(users);
		//取出超级管理员
		User admin = userService.findUserByAccount("admin");
		newu.remove(admin);
		return newu;
	}
	
	@RequiresPermissions("删除单个用户")
	@RequestMapping(value="/userDeleted")
	@ResponseBody
	public String deleteUser(@RequestParam(value="id",required=false) int id) {
		userService.deleteUser(id);
		return "删除成功";
	}
	
	@RequiresPermissions("编辑用户信息")
	@RequestMapping(value="/UserEdited")
	public String editUser(ModelMap modelMap,@RequestParam(name="userId") int userId) {
		User user = userService.findUserById(userId);
		modelMap.addAttribute("user",user);
		return "security/UserEdited";
	}
	
	@RequiresPermissions("查询单个用户详情")
	@RequestMapping(value="/UserView")
	public String viewUser(ModelMap modelMap,@RequestParam(name="userId") int userId) {
		User user = userService.findUserById(userId);
		modelMap.addAttribute("user",user);
		return "security/UserView";
	}
	
	@RequiresPermissions("新增新用户")
	@RequestMapping(value="/saveUserInfo")
	@ResponseBody
	public String addUser(@RequestBody Map<String,Object> reqMap) {
		User user = new User();
		String account = reqMap.get("account").toString();
		User findOne = userService.findUserByAccount(account);
		if(findOne!=null)return "用户名不能重复";
		
		user.setAccount(account);
		
		//密码加密
		String password=reqMap.get("password").toString();
		String encryptpasssord = MD5Util.getMD5(password);
		user.setPassword(encryptpasssord);
		
		user.setRealname(reqMap.get("realname").toString());
		user.setPhone(reqMap.get("phone").toString());
		user.setGender(reqMap.get("gender").toString());
		user.setEmail(reqMap.get("email").toString());
		user.setCreateTime(new Date());
		int groupId = Integer.parseInt(reqMap.get("groupId").toString());
		UserGroup group = userGroupService.findOneUserGroup(groupId);
		user.setUserGroup(group);
		User result = userService.saveUser(user);
		return result==null?"保存失败":"保存成功";
	}
	
	@RequiresPermissions("更新用户信息")
	@RequestMapping(value="/updateUserInfo")
	@ResponseBody
	public String updateUser(@RequestBody Map<String,Object> reqMap) {
		int userId = Integer.parseInt(reqMap.get("userId").toString());
		User user = userService.findUserById(userId);
		String account = reqMap.get("account").toString();
		user.setAccount(account);
		
		//密码加密
		String password=reqMap.get("password").toString();
		String encryptpasssord = MD5Util.getMD5(password);
		user.setPassword(encryptpasssord);
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/mm/dd hh:mm:ss");
		try {
			user.setCreateTime(sdf.parse(reqMap.get("createTime").toString()));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		user.setRealname(reqMap.get("realname").toString());
		user.setPhone(reqMap.get("phone").toString());
		user.setGender(reqMap.get("gender").toString());
		user.setEmail(reqMap.get("email").toString());
		int groupId = Integer.parseInt(reqMap.get("groupId").toString());
		UserGroup group = userGroupService.findOneUserGroup(groupId);
		user.setUserGroup(group);
		user.setUpdateTime(new Date());
		User result = userService.saveUser(user);
		return result==null?"保存失败":"保存成功";
	}
	
	@RequiresPermissions("查询符合条件的用户")
	@RequestMapping(value="/userConditionSearch")
	@ResponseBody
	public List<User> conditionSearch(ModelMap modelMap,@RequestParam String accountFilter,@RequestParam String realnameFilter) {
		List<User> user=userService.findbyFilter(accountFilter,realnameFilter);
		return user;
	}
	
	@RequiresPermissions("批量删除用户")
	@RequestMapping("/deleteUsersInBatches")
	@ResponseBody
	public String deleteUsersInBatches(@RequestParam(value="id",required=false) String id) {
		if(id!=null&& !id.trim().equals("")) {
			String[] temp = id.split(",");
			for(String i : temp) {
				Integer userId = Integer.valueOf(i);
				userService.deleteUser(userId);;
			}
		}
		return "删除成功";
	}
	
	/*@RequestMapping("/userlist")
	public String list(Model model) {
		model.addAttribute("title", "用户管理");
		model.addAttribute("right_main","/security/users/userlist");
		model.addAttribute("users", userService.findAllUser());
		return "base";
	}
	
	
	
	@RequestMapping("/userview")
	public ModelAndView userview(@RequestParam(value="userId",defaultValue="0",required=false) int userId) {
		ModelAndView model = new ModelAndView("base");
		model.addObject("title", "新增用户");
		model.addObject("right_main","/security/users/userview");
		UserForm userForm = new UserForm();
		User user = new User();
		if(userId !=0 ) {
			//判断是否传入userId,传入是修改，否则是新建
			user = userService.findUserById(userId);
			userForm.setSeletedGroup(user.getGroupId());
		}
		userForm.setUser(user);
		List<UserGroup> userGroups = userGroupService.findAllGroup();
		model.addObject("userForm", userForm);
		model.addObject("userGroups", userGroups);
		return model;
	}
	@RequestMapping(value="/useraddorupdate",method=RequestMethod.POST)
	public String useradd(@ModelAttribute(value="userForm") UserForm userForm) {
		//发送保存消息
		System.out.println("新增或修改用户，"+userForm.getUser().getGender()+"用户名称="+userForm.getUser().getRealname());
		User user = userForm.getUser();
		user.setUserGroup(userGroupService.findOneUserGroup(userForm.getSeletedGroup()));
		userService.saveUser(user);
		return "redirect:userlist";
	}
	
	@RequestMapping(value="/userdelete",method=RequestMethod.POST)
	@ResponseBody
	public Callable<String> userdelete(@RequestParam("userId") int userId) {
		
		return new Callable<String>() {
			
			@Override
			public String call() throws Exception {
				// TODO 执行远程消息发送，等待返回
				System.out.println("执行远程消息发送，等待返回");
				Thread.sleep(2000);
				String msgBody = "Hello UserManage";
				GeneralMsg message = GeneralMsg.makeCommandRequestMsg(Services.SECURITYMANAGEMENTSERVICE.getServiceName(),
						P2PMessageCommands.SECURITY_USERMANAGE_LIST.getMsgCommand(), 
						msgBody, true, 0);
				RabbitMqClient client = new RabbitMqClient();
				GeneralMsg resp = client.send(message);
				client.close();
				System.out.println("执行远程消息发送，获得返回");
				System.out.println("返回消息是："+resp.getBody().getMessageBody());
				return "删除成功";
			}
		};
		System.out.println("删除用户，ID="+userId);
		return "删除成功";
	}*/
}
