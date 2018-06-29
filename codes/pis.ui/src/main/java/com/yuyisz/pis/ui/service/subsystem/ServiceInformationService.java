package com.yuyisz.pis.ui.service.subsystem;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.yuyisz.pis.ui.module.subsystem.NameResult;
import com.yuyisz.pis.ui.module.subsystem.MyService;

public interface ServiceInformationService {
	public List<MyService> findAll(File configFile) throws IOException;

	public List<MyService> findByClusterName(File configFile, String clusterName)
			throws IOException;

	public MyService getIpPassword(String clusterName);

	public List<NameResult> getListByCname(List<MyService> list);

	public List<NameResult> getCname(List<MyService> list);

	public List<MyService> findAll(List<MyService> list, int begin, int end);

	public List<MyService> findByClusterName(List<MyService> serviceList,
			String clusterName);

	public boolean restartService(String clusterName);

	public boolean stopService(String clusterName);

	public boolean changeService(String clusterName);

	public boolean replaceService(String clusterName, String nodeType,
			String vmIp, String newVmName, String newVmIp,
			String newServerName, String newServerIp);

	public boolean createService(String clusterName, String newVmName,
			String newVmIp, String newServerName, String newServerIp);

	public List<NameResult> getServerName(List<MyService> serviceList);

	public List<MyService> findByServerName(File configFile, String serverName)
			throws IOException;

	public boolean replaceService(String addCmd, String delCmd);

	public Integer InstallMode();
}
