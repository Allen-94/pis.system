package com.yuyisz.pis.ui.controller.player;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yuyisz.pis.ui.module.player.PlayerListIssuedTask;
import com.yuyisz.pis.ui.module.player.PlayerRole;
import com.yuyisz.pis.ui.module.resources.FileObject;
import com.yuyisz.pis.ui.module.security.DevResources;
import com.yuyisz.pis.ui.module.security.User;
import com.yuyisz.pis.ui.service.fileupload.FileUploadService;
import com.yuyisz.pis.ui.service.player.ContentPackageService;
import com.yuyisz.pis.ui.service.player.PlayerFormatService;
import com.yuyisz.pis.ui.service.player.PlayerGroupService;
import com.yuyisz.pis.ui.service.player.PlayerListIssuedTaskService;
import com.yuyisz.pis.ui.service.player.PlayerListService;
import com.yuyisz.pis.ui.service.player.PlayerRoleService;
import com.yuyisz.pis.ui.service.security.DevResourceService;
import com.yuyisz.pis.ui.vo.PlayerRoleVo;

@Controller
@RequestMapping("/playercontrol")
public class PlayerRoleController {
	
	@Autowired
	private PlayerRoleService playerRoleService;
	@Autowired
	private PlayerGroupService playerGroupService;
	@Autowired
	private PlayerFormatService playerFormatService;
	@Autowired 
	private ContentPackageService contentPackageService;
	@Autowired
	private PlayerListService playerListService;
	@Autowired
	private DevResourceService devResourceService;
	@Autowired
	private PlayerListIssuedTaskService playerListIssuedTaskService;
	
	@Value("${upload.path.businessdata.playerdata}")
	private String playerDataPath;
	
	@Autowired
	private FileUploadService fileUploadService;
	
