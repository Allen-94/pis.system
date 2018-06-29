package com.yuyisz.pis.ui.controller.subsystem.stationsubsystem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

import com.yuyisz.pis.ui.module.enums.DeviceModel;
import com.yuyisz.pis.ui.module.enums.DeviceType;
import com.yuyisz.pis.ui.module.security.DevResources;
import com.yuyisz.pis.ui.module.subsystem.PcConfig;
import com.yuyisz.pis.ui.module.subsystem.PlaybackController;
import com.yuyisz.pis.ui.service.security.DevResourceService;
import com.yuyisz.pis.ui.service.subsystem.PcConfigService;
import com.yuyisz.pis.ui.service.subsystem.PlaybackControllerService;
import com.yuyisz.pis.ui.service.subsystem.ProducerConfigService;
import com.yuyisz.pis.ui.vo.PcListVo;
/**
 * 对播控器的增删该查的动作都在这里
 * @author 郭芝辰
 *
 */
@RequestMapping("/subsystem")
@Controller
public class PcController {
	@Autowired
	private PlaybackControllerService pcService;
	
	@Autowired
	private DevResourceService resourceService;
	
	@Autowired
	private PcConfigService pcConfigService;
	
	@Autowired
	private ProducerConfigService producerConfigService;
	
	@RequestMapping("/isPcIdUsed")
	@ResponseBody
	public boolean isPcIdUsed(@RequestBody Map<String,Object> reqMap){
		int lineid = Integer.parseInt(reqMap.get("line_id").toString());
		int stationid = Integer.parseInt(reqMap.get("station_id").toString());
		int pcid = Integer.parseInt(reqMap.get("pc_id").toString());
		PlaybackController pc =pcService.findUniqueID(lineid,stationid,pcid);
		return pc==null?true:false;
	}
	
	@RequestMapping("/addstationpc")
	@ResponseBody
	public String addstationpc(@RequestBody Map<String, Object> reqMap) {
		
		int lineid = Integer.parseInt(reqMap.get("line_id").toString());
		int stationid = Integer.parseInt(reqMap.get("station_id").toString());
		int pcid = Integer.parseInt(reqMap.get("pc_id").toString());
		
		String pcname = reqMap.get("pc_name").toString();
		int pcstatus = Integer.parseInt(reqMap.get("pc_status").toString());
		int pcplace = Integer.parseInt(reqMap.get("pc_place").toString());
		int pcproducer = Integer.parseInt(reqMap.get("pc_producer").toString());
		int pcmodel = Integer.parseInt(reqMap.get("pc_model").toString());
		
		String pcIP = reqMap.get("pc_IP").toString();
		String pcaccount = reqMap.get("pc_account").toString();
		String pcpassword = reqMap.get("pc_password").toString();
		PlaybackController newpc = new PlaybackController();
		
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
		
		PlaybackController savepc = pcService.save(newpc);
		
		//保存到资源表中
		DevResources resource = new DevResources();
		resource.setId1(1);
		resource.setId2(lineid);
		resource.setId3(stationid);
		resource.setId4(pcid);
		resource.setId5(-1);
		resource.setResourceType(6);
		resource.setResourceName(pcname);
		resourceService.save(resource);
		
		return savepc==null?"操作失败，请重试":"成功添加播控器";
	}
	
	@ResponseBody
	@RequestMapping(value = "AllPcList")
	// 返回所有播控器设备
	public List<PcListVo> AllPcList(@RequestParam(name = "type") int type) {
		List<PlaybackController> lists=pcService.findAll();
		// 整合pcList
		List<PcListVo> list = new ArrayList<PcListVo>();
		for (PlaybackController pc : lists) {
			PcListVo pcList = new PcListVo();
			PcConfig pcConfig = pcConfigService.findByPc(pc);

			// 4. 生成pcVO
			pcList.setId(pc.getId());
			pcList.setPc_id(pc.getPc_id());
			pcList.setPc_name(pc.getPc_name());
			pcList.setPc_model(DeviceModel.get_model_state(pc.getPc_model()));
			pcList.setPc_IP(pc.getPc_ip());
			pcList.setPc_status(pcConfigService.getDeviceStatusName(pc.getPc_status()));
			pcList.setPc_account(pc.getPc_account());
			pcList.setPc_password(pc.getPc_password());
			if (pcConfig == null) {
				pcConfig = new PcConfig();// 初始化一个配置信息并持久保存该pc的配置信息
				pcConfig.setPc(pc);
				pcConfigService.save(pcConfig);
			}
			// 初始的设备配置信息
			pcList.setIsTimeSwitch(pcConfig.getIsAutoSwitch());
			pcList.setSwitchTime(pcConfig.getPcSwitchTime());
			pcList.setIsControllScreenSwitch(pcConfig.getIsScreenAutoSwitch());
			pcList.setScreenSwitchTime(pcConfig.getScreenSwitchTime());
			;
			pcList.setVersion(pcConfig.getVersion());
			list.add(pcList);

		}
		return list;
	}
	
