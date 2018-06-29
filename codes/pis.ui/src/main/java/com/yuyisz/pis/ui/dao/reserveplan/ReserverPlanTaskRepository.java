package com.yuyisz.pis.ui.dao.reserveplan;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.yuyisz.pis.ui.module.reserveplan.ReserverPlanTask;
@Repository
public interface ReserverPlanTaskRepository extends JpaRepository<ReserverPlanTask, String>, JpaSpecificationExecutor<ReserverPlanTask> {

	@Modifying
	@Query("delete from ReserverPlanTask t where t.reservePlanId=?1")
	void delByPlanId(Integer planId);

	//List<ReserverPlanTask> findByReservePlanIdOrderByPlayerIdAndOrderByCmdId(int planId);

	List<ReserverPlanTask> findByReservePlanId(int planId, Sort sort);
	
}
