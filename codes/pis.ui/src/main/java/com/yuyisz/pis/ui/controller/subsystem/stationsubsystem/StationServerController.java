package com.yuyisz.pis.ui.controller.subsystem.stationsubsystem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yuyisz.pis.ui.module.enums.DeviceType;
import com.yuyisz.pis.ui.module.security.DevResources;
import com.yuyisz.pis.ui.module.subsystem.PlaybackController;
import com.yuyisz.pis.ui.module.subsystem.StationServer;
import com.yuyisz.pis.ui.module.subsystem.StationServerConfig;
import com.yuyisz.pis.ui.service.security.DevResourceService;
import com.yuyisz.pis.ui.service.subsystem.ProducerConfigService;
import com.yuyisz.pis.ui.service.subsystem.StationServerConfigService;
import com.yuyisz.pis.ui.service.subsystem.StationServerService;
import com.yuyisz.pis.ui.vo.StationServerVo;

@RequestMapping("/subsystem")
@Controller
public class StationServerController {
	@Autowired
	private StationServerConfigService stationServerConfigService;
	@Autowired
	private StationServerService stationServerService;
	@Autowired
	private DevResourceService resourceService;
	@Autowired
	private ProducerConfigService producerConfigService;
	
	@RequestMapping(value = "/AllStationServerList")
	@ResponseBody
	public List<StationServerVo> AllStationServerList(@RequestParam(name = "type") int stationservertype) {
		List<StationServer> lists = stationServerService.findAll();
		List<StationServerVo> list = new ArrayList<StationServerVo>();
		for (StationServer stationServer : lists) {
			// 1. 找到资源对应的设备数据
			System.out.println(stationServer);
			StationServerConfig serverConfig = stationServerConfigService.findByStationServer(stationServer);
			StationServerVo stationServerVo = new StationServerVo();

			stationServerVo.setStation_server_id(stationServer.getStation_server_id());
			stationServerVo.setAccount(stationServer.getStation_server_account());
			stationServerVo.setId(stationServer.getId());
			stationServerVo.setIp(stationServer.getStation_server_ip());
			stationServerVo.setModel(producerConfigService.getModelName(stationServer.getStation_server_model()));
			stationServerVo.setName(stationServer.getStation_server_name());
			stationServerVo.setPassword(stationServer.getStation_server_password());
			stationServerVo.setStatus(
					stationServerConfigService.getDeviceStatusName(stationServer.getStation_server_status()));
			if (serverConfig == null) {
				// 初始的设备配置信息,并持久该配置信息
				serverConfig = new StationServerConfig();
				serverConfig.setStationServer(stationServer);
				stationServerConfigService.save(serverConfig);
			}
			stationServerVo.setVersion(serverConfig.getVersion());
			list.add(stationServerVo);
		}

		return list;
	}

	@RequestMapping("/searchStationServer")
	@ResponseBody
	public List<StationServerVo> searchStationServer(@RequestParam(name = "type") int type,
			@RequestParam(name = "stationId") int resource_id) {
		// 车站
		DevResources resource = resourceService.findById(resource_id);
		// 车站下的服务器
		List<DevResources> stationservers = resourceService.findResourceBystationType(type, resource.getId2(),
				resource.getId3());

		List<StationServerVo> list = new ArrayList<StationServerVo>();
		for (DevResources devResource : stationservers) {
			// 1. 找到资源对应的设备数据
			int id1 = devResource.getId1();
			int id2 = devResource.getId2();
			int id3 = devResource.getId3();
			int id4 = devResource.getId4();
			int id5 = devResource.getId5();
			// 2. 车站服务器表对应的唯一键是 line_id，station_id,station_server_id,对应资源表的id2,id3,id4
			StationServer stationServer = stationServerService.findOne(id2, id3, id4);
			StationServerConfig serverConfig = stationServerConfigService.findByStationServer(stationServer);

			StationServerVo stationServerVo = new StationServerVo();
			stationServerVo.setStation_server_id(stationServer.getStation_server_id());
			stationServerVo.setAccount(stationServer.getStation_server_account());
			stationServerVo.setId(stationServer.getId());
			stationServerVo.setIp(stationServer.getStation_server_ip());
			stationServerVo.setModel(producerConfigService.getModelName(stationServer.getStation_server_model()));
			stationServerVo.setName(stationServer.getStation_server_name());
			stationServerVo.setPassword(stationServer.getStation_server_password());
			stationServerVo.setStatus(
					stationServerConfigService.getDeviceStatusName(stationServer.getStation_server_status()));
			if (serverConfig == null) {
				// 初始的设备配置信息,并持久该配置信息
				serverConfig = new StationServerConfig();
				serverConfig.setStationServer(stationServer);
				stationServerConfigService.save(serverConfig);
			}
			stationServerVo.setVersion(serverConfig.getVersion());
			list.add(stationServerVo);
		}
		return list;
	}
	
