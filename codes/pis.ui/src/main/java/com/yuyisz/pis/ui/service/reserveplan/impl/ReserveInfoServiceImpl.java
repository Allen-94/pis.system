package com.yuyisz.pis.ui.service.reserveplan.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yuyisz.pis.ui.dao.reserveplan.ReserveInfoRepository;
import com.yuyisz.pis.ui.module.reserveplan.ReserveInfo;
import com.yuyisz.pis.ui.service.reserveplan.ReserveInfoService;
@Service
public class ReserveInfoServiceImpl implements ReserveInfoService {

	@Autowired
	private ReserveInfoRepository reserveInfoRepository;
	@Override
	public List<ReserveInfo> findAllReserveInfo() {
		return reserveInfoRepository.findAll();
	}
	@Override
	public ReserveInfo findById(int infoId) {
		return reserveInfoRepository.findOne(infoId);
	}
	@Override
	public void saveInfo(ReserveInfo info) {
		reserveInfoRepository.saveAndFlush(info);
	}
	@Override
	public void deleteById(int id) {
		reserveInfoRepository.delete(id);
	}

}
