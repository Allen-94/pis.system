package com.yuyisz.pis.ui.service.subsystem.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yuyisz.pis.ui.dao.subsystem.StationServerRepository;
import com.yuyisz.pis.ui.module.subsystem.StationServer;
import com.yuyisz.pis.ui.service.subsystem.StationServerService;
@Service
public class StationServerServiceImpl implements StationServerService {
	@Autowired
	private StationServerRepository stationServerRepository;
	
	@Override
	public StationServer findOne(int id2, int id3, int id4) {
		return stationServerRepository.findByLine_idAndStation_idAndStation_server_id(id2, id3, id4);
	}

	@Override
	public StationServer findById(int id) {
		return stationServerRepository.findById(id);
	}

	@Override
	public StationServer save(StationServer server) {
		return stationServerRepository.save(server);
	}

	@Override
	public void deleteOne(int server_id) {
		stationServerRepository.delete(server_id);
	}

	@Override
	public List<StationServer> findAll() {
		return stationServerRepository.findAll();
	}
	
}
