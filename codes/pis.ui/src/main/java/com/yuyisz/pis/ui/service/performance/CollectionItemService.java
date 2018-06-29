package com.yuyisz.pis.ui.service.performance;

import java.util.List;
import java.util.Set;

import com.yuyisz.pis.ui.module.performance.CollectionItem;

public interface CollectionItemService {

	Set<CollectionItem> findByIds(List<Long> indexIdList);

	void updateBatch(Set<CollectionItem> items);

}
