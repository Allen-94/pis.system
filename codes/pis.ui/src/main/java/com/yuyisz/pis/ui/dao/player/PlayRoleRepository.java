package com.yuyisz.pis.ui.dao.player;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.yuyisz.pis.ui.module.player.PlayerRole;
@Repository
public interface PlayRoleRepository extends JpaRepository<PlayerRole, Long>, JpaSpecificationExecutor<PlayerRole> {

	List<PlayerRole> findByPlayListId(long playerListId);

}