	// server
		@RequestMapping("/modifyserver")
		public String modifyserver(ModelMap map, @RequestParam(name = "id") int id) {
			StationServer server = stationServerService.findById(id);
			map.addAttribute("server", server);
			return "subsystem/modifyserverpassword";
		}

		
		@RequestMapping("/isServerIdUsed")
		@ResponseBody
		public boolean isServerIdUsed(@RequestBody Map<String, Object> reqMap) {
			int lineid = Integer.parseInt(reqMap.get("line_id").toString());
			int stationid = Integer.parseInt(reqMap.get("station_id").toString());
			int serverid = Integer.parseInt(reqMap.get("server_id").toString());
			StationServer findOne = stationServerService.findOne(lineid, stationid, serverid);
			return findOne==null?true:false;
			
		}
		
		
		@RequestMapping("/ss")
		@ResponseBody
		public String ss(@RequestBody Map<String, Object> reqMap) {
			int lineid = Integer.parseInt(reqMap.get("line_id").toString());
			int stationid = Integer.parseInt(reqMap.get("station_id").toString());
			int serverid = Integer.parseInt(reqMap.get("server_id").toString());
			String servername = reqMap.get("server_name").toString();
			int serverstatus = Integer.parseInt(reqMap.get("server_status").toString());
			int serverisused = Integer.parseInt(reqMap.get("server_isused").toString());
			int serverproducer = Integer.parseInt(reqMap.get("server_producer").toString());
			int servermodel = Integer.parseInt(reqMap.get("server_model").toString());
			String serverIP = reqMap.get("server_IP").toString();
			String serveraccount = reqMap.get("server_account").toString();
			String serverpassword = reqMap.get("server_password").toString();
			
			StationServer server = new StationServer();
			server.setLine_id(lineid);
			server.setStation_id(stationid);
			server.setStation_server_id(serverid);
			server.setStation_server_name(servername);
			server.setStation_server_status(serverstatus);
			server.setStation_server_is_used(serverisused);
			server.setStation_server_producer(serverproducer);
			server.setStation_server_model(servermodel);
			server.setStation_server_ip(serverIP);
			server.setStation_server_account(serveraccount);
			server.setStation_server_password(serverpassword);
			StationServer save = stationServerService.save(server);
			//保存到资源表中
			DevResources resources = new DevResources();
			resources.setIds(1, lineid, stationid, serverid, -1);
			resources.setResourceName(servername);
			resources.setResourceType(DeviceType.STATION_SERVER.getDeviceType_enum());
			resourceService.save(resources);
			return save==null?"保存车站服务器失败，请重试":"保存车站服务器成功";
		}
		
		@RequestMapping("/delstationserver")
		@ResponseBody
		public String delstationpc(@RequestParam(value="server_id")int server_id) {
			StationServer server = stationServerService.findById(server_id);
			List<DevResources> resource = resourceService.findById2AndId3AndId4AndAndResourceType(server.getLine_id(), server.getStation_id(), server.getStation_server_id(), DeviceType.STATION_SERVER.getDeviceType_enum());
			stationServerService.deleteOne(server_id);
			resourceService.delete(resource);
			return "成功删除该车站服务器";
		}
		
		@RequestMapping("/deleteStationServerInBatches")
		@ResponseBody
		public String deleteStationPcInBatches(@RequestParam(value = "id", required = false) String id) {
			if (id != null && !id.trim().equals("")) {
				String[] temp = id.split(",");
				for (String i : temp) {
					Integer serverId = Integer.valueOf(i);
					StationServer server = stationServerService.findById(serverId);
					List<DevResources> resource = resourceService.findById2AndId3AndId4AndAndResourceType(server.getLine_id(), server.getStation_id(), server.getStation_server_id(), DeviceType.STATION_SERVER.getDeviceType_enum());
					resourceService.delete(resource);
					stationServerService.deleteOne(serverId);
				}
			}
			return "删除成功";
		}
}
