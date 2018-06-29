package com.yuyisz.pis.ui.service.subsystem.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yuyisz.pis.ui.dao.subsystem.StationRepository;
import com.yuyisz.pis.ui.module.subsystem.Station;
import com.yuyisz.pis.ui.service.subsystem.StationService;
@Service
public class StationServiceImpl implements StationService {
	@Autowired
	private StationRepository stationRepository;
	
	@Override
	public List<Station> findByLineId(int line_id) {
		return stationRepository.findByLine_id(line_id);
	}

	@Override
	public Station findOne(int lineid, int stationid) {
		return stationRepository.findByLine_idAndStation_id(lineid,stationid);
	}

	@Override
	public Station save(Station station) {
		return stationRepository.save(station);
	}

	@Override
	public List<Station> findAll() {
		return stationRepository.findAll();
	}

	@Override
	public Station findById(int station_id) {
		return stationRepository.findOne(station_id);
	}

	@Override
	public void deleteOne(int station_id) {
		stationRepository.delete(station_id);
	}

}
