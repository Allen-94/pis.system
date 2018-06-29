package com.yuyisz.pis.ui.service.player;

import java.util.List;

import com.yuyisz.pis.ui.module.player.PlayerFormat;

public interface PlayerFormatService {

	List<PlayerFormat> findAll();

	PlayerFormat findById(long playerFormatId);

	PlayerFormat saveFormat(PlayerFormat format);

	boolean delPlayerFormat(long formatId);
}
