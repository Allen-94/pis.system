package com.yuyisz.pis.ui.service.security;

import java.util.List;

import com.yuyisz.pis.ui.module.security.DevResources;
import com.yuyisz.pis.ui.module.subsystem.Screen;

public interface DevResourceService {

	/**
	 * 随机获取一个设备，测试用
	 * @return
	 */
	DevResources findRandomOne();

	DevResources save(DevResources resource);

	List<DevResources> findAllResources();

	List<DevResources> findByType(int type);
	
	/*
	 * 获得组对应的所有管理域
	 */
	List<DevResources> findResourceOfGroup(int groupId);

	List<DevResources> findPlayByIds(List<Integer> lIds);
	//联动查询线路下站点
	List<DevResources> findBylinkageType(int type, int lineId);
	
	DevResources findById(int resourceId);
	//获取某站点的设备
	List<DevResources> findResourceBystationType(int type, int id2, int id3);

	void saveAll(DevResources devices);
	/**
	 * 查找符合类型的设备
	 * @param types
	 * @return
	 */
	List<DevResources> findByTypes(List<Integer> types);

	DevResources findById2AndId3AndId4AndId5AndAndResourceType(Integer line_id, Integer station_id, Integer pc_id,
			int i, int type);

	List<DevResources> findById2AndId3AndId4AndAndResourceType(Integer line_id, Integer station_id, Integer pc_id, int i);

	void delete(List<DevResources> resource);
}
