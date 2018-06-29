package com.yuyisz.pis.ui.service.player;

import java.util.List;

import com.yuyisz.pis.ui.module.player.PlayerRole;
import com.yuyisz.pis.ui.module.resources.FileObject;
import com.yuyisz.pis.ui.module.security.DevResources;

public interface PlayerRoleService {

	PlayerRole findById(long playerRoleId);

	PlayerRole saveOrUpdatePlayerRole(PlayerRole role);

	List<PlayerRole> findRoleByPlayerListId(long playerListId);

	void delById(long playerRoleId);

	FileObject makePlayListFile(String playerDir, DevResources player, PlayerRole re);

}
