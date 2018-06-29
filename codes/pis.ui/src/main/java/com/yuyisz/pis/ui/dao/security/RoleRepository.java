package com.yuyisz.pis.ui.dao.security;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.yuyisz.pis.ui.module.security.Roles;

@Repository
public interface RoleRepository extends JpaRepository<Roles, Integer>, JpaSpecificationExecutor<Roles> {
	//条件查询
	@Query(value="select * from t_roles where role_name like %?1% and role_description like %?2% ;",nativeQuery=true)
	List<Roles> findbyFilter(String nameFilter, String descriptionFilter);
	//查询用户组名下角色组
	@Query(value="select c.role_id,c.role_name,c.role_description,c.create_time,c.update_time \r\n" + 
			"from group_roles a, t_usergroups b,t_roles c \r\n" + 
			"where b.group_id = a.user_group_id and c.role_id = a.role_id \r\n" + 
			"and b.group_id =?1",nativeQuery=true)
	List<Roles> findRolesOfGroup(Integer groupId);
	//查询指定名字的角色
	@Query(value="select * from t_roles where role_name = ?1",nativeQuery=true)
	Roles findByRoleName(String roleName);
	
}
