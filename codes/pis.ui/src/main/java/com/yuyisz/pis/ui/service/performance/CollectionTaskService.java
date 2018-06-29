package com.yuyisz.pis.ui.service.performance;

import java.util.List;

import com.yuyisz.pis.ui.module.performance.CollectionTask;
import com.yuyisz.pis.ui.module.security.DevResources;


public interface CollectionTaskService {

	List<CollectionTask> findAllTask();

	CollectionTask saveTaskInfo(CollectionTask task);

	CollectionTask findById(long id);

	void deleteTask(List<Long> ids);

	void startUsingTask(long id, int status);

	void startIndexCollectionTask(DevResources dev);
	
}
