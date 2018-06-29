package com.yuyisz.pis.ui.service.reserveplan.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yuyisz.pis.ui.dao.reserveplan.ReserveActionRepository;
import com.yuyisz.pis.ui.module.reserveplan.ReserveAction;
import com.yuyisz.pis.ui.service.reserveplan.ReserveActionService;
@Service
public class ReserveActionServiceImpl implements ReserveActionService {

	@Autowired
	private ReserveActionRepository reserveActionRepository;
	@Override
	public ReserveAction findById(int actionId) {
		return reserveActionRepository.findOne(actionId);
	}
	@Override
	public List<ReserveAction> findAllAction() {
		return reserveActionRepository.findAll();
	}
}
