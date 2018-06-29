package com.yuyisz.pis.ui.controller.subsystem.centersubsystem;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yuyisz.pis.ui.module.subsystem.NameResult;
import com.yuyisz.pis.ui.module.subsystem.JqgridListForm;
import com.yuyisz.pis.ui.module.subsystem.MyService;
import com.yuyisz.pis.ui.module.subsystem.Result;
import com.yuyisz.pis.ui.service.subsystem.ServiceInformationService;
import com.yuyisz.pis.ui.util.SshUtils;

/*
 * 中心子系统
 */
@Controller
@RequestMapping("/subsystem")
public class CenterSubsystemController {

	@Autowired
	private ServiceInformationService sis;
	private String osName = System.getProperties().getProperty("os.name");
	private String csvFile = "/opt/sys_status.csv";
	public static String CoreFile = "/opt/Core_service_password.csv";
	private List<MyService> serviceList = null;

	// controller初始化方法
	public CenterSubsystemController() {
		if (osName.indexOf("Windows") != -1) {
			String path = ClassUtils.getDefaultClassLoader().getResource("")
					.getPath();
			csvFile = path + "sys_status.csv";
			CoreFile = path + "Core_service_password.csv";
			getCsvFile();
		}
		// Thread myThread = new Thread(new Runnable() {
		// @Override
		// public void run() {
		// while (true) {
		// if (osName.indexOf("Windows") != -1) {
		// getCsvFile();
		// }
		// try {
		// // 每隔2分钟重新执行一次，获取最新的csv，服务信息文件
		// Thread.sleep(120000);
		// } catch (InterruptedException e) {
		// e.printStackTrace();
		// }
		// }
		// }
		// });
		// myThread.start();
	}

