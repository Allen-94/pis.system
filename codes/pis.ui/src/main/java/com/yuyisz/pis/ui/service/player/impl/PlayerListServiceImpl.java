package com.yuyisz.pis.ui.service.player.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yuyisz.pis.ui.dao.player.PlayListRepository;
import com.yuyisz.pis.ui.module.player.PlayerList;
import com.yuyisz.pis.ui.service.player.PlayerListService;
@Service
public class PlayerListServiceImpl implements PlayerListService {

	@Autowired
	private PlayListRepository playListRepositoy;
	@Override
	public List<PlayerList> findAll() {
		return playListRepositoy.findAll();
	}
	@Override
	public PlayerList saveOrUpdatePlayerList(PlayerList pl) {
		return playListRepositoy.saveAndFlush(pl);
	}
	@Override
	public PlayerList findById(long playerListId) {
		return playListRepositoy.findOne(playerListId);
	}
	@Override
	public void delById(long playerListId) {
		playListRepositoy.delete(playerListId);
	}

}
