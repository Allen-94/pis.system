package com.yuyisz.pis.ui.service.subsystem.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yuyisz.pis.ui.dao.subsystem.ServiceInformationRepository;
import com.yuyisz.pis.ui.module.subsystem.MyService;
import com.yuyisz.pis.ui.module.subsystem.NameResult;
import com.yuyisz.pis.ui.service.subsystem.ServiceInformationService;
import com.yuyisz.pis.ui.util.SshUtils;

@Service
public class ServiceInformationServiceImpl implements ServiceInformationService {
	@Autowired
	private ServiceInformationRepository sir;

	@Override
	public List<MyService> findAll(File configFile) throws IOException {
		return sir.findAll(configFile);
	}

	@Override
	public List<MyService> findByClusterName(File configFile, String clusterName)
			throws IOException {
		return sir.findByClusterName(configFile, clusterName);
	}

	@Override
	public List<NameResult> getListByCname(List<MyService> list) {
		String Cname = "";
		List<NameResult> res = new ArrayList<NameResult>();
		NameResult on = new NameResult();
		List<MyService> clist = new ArrayList<MyService>();
		for (int i = 0; i < list.size(); i++) {
			MyService serve = list.get(i);
			if (!Cname.equals(serve.getClusterName())) {
				if ("".equals(Cname)) {
					Cname = serve.getClusterName();
					on.setClusterName(Cname);
				} else {
					on.setServiceList(clist);
					res.add(on);
					Cname = serve.getClusterName();
					on = new NameResult();
					clist = new ArrayList<MyService>();
					on.setClusterName(Cname);
				}
				clist.add(serve);
			} else {
				clist.add(serve);
			}
			if (i == list.size() - 1) {
				on.setServiceList(clist);
				res.add(on);
			}
		}
		return res;
	}

	@Override
	public List<NameResult> getCname(List<MyService> list) {
		Set<String> Cname = new HashSet<String>();
		List<NameResult> res = new ArrayList<NameResult>();
		for (MyService serve : list) {
			if (!Cname.contains(serve.getClusterName()) || Cname.size() == 0) {
				Cname.add(serve.getClusterName());
				NameResult clusterName = new NameResult();
				clusterName.setClusterName(serve.getClusterName());
				res.add(clusterName);
			}
		}
		return res;
	}

	@Override
	public List<MyService> findAll(List<MyService> list, int begin, int end) {
		List<MyService> newList = new ArrayList<MyService>();
		for (int i = begin; i <= end; i++) {
			newList.add(list.get(i));
		}
		return newList;
	}

	@Override
	public List<MyService> findByClusterName(List<MyService> serviceList,
			String clusterName) {
		List<MyService> list = new ArrayList<MyService>();
		for (MyService myService : serviceList) {
			if (clusterName.equals(myService.getClusterName())) {
				list.add(myService);
			}
		}
		return list;
	}

	@Override
	public boolean stopService(String clusterName) {
		MyService service = getIpPassword("SystemManagement");
		String cmd;
		if (clusterName == null || "".equals(clusterName)) {
			cmd = "bash /tmp/sys_install/bin/stop.sh"
					+ " >/dev/null 2>&1;echo $?";
		} else {
			cmd = "bash /tmp/sys_install/bin/stop.sh " + clusterName
					+ " >/dev/null 2>&1;echo $?";
		}
		String res = SshUtils.execCmd(cmd, "root", service.getVmIp(),
				service.getPassword());
		return 0 == Integer.parseInt(res);
	}

	@Override
	public boolean restartService(String clusterName) {
		MyService service = getIpPassword("SystemManagement");
		String cmd;
		if (clusterName == null || "".equals(clusterName)) {
			cmd = "bash /tmp/sys_install/bin/restart.sh"
					+ " >/dev/null 2>&1;echo $?";
		} else {
			cmd = "bash /tmp/sys_install/bin/restart.sh " + clusterName
					+ " >/dev/null 2>&1;echo $?";
		}
		String res = SshUtils.execCmd(cmd, "root", service.getVmIp(),
				service.getPassword());
		return 0 == Integer.parseInt(res);
	}

