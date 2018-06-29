package com.yuyisz.pis.ui.service.subsystem.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yuyisz.pis.ui.dao.subsystem.PcConfigRepository;
import com.yuyisz.pis.ui.module.subsystem.PcConfig;
import com.yuyisz.pis.ui.module.subsystem.PlaybackController;
import com.yuyisz.pis.ui.service.subsystem.PcConfigService;
@Service
public class PcConfigServiceImpl implements PcConfigService {
	@Autowired
	private PcConfigRepository pcConfigRepository;

	@Override
	public String getDeviceStatusName(int status) {
		String statusvalue = "";
		if (status == 0) {
			statusvalue = "未启用";
		}
		if (status == 1) {
			statusvalue = "失去联系";
		}
		if (status == 2) {
			statusvalue = "正常";
		}
		if (status == 3) {
			statusvalue = "故障";
		}
		return statusvalue;
	}

	@Override
	public PcConfig findByPc(PlaybackController pc) {
		return pcConfigRepository.findByPc(pc);
	}

	@Override
	public PcConfig save(PcConfig config) {
		return pcConfigRepository.save(config);
	}
	
	
}
