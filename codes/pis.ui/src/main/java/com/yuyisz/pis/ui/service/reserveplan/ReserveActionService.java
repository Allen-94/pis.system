package com.yuyisz.pis.ui.service.reserveplan;

import java.util.List;

import com.yuyisz.pis.ui.module.reserveplan.ReserveAction;

public interface ReserveActionService {

	ReserveAction findById(int actionId);
	List<ReserveAction> findAllAction();
}
