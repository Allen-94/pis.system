package com.yuyisz.pis.ui.dao.infosource;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.yuyisz.pis.ui.module.infosources.SystemVariable;

@Repository
public interface SystemVariableRepository extends JpaRepository<SystemVariable, Integer>,JpaSpecificationExecutor<SystemVariable> {

}