	@PostMapping("/addPlayerRole")
	@ResponseBody
	public Map<String, Object> addPlayerRole(@RequestBody PlayerRoleVo playerRoleVo){
		Map<String,Object> result = new HashMap<>();
		boolean flag = false;
		String message="";
		PlayerRole role = null;
		if(playerRoleVo.getPlayerRoleId() != 0) {
			//更新
			role = playerRoleService.findById(playerRoleVo.getPlayerRoleId());
			if(role!=null) {
				role.setPlayRoleName(playerRoleVo.getPlayerRoleName());
				role.setEndTime(playerRoleVo.getEndTime());
				role.setStartTime(playerRoleVo.getStartTime());
				role.setMonday(playerRoleVo.isMonday());
				role.setTuesday(playerRoleVo.isTuesday());
				role.setWednesday(playerRoleVo.isWednesday());
				role.setSaturday(playerRoleVo.isSaturday());
				role.setFriday(playerRoleVo.isFriday());
				role.setThursday(playerRoleVo.isThursday());
				role.setSunday(playerRoleVo.isSunday());
				role.setContentPackage(contentPackageService.findById(playerRoleVo.getContentPackageId()));
				role.setPlayerFormat(playerFormatService.findById(playerRoleVo.getPlayerForamtId()));
				role.setSwitchPlayerFormat(playerFormatService.findById(playerRoleVo.getSwitchPlayerFormatId()));
				role.setSwitchTime(playerRoleVo.getSwitchTime());
				role.setPlayers(new HashSet<DevResources>(devResourceService.findPlayByIds(playerRoleVo.getPlayerIds())));
			}else {
				message = "播表规则不存在？ID="+playerRoleVo.getPlayerRoleId();
			}
		}else {
			//新增
			role = new PlayerRole();
			role.setCreateDate(new Date());
			role.setCreater((User) SecurityUtils.getSubject().getPrincipal());
			role.setPlayRoleName(playerRoleVo.getPlayerRoleName());
			role.setEndTime(playerRoleVo.getEndTime());
			role.setStartTime(playerRoleVo.getStartTime());
			role.setMonday(playerRoleVo.isMonday());
			role.setTuesday(playerRoleVo.isTuesday());
			role.setWednesday(playerRoleVo.isWednesday());
			role.setSaturday(playerRoleVo.isSaturday());
			role.setFriday(playerRoleVo.isFriday());
			role.setThursday(playerRoleVo.isThursday());
			role.setSunday(playerRoleVo.isSunday());
			role.setContentPackage(contentPackageService.findById(playerRoleVo.getContentPackageId()));
			role.setPlayList(playerListService.findById(playerRoleVo.getPlayerListId()));
			role.setPlayerFormat(playerFormatService.findById(playerRoleVo.getPlayerForamtId()));
			role.setSwitchPlayerFormat(playerFormatService.findById(playerRoleVo.getSwitchPlayerFormatId()));
			role.setSwitchTime(playerRoleVo.getSwitchTime());
			role.setPlayers(new HashSet<DevResources>(devResourceService.findPlayByIds(playerRoleVo.getPlayerIds())));
		}
		try {
			PlayerRole re = playerRoleService.saveOrUpdatePlayerRole(role);
			/**
			 * 修改/添加播表规则，
			 * 完成后，根据规则中关联的播控，进行分解，为每个播控生成对应的文件，放在响应的文件夹下，
			 * 需要在文件夹下准备一个文件，播控进行访问时，可以判断出那些播表内容是需要跟新的
			 */
			if(re == null) {
				message = "保存|更新播表规则失败！";
			}else {
				Set<DevResources> players = re.getPlayers();
				for(DevResources player:players) {
					String playerDir = playerDataPath + File.separator+player.getId();
					File parentDir = new File(playerDir);
					if(!parentDir.exists()||!parentDir.isDirectory()) {
						parentDir.mkdirs();
					}
					FileObject fb = playerRoleService.makePlayListFile(playerDir,player,re);
					fb = fileUploadService.saveFileObject(fb);
					
					List<PlayerListIssuedTask> tasks = new ArrayList<>();
					//根据规则文件，生成下载任务
					PlayerListIssuedTask task1 = new PlayerListIssuedTask();
					task1.setCreater((User) SecurityUtils.getSubject().getPrincipal());
					task1.setFile(fb);
					task1.setDownloadType(1);
					task1.setIssuedTaskId(UUID.randomUUID().toString());
					task1.setPlayerList(re.getPlayList());
					task1.setResources(player);
					tasks.add(task1);
					//根据版式文件生成下载任务
					PlayerListIssuedTask task2 = new PlayerListIssuedTask();
					task2.setCreater((User) SecurityUtils.getSubject().getPrincipal());
					task2.setFile(re.getPlayerFormat().getFileObject());
					task2.setDownloadType(2);
					task2.setIssuedTaskId(UUID.randomUUID().toString());
					task2.setPlayerList(re.getPlayList());
					task2.setResources(player);
					//备用版式
					PlayerListIssuedTask task3 = new PlayerListIssuedTask();
					task3.setCreater((User) SecurityUtils.getSubject().getPrincipal());
					task3.setFile(re.getPlayerFormat().getFileObject());
					task3.setDownloadType(2);
					task3.setIssuedTaskId(UUID.randomUUID().toString());
					task3.setPlayerList(re.getPlayList());
					task3.setResources(player);
					
					tasks.add(task2);
					tasks.add(task3);
					//根据资源文件，生成下载任务
					//从内容包中获取要下载的资源
					List<FileObject> mediaResources = re.getContentPackage().getMediaResources();
					for(FileObject fo:mediaResources) {
						PlayerListIssuedTask taskN = new PlayerListIssuedTask();
						taskN.setCreater((User) SecurityUtils.getSubject().getPrincipal());
						taskN.setFile(fo);
						taskN.setDownloadType(3);
						taskN.setIssuedTaskId(UUID.randomUUID().toString());
						taskN.setPlayerList(re.getPlayList());
						taskN.setResources(player);
						tasks.add(taskN);
					}
					
					playerListIssuedTaskService.saveTasks(tasks);
				}
				flag = true;
				message = "保存|更新播表规则成功！";
			}
		}catch(Exception e) {
			message = "保存播表规则失败！\n"+e.getMessage();
		}
		result.put("flag", flag);
		result.put("message", message);
		return result;
	}
	
	@RequestMapping("/findRoleByPlayerListId/{playerListId}")
	@ResponseBody
	public List<PlayerRole> findRoleByPlayerListId(@PathVariable("playerListId")long playerListId){
		return playerRoleService.findRoleByPlayerListId(playerListId);
	}
	
	
	@PostMapping("/delPlayerRole/{id}")
	@ResponseBody
	public Map<String, Object> delPlayerRole(@PathVariable("id")long playerRoleId){
		Map<String,Object> result = new HashMap<>();
		boolean flag = false;
		String message="";
		try {
			playerRoleService.delById(playerRoleId);
			flag = true;
			message = "删除播表规则成功！";
		}catch(Exception e) {
			message = "删除播表规则发生异常！";
		}
		result.put("flag", flag);
		result.put("message", message);
		return result;
	}
}
