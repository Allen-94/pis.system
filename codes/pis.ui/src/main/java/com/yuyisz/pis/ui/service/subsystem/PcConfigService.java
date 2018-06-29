package com.yuyisz.pis.ui.service.subsystem;

import com.yuyisz.pis.ui.module.subsystem.PcConfig;
import com.yuyisz.pis.ui.module.subsystem.PlaybackController;

public interface PcConfigService {
	// 获取设备状态
	public String getDeviceStatusName(int status);

	public PcConfig save(PcConfig config);

	PcConfig findByPc(PlaybackController pc);
}
