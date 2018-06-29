package com.yuyisz.pis.ui.service.performance.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yuyisz.pis.ui.dao.performance.CollectionItemRepository;
import com.yuyisz.pis.ui.module.performance.CollectionItem;
import com.yuyisz.pis.ui.service.performance.CollectionItemService;
@Service
public class CollectionItemServiceImpl implements CollectionItemService {

	@Autowired
	private CollectionItemRepository collectionItemRepository;
	@Override
	public Set<CollectionItem> findByIds(List<Long> indexIdList) {
		List<CollectionItem> items = collectionItemRepository.findAll(indexIdList);
		return new HashSet<>(items);
	}
	@Override
	public void updateBatch(Set<CollectionItem> items) {
		// TODO Auto-generated method stub
		collectionItemRepository.save(items);
	}

}
