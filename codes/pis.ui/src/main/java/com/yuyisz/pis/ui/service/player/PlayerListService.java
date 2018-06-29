package com.yuyisz.pis.ui.service.player;

import java.util.List;

import com.yuyisz.pis.ui.module.player.PlayerList;

public interface PlayerListService {

	List<PlayerList> findAll();

	PlayerList saveOrUpdatePlayerList(PlayerList pl);

	PlayerList findById(long playerListId);
	
	void delById(long playerListId);
}
