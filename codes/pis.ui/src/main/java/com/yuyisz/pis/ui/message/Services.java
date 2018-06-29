package com.yuyisz.pis.ui.message;

/**
 * 服务名称
 * @author zm
 * 2017-11-22
 */
public enum Services {
	
	SECURITYMANAGEMENTSERVICE("SecurityManagementService"),
	DEVICEMANAGEMENTSERVICE("DeviceManagementService");
	String serviceName;	//服务名
	
	private Services(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
}
