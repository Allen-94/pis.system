package com.yuyisz.pis.ui.service.reserveplan;

import java.util.List;

import com.yuyisz.pis.ui.module.reserveplan.ReservePlan;

public interface ReservePlanService {

	List<ReservePlan> findAllPlan();

	ReservePlan findById(int id);

	ReservePlan savePlan(ReservePlan plan);

	void delPlan(int id);

	void Start(int id,boolean b);

}
