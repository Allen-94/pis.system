package com.yuyisz.pis.system.api.service.impl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yuyisz.pis.system.api.dao.SystemApiRepository;
import com.yuyisz.pis.system.api.module.CoreStatus;
import com.yuyisz.pis.system.api.module.MyService;
import com.yuyisz.pis.system.api.service.SystemApiService;

@Service
public class SystemApiServiceImpl implements SystemApiService {

	@Autowired
	private SystemApiRepository SARepository;

	@Override
	public List<CoreStatus> getCoreStatus() throws Exception {
		return SARepository.getCoreStatus();
	}

	@Override
	public CoreStatus findCoreByCName(String clusterName) throws Exception {
		List<CoreStatus> coreList = SARepository.getCoreStatus();
		for (CoreStatus coreStatus : coreList) {
			if (clusterName.equals(coreStatus.getClusterName())) {
				return coreStatus;
			}
		}
		return null;
	}

	@Override
	public List<MyService> findStatusByCName(String clusterName)
			throws Exception {
		List<MyService> oldList = SARepository.findStatus();
		List<MyService> newList = new ArrayList<MyService>();
		for (MyService myService : oldList) {
			if (clusterName.equals(myService.getClusterName())) {
				newList.add(myService);
			}
		}
		return newList;
	}

	@Override
	public MyService findStatusByIp(String vmIp) throws Exception {
		List<MyService> list = SARepository.findStatus();
		for (MyService myService : list) {
			if (vmIp.equals(myService.getVmIp())) {
				return myService;
			}
		}
		return null;
	}

	@Override
	public List<MyService> findStatus() throws Exception {
		return SARepository.findStatus();
	}

	@Override
	public String exec(String cmd) {
		String str = null;
		try {
			String[] cmdA = { "/bin/sh", "-c", cmd };
			Process pr = Runtime.getRuntime().exec(cmdA);
			pr.waitFor();
			BufferedReader br = new BufferedReader(new InputStreamReader(
					pr.getInputStream()));
			StringBuffer sb = new StringBuffer();
			String line;
			while ((line = br.readLine()) != null) {
				sb.append(line).append("\n");
			}
			str = sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}

	@Override
	public boolean stopService(String clusterName) {
		String cmd;
		if (clusterName == null || "".equals(clusterName)) {
			cmd = "bash /tmp/sys_install/bin/stop.sh"
					+ " >/dev/null 2>&1;echo $?";
		} else {
			cmd = "bash /tmp/sys_install/bin/stop.sh " + clusterName
					+ " >/dev/null 2>&1;echo $?";
		}
		String res = exec(cmd);
		return 0 == Integer.parseInt(res.trim());
	}

	@Override
	public boolean restartService(String clusterName) {
		String cmd;
		if (clusterName == null || "".equals(clusterName)) {
			cmd = "bash /tmp/sys_install/bin/restart.sh"
					+ " >/dev/null 2>&1;echo $?";
		} else {
			cmd = "bash /tmp/sys_install/bin/restart.sh " + clusterName
					+ " >/dev/null 2>&1;echo $?";
		}
		String res = exec(cmd);
		return 0 == Integer.parseInt(res.trim());
	}

	@Override
	public boolean startService(String clusterName) {
		String cmd;
		if (clusterName == null || "".equals(clusterName)) {
			cmd = "bash /tmp/sys_install/bin/start.sh"
					+ " >/dev/null 2>&1;echo $?";
		} else {
			cmd = "bash /tmp/sys_install/bin/start.sh " + clusterName
					+ " >/dev/null 2>&1;echo $?";
		}
		String res = exec(cmd);
		return 0 == Integer.parseInt(res.trim());
	}

	@Override
	public boolean changeService(String clusterName) {
		String cmd = "bash /tmp/sys_install/bin/change_service_node.sh "
				+ clusterName + " >/dev/null 2>&1;echo $?";
		String res = exec(cmd);
		return 0 == Integer.parseInt(res.trim());
	}

	@Override
	public boolean replaceService(String clusterName, String nodeType,
			String vmIp, String newVmName, String newVmIp,
			String newServerName, String newServerIp) {
		String str = "";
		if (!"".equals(newServerName) && !"".equals(newServerIp)) {
			str = " " + newServerName + " " + newServerIp + " ";
		}
		String cmd = "bash /tmp/sys_install/bin/del_service_node.sh -s "
				+ clusterName + " " + nodeType + " " + vmIp + str
				+ " >/dev/null 2>&1;echo $?";
		String res = exec(cmd);
		if (Integer.parseInt(res.trim()) != 0)
			return false;

		cmd = "bash /tmp/sys_install/bin/add_service_node.sh -s " + clusterName
				+ " " + nodeType + " " + newVmName + " " + newVmIp + str
				+ " >/dev/null 2>&1;echo $?";
		res = exec(cmd);
		return 0 == Integer.parseInt(res.trim());
	}

	@Override
	public Integer InstallMode() {
		String cmd = "cat /tmp/sys_install/config/Install_mode.txt";
		String res = exec(cmd);
		return Integer.parseInt(res.trim());
	}

	@Override
	public boolean createService(String clusterName, String newVmName,
			String newVmIp, String newServerName, String newServerIp) {
		String str = "";
		if (!"".equals(newServerName) && !"".equals(newServerIp)) {
			str = " " + newServerName + " " + newServerIp + " ";
		}
		String cmd = "bash /tmp/sys_install/bin/add_service_node.sh -s "
				+ clusterName + " Data " + newVmName + " " + newVmIp + str
				+ " >/dev/null 2>&1;echo $?";
		String res = exec(cmd);
		return 0 == Integer.parseInt(res.trim());
	}

}
