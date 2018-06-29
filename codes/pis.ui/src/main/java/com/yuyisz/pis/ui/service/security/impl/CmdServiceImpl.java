package com.yuyisz.pis.ui.service.security.impl;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yuyisz.pis.ui.dao.security.CmdRepository;
import com.yuyisz.pis.ui.module.security.Command;
import com.yuyisz.pis.ui.service.security.CmdService;
@Service
public class CmdServiceImpl implements CmdService{
	@Autowired
	private CmdRepository cmdRepository;

	@Override
	public Command save(Command cmd) {
		Command result = cmdRepository.save(cmd);
		return result;
	}

	@Override
	public List<Command> getTen() {
		return cmdRepository.findAll();
	}

	@Override
	public Set<Command> findByCmdsetid(Integer cmdsetid) {
		return cmdRepository.findByCmdsetid(cmdsetid);
	}
	
	@Override
	public List<Command> findAllCmdsBelongToRole(Integer roleId) {
		return cmdRepository.findAllCmdsBelongToRole(roleId);
	}

	@Override
	public List<Command> findCmdsBelongTo_RoleCmdset(Integer roleId,Integer comset_id) {
		return cmdRepository.findCmdsBelongTo_RoleCmdset(roleId,comset_id);
	}

	@Override
	public Command findOneCmd(int cmdId) {
		return cmdRepository.findOne(cmdId);
	}

	@Override
	public List<Command> finAll() {
		return cmdRepository.findAll();
	}

	@Override
	public List<Command> findPermissions(int groupId) {
		return cmdRepository.findPermissions(groupId);
	}


	
	
}
