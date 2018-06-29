package com.yuyisz.pis.system.api.service;

import java.util.List;

import com.yuyisz.pis.system.api.module.CoreStatus;
import com.yuyisz.pis.system.api.module.MyService;

public interface SystemApiService {
	// 获取所有核心信息
	public List<CoreStatus> getCoreStatus() throws Exception;

	// 根据集群名称取得核心信息
	public CoreStatus findCoreByCName(String clusterName) throws Exception;

	// 根据集群名称，获取该集群下的所有服务
	public List<MyService> findStatusByCName(String clusterName)
			throws Exception;

	// 获取所有服务信息
	public List<MyService> findStatus() throws Exception;

	// 根据虚拟机Ip获取该服务的所有信息
	public MyService findStatusByIp(String vmIp) throws Exception;

	// 在当前linux环境中执行某条命令
	public String exec(String cmd);

	// 关闭服务
	public boolean stopService(String clusterName);

	// 重启服务
	public boolean restartService(String clusterName);

	// 启动服务
	public boolean startService(String clusterName);

	// 主备更换
	public boolean changeService(String clusterName);

	// 服务替换
	public boolean replaceService(String clusterName, String nodeType,
			String vmIp, String newVmName, String newVmIp,
			String newServerName, String newServerIp);

	// 查询安装模式
	public Integer InstallMode();

	// 添加数据节点
	public boolean createService(String clusterName, String newVmName,
			String newVmIp, String newServerName, String newServerIp);

}
