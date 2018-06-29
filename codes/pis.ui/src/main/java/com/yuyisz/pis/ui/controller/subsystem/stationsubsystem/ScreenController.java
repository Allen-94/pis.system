package com.yuyisz.pis.ui.controller.subsystem.stationsubsystem;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yuyisz.pis.ui.module.subsystem.PcConfig;
import com.yuyisz.pis.ui.module.subsystem.PlaybackController;
import com.yuyisz.pis.ui.service.subsystem.PcConfigService;
import com.yuyisz.pis.ui.service.subsystem.PlaybackControllerService;

@RequestMapping("/subsystem")
@Controller
public class ScreenController {
	
	@Autowired
	private PlaybackControllerService pcService;
	@Autowired
	private PcConfigService pcConfigService;
	
	@RequestMapping("/screenautoswitchtime")
	public String screenautoswitchtime(ModelMap map, @RequestParam(name = "pc_id") int pc_id) {
		PlaybackController pc = pcService.findById(pc_id);
		PcConfig pcConfig = pcConfigService.findByPc(pc);
		map.addAttribute("pc", pc);
		if (pcConfig == null) {
			// 持久化pc设备配置
			pcConfig = new PcConfig();
			pcConfig.setPc(pc);
			pcConfigService.save(pcConfig);
		}
		map.addAttribute("isTimeSwitch", pcConfig.getIsScreenAutoSwitch().equals("否") ? 0 : 1);
		String time = pcConfig.getScreenSwitchTime();
		String[] split = time.split("-");
		if (split.length == 2) {
			map.addAttribute("from", split[0]);
			map.addAttribute("to", split[1]);
		} else {
			PcConfig config = new PcConfig();
			String[] split2 = config.getScreenSwitchTime().split("-");
			map.addAttribute("from", split2[0]);
			map.addAttribute("to", split2[1]);
		}
		return "subsystem/setscreenautoswitchtime";
	}
	
	@RequestMapping("/setscreenautoswitchtime")
	@ResponseBody
	public String setscreenautoswitchtime(@RequestBody Map<String, Object> reqMap) {
		int pc_id = Integer.parseInt(reqMap.get("id").toString());
		// 从前台取数据
		String from = reqMap.get("from").toString();
		String to = reqMap.get("to").toString();
		String time = from + "-" + to;
		String isused = reqMap.get("isused").toString();

		// 持久化到配置库中
		PlaybackController pc = pcService.findById(pc_id);
		PcConfig pcConfig = pcConfigService.findByPc(pc);
		if (pcConfig == null) {
			pcConfig = new PcConfig();
			pcConfig.setPc(pc);
		}
		pcConfig.setScreenSwitchTime(time);
		pcConfig.setIsScreenAutoSwitch(isused);
		PcConfig save = pcConfigService.save(pcConfig);
		return save == null ? "保存失败" : "保存成功";
	}
}
