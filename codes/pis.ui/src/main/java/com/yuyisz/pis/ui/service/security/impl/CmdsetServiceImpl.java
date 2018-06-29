package com.yuyisz.pis.ui.service.security.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yuyisz.pis.ui.dao.security.CmdsetRepository;
import com.yuyisz.pis.ui.module.security.Command;
import com.yuyisz.pis.ui.module.security.CommandSet;
import com.yuyisz.pis.ui.service.security.CmdService;
import com.yuyisz.pis.ui.service.security.CmdsetService;

@Service
public class CmdsetServiceImpl implements CmdsetService{
	@Autowired
	CmdsetRepository cmdsetRepository;
	
	@Override
	public CommandSet save(CommandSet cmdset) {
		return cmdsetRepository.save(cmdset);
	}

	@Override
	public CommandSet findOneCmdset(Integer cmdsetId) {
		// TODO Auto-generated method stub
		return cmdsetRepository.findOne(cmdsetId);
	}

	@Override
	public List<CommandSet> findAll() {
		return cmdsetRepository.findAll();
	}

	@Override
	public List<CommandSet> findCmdsetsBelongToRole(Integer roleId) {
		return cmdsetRepository.findCmdsetsBelongToRole(roleId);
	}

	

}
