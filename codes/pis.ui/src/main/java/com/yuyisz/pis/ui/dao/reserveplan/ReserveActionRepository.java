package com.yuyisz.pis.ui.dao.reserveplan;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.yuyisz.pis.ui.module.reserveplan.ReserveAction;
@Repository
public interface ReserveActionRepository extends JpaRepository<ReserveAction, Integer>, JpaSpecificationExecutor<ReserveAction> {

}
