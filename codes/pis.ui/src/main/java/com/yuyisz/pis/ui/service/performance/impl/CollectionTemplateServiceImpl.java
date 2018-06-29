package com.yuyisz.pis.ui.service.performance.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yuyisz.pis.ui.dao.performance.CollectionTemplateRepository;
import com.yuyisz.pis.ui.module.performance.CollectionTemplate;
import com.yuyisz.pis.ui.service.performance.CollectionTemplateService;

@Service
public class CollectionTemplateServiceImpl implements CollectionTemplateService {

	@Autowired
	private CollectionTemplateRepository repository;
	
	@Override
	public CollectionTemplate save(CollectionTemplate entry) {
		CollectionTemplate temp = repository.saveAndFlush(entry);
		return temp;
	}

	@Override
	public List<CollectionTemplate> findAllCollectionTemplates() {
		return repository.findAll();
	}

	@Override
	public CollectionTemplate findById(long id) {
		return repository.findOne(id);
	}

	@Override
	public boolean delById(long templateId) {
		try {
			repository.delete(templateId);
			return true;
		}catch(Exception e) {
			return false;
		}
	}

}
