package com.yuyisz.pis.ui.controller.subsystem.stationsubsystem;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
/**
 * 播放控制器
 * @author 郭芝辰
 *
 */
import org.springframework.web.bind.annotation.ResponseBody;

import com.yuyisz.pis.ui.message.DeviceManagementModuleRabbitClient;
import com.yuyisz.pis.ui.message.GeneralMsg;
import com.yuyisz.pis.ui.message.MsgBody;
import com.yuyisz.pis.ui.message.MsgHeader;
import com.yuyisz.pis.ui.message.P2PMessageCommands;
import com.yuyisz.pis.ui.message.P2PMessageType;
import com.yuyisz.pis.ui.module.enums.DeviceInitStatus;
import com.yuyisz.pis.ui.module.enums.DeviceModel;
import com.yuyisz.pis.ui.module.enums.DeviceType;
import com.yuyisz.pis.ui.module.enums.TaskStatus;
import com.yuyisz.pis.ui.module.enums.TaskType;
import com.yuyisz.pis.ui.module.security.DevResources;
import com.yuyisz.pis.ui.module.security.User;
import com.yuyisz.pis.ui.module.subsystem.PcConfig;
import com.yuyisz.pis.ui.module.subsystem.PcGroup;
import com.yuyisz.pis.ui.module.subsystem.PcInitializeCheck;
import com.yuyisz.pis.ui.module.subsystem.PcUpgradeTask;
import com.yuyisz.pis.ui.module.subsystem.PcUpgradeTaskProgress;
import com.yuyisz.pis.ui.module.subsystem.PlaybackController;
import com.yuyisz.pis.ui.module.subsystem.Station;
import com.yuyisz.pis.ui.service.security.DevResourceService;
import com.yuyisz.pis.ui.service.subsystem.PcConfigService;
import com.yuyisz.pis.ui.service.subsystem.PcGroupService;
import com.yuyisz.pis.ui.service.subsystem.PcInitializeCheckService;
import com.yuyisz.pis.ui.service.subsystem.PcUpgradeTaskProgressService;
import com.yuyisz.pis.ui.service.subsystem.PcUpgradeTaskService;
import com.yuyisz.pis.ui.service.subsystem.PlaybackControllerService;
import com.yuyisz.pis.ui.service.subsystem.ProducerConfigService;
import com.yuyisz.pis.ui.service.subsystem.ScreenService;
import com.yuyisz.pis.ui.service.subsystem.StationService;
import com.yuyisz.pis.ui.util.SendToPCUpgradeTaskUtil;
import com.yuyisz.pis.ui.vo.PcListVo;
import com.yuyisz.pis.ui.vo.PcUpgradeTaskProgressVo;

/**
 * 对播控器的增删该查的动作都在这里
 * 
 * @author 郭芝辰
 *
 */
@RequestMapping("/subsystem")
@Controller
public class PcController {
	@Autowired
	private PlaybackControllerService pcService;
	@Autowired
	private StationService stationService;

	@Autowired
	private DevResourceService resourceService;

	@Autowired
	private PcConfigService pcConfigService;
	@Autowired
	private PcGroupService pcGroupService;
	@Autowired
	private ProducerConfigService producerConfigService;
	@Autowired
	private ScreenService screenService;
	@Autowired
	private DeviceManagementModuleRabbitClient deviceManagementModuleRabbitClient;
	@Autowired
	private PcInitializeCheckService pcInitializeCheckService;
	
	@RequestMapping("/restartpc")
	@ResponseBody
	public String Restart(@RequestParam(name = "id") String id) {
		PlaybackController pc = pcService.findById(id);
		GeneralMsg msg = new GeneralMsg();
		String messageBody = "pc_id="+id+"&pc_ip="+pc.getPc_ip()+"&pc_username="+pc.getPc_account()+"&pc_password="+pc.getPc_password();
		msg.setHeader(new MsgHeader(P2PMessageType.P2P_command_message, true,id, "", "devicemanagement", 6000));
		msg.setBody(new MsgBody(P2PMessageCommands.DEVICE_RESTART, messageBody));
		/*try {
			deviceManagementModuleRabbitClient.Send(msg);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}*/
		return "已重启";
	}
	