	// 获取所有的集群名称
	@GetMapping(value = "/getClusterList")
	@ResponseBody
	public Result getClusterList() {

		// if (osName.indexOf("Windows") != -1) {
		// getCsvFile();
		// }

		File configFile = new File(csvFile);
		serviceList = new ArrayList<MyService>();
		try {
			serviceList = sis.findAll(configFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		List<NameResult> cn = sis.getCname(serviceList);
		return Result.success(cn);
	}

	// 获取所有的服务器名称
	@GetMapping(value = "/getServerList")
	@ResponseBody
	public Result getServerList() {

		// if (osName.indexOf("Windows") != -1) {
		// getCsvFile();
		// }

		File configFile = new File(csvFile);
		serviceList = new ArrayList<MyService>();
		try {
			serviceList = sis.findAll(configFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		List<NameResult> cn = sis.getServerName(serviceList);
		return Result.success(cn);
	}

	// 根据集群名称获得相应的集群中的服务列表，带分页
	@GetMapping(value = "/getList")
	@ResponseBody
	public Result getList(
			@RequestParam(value = "page", defaultValue = "1") String page,
			@RequestParam(value = "rows", defaultValue = "10") String rows,
			@RequestParam(value = "clusterName") String clusterName) {
		List<MyService> list = new ArrayList<MyService>();
		if (serviceList == null) {
			if (osName.indexOf("Windows") != -1) {
				getCsvFile();
			}

			File configFile = new File(csvFile);

			try {
				list = sis.findByClusterName(configFile, clusterName);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			list = sis.findByClusterName(serviceList, clusterName);
		}
		JqgridListForm slist = new JqgridListForm();
		int records = list.size();
		slist.setRecords(records);
		int total, begin, end;
		if (records >= Integer.parseInt(rows)) {
			if (records % Integer.parseInt(rows) == 0) {
				total = records / Integer.parseInt(rows);
			} else {
				total = records / Integer.parseInt(rows) + 1;
			}
		} else {
			total = 1;
		}
		begin = Integer.parseInt(page) * Integer.parseInt(rows)
				- Integer.parseInt(rows);
		end = Integer.parseInt(page) * Integer.parseInt(rows) - 1;
		if (end > records - 1) {
			end = records - 1;
		}

		list = sis.findAll(list, begin, end);
		slist.setFormList(list);
		slist.setPage(Integer.parseInt(page));
		slist.setTotal(total);
		return Result.success(slist);
	}

	// 根据集群名称获得相应的集群中的服务列表
	@GetMapping(value = "/getListByClusterName")
	@ResponseBody
	public Result getList(
			@RequestParam(value = "clusterName") String clusterName) {
		List<MyService> list = new ArrayList<MyService>();
		File configFile = new File(csvFile);
		try {
			list = sis.findByClusterName(configFile, clusterName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Result.success(list);
	}

	// 根据服务器名称获得相应的集群中的服务列表
	@GetMapping(value = "/getListByServerName")
	@ResponseBody
	public Result getListByServerName(
			@RequestParam(value = "serverName") String serverName) {
		List<MyService> list = new ArrayList<MyService>();
		File configFile = new File(csvFile);
		try {
			list = sis.findByServerName(configFile, serverName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Result.success(list);
	}

	// 获取配置文件路径和csv服务信息文件方法
	public void getCsvFile() {
		String path = ClassUtils.getDefaultClassLoader().getResource("")
				.getPath();
		String ip = "192.168.0.123";
		String pwd = "Admin@123";
		try {
			SshUtils.sshSftp(ip, "root", pwd, "/opt", "sys_status.csv", path);
			SshUtils.sshSftp(ip, "root", pwd, "/opt/",
					"Core_service_password.csv", path);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 获取服务状态列表方法
	@GetMapping(value = "/getServiceList")
	@ResponseBody
	public Result getServiceList() {
		// if (osName.indexOf("Windows") != -1) {
		// getCsvFile();
		// }
		File configFile = new File(csvFile);
		serviceList = new ArrayList<MyService>();
		try {
			serviceList = sis.findAll(configFile);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return Result.success(serviceList);
	}

	// 停止服务方法
	@PostMapping(value = "/stopService")
	@ResponseBody
	public Result stopService(
			@RequestParam(value = "clusterName", defaultValue = "") String clusterName) {
		sis.stopService(clusterName);
		return Result.success(null);
	}

	// 重启服务方法，返回json数据
	@PostMapping(value = "/restartService")
	@ResponseBody
	public Result restartService(
			@RequestParam(value = "clusterName", defaultValue = "") String clusterName) {
		sis.restartService(clusterName);
		return Result.success(null);
	}

	// 重启服务方法，返回到服务列表页面
	@PostMapping(value = "/restartsService")
	public String restartsService(
			@RequestParam(value = "clusterName", defaultValue = "") String clusterName) {
		sis.restartService(clusterName);
		return "/subsystem/serviceStatusList";
	}

	// 指定集群更换主备方法
	@PostMapping(value = "/changeService")
	public String changeService(
			@RequestParam(value = "clusterName") String clusterName) {
		sis.changeService(clusterName);
		return "/subsystem/serviceStatusList";
	}

	// 服务节点替换
	@PostMapping(value = "/replaceService")
	@ResponseBody
	public Result replaceService(
			@RequestParam(value = "clusterName") String clusterName,
			@RequestParam(value = "nodeType") String nodeType,
			@RequestParam(value = "vmIp") String vmIp,
			@RequestParam(value = "newVmName") String newVmName,
			@RequestParam(value = "newVmIp") String newVmIp,
			@RequestParam(value = "newServerName", defaultValue = "") String newServerName,
			@RequestParam(value = "newServerIp", defaultValue = "") String newServerIp) {
		boolean flag = sis.replaceService(clusterName, nodeType, vmIp,
				newVmName, newVmIp, newServerName, newServerIp);
		if (flag) {
			return Result.success(null);
		}
		return new Result(400, "更换失败");
	}

	// 服务器服务节点替换
	@PostMapping(value = "/replaceServerService")
	@ResponseBody
	public Result replaceServerService(
			@RequestParam(value = "addCmd") String addCmd,
			@RequestParam(value = "delCmd") String delCmd) {

		boolean flag = sis.replaceService(addCmd, delCmd);
		if (flag) {
			return Result.success(null);
		}
		return new Result(400, "更换失败");
	}

	// 添加数据节点
	@PostMapping(value = "/createService")
	@ResponseBody
	public Result createService(
			@RequestParam(value = "clusterName") String clusterName,
			@RequestParam(value = "newVmName") String newVmName,
			@RequestParam(value = "newVmIp") String newVmIp,
			@RequestParam(value = "newServerName", defaultValue = "") String newServerName,
			@RequestParam(value = "newServerIp", defaultValue = "") String newServerIp) {
		boolean flag = sis.createService(clusterName, newVmName, newVmIp,
				newServerName, newServerIp);
		if (flag) {
			return Result.success(null);
		}
		return new Result(400, "更换失败");
	}

	@GetMapping(value = "/InstallMode")
	@ResponseBody
	public Integer InstallMode() {
		return sis.InstallMode();
	}

}
