package com.yuyisz.pis.ui.service.infosources.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yuyisz.pis.ui.dao.infosource.SystemVariableRepository;
import com.yuyisz.pis.ui.module.infosources.SystemVariable;
import com.yuyisz.pis.ui.service.infosources.SystemVariableService;
@Service
public class SystemVariableServiceImpl implements SystemVariableService {

	@Autowired
	private SystemVariableRepository systemVariableRepository;
	
	@Override
	public List<SystemVariable> findAll() {
		return systemVariableRepository.findAll();
	}

	@Override
	public SystemVariable saveSystemVariable(SystemVariable sv) {
		return systemVariableRepository.saveAndFlush(sv);
	}

	@Override
	public void delSystemVariable(int id) {
		systemVariableRepository.delete(id);;
	}

	@Override
	public SystemVariable findOne(int mainId) {
		return systemVariableRepository.findOne(mainId);
	}

}
