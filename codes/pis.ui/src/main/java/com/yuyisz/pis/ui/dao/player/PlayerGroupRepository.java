package com.yuyisz.pis.ui.dao.player;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.yuyisz.pis.ui.module.player.PlayerGroup;
@Repository
public interface PlayerGroupRepository extends JpaRepository<PlayerGroup, Long> ,JpaSpecificationExecutor<PlayerGroup> {

	@Query(value="update u",nativeQuery=true)
	@Modifying
	PlayerGroup update(PlayerGroup group);

	PlayerGroup findByGroupName(String groupName);

	PlayerGroup findByGroupNameAndIdNot(String groupName,long groupId);

}
