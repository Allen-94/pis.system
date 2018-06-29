package com.yuyisz.pis.ui.controller.dev;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 设备管理
 * @author 42254
 *
 */
@Controller
@RequestMapping("/dev")
public class DevController {
	
	/*@RequestMapping("systemoperationmonitor")
	public String systemoperationmonitor(Model model) {
		model.addAttribute("title", "系统运行监测管理");
		model.addAttribute("right_main","/dev/systemoperationmonitor");
		return "base";
	}
	
	@RequestMapping("screenonoff")
	public String screenonoff(Model model) {
		model.addAttribute("title", "显示屏开关机管理");
		model.addAttribute("right_main","/dev/screenonoff");
		return "base";
	}
	
	@RequestMapping("playerversion")
	public String playerversion(Model model) {
		model.addAttribute("title", "播放控制器版本管理");
		model.addAttribute("right_main","/dev/playerversion");
		return "base";
	}
	
	@RequestMapping("loginremoteplayer")
	public String loginremoteplayer(Model model) {
		model.addAttribute("title", "登陆远程播放控制器");
		model.addAttribute("right_main","/dev/loginremoteplayer");
		return "base";
	}*/
}
