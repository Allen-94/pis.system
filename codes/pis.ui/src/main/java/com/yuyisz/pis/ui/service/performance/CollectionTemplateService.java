package com.yuyisz.pis.ui.service.performance;

import java.util.List;

import com.yuyisz.pis.ui.module.performance.CollectionTemplate;

public interface CollectionTemplateService {

	CollectionTemplate save(CollectionTemplate entry);
	List<CollectionTemplate> findAllCollectionTemplates();
	CollectionTemplate findById(long id);
	boolean delById(long templateId);
}
