package com.yuyisz.pis.ui.service.security;

import java.util.List;
import java.util.Set;

import com.yuyisz.pis.ui.module.security.Command;

public interface CmdService {
	Command save(Command cmd);
	
	List<Command> getTen();

	Set<Command> findByCmdsetid(Integer cmdsetid);
	
	List<Command> findAllCmdsBelongToRole(Integer roleId);
	
	List<Command> findCmdsBelongTo_RoleCmdset(Integer roleId,Integer cmset_id);

	Command findOneCmd(int cmdId);

	List<Command> finAll();
	
	/**
	 * 获取用户所有的命令
	 * @param groupId
	 * @return
	 */
	public List<Command> findPermissions(int groupId);
}