	@RequestMapping("/pcremoteAccess")
	@ResponseBody
	public String RemoteAccess(@RequestParam(name = "id") String id) {
		return "远程连接成功";
	}
	
	@RequestMapping("/savepcaccountandpassword")
	@ResponseBody
	public String savepcaccountandpassword(@RequestBody Map<String, Object> reqMap) {
		String account = reqMap.get("account").toString();
		String password = reqMap.get("password").toString();
		String id = reqMap.get("id").toString();
		int type = Integer.parseInt(reqMap.get("type").toString());
		PlaybackController pc = pcService.findById(id);
		pc.setPc_account(account);
		pc.setPc_password(password);
		PlaybackController save = pcService.save(pc);
		//通知播控器修改用户名和密码
		GeneralMsg msg = new GeneralMsg();
		String messageBody = "pc_id="+id+"&pc_ip="+pc.getPc_ip()+"&pc_username="+pc.getPc_account()+"&pc_password="+pc.getPc_password();
		msg.setHeader(new MsgHeader(P2PMessageType.P2P_command_message, true,id, "", "devicemanagement", 6000));
		msg.setBody(new MsgBody(P2PMessageCommands.DEVICE_MODIFY_USERNAME_PASSWORD, messageBody));
		/*try {
			deviceManagementModuleRabbitClient.Send(msg);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}*/
		return save == null ? "设置失败" :"设置成功";
	}
	
	/*@RequestMapping("/isPcIdUsed")
	@ResponseBody
	public boolean isPcIdUsed(@RequestBody Map<String, Object> reqMap) {
		String lineid = reqMap.get("line_id").toString();// 资源ID
		int lineId = resourceService.findById(lineid).getId2();// lineID
		String stationid = reqMap.get("station_id").toString();
		int stationId = resourceService.findById(stationid).getId3();
		int pcId = Integer.parseInt(reqMap.get("pc_id").toString());

		PlaybackController pc = pcService.findUniqueID(lineId, stationId, pcId);
		return pc == null ? true : false;
	}*/

	//新增车站播控器
	@RequestMapping("/addstationpc")
	@ResponseBody
	public String addstationpc(@RequestBody Map<String, Object> reqMap) {

		String lineId = reqMap.get("line_id").toString();// 资源ID
		String stationId = reqMap.get("station_id").toString();// 是资源ID
		int lineid = resourceService.findById(lineId).getId2();// lineID
		int stationid = resourceService.findById(stationId).getId3(); // StationID
		List<PlaybackController> all = pcService.findAll();
		int pcid = 0;//pcID
		for (PlaybackController pc : all) {
			pcid = pcid>pc.getPc_id()?pcid:pc.getPc_id()+1;
		}

		String pcname = reqMap.get("pc_name").toString();
		int pcstatus = Integer.parseInt(reqMap.get("pc_status").toString());
		int pcplace = Integer.parseInt(reqMap.get("pc_place").toString());
		int pcproducer = Integer.parseInt(reqMap.get("pc_producer").toString());
		int pcmodel = Integer.parseInt(reqMap.get("pc_model").toString());

		String pcIP = reqMap.get("pc_IP").toString();
		String pcaccount = reqMap.get("pc_account").toString();
		String pcpassword = reqMap.get("pc_password").toString();
		String pcgroup_id = reqMap.get("pcgroup_id").toString();
		PcGroup pcgroup = pcGroupService.findOne(pcgroup_id);
		PlaybackController newpc = new PlaybackController();

		newpc.setId(UUID.randomUUID().toString());
		newpc.setLine_id(lineid);
		newpc.setStation_id(stationid);
		newpc.setPc_id(pcid);
		newpc.setPc_status(pcstatus);
		newpc.setPc_name(pcname);
		newpc.setPc_producer(pcproducer);
		newpc.setPc_place(pcplace);
		newpc.setPc_ip(pcIP);
		newpc.setPc_account(pcaccount);
		newpc.setPc_model(pcmodel);
		newpc.setPc_password(pcpassword);
		newpc.setPcGroup(pcgroup);

		PlaybackController savepc = pcService.save(newpc);

		// 保存到资源表中
		DevResources resource = new DevResources();
		resource.setId(UUID.randomUUID().toString());
		resource.setId1(1);
		resource.setId2(lineid);
		resource.setId3(stationid);
		resource.setId4(pcid);
		resource.setId5(-1);
		resource.setResourceType(6);
		resource.setResourceName(pcname);
		resourceService.save(resource);

		return savepc == null ? "操作失败，请重试" : "成功添加播控器";
	}

