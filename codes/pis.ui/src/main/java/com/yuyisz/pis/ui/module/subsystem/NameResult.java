package com.yuyisz.pis.ui.module.subsystem;

import java.io.Serializable;
import java.util.List;

public class NameResult implements Serializable {
	private static final long serialVersionUID = 1L;
	private String clusterName;
	private String serverName;
	private List<MyService> serveList;

	public String getClusterName() {
		return clusterName;
	}

	public void setClusterName(String clusterName) {
		this.clusterName = clusterName;
	}

	public List<MyService> getServiceList() {
		return serveList;
	}

	public void setServiceList(List<MyService> serveList) {
		this.serveList = serveList;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}
}
