package com.yuyisz.pis.ui.service.player;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.yuyisz.pis.ui.module.player.PlayerList;
import com.yuyisz.pis.ui.module.player.PlayerListIssuedTask;
import com.yuyisz.pis.ui.module.security.DevResources;

public interface PlayerListIssuedTaskService {

	List<PlayerListIssuedTask> createIssuedTasks(PlayerList playerList, Date issuedDate);

	void saveTasks(List<PlayerListIssuedTask> tasks);

	String IssuedTask(List<PlayerListIssuedTask> tasks);

	Map findIssuedTaskGroupByPlayer(long playerListId, List<Integer> playerIds);


	List<PlayerListIssuedTask> findIssuedTaskByPlayer(PlayerList playerList);

	void IssuedTask(DevResources player, long playerListId, Date issuedDate);

}
