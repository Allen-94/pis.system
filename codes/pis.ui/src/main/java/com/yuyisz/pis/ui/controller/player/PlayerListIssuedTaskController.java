package com.yuyisz.pis.ui.controller.player;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yuyisz.pis.ui.module.player.PlayerList;
import com.yuyisz.pis.ui.module.player.PlayerListIssuedTask;
import com.yuyisz.pis.ui.module.player.PlayerRole;
import com.yuyisz.pis.ui.module.security.DevResources;
import com.yuyisz.pis.ui.service.player.PlayerListIssuedTaskService;
import com.yuyisz.pis.ui.service.player.PlayerListService;
@Controller
@RequestMapping("/playercontrol")
public class PlayerListIssuedTaskController {

	@Autowired
	private PlayerListIssuedTaskService playerListIssuedTaskService;
	@Autowired
	private PlayerListService playerListService;
	/**
	 * 根据播表id获取播表下发任务
	 * @param playerListId
	 * @return ：前端展示需要，对输出结果进行整理
	 */
	@GetMapping("/findIssuedTaskByPlayerListId/{playerListId}")
	@ResponseBody
	public List<PlayerListIssuedTask> findIssuedTaskByPlayerListId(@PathVariable("playerListId")long playerListId) {
		PlayerList pl = playerListService.findById(playerListId);
		return playerListIssuedTaskService.findIssuedTaskByPlayer(pl);
	}
	
	@GetMapping("downloadInfoShow/{playerListId}")
	public String downloadModalShow(Model model,@PathVariable("playerListId")long playerListId) {
		model.addAttribute("playerListId", playerListId);
		return "/player/downloadInfoShow";
	}
	
	/**
	 * 下发任务反馈信息接收接口，
	 */
	
	public boolean putDownloadMessage(String issuedId,int type,String mes) {
		
		return false;
	}
	
	/**
	 * * 下发操作，根据是否是定时任务，启动相应的下发任务，向相关的播控器发送下发信号。
	 * 指定文件夹下保存有下发相关的所有文件
	 * 播表下发，监听播控端接受播表的进程。更新数据库，前端监控任务进展，可能要用到websocket?
	 * @param playerListId：下发的播表id
	 * @param issuedDate：下发时间，定时下发
	 * @return
	 */
	@RequestMapping("/issuedPlayerList")
	@ResponseBody
	public Map<String, Object> issuedPlayerList(@RequestParam("playerListId")long playerListId,@RequestParam("issuedDate") Date issuedDate) {
		Map<String,Object> result = new HashMap<>();
		boolean flag = false;
		String message="";
		//根据播表创建下发任务列表，包括播表，资源，版式，内容包等文件的下发任务
		PlayerList playerList = playerListService.findById(playerListId);
		//获取播表要下发的播控
		if(playerList!=null) {
			Set<PlayerRole> roleSet = playerList.getPlayRoles();
			List<DevResources> players = new ArrayList<>();
			for(PlayerRole role : roleSet) {
				players.addAll(role.getPlayers());
			}
			
			for(DevResources player:players) {
				playerListIssuedTaskService.IssuedTask(player,playerListId,issuedDate);
			}
			message = "播表下发完成。";
			flag = true;
		}else {
			message = "下发的播表不存在。";
		}
		result.put("flag", flag);
		result.put("message", message);
		return result;
	}
	
}
