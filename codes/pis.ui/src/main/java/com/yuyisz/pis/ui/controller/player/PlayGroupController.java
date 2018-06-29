package com.yuyisz.pis.ui.controller.player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yuyisz.pis.ui.module.player.PlayerGroup;
import com.yuyisz.pis.ui.module.security.DevResources;
import com.yuyisz.pis.ui.module.security.User;
import com.yuyisz.pis.ui.service.player.PlayerGroupService;
import com.yuyisz.pis.ui.service.security.DevResourceService;

@Controller
@RequestMapping("/playercontrol")
public class PlayGroupController {
	@Autowired
	private PlayerGroupService playerGroupService;
	@Autowired
	private DevResourceService devResourceService;
	/**
	 * 查询所有分组
	 * @return
	 */
	@RequestMapping("/findAll")
	@ResponseBody
	public List<PlayerGroup> findAllPlayerGroups() {
		List<PlayerGroup> results =  playerGroupService.findAll();
		return results;
	}
	@GetMapping("/check")
	@ResponseBody
	public boolean checkGroupNameUnique(@RequestParam(value="id",required=false,defaultValue="0")long groupId,
			@RequestParam("groupName")String groupName) {
		PlayerGroup group = null;
		if(groupId == 0) {
			 group = playerGroupService.findByGroupName(groupName);
		}else {
			 group = playerGroupService.findByGroupNameAndNotId(groupName,groupId);
		}
		if(group == null) {
			return true;
		}else {
			return false;
		}
	}
	
	@RequestMapping("/addPlayerGroup")
	@ResponseBody
	public Map<String,Object> addPlayerGroup(@RequestParam(value="id",required=false,defaultValue="0")long groupId,
			@RequestParam("groupName")String groupName,
			@RequestParam("description")String description,
			@RequestParam("playerids")String playerids){
		Map<String,Object> result = new HashMap<>();
		boolean flag = false;
		String message="";
		if(playerids == null || playerids.trim().equals("")) {
			message = "包含的播控器不能为空！";
		}else {
			try {
			String[] ids = playerids.split(",");
			List<Integer> lIds = new ArrayList<>();
			for(String id:ids) {
				lIds.add(Integer.valueOf(id));
			}
			PlayerGroup group = null;
			if(groupId == 0) {
				group = new PlayerGroup();
			}else {
				group = playerGroupService.findById(groupId);
			}
			if(group == null) {
				message = "要更新的分组不存在！";
			}else {
				group.setGroupName(groupName);
				group.setDescription(description);
				group.setCreateDate(new Date());
				Subject subject = SecurityUtils.getSubject();
				group.setCreater((User) subject.getPrincipal());
				List<DevResources> player = devResourceService.findPlayByIds(lIds);
				Set<DevResources> playerSet = new HashSet<>(player);
				group.setPlayers(playerSet);
				playerGroupService.savePlayerGroup(group);
				flag = true;
				message = "新增播控分组成功！";
			}
			
			}catch(Exception e) {
				message="新增播控分组异常！\n"+e.getMessage();
			}
		}
		result.put("flag", flag);
		result.put("message", message);
		return result;
	}
	@RequestMapping("/delPlayerGroup/{id}")
	@ResponseBody
	public Map<String,Object> delPlayerGroup(@PathVariable(value="id")long playerId){
		Map<String,Object> result = new HashMap<>();
		boolean flag = false;
		String message="";
		try {
		boolean del = playerGroupService.delPlayerGroupById(playerId);
		if(del) {
			flag = true;
			message = "删除播控分组成功！";
		}else{
			message = "删除播控分组失败！";
		}
		}catch(Exception e) {
			message="删除播控分组异常！\n"+e.getMessage();
		}
		result.put("flag", flag);
		result.put("message", message);
		return result;
	}
	
	@RequestMapping("/findById/{id}")
	@ResponseBody
	public PlayerGroup findById(@PathVariable(value="id")long playerId){
		PlayerGroup group = playerGroupService.findById(playerId);
		return group;
	}
}
