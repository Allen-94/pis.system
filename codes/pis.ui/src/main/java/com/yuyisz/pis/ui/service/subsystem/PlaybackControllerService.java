package com.yuyisz.pis.ui.service.subsystem;

import java.util.List;

import com.yuyisz.pis.ui.module.subsystem.PlaybackController;

public interface PlaybackControllerService {
	public PlaybackController save(PlaybackController pc);

	public PlaybackController findOne(int id2, int id3, int id4);
	
	public PlaybackController findById(int id);

	public PlaybackController findUniqueID(int lineid, int stationid, int pcid);

	public void deleteOne(int pc_id);

	public List<PlaybackController> findAll();
}
