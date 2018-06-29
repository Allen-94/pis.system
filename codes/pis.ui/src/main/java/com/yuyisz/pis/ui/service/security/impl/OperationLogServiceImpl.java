package com.yuyisz.pis.ui.service.security.impl;

import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yuyisz.pis.ui.dao.security.OperationLogRepository;
import com.yuyisz.pis.ui.module.security.OperationLog;
import com.yuyisz.pis.ui.service.security.OperationLogService;

@Service
public class OperationLogServiceImpl implements OperationLogService{
	@Autowired
	OperationLogRepository logRepository;

	@Override
	public OperationLog save(OperationLog log) {
		return logRepository.save(log);
	}

	@Override
	public List<OperationLog> findALl() {
		return logRepository.findAll();
	}

	@Override
	public List<OperationLog> findByOperator(String name) {
		return logRepository.findByOperator(name);
	}

	@Override
	public void deleteLog(int id) {
		logRepository.delete(id);
	}

	@Override
	public List<OperationLog> findByOperatorLike(String operator,String clientIP,String createTime,
			String command,String result) {
		return logRepository.findByOperatorLike(operator,clientIP, createTime,
				command,result);
//		return logRepository.findbyDate(createTime);
	}
}
