package com.yuyisz.pis.ui.service.subsystem.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yuyisz.pis.ui.dao.subsystem.PcRepository;
import com.yuyisz.pis.ui.module.subsystem.PlaybackController;
import com.yuyisz.pis.ui.service.subsystem.PlaybackControllerService;
@Service
public class PlaybackControllerServiceImpl implements PlaybackControllerService {
	@Autowired
	private PcRepository pcReository;
	
	@Override
	public PlaybackController save(PlaybackController pc) {
		PlaybackController Pc = pcReository.saveAndFlush(pc);
		return Pc;
	}

	@Override
	public PlaybackController findOne(int id2, int id3, int id4) {
		return pcReository.findByLine_idAndStation_idAndPc_id(id2,id3,id4);
	}

	@Override
	public PlaybackController findById(int id) {
		return pcReository.findById(id);
	}

	@Override
	public PlaybackController findUniqueID(int lineid, int stationid, int pcid) {
		return pcReository.findByLine_idAndStation_idAndPc_id(lineid, stationid, pcid);
	}

	@Override
	public void deleteOne(int pc_id) {
		 pcReository.delete(pc_id);;
	}

	@Override
	public List<PlaybackController> findAll() {
		return pcReository.findAll();
	}

}
