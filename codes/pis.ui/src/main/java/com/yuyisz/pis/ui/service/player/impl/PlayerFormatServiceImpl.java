package com.yuyisz.pis.ui.service.player.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yuyisz.pis.ui.dao.player.PlayFormatRepository;
import com.yuyisz.pis.ui.module.player.PlayerFormat;
import com.yuyisz.pis.ui.service.player.PlayerFormatService;
@Service
public class PlayerFormatServiceImpl implements PlayerFormatService {
	@Autowired
	private PlayFormatRepository playFormatRepositoy;
	
	@Override
	public List<PlayerFormat> findAll() {
		return playFormatRepositoy.findAll();
	}
	@Override
	public PlayerFormat findById(long playerFormatId) {
		return playFormatRepositoy.findOne(playerFormatId);
	}
	@Override
	public PlayerFormat saveFormat(PlayerFormat format) {
		return playFormatRepositoy.saveAndFlush(format);
	}
	@Override
	public boolean delPlayerFormat(long formatId) {
		try {
			playFormatRepositoy.delete(formatId);
			return true;
		}catch(IllegalArgumentException e) {
			return false;
		}
	}
}
