package com.yuyisz.pis.ui.service.player.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yuyisz.pis.ui.dao.player.PlayerGroupRepository;
import com.yuyisz.pis.ui.module.player.PlayerGroup;
import com.yuyisz.pis.ui.service.player.PlayerGroupService;

@Service
@Transactional
public class PlayerGroupServiceImpl implements PlayerGroupService {
	@Autowired
	private PlayerGroupRepository playerGroupRepository;
	@Override
	public List<PlayerGroup> findAll() {
		return playerGroupRepository.findAll();
	}
	@Override
	public PlayerGroup savePlayerGroup(PlayerGroup group) {
		return playerGroupRepository.saveAndFlush(group);
		/*if(group.getId() == 0) {
			
		}else {
			return playerGroupRepository.update(group);
		}*/
		
	}
	@Override
	public boolean delPlayerGroupById(long playerId) {
		try {
			playerGroupRepository.delete(playerId);
			return true;
		}catch(Exception e){
			return false;
		}
	}
	@Override
	public PlayerGroup findById(long playerId) {
		return playerGroupRepository.findOne(playerId);
	}
	@Override
	public PlayerGroup findByGroupName(String groupName) {
		return playerGroupRepository.findByGroupName(groupName);
	}
	@Override
	public PlayerGroup findByGroupNameAndNotId(String groupName,long groupId) {
		// TODO Auto-generated method stub
		return playerGroupRepository.findByGroupNameAndIdNot(groupName,groupId);
	}

}
