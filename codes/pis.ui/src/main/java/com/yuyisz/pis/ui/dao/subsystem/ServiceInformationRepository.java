package com.yuyisz.pis.ui.dao.subsystem;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.yuyisz.pis.ui.module.subsystem.MyService;

public interface ServiceInformationRepository {
	public List<MyService> findAll(File configFile) throws IOException;

	public List<MyService> findByClusterName(File configFile, String clusterName)
			throws IOException;

	public MyService getIpPassword(String clusterName) throws IOException;

	public List<MyService> findByServerName(File configFile, String serverName)
			throws IOException;
}
