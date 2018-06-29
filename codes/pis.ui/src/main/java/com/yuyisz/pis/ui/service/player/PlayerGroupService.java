package com.yuyisz.pis.ui.service.player;

import java.util.List;

import com.yuyisz.pis.ui.module.player.PlayerGroup;

public interface PlayerGroupService {

	List<PlayerGroup> findAll();

	PlayerGroup savePlayerGroup(PlayerGroup group);

	boolean delPlayerGroupById(long playerId);

	PlayerGroup findById(long playerId);

	PlayerGroup findByGroupName(String groupName);

	PlayerGroup findByGroupNameAndNotId(String groupName,long groupId);
}
