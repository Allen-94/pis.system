package com.yuyisz.pis.ui.service.security;

import java.util.List;

import com.yuyisz.pis.ui.module.security.CommandSet;

public interface CmdsetService {
	CommandSet save(CommandSet cmdset);

	CommandSet findOneCmdset(Integer cmdsetId);

	List<CommandSet> findAll();
	
	List<CommandSet> findCmdsetsBelongToRole(Integer roleId);
}
