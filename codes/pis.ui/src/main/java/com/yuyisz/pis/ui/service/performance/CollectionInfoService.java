package com.yuyisz.pis.ui.service.performance;

import java.util.Date;
import java.util.List;

import com.yuyisz.pis.ui.module.performance.CollectionInfo;
import com.yuyisz.pis.ui.module.security.DevResources;

public interface CollectionInfoService {

	/**
	 * 获取设备当前性能信息
	 * @param devType：设备类型，为空表示全部
	 * @param ids：设备的ids标识，为空表示全部
	 * @return
	 */
	List<CollectionInfo> findCurrentMonitorInfos(List<Integer> devIds);
	/**
	 * 设备的历史性能数据
	 * @param ids：设备ids  必须
	 * @param target：target：指标
	 * @param endDate:时间跨度 ，即截止日期
	 * @return
	 */
	List<CollectionInfo> findHistoryMonitorInfoByIds(int devId, Date endDate);
	/**
	 * 设备当前性能数据
	 * @param ids：设备ids
	 * @param target：统计指标，可以是多个
	 * @param delay：采样间隔
	 * @return
	 */
	List<CollectionInfo> findCurrentMonitorInfo(String ids,List<Integer> target, long delay);
	void save(CollectionInfo info);
	List<CollectionInfo> findNewestCollectionInfo(List<Integer> devs);
}
