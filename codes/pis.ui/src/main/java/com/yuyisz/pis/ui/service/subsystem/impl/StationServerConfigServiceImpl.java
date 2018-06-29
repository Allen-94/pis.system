package com.yuyisz.pis.ui.service.subsystem.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yuyisz.pis.ui.dao.subsystem.StationServerConfigRepository;
import com.yuyisz.pis.ui.module.subsystem.StationServer;
import com.yuyisz.pis.ui.module.subsystem.StationServerConfig;
import com.yuyisz.pis.ui.service.subsystem.StationServerConfigService;
@Service
public class StationServerConfigServiceImpl implements StationServerConfigService {
	@Autowired
	private StationServerConfigRepository stationServerConfigRepository;
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
	public StationServerConfig findByStationServer(StationServer stationserver) {
		return stationServerConfigRepository.findByStationServer(stationserver);
	}

	@Override
	public StationServerConfig save(StationServerConfig config) {
		return stationServerConfigRepository.save(config);
	}

}
