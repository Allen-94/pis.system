package com.yuyisz.pis.ui.runner;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.yuyisz.pis.ui.module.performance.CollectionInfo;
import com.yuyisz.pis.ui.module.performance.CollectionTask;
import com.yuyisz.pis.ui.module.security.DevResources;
import com.yuyisz.pis.ui.service.performance.CollectionInfoService;
import com.yuyisz.pis.ui.service.security.DevResourceService;
import com.yuyisz.pis.ui.util.PerformanceCollectionUtil;

/**
 * 性能采集任务项目自启动
 * 遍历所有设备，查看是否有处于启动状态的任务，
 * 启动任务
 * @author mengz
 *
 */
@Component
@Order(value=2)
public class PerformanceCollectionRunner implements ApplicationRunner {

	@Autowired
	private CollectionInfoService collectionInfoService;
	@Autowired
	private DevResourceService devResourceService;
	
	private ExecutorService threadpool = Executors.newCachedThreadPool();
	private ConcurrentHashMap<Integer, Boolean> flag = new ConcurrentHashMap<>(); //保存线程运行标记     设备ID:是否运行
	@Override
	public void run(ApplicationArguments arg0) throws Exception {
		/*List<DevResources> resources = devResourceService.findAllResources();
		for(DevResources dev:resources) {
			//查看设备的性能检测任务是否启动
			runCollectionTask(dev);
		}*/
	}
	
	private void runCollectionTask(DevResources dev) {
		CollectionTask task = dev.getCollectionTask();
		
		if(task!=null&&task.isCanUse()) {
			flag.put(dev.getId(), true);
			threadpool.execute(new Runnable() {
				@Override
				public void run() {
					while(flag.get(dev.getId())) {
						//启动任务，发送获取性能数据的消息,保存到数据库
						CollectionInfo info = getDevPerformance(dev);
						collectionInfoService.save(info);
						int delay = task.getCollectDelay();
						try {
							Thread.sleep(delay*60*1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			});
		}
	}
	
	private CollectionInfo getDevPerformance(DevResources dev) {
		CollectionInfo info = new CollectionInfo();
		info.setDev(dev);
		info.setCreateTime(new Date());
		try {
			double cpuRation;
			cpuRation = PerformanceCollectionUtil.getCpuLoadPercentage();
			info.setRatios(cpuRation, 0, 0, 0, 0);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return info;
	}
	/**
	 * 启动设备的性能采集任务
	 * @param dev
	 */
	public void startCollectionForDev(DevResources dev) {
		runCollectionTask(dev);
	}
	
	/**
	 * 停止设备上正在运行的采集任务
	 * @param dev
	 */
	public void stopCollectionForDev(DevResources dev) {
		flag.put(dev.getId(), false);
	}

}
