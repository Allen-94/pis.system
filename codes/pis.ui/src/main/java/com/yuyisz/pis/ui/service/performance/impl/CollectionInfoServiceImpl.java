package com.yuyisz.pis.ui.service.performance.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yuyisz.pis.ui.dao.performance.CollectionInfoRepository;
import com.yuyisz.pis.ui.module.performance.CollectionInfo;
import com.yuyisz.pis.ui.service.performance.CollectionInfoService;
@Service
public class CollectionInfoServiceImpl implements CollectionInfoService {

	@Autowired
	private CollectionInfoRepository collectionInfoRepository;
	
	@Override
	public List<CollectionInfo> findCurrentMonitorInfos(List<Integer> devIds) {
		List<CollectionInfo> result = null;  //monitorInfoRepository.findCurrentMonitorInfos(devIds);
		/*List<MonitorInfoBean> result =  monitorInfoRepository.findAll(new Specification<MonitorInfoBean>() {
			
			@Override
			public Predicate toPredicate(Root<MonitorInfoBean> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				if(devType!=null&&!devType.isEmpty()) {
					In<Object> in =cb.in(root.get("devType"));
					for(String type:devType) {
						in.value(type);
					}
					predicates.add(in);
				}
				if(ids!=null&&!ids.isEmpty()) {
					In<Object> in =cb.in(root.get("devIds"));
					for(String id:ids) {
						in.value(id);
					}
					predicates.add(in);
				}
				return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
			}
		});*/
		return result;
	}

	@Override
	public List<CollectionInfo> findHistoryMonitorInfoByIds(int devId, Date endDate) {
		List<CollectionInfo> result = collectionInfoRepository.findByDevIdAndCreateTimeGreaterThanEqualOrderByCreateTimeDesc(devId,endDate);
		return result;
	}

	@Override
	public List<CollectionInfo> findCurrentMonitorInfo(String ids, List<Integer> target, long delay) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void save(CollectionInfo info) {
		collectionInfoRepository.saveAndFlush(info);	
	}

	/**
	 * 查找相关设备的最新性能数据
	 */
	@Override
	public List<CollectionInfo> findNewestCollectionInfo(List<Integer> devs) {
		List<CollectionInfo> infos = collectionInfoRepository.findNewestCollectionInfo(devs);
		return infos;
	}
}
