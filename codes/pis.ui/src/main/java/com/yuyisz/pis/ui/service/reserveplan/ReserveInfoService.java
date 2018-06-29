package com.yuyisz.pis.ui.service.reserveplan;

import java.util.List;

import com.yuyisz.pis.ui.module.reserveplan.ReserveInfo;

public interface ReserveInfoService {

	List<ReserveInfo> findAllReserveInfo();

	ReserveInfo findById(int infoId);

	void saveInfo(ReserveInfo info);

	void deleteById(int id);

}
