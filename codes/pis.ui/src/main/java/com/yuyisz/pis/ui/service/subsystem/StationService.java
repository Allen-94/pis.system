package com.yuyisz.pis.ui.service.subsystem;

import java.util.List;

import com.yuyisz.pis.ui.module.subsystem.Station;

public interface StationService {

	List<Station> findByLineId(int line_id);

	Station findOne(int lineid, int stationid);

	Station save(Station station);

	List<Station> findAll();

	Station findById(int station_id);

	void deleteOne(int station_id);

}
