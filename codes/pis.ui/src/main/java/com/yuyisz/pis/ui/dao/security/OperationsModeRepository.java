package com.yuyisz.pis.ui.dao.security;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.yuyisz.pis.ui.module.security.Command;
import com.yuyisz.pis.ui.module.security.OperationsMode;
@Repository
public interface OperationsModeRepository  extends JpaRepository<OperationsMode, Integer>, JpaSpecificationExecutor<OperationsMode>{

	OperationsMode findById(int id);
}
