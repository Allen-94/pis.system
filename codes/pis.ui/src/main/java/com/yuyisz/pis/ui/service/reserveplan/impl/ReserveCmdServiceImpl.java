package com.yuyisz.pis.ui.service.reserveplan.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yuyisz.pis.ui.dao.reserveplan.ReserveCmdRepository;
import com.yuyisz.pis.ui.module.reserveplan.ReserveCmd;
import com.yuyisz.pis.ui.service.reserveplan.ReserveCmdService;
@Service
public class ReserveCmdServiceImpl implements ReserveCmdService {

	@Autowired
	private ReserveCmdRepository reserveCmdRepository;
	
	@Override
	public ReserveCmd saveCmd(ReserveCmd cmd) {
		return reserveCmdRepository.save(cmd);
	}

}
