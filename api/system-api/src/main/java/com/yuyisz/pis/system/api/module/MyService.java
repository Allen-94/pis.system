package com.yuyisz.pis.system.api.module;

import java.io.Serializable;

/**
 * @author 余荣生
 **/
public class MyService implements Serializable {
	private static final long serialVersionUID = 1L;
	// 集群名称,节点类型,所在虚拟机名称,虚拟机IP,浮动IP,服务状态,所在服务器名,所在服务器IP,CPU占有率,内存占有率,硬盘占有率,,
	private String clusterName;
	private String nodeType;
	private String vmName;
	private String vmIp;
	private String floatIp;
	private Integer status;
	private String serverName;
	private String serverIp;
	private String cpuUtilization;
	private String memUtilization;
	private String diskUtilization;

	public String getClusterName() {
		return clusterName;
	}

	public void setClusterName(String clusterName) {
		this.clusterName = clusterName;
	}

	public String getNodeType() {
		return nodeType;
	}

	public void setNodeType(String nodeType) {
		this.nodeType = nodeType;
	}

	public String getVmName() {
		return vmName;
	}

	public void setVmName(String vmName) {
		this.vmName = vmName;
	}

	public String getVmIp() {
		return vmIp;
	}

	public void setVmIp(String vmIp) {
		this.vmIp = vmIp;
	}

	public String getFloatIp() {
		return floatIp;
	}

	public void setFloatIp(String floatIp) {
		this.floatIp = floatIp;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public String getServerIp() {
		return serverIp;
	}

	public void setServerIp(String serverIp) {
		this.serverIp = serverIp;
	}

	public String getCpuUtilization() {
		return cpuUtilization;
	}

	public void setCpuUtilization(String cpuUtilization) {
		this.cpuUtilization = cpuUtilization;
	}

	public String getMemUtilization() {
		return memUtilization;
	}

	public void setMemUtilization(String memUtilization) {
		this.memUtilization = memUtilization;
	}

	public String getDiskUtilization() {
		return diskUtilization;
	}

	public void setDiskUtilization(String diskUtilization) {
		this.diskUtilization = diskUtilization;
	}

}
