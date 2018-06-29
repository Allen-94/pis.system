package com.yuyisz.pis.ui.service.security;

import java.util.List;

import com.yuyisz.pis.ui.module.security.OperationsMode;

public interface OperationsModeService {
	/*
	 * 查询所有
	 */
	List<OperationsMode> findAllOperationsMode();
	/*
	 * 保存一个对象
	 */
	void save(OperationsMode operationsMode);
	/*
	 * 查询一个对象
	 */
	OperationsMode findById(int id);
	
}
