package com.yuyisz.pis.ui.controller.security;

import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.yuyisz.pis.ui.module.security.DevResources;
import com.yuyisz.pis.ui.module.security.GroupField;
import com.yuyisz.pis.ui.module.security.Roles;
import com.yuyisz.pis.ui.module.security.UserGroup;
import com.yuyisz.pis.ui.service.security.GroupFieldService;
import com.yuyisz.pis.ui.service.security.RoleService;
import com.yuyisz.pis.ui.service.security.TResourceService;
import com.yuyisz.pis.ui.service.security.UserGroupService;

@Controller
@RequestMapping("/security")
public class GroupController {
	@Autowired
	private UserGroupService userGroupService;
	@Autowired
	private RoleService roleservice;
	@Autowired
	private GroupFieldService groupfieldService;
	@Autowired
	private TResourceService tResourceService;
	
	/*@RequiresPermissions("用户组界面列表")
	@RequestMapping(value="/grouplist")
	public String UserGroupList() {
		return "security/grouplist";
	}*/
	
	@RequiresPermissions("查询用户组列表")
	@RequestMapping("/findGroupLists")
	@ResponseBody
	public List<UserGroup> findGroupLists(){
		List<UserGroup> usergroups = userGroupService.findAllGroup();
		LinkedList<UserGroup> newgroups = new LinkedList<UserGroup>(usergroups);
		//取出超级管理组
		UserGroup supergroup = userGroupService.findByGroupName("supergroup");
		newgroups.remove(supergroup);
		return newgroups;
	}
	
	@RequiresPermissions("查询用户对应的用户组")
	@RequestMapping("/findGroupOfUser")
	@ResponseBody
	public UserGroup findGroupOfUser(@RequestParam(value="userId",required=false)int userId){
		return userGroupService.findGroupOfUser(userId);
	}
	
	@RequiresPermissions("删除某个用户组")
	@RequestMapping("/groupDeleted")
	@ResponseBody
	public String  deleteUserGroup(@RequestParam (value="id",required=false)int id) {
		userGroupService.deleteGroup(id);
		return "删除成功";
	}
	
	@RequiresPermissions("修改某个已有的用户组")
	@RequestMapping(value="/groupEdited")
	public String editUserGroup(ModelMap modelmap,@RequestParam(value="groupId")int groupId) {
		UserGroup group = userGroupService.findOneUserGroup(groupId);
		modelmap.addAttribute("group",group);
		return "security/groupEdited";
	}
	
	@RequiresPermissions("查看某个用户组信息")
	@RequestMapping(value="/groupView")
	public String viewUserGroup(ModelMap modelMap, @RequestParam(name="groupId") int groupId) {
		UserGroup group = userGroupService.findOneUserGroup(groupId);
		modelMap.addAttribute("group",group);
		return "security/groupView";
	}
	
	@RequiresPermissions("新增用户组并绑定对应的角色和管理域")
	@RequestMapping(value="/saveGroupInfo")
	@ResponseBody
	public String addGroup(@RequestBody Map<String,Object> reqMap) {
		String roleIds = reqMap.get("roleIds").toString();
		String[] ids = roleIds.split(",");
		Set<Roles> roles = new HashSet<Roles>();
		for (String id : ids) {
			Roles role = roleservice.findOneRoles(Integer.parseInt(id));
			roles.add(role);
		}
		
		UserGroup group = new UserGroup();
		group.setGroupName(reqMap.get("groupName").toString());
		group.setGroup_description(reqMap.get("group_description").toString());
		group.setCreateTime(new Date());
		group.setRoles(roles);
		UserGroup save = userGroupService.save(group);
		//维护管理域
		String groupfieldIds = reqMap.get("groupfieldIds").toString();
		String[] id2s = groupfieldIds.split(",");
		Set<GroupField> groupfields = new HashSet<GroupField>();
		for (String id : id2s) {
			if(id!=null) {
				DevResources resource = tResourceService.findOneResource(Integer.parseInt(id));
				GroupField groupfield = new GroupField();
				groupfield.setId1(resource.getId1());
				groupfield.setId2(resource.getId2());
				groupfield.setId3(resource.getId3());
				groupfield.setId4(resource.getId4());
				groupfield.setId5(resource.getId5());
				groupfield.setResourceType(resource.getResourceType());
				groupfield.setUserGroup(group);
				groupfieldService.save(groupfield);
			} 
		}
		return save==null?"保存失败":"保存成功";
	}
	
	@RequiresPermissions("更新用户组并更新对应的角色和管理域")
	@RequestMapping(value="/updateGroupInfo")
	@ResponseBody
	public String updateRoles(@RequestBody Map<String,Object> reqMap) {
		
		String roleIds = reqMap.get("roleIds").toString();
		String[] ids = roleIds.split(",");
		Set<Roles> roles = new HashSet<Roles>();
		for (String id : ids) {
			Roles role = roleservice.findOneRoles(Integer.parseInt(id));
			roles.add(role);
		}
		//取出usergroup,重新设置
		int groupId = Integer.parseInt(reqMap.get("groupId").toString());
		UserGroup group = userGroupService.findOneUserGroup(groupId);
		group.setGroupName(reqMap.get("groupName").toString());
		group.setGroup_description(reqMap.get("group_description").toString());
		group.setRoles(roles);
		group.setUpdateTime(new Date());
		UserGroup result = userGroupService.save(group);
		
		//删除旧的管理域
		groupfieldService.deleteByGroup(groupId);
		//增加新的管理域
		String groupfieldIds = reqMap.get("groupfieldIds").toString();
		String[] id2s = groupfieldIds.split(",");
		Set<GroupField> groupfields = new HashSet<GroupField>();
		for (String id : id2s) {
			if(id!=null) {
				DevResources resource = tResourceService.findOneResource(Integer.parseInt(id));
				GroupField groupfield = new GroupField();
				groupfield.setId1(resource.getId1());
				groupfield.setId2(resource.getId2());
				groupfield.setId3(resource.getId3());
				groupfield.setId4(resource.getId4());
				groupfield.setId5(resource.getId5());
				groupfield.setResourceType(resource.getResourceType());
				groupfield.setUserGroup(group);
				groupfieldService.save(groupfield);
			}
		}
		
		return result==null?"保存失败":"保存成功";
	}
	
	@RequiresPermissions("查询符合条件的用户组")
	@RequestMapping(value="/groupConditionSearch")
	@ResponseBody
	public List<UserGroup> conditionSearch(ModelMap modelMap,@RequestParam String nameFilter,@RequestParam String descriptionFilter) {
		List<UserGroup> group=userGroupService.findbyFilter(nameFilter,descriptionFilter);
		return group;
	}
	
	@RequiresPermissions("批量删除用户组")
	@RequestMapping("/deleteGroupsInBatches")
	@ResponseBody
	public String deleteGroupsInBatches(@RequestParam(value="id",required=false) String id) {
		if(id!=null&& !id.trim().equals("")) {
			String[] temp = id.split(",");
			for(String i : temp) {
				Integer groupId = Integer.valueOf(i);
				userGroupService.deleteGroup(groupId);;
			}
		}
		return "删除成功";
	}
	
}