	// 返回所有播控器列表
	@ResponseBody
	@RequestMapping(value = "AllPcList")
	public List<PcListVo> AllPcList(@RequestParam(name = "type") int type) {
		List<PlaybackController> lists = pcService.findAll();
		// 整合pcList
		List<PcListVo> list = new ArrayList<PcListVo>();
		for (PlaybackController pc : lists) {
			PcListVo pcList = new PcListVo();
			PcConfig pcConfig = pc.getPcConfig();
			if (pcConfig == null) {
				pcConfig = new PcConfig();// 初始化一个配置信息并持久保存该pc的配置信息
				pcConfig.setId(UUID.randomUUID().toString());
				pc.setPcConfig(pcConfig);
				pcService.save(pc);
			}

			// 4. 生成pcVO
			pcList.setId(pc.getId());
			pcList.setPc_id("0"+pc.getLine_id()+"0"+pc.getStation_id()+"0"+pc.getPc_id());
			pcList.setPc_name(pc.getPc_name());
			pcList.setPc_model(DeviceModel.get_model_state(pc.getPc_model()));
			pcList.setPc_IP(pc.getPc_ip());
			pcList.setPc_status(pcConfigService.getDeviceStatusName(pc.getPc_status()));
			pcList.setPc_account(pc.getPc_account());
			pcList.setPc_password(pc.getPc_password());
			// 初始的设备配置信息
			pcList.setIsTimeSwitch(pc.getPcConfig().getIsAutoSwitch());
			pcList.setSwitchTime(pc.getPcConfig().getPcSwitchTime());
			pcList.setIsControllScreenSwitch(pc.getPcConfig().getIsScreenAutoSwitch());
			pcList.setScreenSwitchTime(pc.getPcConfig().getScreenSwitchTime());
			pcList.setVersion(pc.getPcConfig().getVersion());
			list.add(pcList);

		}
		return list;
	}

	// 返回查询条件需要的的播放控制器
	@SuppressWarnings("null")
	@ResponseBody
	@RequestMapping(value = "/findPCList")
	public List<PcListVo> PlaybackControllerList(@RequestParam(name = "type") int type,
			@RequestParam(name = "stationId", required = false, defaultValue = "-1") String stationId,
			@RequestParam(name="lineId",required = false,defaultValue="-1")String lineId) {
		// 站点下的播控
		List<PlaybackController> lists = null;

		if(!stationId.equals("-1")) {
			DevResources station = resourceService.findById(stationId);
			lists = pcService.findByStation(station);
		}else if(!lineId.equals("-1")){
			DevResources line = resourceService.findById(lineId);
			lists = pcService.findByLine(line);
		}

		// 整合pcList
		List<PcListVo> list = new ArrayList<PcListVo>();
		for (PlaybackController pc : lists) {
			PcListVo pcList = new PcListVo();

			PcConfig pcConfig = pc.getPcConfig();
			if (pcConfig == null) {
				pcConfig = new PcConfig();// 初始化一个配置信息并持久保存该pc的配置信息
				pcConfig.setId(UUID.randomUUID().toString());
				pc.setPcConfig(pcConfig);
				pcService.save(pc);
			}

			// 4. 生成pcVO
			pcList.setId(pc.getId());
			pcList.setPc_id("0"+pc.getLine_id()+"0"+pc.getStation_id()+"0"+pc.getPc_id());
			pcList.setPc_name(pc.getPc_name());
			pcList.setPc_model(DeviceModel.get_model_state(pc.getPc_model()));
			pcList.setPc_IP(pc.getPc_ip());
			pcList.setPc_status(pcConfigService.getDeviceStatusName(pc.getPc_status()));
			pcList.setPc_account(pc.getPc_account());
			pcList.setPc_password(pc.getPc_password());

			// 初始的设备配置信息
			pcList.setIsTimeSwitch(pc.getPcConfig().getIsAutoSwitch());
			pcList.setSwitchTime(pc.getPcConfig().getPcSwitchTime());
			pcList.setIsControllScreenSwitch(pc.getPcConfig().getIsScreenAutoSwitch());
			pcList.setScreenSwitchTime(pc.getPcConfig().getScreenSwitchTime());
			pcList.setVersion(pc.getPcConfig().getVersion());
			list.add(pcList);
		}
		return list;
	}

