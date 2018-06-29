package com.yuyisz.pis.ui.service.subsystem;

import com.yuyisz.pis.ui.module.subsystem.PcConfig;
import com.yuyisz.pis.ui.module.subsystem.StationServer;
import com.yuyisz.pis.ui.module.subsystem.StationServerConfig;

public interface StationServerConfigService {
	// 获取设备状态
	public String getDeviceStatusName(int status);

	public StationServerConfig save(StationServerConfig config);

	StationServerConfig findByStationServer(StationServer stationserver);
}
