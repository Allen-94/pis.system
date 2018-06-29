package com.yuyisz.pis.ui.controller.subsystem.vehiclemountedsubsystem;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yuyisz.pis.ui.module.enums.DeviceModel;
import com.yuyisz.pis.ui.module.enums.DeviceProducer;
import com.yuyisz.pis.ui.module.subsystem.PlaybackController;
import com.yuyisz.pis.ui.service.security.DevResourceService;
import com.yuyisz.pis.ui.service.subsystem.PcConfigService;
import com.yuyisz.pis.ui.service.subsystem.PlaybackControllerService;
import com.yuyisz.pis.ui.service.subsystem.ProducerConfigService;
import com.yuyisz.pis.ui.service.subsystem.ScreenService;
import com.yuyisz.pis.ui.service.subsystem.StationServerConfigService;
import com.yuyisz.pis.ui.service.subsystem.StationServerService;
import com.yuyisz.pis.ui.service.subsystem.StationUpgradeTaskService;
/*
 * 车载子系统控制
 */
@Controller
@RequestMapping(value="/subsystem")
public class VehicleMountedSubsystemController {
	@Autowired
	private DevResourceService resourceService;
	@Autowired
	private PlaybackControllerService pcService;
	@Autowired
	private ProducerConfigService producerConfigService;
	@Autowired
	private PcConfigService pcConfigService;
	@Autowired
	private StationUpgradeTaskService stationUpgradeTaskService;

	@RequestMapping("/modifyvehiclemountedaccountandpassword")
	public String modifyvehiclemountedaccountandpassword(ModelMap map, @RequestParam(name = "id") int id) {
		PlaybackController pc = pcService.findById(id);
		map.addAttribute("pc", pc);
		return "subsystem/modifyvehiclemountedaccountandpassword";
	}
	
	/**
	 * 
	 * @return 设备类型枚举值： 厂家 型号
	 */
	@RequestMapping("/getProducerConfigImformation")
	@ResponseBody
	public ArrayList<ArrayList<String>> getProducerConfigImformation() {
		DeviceProducer[] producers = DeviceProducer.values();
		ArrayList<String> producer = new ArrayList<String>();
		for (DeviceProducer deviceProducer : producers) {
			String temp = deviceProducer.getProducer_enum()+":"+deviceProducer.getProducer_state();
			producer.add(temp);
		}
		ArrayList<String> model = new ArrayList<String>();
		DeviceModel[] models = DeviceModel.values();
		for (DeviceModel deviceModel : models) {
			String temp =deviceModel.getDevice_model_enum()+":"+deviceModel.getDevice_model_state();
			model.add(temp);
		}
		ArrayList<ArrayList<String>> configs = new ArrayList<ArrayList<String>>();
		configs.add(producer);
		configs.add(model);
		return configs;
	}
	
	
}
