package com.yuyisz.pis.ui.service.security.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yuyisz.pis.ui.dao.security.ResourceRepository;
import com.yuyisz.pis.ui.module.security.DevResources;
import com.yuyisz.pis.ui.service.security.TResourceService;
@Service
public class TResourceServiceImpl implements TResourceService {
	@Autowired
	private ResourceRepository resourceRepository;
	
	@Override
	public DevResources findOneResource(int rescourceId) {
		return resourceRepository.findOne(rescourceId);
	}

}