	// pc修改
	@RequestMapping("/modify")
	public String modify(ModelMap map, @RequestParam(name = "id") String id) {
		PlaybackController pc = pcService.findById(id);
		map.addAttribute("pc", pc);
		return "subsystem/modifyusernameandpassword";
	}

	// 播控器跳转到设置自动开关时间的界面
	@RequestMapping("/pcautoswitchtime")
	public String pcautoswitchtime(ModelMap map, @RequestParam(name = "pc_id") String pc_id) {
		PlaybackController pc = pcService.findById(pc_id);
		map.addAttribute("pc", pc);
		PcConfig pcConfig = pc.getPcConfig();
		if (pcConfig == null) {
			// 给跳转的界面全部重置
			pcConfig = new PcConfig();
			pcConfig.setPc(pc);
			pcConfigService.save(pcConfig);
		}
		// 把初始数据上传到界面
		map.addAttribute("isTimeSwitch", pcConfig.getIsAutoSwitch().equals("否") ? 0 : 1);
		String time = pcConfig.getPcSwitchTime();
		// 设置时间
		String[] split = time.split("-");
		if (split.length == 2) {
			map.addAttribute("from", split[0]);
			map.addAttribute("to", split[1]);
		} else {
			PcConfig config = new PcConfig();
			String[] split2 = config.getPcSwitchTime().split("-");
			map.addAttribute("from", split2[0]);
			map.addAttribute("to", split2[1]);
		}
		return "subsystem/setpcautoswitchtime";
	}

	// 后台保存设置播控器开关时间的数据
	@RequestMapping("/setpcautoswitchtime")
	@ResponseBody
	public String setpcautoswitchtime(@RequestBody Map<String, Object> reqMap) {
		String id = reqMap.get("id").toString();
		int type = Integer.parseInt(reqMap.get("type").toString());
		String from = (String) reqMap.get("from");
		String to = (String) reqMap.get("to");
		String autoswitch = from + "-" + to;
		String isused = (String) reqMap.get("isused");
		PlaybackController pc = pcService.findById(id);
		PcConfig pcConfig = pc.getPcConfig();
		if (pcConfig == null) {
			// 没有配置自动开关时间
			pcConfig = new PcConfig();
			pcConfig.setId(UUID.randomUUID().toString());
		}
		pcConfig.setIsAutoSwitch(isused);
		pcConfig.setPcSwitchTime(autoswitch);
		pc.setPcConfig(pcConfig);
		// 数据库持久刷新配置
		PlaybackController save = pcService.save(pc);
		
		GeneralMsg msg = new GeneralMsg();
		String messageBody = "pc_id="+id+"&pc_ip="+pc.getPc_ip()+"&pc_username="+pc.getPc_account()+"&pc_password="+pc.getPc_password();
		msg.setHeader(new MsgHeader(P2PMessageType.P2P_command_message, true,id, "", "devicemanagement", 6000));
		msg.setBody(new MsgBody(P2PMessageCommands.DEVICE_SET_PLAYCONTROLLER_SWITCH_TIME, messageBody));
		/*try {
			deviceManagementModuleRabbitClient.Send(msg);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}*/
		return save == null ? "设置失败" : "设置成功";
	}