	// 返回查询条件需要的的播放控制器
		@SuppressWarnings("null")
		@ResponseBody
		@RequestMapping(value = "/findPCList")
		public List<PcListVo> PlaybackControllerList(@RequestParam(name = "type") int type,
				@RequestParam(name = "stationId") int resource_id) {
			// 是播控器类型
			DevResources resource = resourceService.findById(resource_id);
			// 某个车站下的所有播控器
			List<DevResources> resources = resourceService.findResourceBystationType(type, resource.getId2(),
					resource.getId3());
			// 整合pcList
			List<PcListVo> list = new ArrayList<PcListVo>();
			for (DevResources devResource : resources) {
				PcListVo pcList = new PcListVo();
				// 1. 找到资源对应的设备数据
				int id1 = devResource.getId1();
				int id2 = devResource.getId2();
				int id3 = devResource.getId3();
				int id4 = devResource.getId4();
				int id5 = devResource.getId5();
				// 2. 播控器对应的唯一键是 line_id，station_id,pc_id,对应资源表的id2,id3,id4
				// 查找具体id的播放控制器
				PlaybackController pc = pcService.findOne(id2, id3, id4);
				PcConfig pcConfig = pcConfigService.findByPc(pc);

				// 4. 生成pcVO
				pcList.setPc_id(pc.getPc_id());
				pcList.setPc_name(pc.getPc_name());
				pcList.setPc_model(DeviceModel.get_model_state(pc.getPc_model()));
				pcList.setPc_IP(pc.getPc_ip());
				pcList.setPc_status(pcConfigService.getDeviceStatusName(pc.getPc_status()));
				pcList.setPc_account(pc.getPc_account());
				pcList.setPc_password(pc.getPc_password());
				if (pcConfig == null) {
					pcConfig = new PcConfig();// 初始化一个配置信息并持久保存该pc的配置信息
					pcConfig.setPc(pc);
					pcConfigService.save(pcConfig);
				}
				// 初始的设备配置信息
				pcList.setIsTimeSwitch(pcConfig.getIsAutoSwitch());
				pcList.setSwitchTime(pcConfig.getPcSwitchTime());
				pcList.setIsControllScreenSwitch(pcConfig.getIsScreenAutoSwitch());
				pcList.setScreenSwitchTime(pcConfig.getScreenSwitchTime());
				pcList.setVersion(pcConfig.getVersion());
				list.add(pcList);
			}
			return list;
		}
		
		// pc
		@RequestMapping("/modify")
		public String modify(ModelMap map, @RequestParam(name = "id") int id) {
			PlaybackController pc = pcService.findById(id);
			map.addAttribute("pc", pc);
			return "subsystem/modifyusernameandpassword";
		}
		
		// 播控器跳转到设置自动开关时间的界面
		@RequestMapping("/pcautoswitchtime")
		public String pcautoswitchtime(ModelMap map, @RequestParam(name = "pc_id") int pc_id) {
			PlaybackController pc = pcService.findById(pc_id);
			map.addAttribute("pc", pc);
			PcConfig pcConfig = pcConfigService.findByPc(pc);
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
			int id = Integer.parseInt(reqMap.get("id").toString());
			int type = Integer.parseInt(reqMap.get("type").toString());
			String from = (String) reqMap.get("from");
			String to = (String) reqMap.get("to");
			String autoswitch = from + "-" + to;
			String isused = (String) reqMap.get("isused");
			PlaybackController pc = pcService.findById(id);
			PcConfig pcConfig = pcConfigService.findByPc(pc);
			if (pcConfig == null) {
				// 没有配置自动开关时间
				pcConfig = new PcConfig();
			}
			pcConfig.setIsAutoSwitch(isused);
			pcConfig.setPcSwitchTime(autoswitch);
			// 数据库持久刷新配置
			PcConfig config = pcConfigService.save(pcConfig);
			return config == null ? "设置失败" : "设置成功";
		}
		
		@RequestMapping("/delstationpc")
		@ResponseBody
		public String delstationpc(@RequestParam(value="pc_id")int pc_id) {
			PlaybackController pc = pcService.findById(pc_id);
			List<DevResources> resource = resourceService.findById2AndId3AndId4AndAndResourceType(pc.getLine_id(), pc.getStation_id(), pc.getPc_id(), DeviceType.PLAYBACK_CONTROLLER.getDeviceType_enum());
			pcService.deleteOne(pc_id);
			resourceService.delete(resource);
			return "成功删除该播放控制器";
		}
		
		@RequestMapping("/deleteStationPcInBatches")
		@ResponseBody
		public String deleteStationPcInBatches(@RequestParam(value = "id", required = false) String id) {
			if (id != null && !id.trim().equals("")) {
				String[] temp = id.split(",");
				for (String i : temp) {
					Integer pcId = Integer.valueOf(i);
					PlaybackController pc = pcService.findById(pcId);
					List<DevResources> resource = resourceService.findById2AndId3AndId4AndAndResourceType(pc.getLine_id(), pc.getStation_id(), pc.getPc_id(), DeviceType.PLAYBACK_CONTROLLER.getDeviceType_enum());
					resourceService.delete(resource);
					pcService.deleteOne(pcId);
				}
			}
			return "删除成功";
		}
}
