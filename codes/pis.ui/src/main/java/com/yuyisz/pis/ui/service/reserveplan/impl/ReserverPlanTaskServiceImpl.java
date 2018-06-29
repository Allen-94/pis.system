package com.yuyisz.pis.ui.service.reserveplan.impl;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import com.yuyisz.pis.ui.dao.reserveplan.ReserverPlanTaskRepository;
import com.yuyisz.pis.ui.module.reserveplan.ReserverPlanTask;
import com.yuyisz.pis.ui.service.reserveplan.ReserverPlanTaskService;
@Service
@Transactional
@EnableAsync
public class ReserverPlanTaskServiceImpl implements ReserverPlanTaskService {

	@Autowired
	private ReserverPlanTaskRepository reservePlanTaskRepository;
	
	@Override
	public List<ReserverPlanTask> findAllPlanTask() {
		return reservePlanTaskRepository.findAll();
	}
	
	@Override
	public void delByPlanId(int planId) {
		reservePlanTaskRepository.delByPlanId(planId);
	}
	@Override
	public void savePlanTask(ReserverPlanTask task) {
		reservePlanTaskRepository.save(task);
	}

	@Override
	public List<ReserverPlanTask> findAllPlanTaskByPlanId(int planId) {
		Sort sort = new Sort("playerId","cmdId");
		return reservePlanTaskRepository.findByReservePlanId(planId,sort);
	}

	private ExecutorService threadPool = null;
	/**
	 * 启动预案
	 * @return 
	 */
	@Override
	public ExecutorService runPlanTask(int id) {
		threadPool = Executors.newCachedThreadPool();
		List<ReserverPlanTask> tasks = findAllPlanTaskByPlanId(id);
		for(ReserverPlanTask task:tasks) {
			Runnable aa = new Runnable() {
				
				@Override
				public void run() {
					runTask(task);
				}
			};
			threadPool.submit(aa);
		}
		threadPool.shutdown();
		return threadPool;
	}
	
	private void runTask(ReserverPlanTask task) {
		task.setState(1);//启动中
		reservePlanTaskRepository.saveAndFlush(task);
		//启动预案，向播控发送启动预案的消息
		//此处处理启动过程， 可使用多线程方式异步运行，并使用websocket方式前端实时获取数据
		//System.out.println("启动预案>>>> 指令ID="+task.getCmdId()+",播控ID="+task.getPlayerId());
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		task.setState(2);//启动中
		reservePlanTaskRepository.saveAndFlush(task);
	}
}
