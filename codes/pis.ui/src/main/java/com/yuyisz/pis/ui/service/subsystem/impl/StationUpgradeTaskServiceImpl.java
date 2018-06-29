package com.yuyisz.pis.ui.service.subsystem.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yuyisz.pis.ui.dao.subsystem.StationUpgradeTaskRepository;
import com.yuyisz.pis.ui.module.subsystem.StationUpgradeTask;
import com.yuyisz.pis.ui.service.subsystem.StationUpgradeTaskService;
@Service
public class StationUpgradeTaskServiceImpl implements StationUpgradeTaskService {
	@Autowired
	private StationUpgradeTaskRepository stationUpgradeTaskRepository;
	
	@Override
	public List<StationUpgradeTask> findAll() {
		return stationUpgradeTaskRepository.findAll();
	}

}
