package com.yuyisz.pis.ui.service.security.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yuyisz.pis.ui.dao.security.OperationsModeRepository;
import com.yuyisz.pis.ui.module.security.OperationsMode;
import com.yuyisz.pis.ui.service.security.OperationsModeService;
@Service
public class OperationsModeServiceImpl implements OperationsModeService {
	@Autowired
	private OperationsModeRepository operationsModeRepository;
	@Override
	public List<OperationsMode> findAllOperationsMode() {
		return operationsModeRepository.findAll();
	}
	@Override
	public void save(OperationsMode operationsMode) {
		operationsModeRepository.save(operationsMode);
	}
	@Override
	public OperationsMode findById(int id) {
		return operationsModeRepository.findById(id);
	}
	
}
