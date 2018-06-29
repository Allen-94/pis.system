package com.yuyisz.pis.ui.dao.security;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.yuyisz.pis.ui.module.security.Command;
import com.yuyisz.pis.ui.module.security.UserGroup;

@Repository
public interface CmdRepository extends JpaRepository<Command, Integer>, JpaSpecificationExecutor<Command> {
	@Query(value = "select * from t_cmds where cmdset_id = ?1", nativeQuery = true)
	public Set<Command> findByCmdsetid(Integer cmdsetid);
	
	@Query(value = "select DISTINCT b.cmd_id,b.cmd_name,b.cmd_description,b.cmdset_id \r\n"
			+ "from cmds_roles a ,t_cmds b ,t_cmdsets c \r\n"
			+ "where role_id = ?1  and a.cmd_id = b.cmd_id and b.cmdset_id = c.cmdset_id ", nativeQuery = true)
	public List<Command> findAllCmdsBelongToRole(Integer roleId);

	@Query(value = "select DISTINCT b.cmd_id,b.cmd_name,b.cmd_description,b.cmdset_id \r\n"
			+ "from cmds_roles a ,t_cmds b ,t_cmdsets c \r\n"
			+ "where role_id = ?1 and b.cmdset_id = ?2 and a.cmd_id = b.cmd_id and b.cmdset_id = c.cmdset_id ", nativeQuery = true)
	public List<Command> findCmdsBelongTo_RoleCmdset(Integer roleId,Integer cmdset_id);
	
	@Query(value="select * from t_cmds where cmd_id in \r\n" + 
			"(select cmd_id from cmds_roles where role_id in \r\n" + 
			"(select role_id from group_roles where user_group_id =?1 ))",nativeQuery=true)
	List<Command> findPermissions(int groupId);
	
}
