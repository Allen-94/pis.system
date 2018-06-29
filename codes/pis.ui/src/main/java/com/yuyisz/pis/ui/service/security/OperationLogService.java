package com.yuyisz.pis.ui.service.security;

import java.util.List;

import org.springframework.stereotype.Service;

import com.yuyisz.pis.ui.module.security.OperationLog;


public interface OperationLogService {
	/**
	 * 将日志保存在数据库中
	 * @param message
	 */
	public OperationLog save(OperationLog log);
	public List<OperationLog> findALl();
	public List<OperationLog> findByOperator(String name);
	public void deleteLog(int id);
	/**
	 * 过滤日志
	 * @param operator 操作人
	 * @param clientIP 客户端IP
	 * @param time 创建时间
	 * @param command 命令
	 * @param result 操作结果
	 * @return 
	 */
	public List<OperationLog> findByOperatorLike(String operator,String clientIP,String createTime,
			String command,String result);
}
