package com.yuyisz.pis.system.api.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yuyisz.pis.system.api.module.CoreStatus;
import com.yuyisz.pis.system.api.module.MyService;
import com.yuyisz.pis.system.api.module.Result;
import com.yuyisz.pis.system.api.module.ResultCode;
import com.yuyisz.pis.system.api.service.SystemApiService;

@Controller
@RequestMapping("/system-api")
public class SystemApiController {

	@Autowired
	private SystemApiService SAService;

	// 获取所有服务的核心信息(核心信息:该服务的IP，浮动IP及密码等)
	@GetMapping("findAllServiceInfo")
	@ResponseBody
	public Result findAllServiceInfo() {
		List<CoreStatus> stat = null;
		try {
			stat = SAService.getCoreStatus();
			return Result.success(stat);
		} catch (Exception e) {
			e.printStackTrace();
			return Result.failure(ResultCode.FAILURE);
		}
	}

	// 根据集群获取相应的核心信息
	@GetMapping("findServiceInfo")
	@ResponseBody
	public Result findServiceInfo(
			@RequestParam(value = "clusterName", defaultValue = "") String clusterName) {
		CoreStatus stat = null;
		if (!"".equals(clusterName)) {
			try {
				stat = SAService.findCoreByCName(clusterName);
				return Result.success(stat);
			} catch (Exception e) {
				e.printStackTrace();
				return Result.failure(ResultCode.FAILURE);
			}
		} else {
			return Result.failure(401, "请求参数有误！clusterName值不能为空");
		}
	}

	// 根据集群名称，获取相应的服务状态列表
	@GetMapping("findStatusByCName")
	@ResponseBody
	public Result findStatusByCName(
			@RequestParam(value = "clusterName", defaultValue = "") String clusterName) {
		List<MyService> stat = null;
		if (!"".equals(clusterName)) {
			try {
				stat = SAService.findStatusByCName(clusterName);
				return Result.success(stat);
			} catch (Exception e) {
				e.printStackTrace();
				return Result.failure(ResultCode.FAILURE);
			}
		} else {
			return Result.failure(401, "请求e参数有误！clusterName值不能为空");
		}
	}

	// 根据虚拟机IP获取相应的服务状态
	@GetMapping("findStatusByIp")
	@ResponseBody
	public Result findStatusByIp(
			@RequestParam(value = "vmIp", defaultValue = "") String vmIp) {
		MyService stat = null;
		if (!"".equals(vmIp)) {
			try {
				stat = SAService.findStatusByIp(vmIp);
				return Result.success(stat);
			} catch (Exception e) {
				e.printStackTrace();
				return Result.failure(ResultCode.FAILURE);
			}
		} else {
			return Result.failure(401, "请求参数有误！vmIp值不能为空");
		}
	}

	// 获取所有服务状态
	@GetMapping("findAllStatus")
	@ResponseBody
	public Result findAllStatus() {
		List<MyService> stat = null;
		try {
			stat = SAService.findStatus();
			return Result.success(stat);
		} catch (Exception e) {
			e.printStackTrace();
			return Result.failure(ResultCode.FAILURE);
		}
	}

	@GetMapping("runTest")
	@ResponseBody
	public Result runTest(@RequestParam(value = "cmd") String cmd) {
		String str = SAService.exec(cmd);
		System.out.println(str);
		System.out.println("----------------------------------");
		System.out
				.println(SAService
						.exec("bash /tmp/sys_install/bin/add_service_node.sh -m /opt/add_service.txt >/dev/null 2>&1;echo $?"));
		return Result.success(str);
	}

	// 停止服务方法
	@PostMapping("stopService")
	@ResponseBody
	public Result stopService(
			@RequestParam(value = "clusterName", defaultValue = "") String clusterName) {
		boolean falg = SAService.stopService(clusterName);
		if (falg) {
			return Result.success(null);
		}
		return Result.failure(ResultCode.FAILURE);
	}

	// 重启服务方法
	@PostMapping("restartService")
	@ResponseBody
	public Result restartService(
			@RequestParam(value = "clusterName", defaultValue = "") String clusterName) {
		boolean falg = SAService.restartService(clusterName);
		if (falg) {
			return Result.success(null);
		}
		return Result.failure(ResultCode.FAILURE);
	}

	// 启动服务方法
	@PostMapping("startService")
	@ResponseBody
	public Result startService(
			@RequestParam(value = "clusterName", defaultValue = "") String clusterName) {
		boolean falg = SAService.startService(clusterName);
		if (falg) {
			return Result.success(null);
		}
		return Result.failure(ResultCode.FAILURE);
	}

	// 指定集群更换主备方法
	@PostMapping("changeService")
	@ResponseBody
	public Result changeService(
			@RequestParam(value = "clusterName") String clusterName) {
		boolean falg = SAService.changeService(clusterName);
		if (falg) {
			return Result.success(null);
		}
		return Result.failure(ResultCode.FAILURE);
	}

	// 服务节点替换
	@PostMapping("replaceService")
	@ResponseBody
	public Result replaceService(
			@RequestParam(value = "clusterName") String clusterName,
			@RequestParam(value = "nodeType") String nodeType,
			@RequestParam(value = "vmIp") String vmIp,
			@RequestParam(value = "newVmName") String newVmName,
			@RequestParam(value = "newVmIp") String newVmIp,
			@RequestParam(value = "newServerName", defaultValue = "") String newServerName,
			@RequestParam(value = "newServerIp", defaultValue = "") String newServerIp) {
		boolean flag = SAService.replaceService(clusterName, nodeType, vmIp,
				newVmName, newVmIp, newServerName, newServerIp);
		if (flag) {
			return Result.success(null);
		}
		return new Result(400, "更换失败");
	}

	// 查询安装模式
	@GetMapping("InstallMode")
	@ResponseBody
	public Result InstallMode() {
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("mode", SAService.InstallMode());
		return Result.success(map);
	}

	// 添加数据节点
	@PostMapping("createService")
	@ResponseBody
	public Result createService(
			@RequestParam(value = "clusterName") String clusterName,
			@RequestParam(value = "newVmName") String newVmName,
			@RequestParam(value = "newVmIp") String newVmIp,
			@RequestParam(value = "newServerName", defaultValue = "") String newServerName,
			@RequestParam(value = "newServerIp", defaultValue = "") String newServerIp) {
		boolean flag = SAService.createService(clusterName, newVmName, newVmIp,
				newServerName, newServerIp);
		if (flag) {
			return Result.success(null);
		}
		return new Result(400, "更换失败");
	}

}
