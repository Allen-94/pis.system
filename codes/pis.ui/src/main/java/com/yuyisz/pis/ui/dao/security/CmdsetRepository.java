package com.yuyisz.pis.ui.dao.security;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.yuyisz.pis.ui.module.security.Command;
import com.yuyisz.pis.ui.module.security.CommandSet;
import com.yuyisz.pis.ui.module.security.UserGroup;

@Repository
public interface CmdsetRepository extends JpaRepository<CommandSet, Integer>, JpaSpecificationExecutor<CommandSet> {
	@Query(value = "select DISTINCT c.cmdset_id,c.cmdset_name,c.cmdset_description from cmds_roles a ,t_cmds b ,t_cmdsets c \r\n"
			+ "where role_id = ?1 and a.cmd_id = b.cmd_id and b.cmdset_id = c.cmdset_id ", nativeQuery = true)
	public List<CommandSet> findCmdsetsBelongToRole(Integer roleId);
}
