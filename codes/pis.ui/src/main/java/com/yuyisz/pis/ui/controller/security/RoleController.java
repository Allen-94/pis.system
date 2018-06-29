package com.yuyisz.pis.ui.controller.security;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jackson.JsonObjectDeserializer;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yuyisz.pis.ui.module.security.Command;
import com.yuyisz.pis.ui.module.security.CommandSet;
import com.yuyisz.pis.ui.module.security.Roles;
import com.yuyisz.pis.ui.service.security.CmdService;
import com.yuyisz.pis.ui.service.security.CmdsetService;
import com.yuyisz.pis.ui.service.security.RoleService;

@Controller
@RequestMapping("/security")
public class RoleController {
	@Autowired
	private RoleService roleService;
	@Autowired
	private CmdsetService cmdsetService;
	@Autowired
	private CmdService cmdService;
	
	/*@RequiresPermissions("角色界面列表")
	@RequestMapping(value="/rolelist")
	public String RoleList() {
		return "security/rolelist";
	}*/

	@RequiresPermissions("查询角色列表")
	@RequestMapping(value = "/findAllRoles")
	@ResponseBody
	public List<Roles> findRoles() {
		List<Roles> roles = roleService.findAllRoles();
		LinkedList<Roles> newroles = new LinkedList<Roles>(roles);
		//取出超级管理员
		Roles supermanager = roleService.findByRoleName("supermanager");
		newroles.remove(supermanager);
		return newroles;
	}
	
	@RequiresPermissions("查询所有角色")
	@RequestMapping(value = "/EditRolesofGroup")
	@ResponseBody
	public String EditRolesofGroup(@RequestParam(value="groupId",required=false)int groupId) {
		List<String> lstroles = new ArrayList<String>();
		List<Roles> roles = roleService.findAllRoles();
		List<Roles> rolesOfGroup = roleService.findRolesOfGroup(groupId);
		List<Integer> ids = new ArrayList<Integer>();
		for (Roles role : rolesOfGroup) {
			ids.add(role.getRoleId());
		}
		StringBuffer s = new StringBuffer();
		s.append("[");
		for (Roles role : roles) {
				s.append("{\"roleId\" :"+role.getRoleId()+",\"roleName\":"+ "\"" +role.getRoleName()+ "\"" +",\"role_description\":"+ "\"" +role.getRole_description()+ "\"");
			if(ids.contains(role.getRoleId())){
				s.append(",\"checked\":\"checked\"},");
			}else {
				s.append(",\"checked\":\"\"},");
			}
		}
		String substring = s.substring(0, s.length()-1);
		substring +="]";
//		System.out.println(lstroles);
		return substring.toString();
	}

	@RequiresPermissions("删除某个角色")
	@RequestMapping("/roleDeleted")
	@ResponseBody
	public String deleteRoles(HttpServletRequest request) {
		int parameter = Integer.parseInt(request.getParameter("roleId"));
		System.out.println("-->>deleteRoles:"+parameter);
		roleService.deleteRoles(parameter);
		return "删除成功";
	}

	@RequiresPermissions("进入编辑角色页面")
	@RequestMapping(value = "/roleEdited")
	public String editRoles(ModelMap modelMap, @RequestParam(name = "roleId") int roleId) {
//		System.out.println(roleId);
		Roles role = roleService.findOneRoles(roleId);
//		System.out.println(role.getRoleId());
		modelMap.addAttribute("role", role);
		return "security/roleEdited";
	}

	@RequiresPermissions("查看某个角色信息")
	@RequestMapping(value = "/roleView")
	public String viewRoles(ModelMap modelMap, @RequestParam(name = "roleId") int roleId) {
		Roles role = roleService.findOneRoles(roleId);
		modelMap.addAttribute("role", role);
		return "security/roleView";
	}

	@RequiresPermissions("新增角色")
	@RequestMapping(value = "/saveRolesInfo")
	@ResponseBody
	public String addRoles(@RequestBody Map<String,Object> reqMap) {
		Roles role = new Roles();
		role.setRoleName(reqMap.get("roleName").toString());
		role.setRole_description(reqMap.get("role_description").toString());
		String ids = reqMap.get("ids").toString();
		String[] d = ids.split(",");
		Set<Command> cmds = new HashSet<Command>();
		for (String id : d) {
			System.out.println("id="+id);
			if(id!=null)
			cmds.add(cmdService.findOneCmd(Integer.parseInt(id)));
		}
		role.setCreateTime(new Date());
		role.setCmds(cmds);
		Roles result = roleService.saveRoles(role);
		return result == null ? "保存失败" : "保存成功";
	}

	@RequiresPermissions("更新角色")
	@RequestMapping(value = "/updateRolesInfo")
	@ResponseBody
	public String updateRoles(@RequestBody Map<String,Object> reqMap) {
		int roleId = Integer.parseInt(reqMap.get("roleId").toString());
		String cmdIds = reqMap.get("cmdIds").toString();
		String[] ids = cmdIds.split(",");
		Set<Command> cmds = new HashSet<Command>();
		for (String id : ids) {
			if(id!=null)
				cmds.add(cmdService.findOneCmd(Integer.parseInt(id)));
		}
		Roles role = roleService.findOneRoles(roleId);
		role.setUpdateTime(new Date());
		role.setRoleName(reqMap.get("roleName").toString());
		role.setRole_description(reqMap.get("role_description").toString());
		role.setCmds(cmds);
		Roles result = roleService.saveRoles(role);
		return result == null ? "保存失败" : "保存成功";
	}

