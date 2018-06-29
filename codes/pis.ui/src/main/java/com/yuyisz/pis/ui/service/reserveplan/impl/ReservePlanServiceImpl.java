package com.yuyisz.pis.ui.service.reserveplan.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yuyisz.pis.ui.dao.reserveplan.ReservePlanRepository;
import com.yuyisz.pis.ui.module.reserveplan.ReservePlan;
import com.yuyisz.pis.ui.service.reserveplan.ReservePlanService;
@Service
public class ReservePlanServiceImpl implements ReservePlanService {

	@Autowired
	private ReservePlanRepository reservePlanRepository;
	
	@Override
	public List<ReservePlan> findAllPlan() {
		return reservePlanRepository.findAll();
	}

	@Override
	public ReservePlan findById(int id) {
		return reservePlanRepository.findOne(id);
	}

	@Override
	public ReservePlan savePlan(ReservePlan plan) {
		return reservePlanRepository.saveAndFlush(plan);
	}

	@Override
	public void delPlan(int id) {
		reservePlanRepository.delete(id);		
	}

	@Override
	public void Start(int id,boolean b) {
		ReservePlan plan = reservePlanRepository.findOne(id);
		plan.setState(true);
		reservePlanRepository.save(plan);
	}

}
