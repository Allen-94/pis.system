package com.yuyisz.pis.ui.dao.player;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.yuyisz.pis.ui.module.player.PlayerList;
import com.yuyisz.pis.ui.module.player.PlayerListIssuedTask;

public interface PlayerListIssuedTaskReposutory extends JpaRepository<PlayerListIssuedTask, String>, JpaSpecificationExecutor<PlayerListIssuedTask> {

	List<PlayerListIssuedTask> findByPlayerListOrderByResources(PlayerList playerList);

}
