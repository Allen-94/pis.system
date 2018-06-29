package com.yuyisz.pis.ui.service.reserveplan;

import java.util.List;
import java.util.concurrent.ExecutorService;

import org.springframework.scheduling.annotation.AsyncResult;

import com.yuyisz.pis.ui.module.reserveplan.ReserverPlanTask;



public interface ReserverPlanTaskService {

	List<ReserverPlanTask> findAllPlanTask();
	void delByPlanId(int planId);
	void savePlanTask(ReserverPlanTask task);
	List<ReserverPlanTask> findAllPlanTaskByPlanId(int planId);
	ExecutorService runPlanTask(int id);
}
