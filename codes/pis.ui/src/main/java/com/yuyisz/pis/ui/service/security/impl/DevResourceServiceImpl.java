package com.yuyisz.pis.ui.service.security.impl;

import java.util.List;
import java.util.Random;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.yuyisz.pis.ui.dao.security.ResourceRepository;
import com.yuyisz.pis.ui.module.security.DevResources;
import com.yuyisz.pis.ui.module.subsystem.Screen;
import com.yuyisz.pis.ui.service.security.DevResourceService;

@Service
public class DevResourceServiceImpl implements DevResourceService {

	@Autowired
	private ResourceRepository repository;
	
	Random random ;
	@Override
	public DevResources findRandomOne() {
		List<DevResources> result = repository.findAll(); 
		random = new Random(result.size()-1);
		int index = random.nextInt(result.size()-1);
		return result.get(index);
	}
	@Override
	public DevResources save(DevResources resource) {
		return repository.saveAndFlush(resource);
	}
	@Override
	@Cacheable(cacheNames="findAllResources")
	public List<DevResources> findAllResources() {
		return repository.findAll();
	}
	@Override
	@Cacheable(cacheNames="findByType")
	public List<DevResources> findByType(int type) {
		return repository.findByResourceType(type);
	}
	@Override
	public List<DevResources> findResourceOfGroup(int groupId) {
		return repository.findResourceOfGroup(groupId);
	}
	@Override
	public List<DevResources> findPlayByIds(List<Integer> lIds) {
		return repository.findAll(lIds);
	}
	@Override
	public List<DevResources> findBylinkageType(int type, int id2) {
		return repository.findById2AndResourceType(id2,type);
	}
	@Override
	public DevResources findById(int Id) {
		return repository.findById(Id);
	}
	@Override
	public List<DevResources> findResourceBystationType(int type, int id2, int id3) {
		return repository.findById2AndId3AndResourceType(id2,id3,type);
	}
	@Override
	public void saveAll(DevResources devices) {
		repository.saveAndFlush(devices);
	}
	@Override
	public List<DevResources> findByTypes(List<Integer> types) {
		return repository.findByResourceTypeIn(types);
	}
	@Override
	public DevResources findById2AndId3AndId4AndId5AndAndResourceType(Integer line_id, Integer station_id,
			Integer pc_id, int i, int type) {
		return repository.findById2AndId3AndId4AndId5AndAndResourceType(line_id, station_id, pc_id, i, type);
	}
	@Override
	public List<DevResources> findById2AndId3AndId4AndAndResourceType(Integer line_id, Integer station_id, Integer pc_id,
			int i) {
		return repository.findById2AndId3AndId4AndResourceType(line_id, station_id, pc_id, i);
	}
	@Override
	public void delete(List<DevResources> resource) {
		repository.delete(resource);
	}
}
