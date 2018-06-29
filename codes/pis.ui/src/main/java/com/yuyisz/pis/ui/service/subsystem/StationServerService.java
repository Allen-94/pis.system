package com.yuyisz.pis.ui.service.subsystem;

import java.util.List;

import com.yuyisz.pis.ui.module.subsystem.StationServer;

public interface StationServerService {

	StationServer findOne(int id2, int id3, int id4);

	StationServer findById(int id);

	StationServer save(StationServer server);

	void deleteOne(int server_id);

	List<StationServer> findAll();

}
