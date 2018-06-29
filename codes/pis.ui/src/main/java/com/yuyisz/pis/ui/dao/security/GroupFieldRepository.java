package com.yuyisz.pis.ui.dao.security;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.yuyisz.pis.ui.module.security.GroupField;

@Repository
public interface GroupFieldRepository extends JpaRepository<GroupField, Integer>, JpaSpecificationExecutor<GroupField> {
	@Modifying
	@Transactional
	@Query(value="delete from t_groupfields where user_group_id = ?1",nativeQuery=true)
	void deleteByGroup(int groupId);
}
