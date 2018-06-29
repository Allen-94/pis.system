package com.yuyisz.pis.ui.service.performance.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yuyisz.pis.ui.dao.performance.CollectionTaskRepository;
import com.yuyisz.pis.ui.module.performance.CollectionTask;
import com.yuyisz.pis.ui.module.security.DevResources;
import com.yuyisz.pis.ui.service.performance.CollectionTaskService;
@Service
public class CollectionTaskServiceImpl implements CollectionTaskService {

	@Autowired CollectionTaskRepository collectionTaskRepository;
	
	@Override
	public List<CollectionTask> findAllTask() {
		List<CollectionTask> result = collectionTaskRepository.findByIsAliveOrderById(true);
		return result;
	}

	@Override
	public CollectionTask saveTaskInfo(CollectionTask task) {
		CollectionTask result = collectionTaskRepository.saveAndFlush(task);
		return result;
	}

	@Override
	public CollectionTask findById(long id) {
		return collectionTaskRepository.findOne( id);
	}

	@Override
	public void deleteTask(List<Long> ids) {
		collectionTaskRepository.setAlive(false, ids);
	}
	@Override
	public void startUsingTask(long id, int status) {
		collectionTaskRepository.startUsing(id, status);
	}

	/**
	 * 启动异步任务，运行设备的性能采集
	 * 发送采集消息到设备，设备返回当前的性能数据
	 * 性能采集任务独立运行，项目启动时需要检测所有任务，重新自动启动
	 */
	@Override
	public void startIndexCollectionTask(DevResources dev) {
		
		
	}
}
