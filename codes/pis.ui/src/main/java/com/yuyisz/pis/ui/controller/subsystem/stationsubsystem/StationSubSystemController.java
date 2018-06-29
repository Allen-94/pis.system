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

import com.yuyisz.pis.ui.module.security.DevResources;
import com.yuyisz.pis.ui.module.subsystem.PcConfig;
import com.yuyisz.pis.ui.module.subsystem.PlaybackController;
import com.yuyisz.pis.ui.module.subsystem.Screen;
import com.yuyisz.pis.ui.module.subsystem.StationServer;
import com.yuyisz.pis.ui.module.subsystem.StationServerConfig;
import com.yuyisz.pis.ui.module.subsystem.StationUpgradeTask;
import com.yuyisz.pis.ui.service.security.DevResourceService;
import com.yuyisz.pis.ui.service.subsystem.PcConfigService;
import com.yuyisz.pis.ui.service.subsystem.PlaybackControllerService;
import com.yuyisz.pis.ui.service.subsystem.ProducerConfigService;
import com.yuyisz.pis.ui.service.subsystem.ScreenService;
import com.yuyisz.pis.ui.service.subsystem.StationServerConfigService;
import com.yuyisz.pis.ui.service.subsystem.StationServerService;
import com.yuyisz.pis.ui.service.subsystem.StationUpgradeTaskService;
//import com.yuyisz.pis.ui.util.RemoteShellExecutor;
import com.yuyisz.pis.ui.vo.PcListVo;
import com.yuyisz.pis.ui.vo.StationServerVo;

/*
 * 车站子系统控制
 */
@Controller
@RequestMapping("/subsystem")
public class StationSubSystemController {

	@Autowired
	private PlaybackControllerService pcService;
	@Autowired
	private StationServerService stationServerService;
	@Autowired
	private StationUpgradeTaskService stationUpgradeTaskService;

	@ResponseBody
	@RequestMapping("/findAllUpgradeTask")
	public List<StationUpgradeTask> findAllUpgradeTask() {
		List<StationUpgradeTask> tasks = stationUpgradeTaskService.findAll();
		return tasks;
	}

	@RequestMapping("/restart")
	@ResponseBody
	public String Restart(@RequestParam(name = "id") int id) {
		// RemoteShellExecutor executor = new RemoteShellExecutor("192.168.29.129",
		// "root", "123456");
		try {
			// System.out.println(executor.exec("shutdown -r now"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "重启成功";
	}

	@RequestMapping("/remoteAccess")
	@ResponseBody
	public String RemoteAccess(@RequestParam(name = "id") int id) {
		// RemoteShellExecutor executor = new RemoteShellExecutor("192.168.29.129",
		// "root", "123456");
		return "远程连接成功";
	}

	@RequestMapping("/saveaccountandpassword")
	@ResponseBody
	public String saveaccountandpassword(@RequestBody Map<String, Object> reqMap) {

		String account = reqMap.get("account").toString();
		String password = reqMap.get("password").toString();
		int id = Integer.parseInt(reqMap.get("id").toString());
		int type = Integer.parseInt(reqMap.get("type").toString());
		if (type == 6) {
			PlaybackController pc = pcService.findById(id);
			pc.setPc_account(account);
			pc.setPc_password(password);
			PlaybackController save = pcService.save(pc);
			return save == null ? "修改失败" : "修改成功";
		} else {
			StationServer server = stationServerService.findById(id);
			server.setStation_server_account(account);
			server.setStation_server_password(password);
			StationServer save = stationServerService.save(server);
			return save == null ? "修改失败" : "修改成功";
		}
	}

	
}
