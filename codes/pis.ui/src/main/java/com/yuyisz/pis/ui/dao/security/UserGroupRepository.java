package com.yuyisz.pis.ui.dao.security;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.yuyisz.pis.ui.module.security.UserGroup;

@Repository
public interface UserGroupRepository extends JpaRepository<UserGroup, Integer>, JpaSpecificationExecutor<UserGroup> {
	@Query(value="select * from t_usergroups where group_name like %?1% and group_description like %?2% ;",nativeQuery=true)
	List<UserGroup> findbyFilter(String nameFilter, String descriptionFilter);
	/**
	 * 条件过滤
	 * @param userId
	 * @return
	 */
	@Query(value="select * \r\n" + 
			"from t_users a,t_usergroups b \r\n" + 
			"where a.user_id=?1 and a.group_id = b.group_id",nativeQuery=true)
	UserGroup findGroupOfUser(int userId);

	@Query(value="select * from t_usergroups where group_name = ?1",nativeQuery=true)
	UserGroup findByGroupName(String groupName);
}
