package com.yuyisz.pis.ui.dao.security;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.yuyisz.pis.ui.module.security.Command;
import com.yuyisz.pis.ui.module.security.Roles;
import com.yuyisz.pis.ui.module.security.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>, JpaSpecificationExecutor<User> {

	@Query(value="select * from t_users where account like %?1% and real_name like %?2% ;",nativeQuery=true)
	List<User> findbyFilter(String accountFilter, String realnameFilter);

	User findByAccountAndPassword(String username, String password);

	User findByAccount(String account);
	
	@Query(value="select * from t_users where group_id = ?1 limit 1",nativeQuery=true)
	User findByUserGroup(int groupId);
	
}
