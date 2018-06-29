package com.yuyisz.pis.ui.service.infosources;

import java.util.List;

import com.yuyisz.pis.ui.module.infosources.SystemVariable;

public interface SystemVariableService {

	List<SystemVariable> findAll();

	SystemVariable saveSystemVariable(SystemVariable sv);

	void delSystemVariable(int id);

	SystemVariable findOne(int mainId);

}