	@RequestMapping("/delstationpc")
	@ResponseBody
	public String delstationpc(@RequestParam(value = "pc_id") String pc_id) {
		PlaybackController pc = pcService.findById(pc_id);
		List<DevResources> resource = resourceService.findById2AndId3AndId4AndAndResourceType(pc.getLine_id(),
				pc.getStation_id(), pc.getPc_id(), DeviceType.PLAYBACK_CONTROLLER.getDeviceType_enum());
		// 删除播控器下设备（播控器，显示屏）
		pcService.deleteOne(pc_id);
		screenService.deleteByPc(pc);
		// 删除资源表播控器下数据(播控器，显示屏)
		resourceService.delete(resource);
		resourceService.deleteByScreen(pc);
		return "成功删除该播放控制器";
	}

	//批量删除播控
	@RequestMapping("/deleteStationPcInBatches")
	@ResponseBody
	public String deleteStationPcInBatches(@RequestParam(value = "id", required = false) String id) {
		if (id != null && !id.trim().equals("")) {
			String[] temp = id.split(",");
			for (String i : temp) {
				String pcId = i;
				PlaybackController pc = pcService.findById(pcId);
				List<DevResources> resource = resourceService.findById2AndId3AndId4AndAndResourceType(pc.getLine_id(),
						pc.getStation_id(), pc.getPc_id(), DeviceType.PLAYBACK_CONTROLLER.getDeviceType_enum());
				// 删除播控器下设备（播控器，显示屏）
				pcService.deleteOne(pcId);
				screenService.deleteByPc(pc);
				// 删除资源表播控器下数据(播控器，显示屏)
				resourceService.delete(resource);
				resourceService.deleteByScreen(pc);
			}
		}
		return "删除成功";
	}



	@RequestMapping("/findPcByGroupId")
	@ResponseBody
	public List<PlaybackController> findPcByGroupId(@RequestParam(name = "pcGroupId") String pcGroupId) {
		List<PlaybackController> list = pcService.findByPcgroup_id(pcGroupId);
		return list;
	}
	
	//初始化车站播控器，并等待回复，返回播控器id
	@RequestMapping("/initStationPc")
	@ResponseBody
	public String initStationPc(@RequestParam(name="pc_id")String pc_id) { 
		PlaybackController pc = pcService.findById(pc_id);
		PcInitializeCheck check = pcInitializeCheckService.findById(pc_id);
		if(check==null) {
			check = new PcInitializeCheck();
		}
		check.setPcCheck(-1);
		check.setPc_id(pc.getId());
		pcInitializeCheckService.save(check);
		GeneralMsg msg = new GeneralMsg();
		String messageBody = "pc_id="+pc.getId()+"&pc_ip="+pc.getPc_ip()+"&pc_username="+pc.getPc_account()+"&pc_password="+pc.getPc_password()+"reinitialize=true";
		msg.setHeader(new MsgHeader(P2PMessageType.P2P_command_message, true,pc.getId(), "", "devicemanagement", 6000));
		msg.setBody(new MsgBody(P2PMessageCommands.DEVICE_PC_REINITIALIZE, messageBody));
		String state = null;
		try {
			deviceManagementModuleRabbitClient.Send(msg);
			deviceManagementModuleRabbitClient.Recieve();
			PcInitializeCheck check1 = pcInitializeCheckService.findById(pc_id);
			int pcCheck = check1.getPcCheck();
			state = DeviceInitStatus.getState(pcCheck);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return state;
	}
	
	
	@RequestMapping("/refreshResponse")
	@ResponseBody
	public String refreshResponse(@RequestParam(name="pc_id")String pc_id) {
		String state = null;
		try {
			deviceManagementModuleRabbitClient.Recieve();
			PcInitializeCheck check = pcInitializeCheckService.findById(pc_id);
			int pcCheck = check.getPcCheck();
			state = DeviceInitStatus.getState(pcCheck);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return state;
	}

	
}
