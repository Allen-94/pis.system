package com.yuyisz.pis.ui.controller.subsystem.stationsubsystem;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yuyisz.pis.ui.module.enums.DeviceType;
import com.yuyisz.pis.ui.module.security.DevResources;
import com.yuyisz.pis.ui.module.subsystem.Station;
import com.yuyisz.pis.ui.module.subsystem.StationServer;
import com.yuyisz.pis.ui.service.security.DevResourceService;
import com.yuyisz.pis.ui.service.subsystem.StationService;

@Controller
@RequestMapping("/subsystem")
public class StationController {
	@Autowired
	private StationService stationService;
	
	@Autowired
	private DevResourceService resourceService;
	
	@RequestMapping("/findStationByLineId")
	@ResponseBody
	public List<Station> findStationByLineId(@RequestParam(name="line_id")int line_id){
		List<Station> stations = stationService.findByLineId(line_id);
		return stations;
	}
	
	@RequestMapping("/isStationIdUsed")
	@ResponseBody
	public boolean isStationIdUsed(@RequestBody Map<String,Object> reqMap) {
		int lineid = Integer.parseInt(reqMap.get("line_id").toString());
		int stationid = Integer.parseInt(reqMap.get("station_id").toString());
		Station station = stationService.findOne( lineid, stationid);
		return station==null?true:false;
		
	}
	
	@RequestMapping("/savestation")
	@ResponseBody
	public String savestation(@RequestBody Map<String,Object> reqMap) {
		int lineid = Integer.parseInt(reqMap.get("line_id").toString());
		int stationid = Integer.parseInt(reqMap.get("station_id").toString());
		String stationname = reqMap.get("station_name").toString();
		int stationstatus = Integer.parseInt(reqMap.get("station_status").toString());
		int stationplace = Integer.parseInt(reqMap.get("station_place").toString());
		int stationX = Integer.parseInt(reqMap.get("stationX").toString());
		int stationY = Integer.parseInt(reqMap.get("stationY").toString());
		String stationstate = reqMap.get("station_state").toString();
		
		Station station = new Station();
		station.setLine_id(lineid);
		station.setSite_number(stationplace);
		station.setStation_id(stationid);
		station.setStation_inf(stationstate);
		station.setStation_name(stationname);
		station.setStation_status(stationstatus);
		station.setX_pos(stationX);
		station.setY_pos(stationY);
		
		Station save = stationService.save(station);
		
		DevResources resources = new DevResources();
		resources.setIds(1, lineid, stationid, -1, -1);
		resources.setResourceName(stationname);
		resources.setResourceType(DeviceType.STATION.getDeviceType_enum());
		
		DevResources devResources = resourceService.save(resources);
		return save!=null&&devResources!=null?"保存成功":"保存车站失败，请重试";
		
	}
	
	@RequestMapping("/AllStationList")
	@ResponseBody
	public List<Station> AllStationList(){
		return stationService.findAll();
	}
	
	@RequestMapping("/delstation")
	@ResponseBody
	public String delstationpc(@RequestParam(value="station_id")int station_id) {
		Station station = stationService.findById(station_id);
		List<DevResources> resource = resourceService.findById2AndId3AndId4AndAndResourceType(station.getLine_id(), station.getStation_id(), -1, DeviceType.STATION.getDeviceType_enum());
		stationService.deleteOne(station_id);
		resourceService.delete(resource);
		return "成功删除该车站";
	}
	
	@RequestMapping("/deleteStationInBatches")
	@ResponseBody
	public String deleteStationPcInBatches(@RequestParam(value = "id", required = false) String id) {
		if (id != null && !id.trim().equals("")) {
			String[] temp = id.split(",");
			for (String i : temp) {
				Integer station_id = Integer.valueOf(i);
				Station station = stationService.findById(station_id);
				List<DevResources> resource = resourceService.findById2AndId3AndId4AndAndResourceType(station.getLine_id(), station.getStation_id(), -1, DeviceType.STATION.getDeviceType_enum());
				stationService.deleteOne(station_id);
				resourceService.delete(resource);
			}
		}
		return "删除成功";
	}
}