	@RequiresPermissions("查询符合条件的角色")
	@RequestMapping(value = "/roleConditionSearch")
	@ResponseBody
	public List<Roles> conditionSearch(ModelMap modelMap, @RequestParam String nameFilter,
			@RequestParam String descriptionFilter) {
//		System.out.println("conditionSearch");
		List<Roles> roles = roleService.findbyFilter(nameFilter, descriptionFilter);
		return roles;
	}

	@RequiresPermissions("批量删除角色")
	@RequestMapping("/deleteRolesInBatches")
	@ResponseBody
	public String deleteRolesInBatches(@RequestParam(value = "id", required = false) String id) {
		if (id != null && !id.trim().equals("")) {
			String[] temp = id.split(",");
			for (String i : temp) {
				Integer roleId = Integer.valueOf(i);
				roleService.deleteRoles(roleId);
			}
		}
		return "删除成功";
	}

	//权限管理
	@RequiresPermissions("查询权限列表")
	@RequestMapping(value = "/ShowAllAuthority")
	@ResponseBody
	public List<String> ShowAllAuthority(@RequestParam(value="roleId",required=false,defaultValue="-1")int roleId) {
		
		List<Integer> ids = new ArrayList<Integer>();
		if(roleId!=-1) {
			List<Command> allCmdsofRole = cmdService.findAllCmdsBelongToRole(roleId);
			for (Command command : allCmdsofRole) {
				ids.add(command.getCmdId());
			}
		}
		
		List<String> lstTree = new ArrayList<String>();
		List<CommandSet> list = cmdsetService.findAll();
		lstTree.add("{id:" + 1 + ",pId:" + 0 + ",name:" + "\"" + "权限" + "\"" + ",open:true}");
		int secondtreeid=cmdService.finAll().size()+1;//为了保证树id的唯一性,重新设置cmdset节点的id实在cmds的总数基础上依次加一
		for (CommandSet commandSet : list) {
			lstTree.add("{id:" + secondtreeid + ",pId:" + 1 + ",name:" + "\"" + commandSet.getCmdsetName()
					+ "\"" + "}");
			Set<Command> findByCmdsetid = cmdService.findByCmdsetid(commandSet.getCmdsetId());
			if(findByCmdsetid!=null) {
				List<Command> cmds = new ArrayList<Command>(findByCmdsetid);
				for (Command command : cmds) {
					if(ids.contains(command.getCmdId())) {
						lstTree.add("{id:" + command.getCmdId() + ",pId:" + secondtreeid + ",name:" + "\"" + command.getCmdName()
						+ "\"" + ",checked:true}");
					}else {
						lstTree.add("{id:" + command.getCmdId() + ",pId:" + secondtreeid + ",name:" + "\"" + command.getCmdName()
						+ "\"" + "}");
						
					}
				}
			}
			secondtreeid++;//为下一个cmdset节点赋值
		}
//		System.out.println(lstTree);
		return lstTree;
	}
	
	@RequiresPermissions("查询某个用户名下的所有权限")
	@RequestMapping(value = "/findRoleAuthority")
	@ResponseBody
	public List<String> findRoleAuthority(@RequestParam(value = "roleId", required = false) int roleId) {
		List<String> lstTree = new ArrayList<String>();
		List<CommandSet> list = cmdsetService.findCmdsetsBelongToRole(roleId);
		lstTree.add("{id:" + 1 + ",pid:" + 0 + ",name:" + "\"" + "权限" + "\"" + ",open:true}");
		int secondtreeid=cmdService.finAll().size()+1;//为了保证树id的唯一性,重新设置cmdset节点的id实在cmds的总数基础上依次加一
		for (CommandSet commandSet : list) {
			lstTree.add("{id:" + secondtreeid + ",pId:" + 1 + ",name:" + "\"" + commandSet.getCmdsetName()
					+ "\"" + "}");
			List<Command> cmds = cmdService.findCmdsBelongTo_RoleCmdset(roleId,commandSet.getCmdsetId());
			if(cmds!=null) {
				for (Command command : cmds) {
					lstTree.add("{id:" + command.getCmdId() + ",pId:" + secondtreeid + ",name:" + "\"" + command.getCmdName()
					+ "\"" + "}");
				}
			}
			secondtreeid++;//为下一个cmdset节点赋值
		}
//		System.out.println(lstTree);
		return lstTree;
	}
	
	/*@RequiresPermissions("查询用户组所属的角色")*/
	@RequestMapping(value="/findRolesOfGroup")
	@ResponseBody
	public List<Roles> findRolesOfGroup (@RequestParam(value="groupId",required=false,defaultValue="-1")int groupId){
		List<Roles> list = new ArrayList<Roles>();
		if(groupId!=-1) {
			list = roleService.findRolesOfGroup(groupId);
		}
		return list;
	}
}
