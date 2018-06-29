package com.yuyisz.pis.ui.service.subsystem.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yuyisz.pis.ui.dao.subsystem.ProducerConfigRepository;
import com.yuyisz.pis.ui.module.enums.DeviceModel;
import com.yuyisz.pis.ui.module.subsystem.ProducerConfig;
import com.yuyisz.pis.ui.service.subsystem.ProducerConfigService;
@Service
//设备 配置 服务
public class ProducerConfigServiceImpl implements ProducerConfigService {
	@Autowired
	private ProducerConfigRepository producerConfigRepository;
	
	@Override
	public String getModelName(int pc_model) {
		return DeviceModel.get_model_state(pc_model);
	}

}