	@Override
	public boolean changeService(String clusterName) {
		MyService service = getIpPassword("SystemManagement");
		String cmd = "bash /tmp/sys_install/bin/change_service_node.sh "
				+ clusterName + " >/dev/null 2>&1;echo $?";
		String res = SshUtils.execCmd(cmd, "root", service.getVmIp(),
				service.getPassword());
		return 0 == Integer.parseInt(res);
	}

	@Override
	public boolean replaceService(String clusterName, String nodeType,
			String vmIp, String newVmName, String newVmIp,
			String newServerName, String newServerIp) {
		String str = "";
		if (!"".equals(newServerName) && !"".equals(newServerIp)) {
			str = " " + newServerName + " " + newServerIp + " ";
		}
		MyService service = getIpPassword("SystemManagement");
		String cmd = "bash /tmp/sys_install/bin/del_service_node.sh -s "
				+ clusterName + " " + nodeType + " " + vmIp + str
				+ " >/dev/null 2>&1;echo $?";
		String res = SshUtils.execCmd(cmd, "root", service.getVmIp(),
				service.getPassword());
		if (Integer.parseInt(res) != 0)
			return false;

		cmd = "bash /tmp/sys_install/bin/add_service_node.sh -s " + clusterName
				+ " " + nodeType + " " + newVmName + " " + newVmIp + str
				+ " >/dev/null 2>&1;echo $?";
		res = SshUtils.execCmd(cmd, "root", service.getVmIp(),
				service.getPassword());
		return 0 == Integer.parseInt(res);
	}

	@Override
	public boolean createService(String clusterName, String newVmName,
			String newVmIp, String newServerName, String newServerIp) {
		String str = "";
		if (!"".equals(newServerName) && !"".equals(newServerIp)) {
			str = " " + newServerName + " " + newServerIp + " ";
		}
		MyService service = getIpPassword("SystemManagement");
		String cmd = "bash /tmp/sys_install/bin/add_service_node.sh -s "
				+ clusterName + " Data " + newVmName + " " + newVmIp + str
				+ " >/dev/null 2>&1;echo $?";
		String res = SshUtils.execCmd(cmd, "root", service.getVmIp(),
				service.getPassword());
		return 0 == Integer.parseInt(res);
	}

	@Override
	public MyService getIpPassword(String clusterName) {
		MyService service = null;
		try {
			service = sir.getIpPassword(clusterName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return service;
	}

	@Override
	public List<NameResult> getServerName(List<MyService> serviceList) {
		Set<String> Sname = new HashSet<String>();
		List<NameResult> res = new ArrayList<NameResult>();
		for (MyService serve : serviceList) {
			if (!Sname.contains(serve.getServerName()) || Sname.size() == 0) {
				Sname.add(serve.getServerName());
				NameResult name = new NameResult();
				name.setServerName(serve.getServerName());
				res.add(name);
			}
		}
		return res;
	}

	@Override
	public List<MyService> findByServerName(File configFile, String serverName)
			throws IOException {
		return sir.findByServerName(configFile, serverName);
	}

	@Override
	public boolean replaceService(String addCmd, String delCmd) {
		MyService service = getIpPassword("SystemManagement");
		String res = SshUtils.execCmd(delCmd, "root", service.getVmIp(),
				service.getPassword());
		if (Integer.parseInt(res) != 0)
			return false;
		res = SshUtils.execCmd(addCmd, "root", service.getVmIp(),
				service.getPassword());
		if (Integer.parseInt(res) != 0)
			return false;
		String cmd = "bash /tmp/sys_install/bin/del_service_node.sh -m /opt/del_service.txt >/dev/null 2>&1;echo $?";
		res = SshUtils.execCmd(cmd, "root", service.getVmIp(),
				service.getPassword());
		if (Integer.parseInt(res) != 0)
			return false;

		cmd = "bash /tmp/sys_install/bin/add_service_node.sh -m /opt/add_service.txt >/dev/null 2>&1;echo $?";
		res = SshUtils.execCmd(cmd, "root", service.getVmIp(),
				service.getPassword());
		return 0 == Integer.parseInt(res);
	}

	@Override
	public Integer InstallMode() {
		MyService service = getIpPassword("SystemManagement");
		String cmd = "cat /tmp/sys_install/config/Install_mode.txt";
		String res = SshUtils.execCmd(cmd, "root", service.getVmIp(),
				service.getPassword());
		return Integer.parseInt(res);
	}

}
