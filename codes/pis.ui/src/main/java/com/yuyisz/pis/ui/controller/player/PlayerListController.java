package com.yuyisz.pis.ui.controller.player;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yuyisz.pis.ui.module.player.PlayerList;
import com.yuyisz.pis.ui.module.security.User;
import com.yuyisz.pis.ui.service.player.PlayerListService;
import com.yuyisz.pis.ui.vo.PlayerListVo;

@Controller
@RequestMapping("/playercontrol")
public class PlayerListController {

	@Autowired
	private PlayerListService playerListService;
	
	@GetMapping("/findAllPlayerList")
	@ResponseBody
	public List<PlayerList> findAllPlayerList() {
		return playerListService.findAll();
	}
	
	@PostMapping("/addPlayerList")
	@ResponseBody
	public Map<String, Object> addPlayerList(@RequestBody PlayerListVo playerListVo){
		Map<String,Object> result = new HashMap<>();
		boolean flag = false;
		String message="";
		PlayerList pl = null;
		if(playerListVo.getPlayerListId() != 0) {
			//跟新
			pl = playerListService.findById(playerListVo.getPlayerListId());
			if(pl == null) {
				//没找到
				message = "播表ID="+playerListVo.getPlayerListId()+"不存在！";
			}else {
				pl.setDefault(playerListVo.isDefault());
				pl.setPlayListName(playerListVo.getPlayerListName());
				pl.setStartDate(playerListVo.getStartDate());
				pl.setEndDate(playerListVo.getEndDate());
				pl.setDescription(playerListVo.getDescription());
			}
		}else {
			//新建
			pl = new PlayerList();
			pl.setDefault(playerListVo.isDefault());
			pl.setPlayListName(playerListVo.getPlayerListName());
			pl.setStartDate(playerListVo.getStartDate());
			pl.setEndDate(playerListVo.getEndDate());
			pl.setDescription(playerListVo.getDescription());
			pl.setCreateDate(new Date());
			pl.setCreater((User) SecurityUtils.getSubject().getPrincipal());
		}
		try {
			if(pl!=null) {
				PlayerList re = playerListService.saveOrUpdatePlayerList(pl);
				if(re != null) {
					flag = true;
					message = "保存播表成功！";
				}
			}
		}catch(Exception e) {
			message = "保存播表发生异常！";
		}
		
		result.put("flag", flag);
		result.put("message", message);
		return result;
	}
	
	@PostMapping("/delPlayerList/{id}")
	@ResponseBody
	public Map<String, Object> delPlayerList(@PathVariable("id")long playerListId){
		Map<String,Object> result = new HashMap<>();
		boolean flag = false;
		String message="";
		try {
			playerListService.delById(playerListId);
			flag = true;
			message = "删除播表成功！";
		}catch(Exception e) {
			message = "删除播表发生异常！";
		}
		result.put("flag", flag);
		result.put("message", message);
		return result;
	}
}
