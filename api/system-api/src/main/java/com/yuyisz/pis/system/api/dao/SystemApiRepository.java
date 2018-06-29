package com.yuyisz.pis.system.api.dao;

import java.util.List;

import com.yuyisz.pis.system.api.module.CoreStatus;
import com.yuyisz.pis.system.api.module.MyService;

public interface SystemApiRepository {

	// 获取Core_service_password配置文件的 所有信息
	public List<CoreStatus> getCoreStatus() throws Exception;

	// 获取所有服务的信息
	public List<MyService> findStatus() throws Exception;
}
